/* ══════════════════════════════════════════════════════
   Tortuga Map — app.js  v3
   Features: typed markers (capital/fortress/port/resource),
   sea routing, territory overlays, admin data sync
   ══════════════════════════════════════════════════════ */

// ── Constants ─────────────────────────────────────────
const MAP_IMAGE_PATH = 'assets/map.png';
const IMG_W  = 5291;
const IMG_H  = 5067;
const SCALE  = 128;
const ORIG_X = 810;
const ORIG_Z = 730;

const WALK_SPEED   = 4.317;
const HORSE_SPEED  = 14.0;

// ── State ─────────────────────────────────────────────
let mapL, markerLayer, routeLayer, territoryLayer;
let allMarkers    = [];
let routePoints   = [];
let measurePoints = [];
let activeFilters = { regions: new Set(), factions: new Set(), types: new Set(['city','capital','fortress','port','resource']) };
let currentMode   = null;
let mapImgSize    = { w: IMG_W, h: IMG_H };
let roadGraph     = null;
let seaGraph      = null;
let toastTimer    = null;
let measureLineLayer = null;

// Runtime overrides (merged from overrides.js + localStorage admin edits)
let OV = {};

// ── Faction names ──────────────────────────────────────
const FAC = {
  NIGHT_WATCH:'Ночной дозор', WILDLING:'Одичалые', NORTH:'Север',
  ARRYN:'Дом Аррен', RIVERLANDS:'Речные земли', WESTERLANDS:'Ланнистеры',
  REACH:'Простор', STORMLANDS:'Баратеоны', CROWNLANDS:'Корол. Гвардия',
  DORNE:'Дорн', IRONBORN:'Железнорождённые', DRAGONSTONE:'Таргариены',
  WHITE_WALKER:'Белые ходоки', DOTHRAKI:'Дотракийцы', LHAZAR:'Лхазар',
  MOSSOVY:'Мосовия', FREE:'Вольные города', UNALIGNED:'Нейтральные',
  HOSTILE:'Враждебные', GHISCAR:'Гхискар', ASSHAI:'Ассхай',
  VALYRIA:'Валирия', YI_TI:'Йи Ти', SUMMER_ISLANDS:'Летние острова',
  JOGOS:'Джогос-Нхай', IBBEN:'Иббен', SOTHORYOS:'Соторис', QARTH:'Кварт', ULTHOS:'Улхос',
};

// Faction colors for territory overlays (brighter & more vivid)
const FAC_COLORS = {
  NIGHT_WATCH:'#5599dd', WILDLING:'#b88642', NORTH:'#30c0e8',
  ARRYN:'#3388ff', RIVERLANDS:'#66dd33', WESTERLANDS:'#ffcc00',
  REACH:'#22ee22', STORMLANDS:'#9955ff', CROWNLANDS:'#ff3333',
  DORNE:'#ff7711', IRONBORN:'#4488cc', DRAGONSTONE:'#dd33ee',
  WHITE_WALKER:'#88ffff', DOTHRAKI:'#e8a832', LHAZAR:'#ccff00',
  MOSSOVY:'#55cc55', FREE:'#ffd040', UNALIGNED:'#cccccc',
  HOSTILE:'#ff1111', GHISCAR:'#ff9922', ASSHAI:'#bb22bb',
  VALYRIA:'#ff4444', YI_TI:'#ffbb00', SUMMER_ISLANDS:'#22ff55',
  JOGOS:'#dd8833', IBBEN:'#88aabb', SOTHORYOS:'#33cc33',
  QARTH:'#ffe033', ULTHOS:'#cc4488',
};

// ── Helpers ────────────────────────────────────────────
const $       = id => document.getElementById(id);
const toLL    = (ix, iy) => L.latLng(-iy, ix);
const wpLL    = wp => toLL(wp.imgX, wp.imgY);
const getWP   = id => MAP_DATA.waypoints.find(w => w.id === id);
const isOnRoad = id => roadGraph && (roadGraph[id]?.length ?? 0) > 0;
const isPort   = id => OV.ports?.includes(id);
const isCapital = id => OV.capitals?.includes(id);
const isFortress = id => OV.fortresses?.includes(id);
const isResource = id => OV.resources?.includes(id);
const wpName   = wp => OV.renames?.[wp.id] || wp.name;
const wpFaction = wp => OV.factions?.[wp.id] || wp.faction;
const wpColor   = wp => (OV.factions?.[wp.id] && FAC_COLORS[OV.factions[wp.id]]) ? FAC_COLORS[OV.factions[wp.id]] : wp.color;

function getWpType(id) {
  if (isCapital(id))  return 'capital';
  if (isFortress(id)) return 'fortress';
  if (isPort(id))     return 'port';
  if (isResource(id)) return 'resource';
  return 'city';
}

const fmtN      = n => Math.round(n).toLocaleString('ru-RU');
const fmtBlocks = n => fmtN(n) + ' бл.';
const fmtChunks = n => fmtN(n / 16) + ' чанк.';
const fmtTime   = s => s < 60
  ? `${Math.round(s)} сек.`
  : `${Math.floor(s / 60)} мин ${Math.round(s % 60)} сек.`;

function distWp(a, b) {
  const dx = b.worldX - a.worldX, dz = b.worldZ - a.worldZ;
  return Math.sqrt(dx * dx + dz * dz);
}

// ── Toast ──────────────────────────────────────────────
function toast(msg, ms = 2200) {
  const el = $('toast');
  el.textContent = msg;
  el.style.display = 'block';
  el.style.animation = 'none';
  el.offsetHeight;
  el.style.animation = '';
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => { el.style.display = 'none'; }, ms);
}

// ── Helper: merge API data with static OVERRIDES and localStorage edits ─
function mergeMapData(data) {
  const baseOverrides = typeof OVERRIDES !== 'undefined' ? JSON.parse(JSON.stringify(OVERRIDES)) : {
    capitals: [], fortresses: [], ports: [], resources: [],
    renames: {}, leaders: {}, factions: {}, seaRoutes: [], terrainZones: [],
    terrainMultipliers: { default: 1.0, swamp: 2.0, mountain: 5.0, hills: 1.5, forest: 1.3, ocean: 1.0 }
  };
  
  let mergedOV = {
    ...baseOverrides,
    ...(data?.overrides || {}),
    territories: (data?.territories && data.territories.length) ? data.territories : (baseOverrides.territories || []),
    terrainZones: (data?.terrainZones && data.terrainZones.length) ? data.terrainZones : (baseOverrides.terrainZones || []),
    terrainMultipliers: data?.terrainMultipliers || baseOverrides.terrainMultipliers || { default: 1.0, swamp: 2.0, mountain: 5.0, hills: 1.5, forest: 1.3, ocean: 1.0 }
  };

  const dbWp = data?.overrides?.customWaypoints || data?.overrides?.customPoints || [];
  if (dbWp.length) mergedOV.customWaypoints = dbWp;
  if (!mergedOV.customWaypoints) mergedOV.customWaypoints = [];

  // If DB returned empty lists/objects for capitals/ports/factions/etc., restore from static OVERRIDES
  if (!mergedOV.capitals || !mergedOV.capitals.length)     mergedOV.capitals   = [...(baseOverrides.capitals || [])];
  if (!mergedOV.fortresses || !mergedOV.fortresses.length) mergedOV.fortresses = [...(baseOverrides.fortresses || [])];
  if (!mergedOV.ports || !mergedOV.ports.length)           mergedOV.ports      = [...(baseOverrides.ports || [])];
  if (!mergedOV.seaRoutes || !mergedOV.seaRoutes.length)   mergedOV.seaRoutes  = [...(baseOverrides.seaRoutes || [])];
  if (!mergedOV.factions) mergedOV.factions = { ...(baseOverrides.factions || {}) };
  if (!mergedOV.leaders)  mergedOV.leaders  = { ...(baseOverrides.leaders || {}) };
  if (!mergedOV.renames)  mergedOV.renames  = { ...(baseOverrides.renames || {}) };

  // Restore any local browser edits from localStorage if DB doesn't have them yet
  try {
    const localRaw = localStorage.getItem('tortuga_admin_data');
    if (localRaw) {
      const ad = JSON.parse(localRaw);
      if (!mergedOV.territories || !mergedOV.territories.length) if (ad.territories) mergedOV.territories = ad.territories;
      if (!Object.keys(mergedOV.factions || {}).length) if (ad.factions) Object.assign(mergedOV.factions, ad.factions);
      if (!Object.keys(mergedOV.leaders || {}).length)  if (ad.leaders)  Object.assign(mergedOV.leaders, ad.leaders);
      if (!Object.keys(mergedOV.renames || {}).length)  if (ad.renames)  Object.assign(mergedOV.renames, ad.renames);
      if (ad.customWaypoints && ad.customWaypoints.length) {
        if (!mergedOV.customWaypoints) mergedOV.customWaypoints = [];
        for (const cwp of ad.customWaypoints) {
          const idx = mergedOV.customWaypoints.findIndex(w => w.id === cwp.id);
          if (idx >= 0) mergedOV.customWaypoints[idx] = { ...mergedOV.customWaypoints[idx], ...cwp };
          else mergedOV.customWaypoints.push(cwp);
        }
      }
      if (!mergedOV.terrainZones || !mergedOV.terrainZones.length)       if (ad.terrainZones)    mergedOV.terrainZones = ad.terrainZones;
      if (ad.seaRoutes && ad.seaRoutes.length) mergedOV.seaRoutes = ad.seaRoutes;
      if (ad.mapSettings) mergedOV.mapSettings = { ...(mergedOV.mapSettings || {}), ...ad.mapSettings };
      if (ad.terrainMultipliers) mergedOV.terrainMultipliers = { ...(mergedOV.terrainMultipliers || {}), ...ad.terrainMultipliers };
    }
  } catch (e) {}

  // Автоматическая миграция старых морских маршрутов (если в localStorage или БД остался Sandstone вместо Hellholt)
  if (mergedOV.seaRoutes && Array.isArray(mergedOV.seaRoutes)) {
    mergedOV.seaRoutes = mergedOV.seaRoutes.map(item => {
      let a = Array.isArray(item) ? item[0] : item.a;
      let b = Array.isArray(item) ? item[1] : item.b;
      let changed = false;
      if (a === 'Sandstone') { a = 'Hellholt'; changed = true; }
      if (b === 'Sandstone') { b = 'Hellholt'; changed = true; }
      if (changed) {
        if (Array.isArray(item)) return [a, b];
        return { ...item, a, b };
      }
      return item;
    });
  }

  return mergedOV;
}

