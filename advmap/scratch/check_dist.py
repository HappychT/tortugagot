import json, re, math

with open('c:/Users/rareh/IdeaProjects/tortugagot/advmap/data/waypoints.js', 'r', encoding='utf-8') as f:
    text = f.read()

# Extract json
m = re.search(r'const MAP_DATA = (\{.*?\});', text, re.DOTALL)
if not m:
    m = re.search(r'const MAP_DATA = (\{.*)', text, re.DOTALL)

# let's parse waypoints by regex or json
wps = {}
for match in re.finditer(r'\{[^{}]*"id":\s*"([^"]+)"[^{}]*"imgX":\s*([\d\.-]+)[^{}]*"imgY":\s*([\d\.-]+)[^{}]*\}', text):
    wps[match.group(1)] = (float(match.group(2)), float(match.group(3)))

print(f"Loaded {len(wps)} waypoints")

def dist(a, b):
    if a not in wps or b not in wps: return float('inf')
    x1, y1 = wps[a]
    x2, y2 = wps[b]
    return math.hypot(x1 - x2, y1 - y2) * 64.0  # SCALE is 64

path = ["EastWatch", "WhiteHarbour", "Sisterton", "Gulltown", "Dragonstone", "EvenfallHall", "StormsEnd", "RainHouse", "Greenstone", "WeepingTown", "Sunspear", "PlankyTown", "Saltshore", "Hellholt", "Starfall", "SunHouse"]

sea_dist = 0
for i in range(len(path)-1):
    d = dist(path[i], path[i+1])
    print(f"{path[i]} -> {path[i+1]}: {d:.1f}")
    sea_dist += d

euclid = dist("EastWatch", "SunHouse")
print(f"\nTotal Sea Dist: {sea_dist:.1f}")
print(f"Euclid Dist: {euclid:.1f}")
print(f"Ratio: {sea_dist / euclid:.2f}")
