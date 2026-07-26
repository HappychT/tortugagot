import json, re, math

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

path = ["EastWatch", "WhiteHarbour", "Sisterton", "Gulltown", "Dragonstone", "EvenfallHall", "StormsEnd", "RainHouse", "Greenstone", "WeepingTown", "Sunspear", "PlankyTown", "Saltshore", "Hellholt", "Starfall", "SunHouse"]

sea_dist = 0
for i in range(len(path)-1):
    d = dist_world(path[i], path[i+1])
    print(f"{path[i]} -> {path[i+1]}: {d:.1f}")
    sea_dist += d

euclid = dist_world("EastWatch", "SunHouse")
print(f"\nTotal Sea Dist (world): {sea_dist:.1f}")
print(f"Euclid Dist (world): {euclid:.1f}")
print(f"Ratio: {sea_dist / euclid:.2f}")