// ── Load overrides (merge API + static + admin localStorage) ─
async function loadOverrides() {
  let apiData = null;
  try {
    const res = await fetch('/api/map/data');
    if (res.ok) {
      const data = await res.json();
      if (data && data.status === 'success') {
        apiData = data;
        console.log('✅ Loaded live map data from FastAPI + PostgreSQL backend');
      }
    }
  } catch (err) {
    console.warn('⚠️ Backend /api/map/data unreachable, falling back to static/localStorage');
  }

  OV = mergeMapData(apiData);
  if (!OV.terrainZones) OV.terrainZones = [];
  if (!OV.terrainMultipliers) OV.terrainMultipliers = { default: 1.0, swamp: 2.0, mountain: 5.0, hills: 1.5, forest: 1.3, ocean: 1.0 };

  // Inject custom waypoints into MAP_DATA
  if (typeof MAP_DATA !== 'undefined' && MAP_DATA?.waypoints && OV.customWaypoints?.length) {
    for (const cwp of OV.customWaypoints) {
      if (cwp.worldX === undefined && cwp.imgX !== undefined) cwp.worldX = Math.round((cwp.imgX - ORIG_X + 0.5) * SCALE);
      if (cwp.worldZ === undefined && cwp.imgY !== undefined) cwp.worldZ = Math.round((cwp.imgY - ORIG_Z + 0.5) * SCALE);
      const idx = MAP_DATA.waypoints.findIndex(w => w.id === cwp.id);
      if (idx >= 0) MAP_DATA.waypoints[idx] = { ...MAP_DATA.waypoints[idx], ...cwp };
      else MAP_DATA.waypoints.push(cwp);
    }
  }
}

// ════════════════════════════════════════════════════════
//   ROUTING ENGINE
// ════════════════════════════════════════════════════════

// ── MinHeap для Dijkstra (O(log n) вместо O(n log n)) ──
class MinHeap {
  constructor() { this.h = []; }
  push(item) {
    this.h.push(item);
    let i = this.h.length - 1;
    while (i > 0) {
      const p = (i - 1) >> 1;
      if (this.h[p].d <= this.h[i].d) break;
      [this.h[p], this.h[i]] = [this.h[i], this.h[p]]; i = p;
    }
  }
  pop() {
    const top = this.h[0], last = this.h.pop();
    if (this.h.length) {
      this.h[0] = last;
      let i = 0;
      while (true) {
        let s = i, l = 2*i+1, r = 2*i+2;
        if (l < this.h.length && this.h[l].d < this.h[s].d) s = l;
        if (r < this.h.length && this.h[r].d < this.h[s].d) s = r;
        if (s === i) break;
        [this.h[s], this.h[i]] = [this.h[i], this.h[s]]; i = s;
      }
    }
    return top;
  }
  get size() { return this.h.length; }
}

function dijkstra(graph, src, dst) {
  if (!graph) return null;
  if (src === dst) return { dist: 0, segs: [] };
  const D = { [src]: 0 }, prev = {}, vis = new Set();
  const pq = new MinHeap();
  pq.push({ id: src, d: 0 });
  while (pq.size) {
    const { id: u, d: du } = pq.pop();
    if (vis.has(u)) continue;
    vis.add(u);
    if (u === dst) break;
    for (const e of (graph[u] || [])) {
      const weight = getSegmentWeight(e.curve, e.type || 'road');
      const nd = du + e.dist * weight;
      if (D[e.to] === undefined || nd < D[e.to]) {
        D[e.to] = nd; prev[e.to] = { from: u, e }; pq.push({ id: e.to, d: nd });
      }
    }
  }
  if (D[dst] === undefined) return null;
  const segs = [];
  let cur = dst;
  let totalPhysicalDist = 0;
  while (prev[cur]) {
    const { from, e } = prev[cur];
    segs.unshift({ from, to: cur, curve: e.curve, dist: e.dist, type: e.type || 'road' });
    totalPhysicalDist += e.dist;
    cur = from;
  }
  return { dist: totalPhysicalDist, segs };
}

// ── Water Grid & Pathfinding for Sea Routes ───────────
let waterGrid = null;
const W_GRID = 1024, H_GRID = 1024;
const _waterPathCache = new Map();

function initWaterGrid(img) {
  try {
    const c = document.createElement('canvas');
    c.width = W_GRID; c.height = H_GRID;
    const ctx = c.getContext('2d');
    ctx.drawImage(img, 0, 0, W_GRID, H_GRID);
    const data = ctx.getImageData(0, 0, W_GRID, H_GRID).data;
    waterGrid = new Uint8Array(W_GRID * H_GRID);
    for (let i = 0; i < W_GRID * H_GRID; i++) {
      const r = data[i*4], g = data[i*4+1], b = data[i*4+2], a = data[i*4+3];
      // Ocean color #004a77 and all water shades (including turquoise/cyan coastal shelves and straits)
      if (a > 100 && b > r + 3 && b >= g - 35 && b > 30) {
        waterGrid[i] = 1;
      }
    }
  } catch(e) { console.warn('Failed to init water grid:', e); }
}

let terrainGrid = null;

function initTerrainGrid() {
  if (!waterGrid) return;
  terrainGrid = new Uint8Array(W_GRID * H_GRID);
  for (let i = 0; i < W_GRID * H_GRID; i++) {
    if (waterGrid[i] === 1) terrainGrid[i] = 5; // 5 = ocean
  }

  if (!OV.terrainZones?.length) return;
  const typeToId = { swamp: 1, mountain: 2, hills: 3, forest: 4, ocean: 5 };
  const imgW = (typeof mapImgSize !== 'undefined' && mapImgSize?.w) ? mapImgSize.w : 5291;
  const imgH = (typeof mapImgSize !== 'undefined' && mapImgSize?.h) ? mapImgSize.h : 5067;

  for (const zone of OV.terrainZones) {
    const tid = typeToId[zone.type] || 0;
    if (!tid) continue;
    const polys = normalizeToMultiPolygon(zone.points || []);
    for (const poly of polys) {
      for (const ring of poly) {
        if (!ring || ring.length < 3) continue;
        let minGx = W_GRID, maxGx = 0, minGy = H_GRID, maxGy = 0;
        for (const [x, y] of ring) {
          const gx = Math.floor((x / imgW) * W_GRID);
          const gy = Math.floor((y / imgH) * H_GRID);
          if (gx < minGx) minGx = Math.max(0, gx);
          if (gx > maxGx) maxGx = Math.min(W_GRID - 1, gx);
          if (gy < minGy) minGy = Math.max(0, gy);
          if (gy > maxGy) maxGy = Math.min(H_GRID - 1, gy);
        }
        for (let gy = minGy; gy <= maxGy; gy++) {
          const yVal = (gy + 0.5) * (imgH / H_GRID);
          for (let gx = minGx; gx <= maxGx; gx++) {
            const xVal = (gx + 0.5) * (imgW / W_GRID);
            let inside = false;
            for (let i = 0, j = ring.length - 1; i < ring.length; j = i++) {
              const xi = ring[i][0], yi = ring[i][1];
              const xj = ring[j][0], yj = ring[j][1];
              const intersect = ((yi > yVal) !== (yj > yVal)) &&
                  (xVal < (xj - xi) * (yVal - yi) / (yj - yi) + xi);
              if (intersect) inside = !inside;
            }
            if (inside) {
              terrainGrid[gy * W_GRID + gx] = tid;
            }
          }
        }
      }
    }
  }
}

function getSegmentWeight(curve, type) {
  if (!terrainGrid || !curve || !curve.length) return 1.0;
  const mults = OV.terrainMultipliers || { default: 1.0, swamp: 2.0, mountain: 5.0, hills: 1.5, forest: 1.3, ocean: 1.0 };
  const idToMult = { 0: mults.default || 1.0, 1: mults.swamp || 2.0, 2: mults.mountain || 5.0, 3: mults.hills || 1.5, 4: mults.forest || 1.3, 5: mults.ocean || 1.0 };
  
  if (type === 'sea') return idToMult[5] || 1.0;

  const imgW = (typeof mapImgSize !== 'undefined' && mapImgSize?.w) ? mapImgSize.w : 5291;
  const imgH = (typeof mapImgSize !== 'undefined' && mapImgSize?.h) ? mapImgSize.h : 5067;
  
  let totalMult = 0;
  let count = 0;
  for (const [x, y] of curve) {
    const gx = Math.max(0, Math.min(W_GRID - 1, Math.floor((x / imgW) * W_GRID)));
    const gy = Math.max(0, Math.min(H_GRID - 1, Math.floor((y / imgH) * H_GRID)));
    const nIdx = gy * W_GRID + gx;
    const tid = terrainGrid[nIdx];
    let cellMult = idToMult[tid] || 1.0;
    if (tid === 5 || (waterGrid && waterGrid[nIdx] === 1)) {
      cellMult = 30.0; // Штраф для сухопутного маршрута за заход в воду
    }
    totalMult += cellMult;
    count++;
  }
  return count > 0 ? (totalMult / count) : 1.0;
}

