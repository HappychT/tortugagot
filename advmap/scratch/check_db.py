import sqlite3, json

conn = sqlite3.connect('c:/Users/rareh/IdeaProjects/tortugagot/advmap/server/map_database.db')
cursor = conn.cursor()

cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
tables = cursor.fetchall()
print("Tables:", tables)

for table in tables:
    tname = table[0]
    print(f"\n--- Table: {tname} ---")
    cursor.execute(f"SELECT * FROM {tname} LIMIT 5;")
    rows = cursor.fetchall()
    for r in rows:
        print(r)

conn.close()
