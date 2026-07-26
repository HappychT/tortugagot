import os, json
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker

engine = create_engine("postgresql://postgres:secret@localhost:5433/tortuga")
Session = sessionmaker(bind=engine)
db = Session()

with open("scratch/overrides.json", "r", encoding="utf-8") as f:
    overrides_str = f.read()

with open("scratch/territories.json", "r", encoding="utf-8") as f:
    territories_str = f.read()

row = db.execute(text("SELECT key FROM map_config WHERE key = 'overrides'")).fetchone()
if row:
    db.execute(text("UPDATE map_config SET value_json = :val, updated_at = NOW() WHERE key = 'overrides'"), {"val": overrides_str})
else:
    db.execute(text("INSERT INTO map_config (key, value_json, updated_at) VALUES ('overrides', :val, NOW())"), {"val": overrides_str})

row_t = db.execute(text("SELECT key FROM map_config WHERE key = 'territories'")).fetchone()
if row_t:
    db.execute(text("UPDATE map_config SET value_json = :val, updated_at = NOW() WHERE key = 'territories'"), {"val": territories_str})
else:
    db.execute(text("INSERT INTO map_config (key, value_json, updated_at) VALUES ('territories', :val, NOW())"), {"val": territories_str})

db.commit()
print("[SUCCESS] Restored complete overrides and territories into PostgreSQL successfully!")
db.close()