function findWaterPath(x1, y1, x2, y2) {
  if (!waterGrid) return [[x1, y1], [x2, y2]];

  // Cache: одни и те же пары портов вызываются повторно — кэшируем
  const cacheKey = `${Math.round(x1)},${Math.round(y1)}|${Math.round(x2)},${Math.round(y2)}`;
  if (_waterPathCache.has(cacheKey)) return _waterPathCache.get(cacheKey);
  const cacheKeyRev = `${Math.round(x2)},${Math.round(y2)}|${Math.round(x1)},${Math.round(y1)}`;
  if (_waterPathCache.has(cacheKeyRev)) return [..._waterPathCache.get(cacheKeyRev)].reverse();

  const imgW = (typeof mapImgSize !== 'undefined' && mapImgSize?.w) ? mapImgSize.w : 5291;
  const imgH = (typeof mapImgSize !== 'undefined' && mapImgSize?.h) ? mapImgSize.h : 5067;
  const toGridX = (val) => Math.max(0, Math.min(W_GRID - 1, Math.floor((val / imgW) * W_GRID)));
  const toGridY = (val) => Math.max(0, Math.min(H_GRID - 1, Math.floor((val / imgH) * H_GRID)));
  const gx1 = toGridX(x1), gy1 = toGridY(y1);
  const gx2 = toGridX(x2), gy2 = toGridY(y2);

  const findNearestWater = (gx, gy) => {
    if (waterGrid[gy * W_GRID + gx] === 1) return [gx, gy];
    for (let r = 1; r <= 80; r++) {
      for (let dy = -r; dy <= r; dy++) {
        for (let dx = -r; dx <= r; dx++) {
          if (Math.abs(dx) === r || Math.abs(dy) === r) {
            const nx = gx + dx, ny = gy + dy;
            if (nx >= 0 && nx < W_GRID && ny >= 0 && ny < H_GRID && waterGrid[ny * W_GRID + nx] === 1) {
              return [nx, ny];
            }
          }
        }
      }
    }
    return [gx, gy];
  };

  const [sx, sy] = findNearestWater(gx1, gy1);
  const [ex, ey] = findNearestWater(gx2, gy2);

  if (sx === ex && sy === ey) return [[x1, y1], [x2, y2]];

  const startIdx = sy * W_GRID + sx;
  const endIdx = ey * W_GRID + ex;
  const gScore = new Float32Array(W_GRID * H_GRID).fill(Infinity);
  const fScore = new Float32Array(W_GRID * H_GRID).fill(Infinity);
  const cameFrom = new Int32Array(W_GRID * H_GRID).fill(-1);
  const closed = new Uint8Array(W_GRID * H_GRID);

  gScore[startIdx] = 0;
  fScore[startIdx] = Math.hypot(ex - sx, ey - sy);

  // Set для O(1) проверки наличия в open-листе
  const openSet = new Set([startIdx]);
  // MinHeap для A* (быстрее чем linear scan по массиву)
  const openHeap = [{ idx: startIdx, f: fScore[startIdx] }];
  const heapPush = (item) => {
    openHeap.push(item);
    let i = openHeap.length - 1;
    while (i > 0) {
      const p = (i - 1) >> 1;
      if (openHeap[p].f <= openHeap[i].f) break;
      [openHeap[p], openHeap[i]] = [openHeap[i], openHeap[p]]; i = p;
    }
  };
  const heapPop = () => {
    const top = openHeap[0], last = openHeap.pop();
    if (openHeap.length) {
      openHeap[0] = last;
      let i = 0;
      while (true) {
        let s = i, l = 2*i+1, r = 2*i+2;
        if (l < openHeap.length && openHeap[l].f < openHeap[s].f) s = l;
        if (r < openHeap.length && openHeap[r].f < openHeap[s].f) s = r;
        if (s === i) break;
        [openHeap[s], openHeap[i]] = [openHeap[i], openHeap[s]]; i = s;
      }
    }
    return top;
  };

  const dirs = [
    [0,-1,1], [0,1,1], [-1,0,1], [1,0,1],
    [-1,-1,1.414], [1,-1,1.414], [-1,1,1.414], [1,1,1.414]
  ];

  let found = false, iter = 0;
  while (openHeap.length > 0 && iter++ < 60000) {
    const { idx: curr } = heapPop();
    openSet.delete(curr);
    if (curr === endIdx) { found = true; break; }
    if (closed[curr]) continue;
    closed[curr] = 1;

    const cx = curr % W_GRID, cy = Math.floor(curr / W_GRID);
    for (const [dx, dy, cost] of dirs) {
      const nx = cx + dx, ny = cy + dy;
      if (nx < 0 || nx >= W_GRID || ny < 0 || ny >= H_GRID) continue;
      const nIdx = ny * W_GRID + nx;
      if (closed[nIdx] || waterGrid[nIdx] === 0) continue;
      if (dx !== 0 && dy !== 0 && (waterGrid[cy * W_GRID + nx] === 0 || waterGrid[ny * W_GRID + cx] === 0)) continue;

      const cellTerrain = terrainGrid ? terrainGrid[nIdx] : 0;
      if (cellTerrain >= 1 && cellTerrain <= 4) continue; // Ocean only rule: ships cannot pass through land terrain!
      const mult = (cellTerrain === 5 && OV.terrainMultipliers?.ocean) ? OV.terrainMultipliers.ocean : 1.0;

      const tentG = gScore[curr] + cost * mult;
      if (tentG < gScore[nIdx]) {
        cameFrom[nIdx] = curr;
        gScore[nIdx] = tentG;
        fScore[nIdx] = tentG + Math.hypot(ex - nx, ey - ny);
        if (!openSet.has(nIdx)) { openSet.add(nIdx); heapPush({ idx: nIdx, f: fScore[nIdx] }); }
      }
    }
  }

  if (!found) {
    const fallback = [[x1, y1], [x2, y2]];
    _waterPathCache.set(cacheKey, fallback);
    return fallback;
  }

  const gridPath = [];
  let cur = endIdx;
  while (cur !== -1) {
    gridPath.push([cur % W_GRID, Math.floor(cur / W_GRID)]);
    if (cur === startIdx) break;
    cur = cameFrom[cur];
  }
  gridPath.reverse();

  const hasLineOfSight = (p1, p2) => {
    const xA = p1[0], yA = p1[1], xB = p2[0], yB = p2[1];
    const dist = Math.hypot(xB - xA, yB - yA);
    const steps = Math.ceil(dist * 2);
    for (let i = 1; i < steps; i++) {
      const t = i / steps;
      const x = Math.round(xA + (xB - xA) * t);
      const y = Math.round(yA + (yB - yA) * t);
      if (waterGrid[y * W_GRID + x] === 0) return false;
    }
    return true;
  };

  const smoothed = [gridPath[0]];
  let currIdx = 0;
  while (currIdx < gridPath.length - 1) {
    let nextIdx = gridPath.length - 1;
    while (nextIdx > currIdx + 1 && !hasLineOfSight(gridPath[currIdx], gridPath[nextIdx])) {
      nextIdx--;
    }
    smoothed.push(gridPath[nextIdx]);
    currIdx = nextIdx;
  }

  const mapPath = smoothed.map(([gx, gy]) => [(gx + 0.5) * (imgW / W_GRID), (gy + 0.5) * (imgH / H_GRID)]);
  mapPath[0] = [x1, y1];
  mapPath[mapPath.length - 1] = [x2, y2];
  _waterPathCache.set(cacheKey, mapPath);
  return mapPath;
}

const _landPathCache = new Map();

