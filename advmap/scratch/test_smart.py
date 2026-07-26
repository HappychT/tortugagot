import json, re, math

with open('c:/Users/rareh/IdeaProjects/tortugagot/advmap/data/waypoints.js', 'r', encoding='utf-8') as f:
    wp_text = f.read()

wps = {}
for match in re.finditer(r'\{[^{}]*"id":\s*"([^"]+)"[^{}]*"imgX":\s*([\d\.-]+)[^{}]*"imgY":\s*([\d\.-]+)[^{}]*\}', wp_text):
    wps[match.group(1)] = (float(match.group(2)), float(match.group(3)))

def dist(a, b):
    if a not in wps or b not in wps: return float('inf')
    x1, y1 = wps[a]
    x2, y2 = wps[b]
    return math.hypot(x1 - x2, y1 - y2) * 64.0

with open('c:/Users/rareh/IdeaProjects/tortugagot/advmap/data/roads.js', 'r', encoding='utf-8') as f:
    roads_text = f.read()

# extract JSON graph
m = re.search(r'const ROADS_DATA = (\{.*?\});?\s*$', roads_text, re.DOTALL)
if not m:
    m = re.search(r'const ROADS_DATA = (\{.*)', roads_text, re.DOTALL)

roads_data = json.loads(m.group(1))
adj = {}
for u, edges in roads_data["graph"].items():
    for edge in edges:
        v = edge["to"]
        w = edge["dist"]
        adj.setdefault(u, []).append((v, w))
        adj.setdefault(v, []).append((u, w))

print(f"Loaded road nodes: {len(adj)}")

import heapq

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

road_d, road_p = dijkstra("EastWatch", "SunHouse", adj)
print(f"Road dist EastWatch -> SunHouse: {road_d:.1f}")
if road_p:
    print(f"Road path: {road_p[0]} ... ({len(road_p)} nodes) ... {road_p[-1]}")

