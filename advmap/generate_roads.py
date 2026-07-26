#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Парсит GOTRoads.java и генерирует roads.js:
- Граф смежности вейпоинтов (для Dijkstra / A*)
- Кривые Безье каждого отрезка (для отрисовки)
"""
import sys, io, re, json, os, math

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8', errors='replace')

SCRIPT_DIR   = os.path.dirname(os.path.abspath(__file__))
PROJECT_ROOT = os.path.dirname(SCRIPT_DIR)

ROADS_JAVA = os.path.join(PROJECT_ROOT, "src/main/java/got/common/world/map/GOTRoads.java")
WP_JAVA    = os.path.join(PROJECT_ROOT, "src/main/java/got/common/world/map/GOTWaypoint.java")
OUTPUT_JS  = os.path.join(SCRIPT_DIR, "data/roads.js")

SCALE   = 128
ORIG_X  = 810
ORIG_Z  = 730

# ── Coordinate helpers ──────────────────────────────────────────────────
def map_to_world_x(ix): return round((ix - ORIG_X + 0.5) * SCALE)
def map_to_world_z(iy): return round((iy - ORIG_Z + 0.5) * SCALE)
# inverse: world → imgX
def world_to_img_x(wx): return wx / SCALE - 0.5 + ORIG_X
def world_to_img_z(wz): return wz / SCALE - 0.5 + ORIG_Z

# ── Parse waypoints from Java ───────────────────────────────────────────
def parse_waypoints(java_file):
    """Returns dict name → {imgX, imgY, worldX, worldZ}"""
    wps = {}
    with open(java_file, encoding='utf-8') as f:
        content = f.read()
    pattern = re.compile(
        r"^\s*([A-Z][A-Za-z0-9_]*)\s*\(\s*Region\.[A-Z_]+\s*,\s*GOTFaction\.[A-Z_]+\s*,\s*(-?\d+)\s*,\s*(-?\d+)",
        re.MULTILINE)
    # only parse enum section
    enum_end = content.find('\tpublic Region region;')
    search = content[:enum_end] if enum_end > 0 else content
    seen = set()
    for m in pattern.finditer(search):
        name, ix, iy = m.group(1), int(m.group(2)), int(m.group(3))
        if name in seen: continue
        seen.add(name)
        wps[name] = dict(id=name, imgX=ix, imgY=iy,
                         worldX=map_to_world_x(ix), worldZ=map_to_world_z(iy))
    return wps

# ── Parse road registrations ────────────────────────────────────────────
NEAR_PAT  = re.compile(r"near\s*\(\s*GOTWaypoint\.(\w+)\s*,\s*(-?\d+)\s*,\s*(-?\d+)\s*\)")
WP_PAT    = re.compile(r"GOTWaypoint\.(\w+)")
COORD_PAT = re.compile(r"new\s+int\[\]\s*\{\s*(-?\d+)\s*,\s*(-?\d+)\s*\}")

def resolve_arg(token, waypoints):
    """Converts a single road-point token string to {imgX, imgY, id?}"""
    # near(GOTWaypoint.X, dx, dy)
    m = NEAR_PAT.search(token)
    if m:
        wpname, dx, dy = m.group(1), int(m.group(2)), int(m.group(3))
        if wpname in waypoints:
            wp = waypoints[wpname]
            return {'imgX': wp['imgX'] + dx, 'imgY': wp['imgY'] + dy,
                    'id': f'near_{wpname}_{dx}_{dy}'}
    # GOTWaypoint.X
    m = WP_PAT.search(token)
    if m:
        wpname = m.group(1)
        if wpname in waypoints:
            wp = waypoints[wpname]
            return {'imgX': wp['imgX'], 'imgY': wp['imgY'], 'id': wpname}
    # new int[]{x, y}
    m = COORD_PAT.search(token)
    if m:
        ix, iy = int(m.group(1)), int(m.group(2))
        return {'imgX': ix, 'imgY': iy, 'id': None}
    return None

def parse_roads(roads_java, waypoints):
    """
    Returns list of road segments:
    Each segment = list of points [{imgX, imgY, id?}]
    """
    with open(roads_java, encoding='utf-8') as f:
        content = f.read()

    # Find onInit block
    init_start = content.find('public static void onInit()')
    init_end   = content.find('\n\t}', init_start)
    block = content[init_start:init_end]

    segments = []

    # Find all registerRoad / registerHiddenRoad calls
    call_pat = re.compile(
        r"registerRoad\s*\(\s*id\+\+\s*,(.*?)\)\s*;",
        re.DOTALL)

    for m in call_pat.finditer(block):
        args_raw = m.group(1)

        # Split on comma BUT not within brackets
        # Strategy: tokenise by top-level commas
        tokens = []
        depth = 0
        cur = []
        for ch in args_raw:
            if ch in '([{':
                depth += 1; cur.append(ch)
            elif ch in ')]}':
                depth -= 1; cur.append(ch)
            elif ch == ',' and depth == 0:
                tokens.append(''.join(cur).strip())
                cur = []
            else:
                cur.append(ch)
        if cur:
            tokens.append(''.join(cur).strip())

        points = []
        for tok in tokens:
            tok = tok.strip()
            if not tok:
                continue
            pt = resolve_arg(tok, waypoints)
            if pt:
                points.append(pt)

        if len(points) >= 2:
            segments.append(points)

    return segments

# ── Bezier spline math (mirrors Java BezierCurves) ──────────────────────
def get_control_points(src):
    n = len(src) - 1
    a = [0.0]*n; b = [0.0]*n; c = [0.0]*n; r = [0.0]*n
    a[0] = 0.0; b[0] = 2.0; c[0] = 1.0; r[0] = src[0] + 2*src[1]
    for i in range(1, n-1):
        a[i] = 1.0; b[i] = 4.0; c[i] = 1.0; r[i] = 4*src[i] + 2*src[i+1]
    a[n-1] = 2.0; b[n-1] = 7.0; c[n-1] = 0.0; r[n-1] = 8*src[n-1] + src[n]
    for i in range(1, n):
        m = a[i] / b[i-1]
        b[i] -= m * c[i-1]
        r[i] -= m * r[i-1]
    p1 = [0.0]*n
    p1[n-1] = r[n-1] / b[n-1]
    for i in range(n-2, -1, -1):
        p1[i] = (r[i] - c[i]*p1[i+1]) / b[i]
    p2 = [0.0]*n
    for i in range(n-1):
        p2[i] = 2*src[i+1] - p1[i+1]
    p2[n-1] = 0.5*(src[n] + p1[n-1])
    return p1, p2

def cubic_bezier(p0, cp1, cp2, p3, t):
    """Returns point on cubic bezier at parameter t"""
    u = 1 - t
    x = u**3*p0[0] + 3*u**2*t*cp1[0] + 3*u*t**2*cp2[0] + t**3*p3[0]
    y = u**3*p0[1] + 3*u**2*t*cp1[1] + 3*u*t**2*cp2[1] + t**3*p3[1]
    return [x, y]

def segment_to_bezier_points(points_raw, steps=20):
    """
    Convert a list of control points (imgX,imgY) to a smooth polyline.
    For 2 points: straight line. For 3+: cubic bezier splines.
    Returns list of [imgX, imgY] points.
    """
    coords_x = [p['imgX'] for p in points_raw]
    coords_y = [p['imgY'] for p in points_raw]
    n = len(coords_x)

    if n == 2:
        # straight segment — sample into steps
        return [[coords_x[0] + (coords_x[1]-coords_x[0])*t/steps,
                 coords_y[0] + (coords_y[1]-coords_y[0])*t/steps]
                for t in range(steps+1)]

    p1x, p2x = get_control_points(coords_x)
    p1y, p2y = get_control_points(coords_y)

    result = []
    for i in range(n-1):
        px0 = [coords_x[i], coords_y[i]]
        pcp1 = [p1x[i], p1y[i]]
        pcp2 = [p2x[i], p2y[i]]
        px3 = [coords_x[i+1], coords_y[i+1]]

        dx = coords_x[i+1] - coords_x[i]
        dy = coords_y[i+1] - coords_y[i]
        seg_len = max(1, math.sqrt(dx*dx + dy*dy))
        seg_steps = max(4, int(seg_len / 2))  # ~1 point per 2px

        for s in range(seg_steps + (1 if i == n-2 else 0)):
            t = s / seg_steps
            result.append(cubic_bezier(px0, pcp1, pcp2, px3, t))

    return result

# ── Build adjacency graph for routing ──────────────────────────────────
def build_graph(segments, waypoints):
    """
    graph[wpId] = list of {to: wpId, dist: float, curve: [[imgX,imgY],...]}
    Only edges between named waypoints (with known id).
    """
    graph = {k: [] for k in waypoints}

    for seg_pts in segments:
        # Collect named waypoint ids in this segment in order
        named = [(i, pt['id']) for i, pt in enumerate(seg_pts) if pt.get('id') and pt['id'] in waypoints]

        if len(named) < 2:
            continue

        # For each consecutive pair of named WPs in this segment, build an edge
        for k in range(len(named)-1):
            i0, id0 = named[k]
            i1, id1 = named[k+1]
            sub_pts = seg_pts[i0:i1+1]

            if len(sub_pts) < 2:
                continue

            curve = segment_to_bezier_points(sub_pts, steps=20)

            # distance in img pixels
            dist = sum(
                math.sqrt((curve[j+1][0]-curve[j][0])**2 + (curve[j+1][1]-curve[j][1])**2)
                for j in range(len(curve)-1)
            )
            # convert pixel dist to world blocks: 1 img pixel ≈ 128 blocks
            dist_blocks = dist * SCALE

            # Simplify curve: keep every 3rd point + endpoints
            step = max(1, len(curve)//30)
            simplified = curve[::step]
            if simplified[-1] != curve[-1]:
                simplified.append(curve[-1])

            edge_fwd = {'to': id1, 'dist': dist_blocks, 'curve': simplified}
            edge_bwd = {'to': id0, 'dist': dist_blocks, 'curve': list(reversed(simplified))}

            graph[id0].append(edge_fwd)
            graph[id1].append(edge_bwd)

    return graph

# ── Main ────────────────────────────────────────────────────────────────
def main():
    print(f"Parsing waypoints from: {WP_JAVA}")
    waypoints = parse_waypoints(WP_JAVA)
    print(f"  Waypoints: {len(waypoints)}")

    print(f"Parsing roads from: {ROADS_JAVA}")
    segments = parse_roads(ROADS_JAVA, waypoints)
    print(f"  Raw segments: {len(segments)}")

    print("Building adjacency graph...")
    graph = build_graph(segments, waypoints)

    # Count edges
    edge_count = sum(len(v) for v in graph.values())
    print(f"  Graph edges: {edge_count}")

    # Serialize: graph as dict, curves embedded in edges
    # Only include nodes that have at least one edge
    connected = {k: v for k, v in graph.items() if v}
    print(f"  Connected waypoints: {len(connected)}")

    os.makedirs(os.path.dirname(OUTPUT_JS), exist_ok=True)
    with open(OUTPUT_JS, 'w', encoding='utf-8') as f:
        f.write("// Auto-generated from GOTRoads.java — DO NOT EDIT MANUALLY\n")
        f.write("// Run: python generate_roads.py\n\n")
        f.write("const ROADS_DATA = ")
        json.dump({
            "graph": connected,
            "meta": {"scale": SCALE, "originX": ORIG_X, "originZ": ORIG_Z}
        }, f, ensure_ascii=False, separators=(',', ':'))
        f.write(";\n")

    size_kb = os.path.getsize(OUTPUT_JS) / 1024
    print(f"Written to: {OUTPUT_JS} ({size_kb:.1f} KB)")

if __name__ == "__main__":
    main()