function findLandPath(x1, y1, x2, y2) {
  if (!terrainGrid && !waterGrid) return [[x1, y1], [x2, y2]];

  const cacheKey = `${Math.round(x1)},${Math.round(y1)}|${Math.round(x2)},${Math.round(y2)}`;
  if (_landPathCache.has(cacheKey)) return _landPathCache.get(cacheKey);
  const cacheKeyRev = `${Math.round(x2)},${Math.round(y2)}|${Math.round(x1)},${Math.round(y1)}`;
  if (_landPathCache.has(cacheKeyRev)) return [..._landPathCache.get(cacheKeyRev)].reverse();

  const imgW = (typeof mapImgSize !== 'undefined' && mapImgSize?.w) ? mapImgSize.w : 5291;
  const imgH = (typeof mapImgSize !== 'undefined' && mapImgSize?.h) ? mapImgSize.h : 5067;
  const toGridX = (val) => Math.max(0, Math.min(W_GRID - 1, Math.floor((val / imgW) * W_GRID)));
  const toGridY = (val) => Math.max(0, Math.min(H_GRID - 1, Math.floor((val / imgH) * H_GRID)));
  const gx1 = toGridX(x1), gy1 = toGridY(y1);
  const gx2 = toGridX(x2), gy2 = toGridY(y2);

  const isWaterCell = (idx) => {
    return (waterGrid && waterGrid[idx] === 1) || (terrainGrid && terrainGrid[idx] === 5);
  };

  const findNearestLand = (gx, gy) => {
    const idx = gy * W_GRID + gx;
    if (!isWaterCell(idx)) return [gx, gy];
    for (let r = 1; r <= 80; r++) {
      for (let dy = -r; dy <= r; dy++) {
        for (let dx = -r; dx <= r; dx++) {
          if (Math.abs(dx) === r || Math.abs(dy) === r) {
            const nx = gx + dx, ny = gy + dy;
            if (nx >= 0 && nx < W_GRID && ny >= 0 && ny < H_GRID) {
              const nIdx = ny * W_GRID + nx;
              if (!isWaterCell(nIdx)) return [nx, ny];
            }
          }
        }
      }
    }
    return [gx, gy];
  };

  const [sx, sy] = findNearestLand(gx1, gy1);
  const [ex, ey] = findNearestLand(gx2, gy2);

  if (sx === ex && sy === ey) return [[x1, y1], [x2, y2]];

  const startIdx = sy * W_GRID + sx;
  const endIdx = ey * W_GRID + ex;
  const gScore = new Float32Array(W_GRID * H_GRID).fill(Infinity);
  const fScore = new Float32Array(W_GRID * H_GRID).fill(Infinity);
  const cameFrom = new Int32Array(W_GRID * H_GRID).fill(-1);
  const closed = new Uint8Array(W_GRID * H_GRID);

  gScore[startIdx] = 0;
  fScore[startIdx] = Math.hypot(ex - sx, ey - sy);

  const openSet = new Set([startIdx]);
  const openHeap = [{ idx: startIdx, f: fScore[startIdx] }];
  const heapPush = (item) => {
    openHeap.push(item);
    let i = openHeap.length - 1;
    while (i > 0) {
      const p = (i - 1) >> 1;
      if (openHeap[p].f <= openHeap[i].f) break;
      [openHeap[p], openHeap[i]] = [openHeap[i], openHeap[p]]; i = p;
    }
  };
  const heapPop = () => {
    const top = openHeap[0], last = openHeap.pop();
    if (openHeap.length) {
      openHeap[0] = last;
      let i = 0;
      while (true) {
        let s = i, l = 2*i+1, r = 2*i+2;
        if (l < openHeap.length && openHeap[l].f < openHeap[s].f) s = l;
        if (r < openHeap.length && openHeap[r].f < openHeap[s].f) s = r;
        if (s === i) break;
        [openHeap[s], openHeap[i]] = [openHeap[i], openHeap[s]]; i = s;
      }
    }
    return top;
  };

  const dirs = [
    [0,-1,1], [0,1,1], [-1,0,1], [1,0,1],
    [-1,-1,1.414], [1,-1,1.414], [-1,1,1.414], [1,1,1.414]
  ];

  const mults = OV.terrainMultipliers || { default: 1.0, swamp: 2.0, mountain: 5.0, hills: 1.5, forest: 1.3, ocean: 1.0 };
  const idToMult = { 0: mults.default || 1.0, 1: mults.swamp || 2.0, 2: mults.mountain || 5.0, 3: mults.hills || 1.5, 4: mults.forest || 1.3, 5: Infinity };

  let found = false, iter = 0;
  while (openHeap.length > 0 && iter++ < 60000) {
    const { idx: curr } = heapPop();
    openSet.delete(curr);
    if (curr === endIdx) { found = true; break; }
    if (closed[curr]) continue;
    closed[curr] = 1;

    const cx = curr % W_GRID, cy = Math.floor(curr / W_GRID);
    for (const [dx, dy, cost] of dirs) {
      const nx = cx + dx, ny = cy + dy;
      if (nx < 0 || nx >= W_GRID || ny < 0 || ny >= H_GRID) continue;
      const nIdx = ny * W_GRID + nx;
      if (closed[nIdx] || isWaterCell(nIdx)) continue;
      if (dx !== 0 && dy !== 0 && (isWaterCell(cy * W_GRID + nx) || isWaterCell(ny * W_GRID + cx))) continue;

      const cellTerrain = terrainGrid ? terrainGrid[nIdx] : 0;
      const mult = idToMult[cellTerrain] || 1.0;
      if (!isFinite(mult)) continue;

      const tentG = gScore[curr] + cost * mult;
      if (tentG < gScore[nIdx]) {
        cameFrom[nIdx] = curr;
        gScore[nIdx] = tentG;
        fScore[nIdx] = tentG + Math.hypot(ex - nx, ey - ny);
        if (!openSet.has(nIdx)) { openSet.add(nIdx); heapPush({ idx: nIdx, f: fScore[nIdx] }); }
      }
    }
  }

  if (!found) {
    const fallback = [[x1, y1], [x2, y2]];
    _landPathCache.set(cacheKey, fallback);
    return fallback;
  }

  const gridPath = [];
  let cur = endIdx;
  while (cur !== -1) {
    gridPath.push([cur % W_GRID, Math.floor(cur / W_GRID)]);
    if (cur === startIdx) break;
    cur = cameFrom[cur];
  }
  gridPath.reverse();

  const hasLandLineOfSight = (p1, p2) => {
    const xA = p1[0], yA = p1[1], xB = p2[0], yB = p2[1];
    const dist = Math.hypot(xB - xA, yB - yA);
    const steps = Math.ceil(dist * 2);
    for (let i = 1; i < steps; i++) {
      const t = i / steps;
      const x = Math.round(xA + (xB - xA) * t);
      const y = Math.round(yA + (yB - yA) * t);
      const idx = y * W_GRID + x;
      if (isWaterCell(idx)) return false;
      const tid = terrainGrid ? terrainGrid[idx] : 0;
      if (tid === 2 && terrainGrid[p1[1] * W_GRID + p1[0]] !== 2) return false;
    }
    return true;
  };

  const smoothed = [gridPath[0]];
  let currIdx = 0;
  while (currIdx < gridPath.length - 1) {
    let nextIdx = gridPath.length - 1;
    while (nextIdx > currIdx + 1 && !hasLandLineOfSight(gridPath[currIdx], gridPath[nextIdx])) {
      nextIdx--;
    }
    smoothed.push(gridPath[nextIdx]);
    currIdx = nextIdx;
  }

  const mapPath = smoothed.map(([gx, gy]) => [(gx + 0.5) * (imgW / W_GRID), (gy + 0.5) * (imgH / H_GRID)]);
  mapPath[0] = [x1, y1];
  mapPath[mapPath.length - 1] = [x2, y2];
  _landPathCache.set(cacheKey, mapPath);
  return mapPath;
}

// ── Build sea graph from OV.seaRoutes ─────────────────
function getSeaRouteCurve(item) {
  const a = Array.isArray(item) ? item[0] : item.a;
  const b = Array.isArray(item) ? item[1] : item.b;
  const anchors = Array.isArray(item) ? item[2] : item.anchors;
  const wpA = getWP(a), wpB = getWP(b);
  if (!wpA || !wpB) return [];

  if (anchors && anchors.length > 0) {
    const pts = [[wpA.imgX, wpA.imgY], ...anchors, [wpB.imgX, wpB.imgY]];
    const curve = [];
    for (let i = 0; i < pts.length - 1; i++) {
      const part = typeof findWaterPath === 'function' ? findWaterPath(pts[i][0], pts[i][1], pts[i+1][0], pts[i+1][1]) : [pts[i], pts[i+1]];
      if (i === 0) curve.push(...part);
      else curve.push(...part.slice(1));
    }
    return curve;
  }
  return typeof findWaterPath === 'function' ? findWaterPath(wpA.imgX, wpA.imgY, wpB.imgX, wpB.imgY) : [[wpA.imgX, wpA.imgY], [wpB.imgX, wpB.imgY]];
}

function buildSeaGraph() {
  if (!OV.seaRoutes?.length) return null;
  const g = {};
  for (const item of OV.seaRoutes) {
    const a = Array.isArray(item) ? item[0] : item.a;
    const b = Array.isArray(item) ? item[1] : item.b;
    const wpA = getWP(a), wpB = getWP(b);
    if (!wpA || !wpB) continue;
    const curve = getSeaRouteCurve(item);
    let dist = distWp(wpA, wpB);
    if (curve && curve.length > 1) {
      let d = 0;
      for (let i = 0; i < curve.length - 1; i++) {
        d += Math.hypot(curve[i+1][0] - curve[i][0], curve[i+1][1] - curve[i][1]);
      }
      dist = d * (typeof SCALE !== 'undefined' ? SCALE : 128);
    }
    if (!g[a]) g[a] = [];
    if (!g[b]) g[b] = [];
    g[a].push({ to: b, dist, curve, type: 'sea' });
    g[b].push({ to: a, dist, curve, type: 'sea' });
  }
  return g;
}

function findNearestRoadNodes(wp, n = 3) {
  if (!roadGraph) return [];
  return Object.keys(roadGraph)
    .map(id => ({ id, wp: getWP(id) }))
    .filter(x => x.wp)
    .map(x => ({ id: x.id, dist: distWp(wp, x.wp), wp: x.wp }))
    .sort((a, b) => a.dist - b.dist)
    .slice(0, n);
}

function findNearestSeaNodes(wp, n = 3) {
  if (!seaGraph) return [];
  return Object.keys(seaGraph)
    .map(id => ({ id, wp: getWP(id) }))
    .filter(x => x.wp)
    .map(x => ({ id: x.id, dist: distWp(wp, x.wp), wp: x.wp }))
    .sort((a, b) => a.dist - b.dist)
    .slice(0, n);
}

