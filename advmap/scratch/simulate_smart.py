import json, re, math, heapq

with open('c:/Users/rareh/IdeaProjects/tortugagot/advmap/data/waypoints.js', 'r', encoding='utf-8') as f:
    wp_text = f.read()

wps = {}
for match in re.finditer(r'\{[^{}]*"id":\s*"([^"]+)"[^{}]*"imgX":\s*([\d\.-]+)[^{}]*"imgY":\s*([\d\.-]+)[^{}]*"worldX":\s*([\d\.-]+)[^{}]*"worldZ":\s*([\d\.-]+)[^{}]*\}', wp_text):
    wps[match.group(1)] = (float(match.group(4)), float(match.group(5)))

def dist_world(a, b):
    if a not in wps or b not in wps: return float('inf')
    x1, z1 = wps[a]
    x2, z2 = wps[b]
    return math.hypot(x1 - x2, z1 - z2)

with open('c:/Users/rareh/IdeaProjects/tortugagot/advmap/data/overrides.js', 'r', encoding='utf-8') as f:
    ov_text = f.read()

sea_routes = re.findall(r'\[\s*"([^"]+)"\s*,\s*"([^"]+)"\s*\]', ov_text)
sea_adj = {}
for u, v in sea_routes:
    d = dist_world(u, v)
    sea_adj.setdefault(u, []).append((v, d))
    sea_adj.setdefault(v, []).append((u, d))

def dijkstra(start, end, graph_adj):
    pq = [(0, start, [start])]
    visited = set()
    while pq:
        d, curr, path = heapq.heappop(pq)
        if curr == end:
            return d, path
        if curr in visited:
            continue
        visited.add(curr)
        for nxt, weight in graph_adj.get(curr, []):
            if nxt not in visited:
                heapq.heappush(pq, (d + weight, nxt, path + [nxt]))
    return float('inf'), []

# Simulate smartSegment("EastWatch", "SunHouse")
idA, idB = "EastWatch", "SunHouse"
euclidDist = dist_world(idA, idB)

roadDist = float('inf')
roadBridgeDist = float('inf')

# sea bridge
sea_d, sea_p = dijkstra(idA, idB, sea_adj)
seaDist = sea_d if sea_p else float('inf')

bestLandDist = min(roadDist, roadBridgeDist, euclidDist * 1.25)

print(f"euclidDist: {euclidDist:.1f}")
print(f"seaDist: {seaDist:.1f}")
print(f"bestLandDist: {bestLandDist:.1f}")
print(f"bestLandDist * 2.8: {bestLandDist * 2.8:.1f}")

if seaDist <= bestLandDist * 2.8:
    print("RESULT: SEA BRIDGE SELECTED!")
    print("Path:", " -> ".join(sea_p))
else:
    print("RESULT: REJECTED SEA BRIDGE! WENT STRAIGHT!")
