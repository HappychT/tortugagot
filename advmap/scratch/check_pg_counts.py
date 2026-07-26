import json
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker

engine = create_engine("postgresql://postgres:secret@localhost:5433/tortuga")
Session = sessionmaker(bind=engine)
db = Session()

rows = db.execute(text("SELECT key, value_json, updated_at FROM map_config")).fetchall()
for key, val, upd in rows:
    try:
        data = json.loads(val)
        if isinstance(data, list):
            print(f"KEY: {key} -> List with {len(data)} items (Updated: {upd})")
        elif isinstance(data, dict):
            print(f"KEY: {key} -> Dict with keys {list(data.keys())} (Updated: {upd})")
            if "customPoints" in data:
                print(f"   customPoints: {len(data['customPoints'])}")
            if "seaRoutes" in data:
                print(f"   seaRoutes: {len(data['seaRoutes'])}")
    except Exception as e:
        print(f"KEY: {key} -> Error: {e}")

db.close()