function straight(idA, idB, type = 'straight') {
  const wpA = getWP(idA), wpB = getWP(idB);
  let curve = [[wpA.imgX, wpA.imgY], [wpB.imgX, wpB.imgY]];
  if ((type === 'sea' || type === 'sea-only') && typeof findWaterPath === 'function') {
    const existingRoute = (OV.seaRoutes || []).find(r => {
      const a = Array.isArray(r) ? r[0] : r.a, b = Array.isArray(r) ? r[1] : r.b;
      return (a === idA && b === idB) || (a === idB && b === idA);
    });
    if (existingRoute) {
      curve = getSeaRouteCurve(existingRoute);
    } else {
      curve = findWaterPath(wpA.imgX, wpA.imgY, wpB.imgX, wpB.imgY);
    }
  } else if (typeof findLandPath === 'function') {
    // Для сухопутного маршрута (прямой, approach, depart) используем сеточный поиск, чтобы обойти горы и не лезть в океан/воду!
    curve = findLandPath(wpA.imgX, wpA.imgY, wpB.imgX, wpB.imgY);
  }
  // Пересчитываем дистанцию по фактической кривой в игровых блоках (1 пиксель карты = SCALE = 128 блоков)
  let actualDist = distWp(wpA, wpB);
  if (curve && curve.length > 2) {
    let d = 0;
    for (let i = 0; i < curve.length - 1; i++) {
      const dx = curve[i+1][0] - curve[i][0];
      const dy = curve[i+1][1] - curve[i][1];
      d += Math.hypot(dx, dy);
    }
    actualDist = d * (typeof SCALE !== 'undefined' ? SCALE : 128);
  }
  return { from: idA, to: idB, curve, dist: actualDist, type };
}

function findSeaBridge(wpA, wpB) {
  if (!seaGraph || !OV.ports?.length) return null;

  // Возвращает список портов, достижимых из данной точки (по дороге или напрямую)
  const getReachablePorts = (wp) => {
    // Сама точка — порт
    if (isPort(wp.id)) return [{ id: wp.id, wp, dist: 0, roadPart: { dist: 0, segs: [] } }];

    const allPorts = OV.ports
      .map(id => getWP(id)).filter(Boolean)
      .map(p => ({ id: p.id, wp: p, dist: distWp(wp, p) }))
      .sort((a, b) => a.dist - b.dist);

    // 1) Пробуем найти подход по дороге к любому из портов
    const roadConnected = [];
    for (const p of allPorts.slice(0, 20)) {
      const rp = dijkstra(roadGraph, wp.id, p.id);
      if (rp && rp.segs?.length) {
        roadConnected.push({ id: p.id, wp: p.wp, dist: rp.dist, roadPart: rp });
      }
    }
    roadConnected.sort((a, b) => a.dist - b.dist);
    
    // Если по дороге нашлось достаточно портов, берём до 10 лучших
    if (roadConnected.length >= 4) return roadConnected.slice(0, 10);

    // 2) Добавляем ближайшие порты по прямой (до 8 штук), если дорожных мало или нет
    const nearbyPorts = allPorts.slice(0, 8).map(p => ({
      id: p.id,
      wp: p.wp,
      dist: p.dist,
      roadPart: {
        dist: p.dist,
        segs: [straight(wp.id, p.id, 'approach')],
      },
    }));
    
    // Объединяем дорожные и прямые подходы, убирая дубликаты
    const seen = new Set();
    const combined = [];
    for (const item of [...roadConnected, ...nearbyPorts]) {
      if (!seen.has(item.id)) {
        seen.add(item.id);
        combined.push(item);
      }
    }
    return combined.slice(0, 10);
  };

  const portsA = getReachablePorts(wpA);
  const portsB = getReachablePorts(wpB);
  if (!portsA.length || !portsB.length) return null;

  let best = null;
  const euclidAB = distWp(wpA, wpB);
  for (const pA of portsA) {
    for (const pB of portsB) {
      if (pA.id === pB.id) continue;
      // Если только пеший подход к портам уже длиннее прямого пути между точками, такой морской путь не имеет смысла!
      if (pA.roadPart.dist + pB.roadPart.dist > euclidAB * 1.1 && euclidAB < 30000) continue;
      
      const seaPart = dijkstra(seaGraph, pA.id, pB.id);
      if (!seaPart?.segs?.length) continue;

      const totalDist = pA.roadPart.dist + seaPart.dist + pB.roadPart.dist;
      if (!best || totalDist < best.totalDist) {
        best = { totalDist, segs: [...pA.roadPart.segs, ...seaPart.segs, ...pB.roadPart.segs] };
      }
    }
  }
  return best?.segs || null;
}

function smartSegment(idA, idB) {
  const wpA = getWP(idA), wpB = getWP(idB);
  if (!wpA || !wpB) return [straight(idA, idB)];

  const euclidDist = distWp(wpA, wpB);

  // 1) Прямой дорожный маршрут (если обе точки лежат на графе дорог)
  const roadResult = dijkstra(roadGraph, idA, idB);
  const roadDist = (roadResult && roadResult.segs?.length) ? roadResult.dist : Infinity;

  // 2) Дорожный мост (подход к ближайшей дороге -> дорога -> сход с дороги)
  // Ищем до 25 ближайших узлов дорожного графа в радиусе 250000 блоков
  const nearA = findNearestRoadNodes(wpA, 25).filter(n => n.dist <= 250000);
  const nearB = findNearestRoadNodes(wpB, 25).filter(n => n.dist <= 250000);
  
  // Допустимый коэффициент извилистости для дорожного графа
  const maxRoadDetourMult = euclidDist > 15000 ? 6.5 : (euclidDist > 5000 ? 4.8 : 3.6);

  let bestBridge = null;
  for (const nA of nearA) {
    for (const nB of nearB) {
      if (nA.id === nB.id) continue;
      const roadPart = dijkstra(roadGraph, nA.id, nB.id);
      if (!roadPart || !roadPart.segs?.length) continue;
      const total = nA.dist + roadPart.dist + nB.dist;
      if (total > euclidDist * maxRoadDetourMult) continue;
      if (!bestBridge || total < bestBridge.total) {
        bestBridge = { total, nA, nB, roadPart };
      }
    }
  }

  // Формируем сегменты для дорожного моста (если найден)
  let roadBridgeSegs = null;
  if (bestBridge) {
    const { nA, nB, roadPart } = bestBridge;
    const result = [];
    if (nA.dist > 50) result.push(straight(idA, nA.id, 'approach'));
    result.push(...roadPart.segs.map(s => ({ ...s, type: 'road' })));
    if (nB.dist > 50) result.push(straight(nB.id, idB, 'depart'));
    if (result.length) roadBridgeSegs = result;
  }

  // 3) Морской мост (через порты) — ищем если расстояние между точками >= 800
  const seaBridgeResult = euclidDist >= 800 ? findSeaBridge(wpA, wpB) : null;

  // 4) Сравниваем все варианты по ЭФФЕКТИВНОМУ ВРЕМЕНИ / СТОИМОСТИ ПУТИ
  const calcSegsTimeCost = (segs) => {
    if (!segs || !segs.length) return Infinity;
    return segs.reduce((sum, s) => {
      if (s.type === 'sea') {
        return sum + s.dist * 0.75;
      } else if (s.type === 'road') {
        return sum + s.dist * 1.0;
      } else {
        const terrainMult = typeof getSegmentWeight === 'function' ? getSegmentWeight(s.curve, s.type) : 1.0;
        return sum + s.dist * terrainMult * 1.6;
      }
    }, 0);
  };

  const costRoad = (roadResult && roadResult.segs?.length) ? calcSegsTimeCost(roadResult.segs) : Infinity;
  const costRoadBridge = roadBridgeSegs ? calcSegsTimeCost(roadBridgeSegs) : Infinity;
  const costSea = (seaBridgeResult && seaBridgeResult.length) ? calcSegsTimeCost(seaBridgeResult) : Infinity;
  const straightSegs = [straight(idA, idB)];
  const costStraight = calcSegsTimeCost(straightSegs);

  // Выбираем лучший сухопутный вариант (дорога, мост или путь по суше в обход препятствий через findLandPath)
  let bestLandSegs = straightSegs;
  let bestLandCost = costStraight;
  if (costRoad < bestLandCost || costRoadBridge < bestLandCost) {
    if (costRoad <= costRoadBridge * 1.05 && costRoad <= bestLandCost * 1.12) {
      bestLandSegs = roadResult.segs;
      bestLandCost = costRoad;
    } else if (costRoadBridge <= bestLandCost * 1.12) {
      bestLandSegs = roadBridgeSegs;
      bestLandCost = costRoadBridge;
    }
  }

  // Сравниваем лучший сухопутный маршрут с морским мостом
  if (costSea < Infinity && seaBridgeResult) {
    const seaApproachDist = seaBridgeResult.reduce((sum, s) => (s.type === 'approach' || s.type === 'depart') ? sum + s.dist : sum, 0);
    // Морской путь берём ТОЛЬКО если подход к портам не слишком длинный и если море по времени реально быстрее или почти равно суше
    if (costSea <= bestLandCost * 0.98 && seaApproachDist <= euclidDist * 0.85) {
      return seaBridgeResult;
    }
  }

  return bestLandSegs;
}

// findCityHops удалён — геометрические хопы без графа давали случайные
// результаты (выбирались города близкие к линии A→B, но без учёта связей).
// Теперь этот случай покрывается Шагом 4 (мост через дорожный граф).


