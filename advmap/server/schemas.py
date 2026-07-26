from typing import Any, Dict, List, Optional
from pydantic import BaseModel, Field

class LoginRequest(BaseModel):
    password: str = Field(..., description="Admin password or SHA-256 hash")

class LoginResponse(BaseModel):
    success: bool
    token: Optional[str] = None
    message: str

class SaveMapDataRequest(BaseModel):
    overrides: Optional[Dict[str, Any]] = None
    territories: Optional[List[Any]] = None
    terrainZones: Optional[List[Any]] = None
    terrainMultipliers: Optional[Dict[str, float]] = None
    token: Optional[str] = None

class MapDataResponse(BaseModel):
    overrides: Dict[str, Any] = Field(default_factory=dict)
    territories: List[Any] = Field(default_factory=list)
    terrainZones: List[Any] = Field(default_factory=list)
    terrainMultipliers: Dict[str, float] = Field(
        default_factory=lambda: {
            "default": 1.0,
            "swamp": 2.0,
            "mountain": 5.0,
            "hills": 1.5,
            "forest": 1.3,
            "ocean": 1.0
        }
    )
    status: str = "success"
