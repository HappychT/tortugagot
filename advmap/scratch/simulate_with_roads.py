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

with open('c:/Users/rareh/IdeaProjects/tortugagot/advmap/data/roads.js', 'r', encoding='utf-8') as f:
    roads_text = f.read()

m = re.search(r'const ROADS_DATA = (\{.*?\});?\s*$', roads_text, re.DOTALL)
if not m:
    m = re.search(r'const ROADS_DATA = (\{.*)', roads_text, re.DOTALL)

roads_data = json.loads(m.group(1))
road_adj = {}
for u, edges in roads_data["graph"].items():
    for edge in edges:
        v = edge["to"]
        w = edge["dist"]
        road_adj.setdefault(u, []).append((v, w))
        road_adj.setdefault(v, []).append((u, w))

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

idA, idB = "EastWatch", "SunHouse"
euclidDist = dist_world(idA, idB)

road_d, _ = dijkstra(idA, idB, road_adj)
roadDist = road_d

# nearA and nearB
def find_nearest_roads(wp_id, n=15):
    res = []
    for u in road_adj.keys():
        d = dist_world(wp_id, u)
        res.append((u, d))
    res.sort(key=lambda x: x[1])
    return [x for x in res[:n] if x[1] <= 40000]

nearA = find_nearest_roads(idA, 15)
nearB = find_nearest_roads(idB, 15)

bestBridge = float('inf')
for nA, dA in nearA:
    for nB, dB in nearB:
        if nA == nB: continue
        rd, _ = dijkstra(nA, nB, road_adj)
        if rd < float('inf'):
            total = dA + rd + dB
            if total <= euclidDist * 3.0:
                if total < bestBridge:
                    bestBridge = total

roadBridgeDist = bestBridge

sea_d, _ = dijkstra(idA, idB, sea_adj)
seaDist = sea_d

bestLandDist = min(roadDist, roadBridgeDist, euclidDist * 1.25)

print(f"euclidDist: {euclidDist:.1f}")
print(f"roadDist: {roadDist:.1f}")
print(f"roadBridgeDist: {roadBridgeDist:.1f}")
print(f"seaDist: {seaDist:.1f}")
print(f"bestLandDist: {bestLandDist:.1f}")
print(f"bestLandDist * 1.35: {bestLandDist * 1.35:.1f}")