function buildRoute(ids) {
  let allSegs = [], totalDist = 0, totalTimeDist = 0;
  let roadCount = 0, approachCount = 0, seaCount = 0, straightCount = 0;
  for (let i = 0; i < ids.length - 1; i++) {
    const segs = smartSegment(ids[i], ids[i + 1]);
    allSegs.push(...segs);
    segs.forEach(s => {
      const weight = getSegmentWeight(s.curve, s.type);
      totalDist += s.dist;
      totalTimeDist += s.dist * weight;
      if (s.type === 'road')                          roadCount++;
      else if (s.type === 'approach' || s.type === 'depart') approachCount++;
      else if (s.type === 'sea')                      seaCount++;
      else                                            straightCount++;
    });
  }
  let summary = 'straight';
  if (seaCount > 0 && roadCount === 0 && straightCount === 0) summary = 'sea-only';
  else if (seaCount > 0)                                       summary = 'sea-mixed';
  else if (roadCount > 0 && approachCount === 0 && straightCount === 0) summary = 'road-only';
  else if (roadCount > 0)                                      summary = 'road-bridge';
  const viaCities = [...new Set(allSegs.filter(s => s.viaName).map(s => s.viaName))];
  return { segs: allSegs, totalDist, totalTimeDist, summary, viaCities };
}


// ════════════════════════════════════════════════════════
//   MAP INIT
// ════════════════════════════════════════════════════════
function updateMapBgAndBounds() {
  if (OV.mapSettings?.bgColor) {
    document.documentElement.style.setProperty('--map-bg', OV.mapSettings.bgColor);
  }
  const minX = Math.max(0, Math.min(8192, OV.mapSettings?.minX ?? 0));
  const minY = Math.max(0, Math.min(8192, OV.mapSettings?.minY ?? 0));
  const maxX = Math.max(minX + 10, Math.min(8192, OV.mapSettings?.maxX ?? mapImgSize.w));
  const maxY = Math.max(minY + 10, Math.min(8192, OV.mapSettings?.maxY ?? mapImgSize.h));
  const bounded = (minX > 0 || minY > 0 || maxX < mapImgSize.w || maxY < mapImgSize.h);

  if (mapL) {
    if (bounded) {
      const bounds = L.latLngBounds(toLL(minX, minY), toLL(maxX, maxY));
      mapL.setMaxBounds(bounds);
      mapL.options.maxBoundsViscosity = 1.0; // 1.0 makes bounds a rigid wall: zero elastic throwback, zero 20 FPS stutter!
      mapL.fitBounds(bounds);
      try {
        const minZ = mapL.getBoundsZoom(bounds, false);
        mapL.setMinZoom(Math.max(-3, minZ));
      } catch(e) {}
    } else {
      mapL.setMaxBounds(null);
      mapL.setMinZoom(-3);
      mapL.setView([-1560, 860], -1);
    }
  }
}

async function initMap() {
  await loadOverrides();
  if (typeof ROADS_DATA !== 'undefined') roadGraph = ROADS_DATA.graph;
  seaGraph = null;

  await new Promise(resolve => {
    const img = new Image();
    img.onload  = () => {
      mapImgSize = { w: img.naturalWidth, h: img.naturalHeight };
      initWaterGrid(img);
      if (typeof initTerrainGrid === 'function') initTerrainGrid();
      seaGraph = buildSeaGraph();
      resolve();
    };
    img.onerror = () => resolve();
    img.src = MAP_IMAGE_PATH;
  });

  mapL = L.map('map', {
    crs: L.CRS.Simple, minZoom: -3, maxZoom: 4,
    zoomSnap: 0.25, attributionControl: true, zoomControl: true,
  });

  L.imageOverlay(MAP_IMAGE_PATH, [[0, 0], [-mapImgSize.h, mapImgSize.w]], {
    attribution: 'Tortuga', opacity: 1, interactive: false,
  }).addTo(mapL);

  updateMapBgAndBounds();

  // Layers (order matters: territories below markers)
  territoryLayer = L.layerGroup().addTo(mapL);
  markerLayer    = L.layerGroup().addTo(mapL);
  routeLayer     = L.layerGroup().addTo(mapL);

  mapL.on('click', onMapClick);
  mapL.on('mousemove', e => {
    const wx = Math.round((e.latlng.lng  - ORIG_X + 0.5) * SCALE);
    const wz = Math.round((-e.latlng.lat - ORIG_Z + 0.5) * SCALE);
    $('coord-x').textContent = fmtN(wx);
    $('coord-z').textContent = fmtN(wz);
  });

  buildMarkers();
  buildFilters();
  renderTerritories();
  $('stats-total').textContent   = MAP_DATA.meta.total;
  $('stats-visible').textContent = allMarkers.length;

  // Background polling for live real-time map updates from database (zero DB load via in-memory cache)
  setInterval(async () => {
    try {
      const res = await fetch('/api/map/data');
      if (res.ok) {
        const data = await res.json();
        if (data && data.status === 'success') {
          const oldLen = allMarkers.length;
          const oldTerr = JSON.stringify(OV.territories || []);
          const oldTerrain = JSON.stringify(OV.terrainZones || []);
          const oldMults = JSON.stringify(OV.terrainMultipliers || {});

          const oldSea = JSON.stringify(OV.seaRoutes || []);
          OV = mergeMapData(data);

          if (oldTerr !== JSON.stringify(OV.territories || [])) renderTerritories();
          if (oldTerrain !== JSON.stringify(OV.terrainZones || []) || oldMults !== JSON.stringify(OV.terrainMultipliers || {}) || oldSea !== JSON.stringify(OV.seaRoutes || [])) {
            if (typeof initTerrainGrid === 'function') initTerrainGrid();
            seaGraph = buildSeaGraph();
            if (routePoints.length >= 2) redrawRoute();
          }
          buildMarkers();
          if (allMarkers.length !== oldLen) applyFilters();
        }
      }
    } catch (e) {}
  }, 45000);
}

// ── Territories ────────────────────────────────────────
function normalizeToMultiPolygon(pts) {
  if (!pts || !pts.length) return [];
  if (typeof pts[0]?.[0]?.[0]?.[0] === 'number') return pts;
  if (typeof pts[0]?.[0]?.[0] === 'number') return pts.map(ring => [ring]);
  if (typeof pts[0]?.[0] === 'number') return [[pts]];
  return [];
}

function renderTerritories() {
  if (!territoryLayer) return;
  territoryLayer.clearLayers();

  let terrs = [];
  if (OV.territories && OV.territories.length) {
    terrs = [...OV.territories];
  } else if (typeof TERRITORIES !== 'undefined') {
    terrs = [...TERRITORIES];
    try {
      const ad = JSON.parse(localStorage.getItem('tortuga_admin_data') || '{}');
      if (ad.territories?.length) {
        const adMap = new Map(ad.territories.map(t => [t.id, t]));
        terrs = terrs.map(t => adMap.has(t.id) ? adMap.get(t.id) : t);
        ad.territories.forEach(t => { if (!terrs.some(x => x.id === t.id)) terrs.push(t); });
      }
    } catch(e) {}
  }

  terrs.forEach(t => {
    const color = t.color || FAC_COLORS[t.faction] || '#aaaaaa';
    const polys = normalizeToMultiPolygon(t.points);
    polys.forEach(poly => {
      const lls = poly.map(ring => ring.map(([ix, iy]) => toLL(ix, iy)));
      L.polygon(lls, {
        color: color, weight: 1.5, opacity: 0.6,
        fillColor: color, fillOpacity: 0.15,
        interactive: true,
        className: 'no-outline',
      }).bindTooltip(`<div style="font-weight:600;font-size:13px">${t.name || FAC[t.faction] || t.faction}</div>`, {
        sticky: true, className: 'territory-tooltip',
      }).addTo(territoryLayer);
    });
  });
}

// ── Markers ────────────────────────────────────────────
function buildMarkers() {
  allMarkers = [];
  markerLayer.clearLayers();
  MAP_DATA.waypoints.forEach(wp => {
    if (wp.hidden) return;
    const type = getWpType(wp.id);
    const name = wpName(wp);
    const marker = makeMarker(wp, type, name);
    marker.bindPopup(() => makePopup(wp, type, name), { maxWidth: 280, minWidth: 230 });
    marker.on('click', e => { L.DomEvent.stopPropagation(e); onMarkerClick(wp, marker); });
    markerLayer.addLayer(marker);
    allMarkers.push({ wp, marker, type });
  });
}

