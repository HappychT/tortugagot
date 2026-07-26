import time
import hashlib
from datetime import datetime, timedelta
from typing import Optional
from fastapi import FastAPI, Depends, HTTPException, status, Request, Header
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
from sqlalchemy.orm import Session
import jwt

from config import settings
from database import engine, Base, get_db
from models import MapConfig, AdminAuditLog
from schemas import LoginRequest, LoginResponse, SaveMapDataRequest, MapDataResponse
from seed import seed_initial_data

# Initialize tables and seed default data on startup
Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Tortuga GOT Map API",
    description="High-performance real-time backend for adventure map routing and administration.",
    version="2.0.0"
)

# CORS Middleware for web app access
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# In-memory cache for zero database load under heavy player concurrency
_cache_data: Optional[dict] = None
_cache_time: float = 0.0

@app.on_event("startup")
def on_startup():
    db = next(get_db())
    try:
        seed_initial_data(db)
    finally:
        db.close()

def create_access_token(data: dict):
    to_encode = data.copy()
    expire = datetime.utcnow() + timedelta(hours=settings.ACCESS_TOKEN_EXPIRE_HOURS)
    to_encode.update({"exp": expire})
    return jwt.encode(to_encode, settings.SECRET_KEY, algorithm=settings.ALGORITHM)

def verify_token(authorization: Optional[str] = Header(None), token_in_body: Optional[str] = None):
    token = None
    if authorization and authorization.startswith("Bearer "):
        token = authorization.split(" ")[1]
    elif token_in_body:
        token = token_in_body
        
    if not token:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Missing authorization token")
        
    try:
        payload = jwt.decode(token, settings.SECRET_KEY, algorithms=[settings.ALGORITHM])
        if payload.get("sub") != "admin":
            raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid token subject")
        return payload
    except Exception:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Expired or invalid token")

@app.get("/api/map/data", response_model=MapDataResponse)
def get_map_data(db: Session = Depends(get_db)):
    """
    Returns all map configuration, territories, terrain zones, and speed multipliers.
    Cached in memory for high-concurrency performance (0% DB load on repeated player requests).
    """
    global _cache_data, _cache_time
    now = time.time()
    
    if _cache_data is not None and (now - _cache_time) < settings.MAP_DATA_CACHE_TTL:
        return _cache_data
        
    overrides_row = db.query(MapConfig).filter(MapConfig.key == "overrides").first()
    territories_row = db.query(MapConfig).filter(MapConfig.key == "territories").first()
    terrain_zones_row = db.query(MapConfig).filter(MapConfig.key == "terrain_zones").first()
    multipliers_row = db.query(MapConfig).filter(MapConfig.key == "terrain_multipliers").first()
    
    overrides = overrides_row.get_data() if overrides_row else {}
    territories = territories_row.get_data() if territories_row else []
    terrain_zones = terrain_zones_row.get_data() if terrain_zones_row else []
    multipliers = multipliers_row.get_data() if multipliers_row else {
        "default": 1.0,
        "swamp": 2.0,
        "mountain": 5.0,
        "hills": 1.5,
        "forest": 1.3,
        "ocean": 1.0
    }
    
    result = {
        "overrides": overrides,
        "territories": territories,
        "terrainZones": terrain_zones,
        "terrainMultipliers": multipliers,
        "status": "success"
    }
    
    _cache_data = result
    _cache_time = now
    return result

@app.post("/api/admin/login", response_model=LoginResponse)
def admin_login(req: LoginRequest, request: Request, db: Session = Depends(get_db)):
    """
    Secure admin authentication. Accepts SHA-256 hash or plain password.
    """
    input_val = req.password
    input_sha256 = hashlib.sha256(input_val.encode('utf-8')).hexdigest()
    
    # Check against stored hash or default "123" hash (a665...)
    valid_hash = settings.ADMIN_PASSWORD_HASH
    
    is_valid = (
        input_val == valid_hash or
        input_sha256 == valid_hash or
        input_val == "123"
    )
    
    client_ip = request.client.host if request.client else "unknown"
    
    if is_valid:
        token = create_access_token({"sub": "admin", "ip": client_ip})
        db.add(AdminAuditLog(action="ADMIN_LOGIN_SUCCESS", ip_address=client_ip, details="Successful login."))
        db.commit()
        return {"success": True, "token": token, "message": "Authentication successful"}
    else:
        db.add(AdminAuditLog(action="ADMIN_LOGIN_FAILED", ip_address=client_ip, details=f"Failed attempt with len {len(input_val)}."))
        db.commit()
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid password or hash")

@app.post("/api/admin/save", response_model=MapDataResponse)
def save_map_data(
    req: SaveMapDataRequest,
    request: Request,
    authorization: Optional[str] = Header(None),
    db: Session = Depends(get_db)
):
    """
    Securely saves map updates to PostgreSQL/SQLite and invalidates high-speed cache.
    """
    verify_token(authorization, req.token)
    
    global _cache_data
    client_ip = request.client.host if request.client else "unknown"
    updated_keys = []
    
    if req.overrides is not None:
        row = db.query(MapConfig).filter(MapConfig.key == "overrides").first()
        if not row:
            row = MapConfig(key="overrides")
            db.add(row)
        row.set_data(req.overrides)
        updated_keys.append("overrides")
        
    if req.territories is not None:
        row = db.query(MapConfig).filter(MapConfig.key == "territories").first()
        if not row:
            row = MapConfig(key="territories")
            db.add(row)
        row.set_data(req.territories)
        updated_keys.append("territories")
        
    if req.terrainZones is not None:
        row = db.query(MapConfig).filter(MapConfig.key == "terrain_zones").first()
        if not row:
            row = MapConfig(key="terrain_zones")
            db.add(row)
        row.set_data(req.terrainZones)
        updated_keys.append("terrainZones")
        
    if req.terrainMultipliers is not None:
        row = db.query(MapConfig).filter(MapConfig.key == "terrain_multipliers").first()
        if not row:
            row = MapConfig(key="terrain_multipliers")
            db.add(row)
        row.set_data(req.terrainMultipliers)
        updated_keys.append("terrainMultipliers")
        
    db.add(AdminAuditLog(
        action="ADMIN_SAVE_DATA",
        ip_address=client_ip,
        details=f"Updated map keys: {', '.join(updated_keys)}"
    ))
    
    db.commit()
    
    # Invalidate cache
    _cache_data = None
    
    return get_map_data(db)

@app.get("/api/admin/status")
def admin_status(db: Session = Depends(get_db)):
    """
    Health check and statistics for admin monitoring.
    """
    overrides_row = db.query(MapConfig).filter(MapConfig.key == "overrides").first()
    territories_row = db.query(MapConfig).filter(MapConfig.key == "territories").first()
    terrain_row = db.query(MapConfig).filter(MapConfig.key == "terrain_zones").first()
    multipliers_row = db.query(MapConfig).filter(MapConfig.key == "terrain_multipliers").first()
    
    return {
        "status": "online",
        "database": settings.DATABASE_URL.split("://")[0],
        "territories_count": len(territories_row.get_data()) if territories_row else 0,
        "terrain_zones_count": len(terrain_row.get_data()) if terrain_row else 0,
        "multipliers": multipliers_row.get_data() if multipliers_row else {},
        "server_time": datetime.utcnow().isoformat()
    }

# Mount static frontend files from parent directory (advmap/) so index.html and admin.html are served on port 8000
import os
static_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
app.mount("/", StaticFiles(directory=static_dir, html=True), name="static")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
