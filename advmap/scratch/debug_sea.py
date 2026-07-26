import json, re

with open('c:/Users/rareh/IdeaProjects/tortugagot/advmap/data/overrides.js', 'r', encoding='utf-8') as f:
    text = f.read()

routes = re.findall(r'\[\s*"([^"]+)"\s*,\s*"([^"]+)"\s*\]', text)
print(f"Total sea routes: {len(routes)}")

# Build adjacency
adj = {}
for a, b in routes:
    adj.setdefault(a, []).append(b)
    adj.setdefault(b, []).append(a)

# BFS from EastWatch to SunHouse
start = "EastWatch"
end = "SunHouse"

queue = [[start]]
visited = {start}
found = None

while queue:
    path = queue.pop(0)
    curr = path[-1]
    if curr == end:
        found = path
        break
    for nxt in adj.get(curr, []):
        if nxt not in visited:
            visited.add(nxt)
            queue.append(path + [nxt])

if found:
    print("Found path:", " -> ".join(found))
else:
    print(f"NO PATH from {start} to {end}!")
    print(f"Reachable from {start}: {visited}")