function makeMarker(wp, type, name) {
  let html = '', size = [9, 9], anchor = [4, 4];
  const color = wpColor(wp);

  if (type === 'capital') {
    size = [28, 28]; anchor = [14, 14];
    html = `<div class="wp-capital" style="color:${color};--glow:${color};display:flex;align-items:center;justify-content:center;filter:drop-shadow(0 0 10px ${color});">
      <svg viewBox="0 0 32 32" width="26" height="26">
        <polygon points="16,2 20,12 30,16 20,20 16,30 12,20 2,16 12,12" fill="${color}" stroke="#ffffff" stroke-width="2"/>
        <circle cx="16" cy="16" r="4.5" fill="#ffffff" opacity="0.95"/>
      </svg>
    </div>`;
  } else if (type === 'fortress') {
    size = [24, 24]; anchor = [12, 12];
    html = `<div class="wp-fortress" style="display:flex;align-items:center;justify-content:center;filter:drop-shadow(0 0 8px #000);">
      <svg viewBox="0 0 24 24" width="22" height="22">
        <path d="M4,3 L20,3 L20,10 C20,17 12,22 12,22 C12,22 4,17 4,10 Z" fill="${color}" stroke="#ffffff" stroke-width="2" stroke-linejoin="round"/>
        <path d="M12,6 L12,16 M7,11 L17,11" stroke="#ffffff" stroke-width="2" opacity="0.85"/>
      </svg>
    </div>`;
  } else if (type === 'resource') {
    size = [22, 22]; anchor = [11, 11];
    html = `<div class="wp-resource" style="display:flex;align-items:center;justify-content:center;filter:drop-shadow(0 0 6px #000);">
      <svg viewBox="0 0 24 24" width="20" height="20">
        <polygon points="12,2 21,8 21,16 12,22 3,16 3,8" fill="${color}" stroke="#ffffff" stroke-width="2"/>
        <polygon points="12,6 17,10 17,14 12,18 7,14 7,10" fill="#ffffff" opacity="0.45"/>
      </svg>
    </div>`;
  } else if (type === 'port') {
    size = [20, 20]; anchor = [10, 10];
    html = `<div class="wp-port" style="display:flex;align-items:center;justify-content:center;filter:drop-shadow(0 0 6px #000);">
      <svg viewBox="0 0 24 24" width="18" height="18">
        <circle cx="12" cy="12" r="10" fill="${color}" stroke="#ffffff" stroke-width="2"/>
        <circle cx="12" cy="12" r="4" fill="none" stroke="#ffffff" stroke-width="2"/>
        <line x1="12" y1="2" x2="12" y2="6" stroke="#ffffff" stroke-width="2"/>
        <line x1="12" y1="18" x2="12" y2="22" stroke="#ffffff" stroke-width="2"/>
        <line x1="2" y1="12" x2="6" y2="12" stroke="#ffffff" stroke-width="2"/>
        <line x1="18" y1="12" x2="22" y2="12" stroke="#ffffff" stroke-width="2"/>
      </svg>
    </div>`;
  } else {
    html = `<div class="wp-dot${isOnRoad(wp.id) ? ' wp-road' : ''}" style="background:${color};box-shadow:0 0 5px ${color}55"></div>`;
  }

  return L.marker(wpLL(wp), {
    icon: L.divIcon({ html, className: '', iconSize: size, iconAnchor: anchor }),
    title: name,
    zIndexOffset: type === 'capital' ? 300 : type === 'fortress' ? 200 : type === 'resource' ? 150 : 0,
  });
}

function makePopup(wp, type, name) {
  const onRoad = isOnRoad(wp.id);
  const leader = OV.leaders?.[wp.id];
  const color  = wpColor(wp);
  const faction = wpFaction(wp);
  const typeLabels = { capital:'★ Столица', fortress:'❖ Крепость', port:'◯ Порт', resource:'◈ Ресурс', city:'• Город' };
  const div = document.createElement('div');
  div.className = 'c-popup';
  div.innerHTML = `
    <div class="cp-color-line" style="background:${color};"></div>
    <div class="cp-head" style="border-bottom-color:${color}44;">
      <div class="cp-name" style="color:${color}">${name}</div>
      <div class="cp-region">${wp.regionName} &bull; <strong style="color:${color}">${FAC[faction] || faction}</strong></div>
    </div>
    <div class="cp-body">
      <div class="cp-chips">
        <span class="chip">${typeLabels[type] || '• Город'}</span>
        ${onRoad ? `<span class="chip chip-road">═ Дорога</span>` : ''}
        ${isPort(wp.id) ? `<span class="chip chip-sea">≈ Порт</span>` : ''}
      </div>
      ${leader ? `<div class="cp-leader">★ ${leader}</div>` : ''}
      <div class="cp-coords">
        <span>X: <strong style="color:var(--tx-w)">${fmtN(wp.worldX)}</strong></span>
        <div class="cp-coords-sep"></div>
        <span>Z: <strong style="color:var(--tx-w)">${fmtN(wp.worldZ)}</strong></span>
      </div>
    </div>
    <div class="cp-actions">
      <button class="cp-action-btn" onclick="appHandlePopup('route','${wp.id}')">═ В маршрут</button>
      <button class="cp-action-btn" onclick="appHandlePopup('measure','${wp.id}')">→ Измерить</button>
    </div>`;
  return div;
}

window.appHandlePopup = function(action, id) {
  const wp = getWP(id);
  if (!wp) return;
  if (action === 'route')   addToRoute(wp);
  if (action === 'measure') startMeasure(wp);
  mapL.closePopup();
};

// ── Mode ──────────────────────────────────────────────
function toggleMode(mode) {
  if (currentMode === mode) { cancelMode(); return; }
  currentMode = mode;
  $('btn-route').classList.toggle('active', mode === 'route');
  $('btn-measure').classList.toggle('active', mode === 'measure');
  if (mode === 'route') {
    $('mode-icon').textContent = '═';
    $('mode-text').textContent = 'Режим маршрута — кликай по точкам';
    $('map').style.cursor = 'crosshair';
    showPanel('route');
    toast('Кликни по точке для добавления в маршрут');
  } else {
    $('mode-icon').textContent = '→';
    $('mode-text').textContent = 'Измерение — выбери две точки';
    $('map').style.cursor = 'crosshair';
    measurePoints = [];
    showPanel('measure');
    $('measure-result').style.display = 'none';
    $('measure-hint').style.display   = 'flex';
    toast('Кликни на два места для измерения');
  }
  $('mode-banner').style.display = 'flex';
}

function cancelMode() {
  currentMode = null;
  $('btn-route').classList.remove('active');
  $('btn-measure').classList.remove('active');
  $('mode-banner').style.display = 'none';
  $('map').style.cursor = '';
  measurePoints = [];
  if (measureLineLayer) { mapL.removeLayer(measureLineLayer); measureLineLayer = null; }
}

function showPanel(which) {
  $('active-panel').style.display  = 'block';
  $('route-panel').style.display   = which === 'route'   ? 'block' : 'none';
  $('measure-panel').style.display = which === 'measure' ? 'block' : 'none';
}

// ── Clicks ─────────────────────────────────────────────
function onMapClick(e) {
  if (currentMode !== 'measure') return;
  const ix = e.latlng.lng, iy = -e.latlng.lat;
  const wx = Math.round((ix - ORIG_X + 0.5) * SCALE);
  const wz = Math.round((iy - ORIG_Z + 0.5) * SCALE);
  addMeasurePoint({ name: `(${fmtN(wx)}, ${fmtN(wz)})`, worldX: wx, worldZ: wz, imgX: ix, imgY: iy });
}

function onMarkerClick(wp, marker) {
  if      (currentMode === 'route')   addToRoute(wp);
  else if (currentMode === 'measure') addMeasurePoint(wp);
  else                                marker.openPopup();
}

// ── Measurement ────────────────────────────────────────
function startMeasure(wp) {
  toggleMode('measure');
  measurePoints = [wp];
  updateMeasureUI();
}
function addMeasurePoint(pt) {
  measurePoints.push(pt);
  if (measurePoints.length > 2) measurePoints.shift();
  updateMeasureUI();
}
function updateMeasureUI() {
  if (measureLineLayer) { mapL.removeLayer(measureLineLayer); measureLineLayer = null; }
  const [a, b] = measurePoints;
  if (!a) return;
  if (b) {
    const dist = distWp(a, b);
    const curve = [[a.imgX, a.imgY], [b.imgX, b.imgY]];
    const weight = getSegmentWeight(curve, 'straight');
    const timeDist = dist * weight;
    measureLineLayer = L.polyline([toLL(a.imgX, a.imgY), toLL(b.imgX, b.imgY)],
      { color: '#7aa2f0', weight: 2, opacity: 0.8, dashArray: '6 4' }).addTo(mapL);
    $('m-from').textContent   = a.name || '—';
    $('m-to').textContent     = b.name || '—';
    $('m-blocks').textContent = fmtBlocks(dist);
    $('m-chunks').textContent = fmtChunks(dist);
    $('m-walk').textContent   = fmtTime(timeDist / WALK_SPEED);
    $('m-horse').textContent  = fmtTime(timeDist / HORSE_SPEED);
    $('measure-result').style.display = 'block';
    $('measure-hint').style.display   = 'none';
  } else {
    toast(`Выбрана: ${a.name}. Кликни вторую точку.`);
  }
}

// ── Route ──────────────────────────────────────────────
function addToRoute(wp) {
  if (routePoints.length && routePoints.at(-1).id === wp.id) { toast('Точка уже добавлена'); return; }
  routePoints.push(wp);
  $('active-panel').style.display = 'block';
  $('route-panel').style.display  = 'block';
  redrawRouteList();
  redrawRoute();
  toast(`＋ ${wpName(wp)}`);
}
function removeFromRoute(i) { routePoints.splice(i, 1); redrawRouteList(); redrawRoute(); }

function redrawRouteList() {
  const list = $('route-list');
  list.innerHTML = '';
  routePoints.forEach((wp, i) => {
    const item = document.createElement('div');
    item.className = 'route-item';
    item.innerHTML = `<span class="ri-num">${i+1}</span><span class="ri-dot" style="background:${wp.color}"></span><span class="ri-name">${wpName(wp)}</span><button class="ri-del" onclick="removeFromRoute(${i})">✕</button>`;
    list.appendChild(item);
  });
  $('route-count').textContent  = `${routePoints.length}`;
  $('route-hint').style.display = routePoints.length === 0 ? 'flex' : 'none';
}

