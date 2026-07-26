import os
import json
import sqlite3
from sqlalchemy.orm import Session
from models import MapConfig, AdminAuditLog
from database import SessionLocal, engine, Base

def migrate_from_sqlite(db: Session):
    sqlite_path = os.path.join(os.path.dirname(__file__), "map_database.db")
    if os.path.exists(sqlite_path):
        print(f"Found SQLite database at {sqlite_path}. Migrating data to PostgreSQL...")
        try:
            conn = sqlite3.connect(sqlite_path)
            cursor = conn.cursor()
            cursor.execute("SELECT name FROM sqlite_master WHERE type='table' AND name='map_config'")
            if cursor.fetchone():
                cursor.execute("SELECT key, value_json FROM map_config")
                rows = cursor.fetchall()
                for key, val_json in rows:
                    if val_json and val_json != '[]' and val_json != '{}':
                        existing = db.query(MapConfig).filter(MapConfig.key == key).first()
                        if not existing:
                            print(f"Migrating new key from SQLite: {key}")
                            db.add(MapConfig(key=key, value_json=val_json))
                db.commit()
                print("[SUCCESS] Successfully checked SQLite migration without overwriting PostgreSQL!")
            conn.close()
            # Rename SQLite database so we never attempt migration on every boot
            try:
                os.rename(sqlite_path, sqlite_path + ".migrated_bak")
            except Exception:
                pass
        except Exception as e:
            print(f"[ERROR] Error migrating from SQLite: {e}")

def seed_initial_data(db: Session):
    # Ensure tables exist
    Base.metadata.create_all(bind=engine)

    # First, try to migrate existing data from SQLite if present
    migrate_from_sqlite(db)

    # Check if overrides exist
    overrides_entry = db.query(MapConfig).filter(MapConfig.key == "overrides").first()
    if not overrides_entry:
        default_overrides = {
            "customPoints": [],
            "capitals": [
                "KingsLanding", "Winterfell", "CasterlyRock", "Highgarden",
                "Dragonstone", "Sunspear", "Pyke", "Eyrie", "Riverrun",
                "StormEnd", "OldTown"
            ],
            "fortresses": [
                "CastleBlack", "Harrenhal", "Dreadfort", "Twins",
                "Nightfort", "ShadowTower"
            ],
            "ports": [
                "Lannisport", "Lordsport", "Ryamsport", "SweetportSound",
                "Seagard", "WhiteHarbour", "Oldtown", "Gulltown",
                "Barrowtown", "Saltpans", "Maidenpool", "KingsLanding",
                "Dragonstone", "Sunspear", "PlankyTown", "EastWatch",
                "Sisterton", "WeepingTown", "StarfishHarbor", "Spicetown",
                "Driftmark", "Pyke", "TenTowers",
                "Crakehall", "OldOak", "HewettTown", "Bandallon",
                "SunHouse", "Starfall", "Sandstone", "Hellholt", "Saltshore",
                "Greenstone", "RainHouse", "StormsEnd", "EvenfallHall",
                "FlintsFinger", "VictarionLanding"
            ],
            "resources": [],
            "renames": {},
            "leaders": {},
            "factions": {},
            "seaRoutes": [],
            "settings": {}
        }
        db.add(MapConfig(key="overrides", value_json=json.dumps(default_overrides, ensure_ascii=False)))

    # Check if territories exist
    territories_entry = db.query(MapConfig).filter(MapConfig.key == "territories").first()
    if not territories_entry:
        db.add(MapConfig(key="territories", value_json=json.dumps([], ensure_ascii=False)))

    # Check if terrain_zones exist
    terrain_zones_entry = db.query(MapConfig).filter(MapConfig.key == "terrain_zones").first()
    if not terrain_zones_entry:
        db.add(MapConfig(key="terrain_zones", value_json=json.dumps([], ensure_ascii=False)))

    # Check if terrain_multipliers exist
    multipliers_entry = db.query(MapConfig).filter(MapConfig.key == "terrain_multipliers").first()
    if not multipliers_entry:
        default_multipliers = {
            "default": 1.0,
            "swamp": 2.0,
            "mountain": 5.0,
            "hills": 1.5,
            "forest": 1.3,
            "ocean": 1.0
        }
        db.add(MapConfig(key="terrain_multipliers", value_json=json.dumps(default_multipliers, ensure_ascii=False)))

    db.commit()

if __name__ == "__main__":
    db = SessionLocal()
    try:
        seed_initial_data(db)
        print("Successfully seeded initial map data into database!")
    finally:
        db.close()
