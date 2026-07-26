with open('c:/Users/rareh/IdeaProjects/tortugagot/advmap/admin.js', 'r', encoding='utf-8') as f:
    lines = f.readlines()

for i, line in enumerate(lines):
    if 'seaRoutes' in line or 'seaRoute' in line:
        print(f"{i+1}: {line.strip()}")
