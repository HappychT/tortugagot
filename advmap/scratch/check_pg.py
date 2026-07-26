from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker

engine = create_engine("postgresql://postgres:secret@localhost:5433/tortuga")
Session = sessionmaker(bind=engine)
db = Session()

rows = db.execute(text("SELECT key, value_json, updated_at FROM map_config")).fetchall()
print("Map Config rows in PostgreSQL:")
for key, val, upd in rows:
    print(f"\nKEY: {key} (Updated: {upd})")
    print("VAL:", val[:300], "..." if len(val) > 300 else "")

db.close()