function redrawRoute() {
  routeLayer.clearLayers();
  if (routePoints.length < 2) {
    $('route-stats').style.display    = 'none';
    $('route-info-box').style.display = 'none';
    return;
  }
  const ids = routePoints.map(w => w.id);
  const { segs, totalDist, totalTimeDist, summary, viaCities } = buildRoute(ids);

  segs.forEach(seg => {
    const pts = seg.curve.map(([ix, iy]) => toLL(ix, iy));
    if (seg.type === 'road') {
      // Толстая тёмная обводка + яркая золотая линия сверху
      L.polyline(pts, { color: '#000000', weight: 7, opacity: 0.45 }).addTo(routeLayer);
      L.polyline(pts, { color: '#f5c842', weight: 5, opacity: 1.0  }).addTo(routeLayer);
    } else if (seg.type === 'sea') {
      // Тёмная обводка + яркий голубой пунктир
      L.polyline(pts, { color: '#000000', weight: 6, opacity: 0.45 }).addTo(routeLayer);
      L.polyline(pts, { color: '#38d4ff', weight: 4, opacity: 1.0, dashArray: '12 5' }).addTo(routeLayer);
    } else if (seg.type === 'approach' || seg.type === 'depart') {
      // Тонкий но заметный бирюзовый пунктир с обводкой
      L.polyline(pts, { color: '#000000', weight: 4, opacity: 0.35 }).addTo(routeLayer);
      L.polyline(pts, { color: '#2ee8c0', weight: 2.5, opacity: 0.95, dashArray: '7 5' }).addTo(routeLayer);
    } else {
      // Прямая — серый пунктир с контуром
      L.polyline(pts, { color: '#000000', weight: 3.5, opacity: 0.30 }).addTo(routeLayer);
      L.polyline(pts, { color: '#b0b8c8', weight: 2, opacity: 0.85, dashArray: '5 7' }).addTo(routeLayer);
    }
  });


  // Stop markers
  routePoints.forEach((wp, i) => {
    const el = document.createElement('div');
    el.className = 'stop-num';
    el.textContent = i + 1;
    el.style.cssText = `background:${wp.color};box-shadow:0 0 10px ${wp.color}88,0 2px 8px rgba(0,0,0,0.5);`;
    L.marker(wpLL(wp), {
      icon: L.divIcon({ html: el, className: '', iconSize: [22, 22], iconAnchor: [11, 11] }),
      zIndexOffset: 999,
    }).addTo(routeLayer);
  });

  $('stat-distance').textContent = fmtBlocks(totalDist);
  $('stat-chunks').textContent   = fmtChunks(totalDist);
  $('stat-walk').textContent     = fmtTime(totalTimeDist / WALK_SPEED);
  $('stat-horse').textContent    = fmtTime(totalTimeDist / HORSE_SPEED);
  $('route-stats').style.display = 'block';

  const infoBox = $('route-info-box');
  infoBox.style.display = 'block';
  infoBox.className = 'route-info-box';
  const summaryMap = {
    'road-only':   { cls:'info-road',     icon:'═', text:'Маршрут по дорогам' },
    'road-bridge': { cls:'info-bridge',   icon:'═', text:'Через дороги' },
    'sea-only':    { cls:'info-sea',      icon:'≈', text:'Морской маршрут' },
    'sea-mixed':   { cls:'info-sea',      icon:'≈', text:'Морской + сухопутный' },
    'straight':    { cls:'info-straight', icon:'→', text:'Напрямую' },
  };
  const sm = summaryMap[summary] || summaryMap['straight'];
  infoBox.classList.add(sm.cls);
  const sub = viaCities.length ? `<div class="rib-sub">Через: ${viaCities.slice(0,3).join(' → ')}</div>` : '';
  infoBox.innerHTML = `<div class="rib-row"><span class="rib-icon">${sm.icon}</span><span class="rib-text">${sm.text}</span></div>${sub}`;
}

// ── Clear ──────────────────────────────────────────────
function clearAll() {
  routePoints = []; measurePoints = [];
  if (measureLineLayer) { mapL.removeLayer(measureLineLayer); measureLineLayer = null; }
  routeLayer.clearLayers();
  $('active-panel').style.display    = 'none';
  $('route-panel').style.display     = 'none';
  $('measure-panel').style.display   = 'none';
  $('route-list').innerHTML          = '';
  $('route-count').textContent       = '0';
  $('route-stats').style.display     = 'none';
  $('route-info-box').style.display  = 'none';
  $('route-hint').style.display      = 'flex';
  cancelMode(); mapL.closePopup(); toast('Очищено ✓');
}

// ── Filters ────────────────────────────────────────────
function buildFilters() {
  MAP_DATA.regions.forEach(r => activeFilters.regions.add(r.id));
  MAP_DATA.factions.forEach(f => activeFilters.factions.add(f.id));

  const rList = $('filter-region');
  MAP_DATA.regions.forEach(r => {
    if (['AMOGUS','ABOBA','OCEAN'].includes(r.id)) return;
    const cnt = MAP_DATA.waypoints.filter(w => w.region===r.id && !w.hidden).length;
    rList.appendChild(mkFilterItem(r.id, r.name, '#c9a84c', cnt, 'region'));
  });
  const fList = $('filter-faction');
  MAP_DATA.factions.forEach(f => {
    const cnt = MAP_DATA.waypoints.filter(w => wpFaction(w)===f.id && !w.hidden).length;
    fList.appendChild(mkFilterItem(f.id, FAC[f.id]||f.id, f.color, cnt, 'faction'));
  });
}

function mkFilterItem(id, label, color, count, type) {
  const div = document.createElement('div');
  div.className = 'f-item';
  div.innerHTML = `<div class="f-check on" id="fc-${type}-${id}"></div><div class="f-color" style="background:${color}"></div><span class="f-label">${label}</span><span class="f-count">${count}</span>`;
  div.addEventListener('click', () => {
    const set = activeFilters[type+'s'];
    const chk = $(`fc-${type}-${id}`);
    if (set.has(id)) { set.delete(id); chk.classList.remove('on'); }
    else             { set.add(id);    chk.classList.add('on'); }
    applyFilters();
  });
  return div;
}

function applyFilters() {
  let vis = 0;
  allMarkers.forEach(({ wp, marker, type }) => {
    const show = activeFilters.regions.has(wp.region)
      && activeFilters.factions.has(wpFaction(wp))
      && activeFilters.types.has(type);
    if (show) { if (!markerLayer.hasLayer(marker)) markerLayer.addLayer(marker); vis++; }
    else      { if (markerLayer.hasLayer(marker))  markerLayer.removeLayer(marker); }
  });
  $('stats-visible').textContent = vis;
}

function switchFilterTab(tab) {
  $('filter-region').style.display  = tab==='region'  ? 'flex' : 'none';
  $('filter-faction').style.display = tab==='faction' ? 'flex' : 'none';
  $('ftab-region').classList.toggle('active',  tab==='region');
  $('ftab-faction').classList.toggle('active', tab==='faction');
}

// ── Search ─────────────────────────────────────────────
function setupSearch() {
  const inp  = $('search-input');
  const clr  = $('search-clear');
  const drop = $('search-results');

  inp.addEventListener('input', () => {
    const q = inp.value.trim().toLowerCase();
    clr.style.display = q ? 'block' : 'none';
    if (!q) { drop.style.display = 'none'; return; }
    const matches = MAP_DATA.waypoints
      .filter(w => !w.hidden && (wpName(w).toLowerCase().includes(q) || w.id.toLowerCase().includes(q)))
      .slice(0, 20);
    drop.innerHTML = '';
    if (!matches.length) {
      drop.innerHTML = '<div class="s-item s-empty">Ничего не найдено</div>';
    } else {
      matches.forEach(wp => {
        const type = getWpType(wp.id);
        const typeIcons = { capital:'★', fortress:'❖', port:'◯', resource:'◈', city:'•' };
        const item = document.createElement('div');
        item.className = 's-item';
        item.innerHTML = `<div class="s-dot" style="background:${wp.color}"></div><div class="s-info"><div class="s-name">${wpName(wp)} <span style="opacity:.4;font-size:11px">${typeIcons[type]||''}</span></div><div class="s-region">${wp.regionName}</div></div>`;
        item.addEventListener('click', () => {
          mapL.flyTo(wpLL(wp), 1, { duration: 0.7 });
          drop.style.display = 'none'; inp.value = ''; clr.style.display = 'none';
          setTimeout(() => allMarkers.find(m => m.wp.id === wp.id)?.marker.openPopup(), 750);
        });
        drop.appendChild(item);
      });
    }
    drop.style.display = 'block';
  });
  clr.addEventListener('click', () => { inp.value=''; clr.style.display='none'; drop.style.display='none'; });
  document.addEventListener('click', e => { if (!e.target.closest('.widget-search')) drop.style.display='none'; });
}

// ── Sidebar toggles ────────────────────────────────────
function toggleCard(id) {
  const el = $(id), arrow = $('arrow-' + id);
  const wasHidden = el.style.display === 'none';
  el.style.display = wasHidden ? 'block' : 'none';
  arrow?.classList.toggle('closed', !wasHidden);
}

// ── Boot ──────────────────────────────────────────────
setupSearch();
initMap();

window.addEventListener('storage', (e) => {
  if (e.key === 'tortuga_admin_data') {
    loadOverrides();
    if (typeof renderTerritories === 'function') renderTerritories();
    if (typeof buildMarkers === 'function') buildMarkers();
    if (typeof updateMapBgAndBounds === 'function') updateMapBgAndBounds();
    if (typeof renderSeaRoutes === 'function' && typeof seaRouteLayer !== 'undefined') {
      seaRouteLayer.clearLayers();
      renderSeaRoutes();
    }
  }
});
