/* ══════════════════════════════════════════════
   Tortuga Admin Panel — admin.js
   Security: token + SHA-256 password + rate limiting
   ══════════════════════════════════════════════ */

// ── Constants ────────────────────────────────────
const MAP_IMAGE_PATH = 'assets/map.png';
const IMG_W = 5291, IMG_H = 5067;
const SCALE = 128, ORIG_X = 810, ORIG_Z = 730;
const STORAGE_KEY  = 'tortuga_admin_data';
const SESSION_KEY  = 'tortuga_admin_session';
const LOCK_KEY     = 'tortuga_admin_lock';

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

// ── State ────────────────────────────────────────
let maps = {};       // { points, territories, sea }
let adminData = {};  // merged edits
let selectedWP = null;
let pickingPosition = false;
let pendingTerritoryPoints = null;
let drawControl = null;
let terrLayer = null;
let seaRouteLayer = null;
let terrainLayer = null;
let currentTerritoryMode = 'factions';

// ── SHA-256 (native WebCrypto) ───────────────────
async function sha256(str) {
  const buf = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(str));
  return Array.from(new Uint8Array(buf)).map(b => b.toString(16).padStart(2,'0')).join('');
}

// ════════════════════════════════════════════════
//   SECURITY
//   Слой 1: Apache Basic Auth (.htaccess) — блокирует страницу на уровне сервера
//   Слой 2: SHA-256 пароль (ADMIN_CFG.pwHash) — второй фактор, защита сессии
// ════════════════════════════════════════════════

// Защита от брутфорса (сессионная блокировка)
function getLockState() {
  try {
    const s = sessionStorage.getItem(LOCK_KEY);
    return s ? JSON.parse(s) : { attempts: 0, lockedUntil: 0 };
  } catch { return { attempts: 0, lockedUntil: 0 }; }
}
function saveLockState(s) { sessionStorage.setItem(LOCK_KEY, JSON.stringify(s)); }

function showLoginError(msg) {
  const el = document.getElementById('login-error');
  el.textContent = msg; el.style.display = 'block';
}

async function attemptLogin() {
  const lock = getLockState();
  const now = Date.now();

  if (lock.lockedUntil > now) {
    const mins = Math.ceil((lock.lockedUntil - now) / 60000);
    showLoginError(`Слишком много попыток. Повторите через ${mins} мин.`);
    return;
  }

  const pw = document.getElementById('login-pw').value;
  if (!pw) { showLoginError('Введите пароль.'); return; }

  try {
    const res = await fetch('/api/admin/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ password: pw })
    });
    if (res.ok) {
      const data = await res.json();
      if (data.token) {
        sessionStorage.setItem('tortuga_admin_token', data.token);
        sessionStorage.setItem(SESSION_KEY, 'ok:backend');
        saveLockState({ attempts: 0, lockedUntil: 0 });
        initAdminPanel();
        return;
      }
    }
  } catch (err) {
    console.warn('Backend login check offline, using local verification');
  }

  const hash = await sha256(pw);
  if (hash === ADMIN_CFG.pwHash) {
    // Успех — сохраняем сессию (сбрасывается при закрытии вкладки)
    sessionStorage.setItem(SESSION_KEY, 'ok:' + hash.slice(0,8));
    saveLockState({ attempts: 0, lockedUntil: 0 });
    initAdminPanel();
  } else {
    const attempts = lock.attempts + 1;
    if (attempts >= ADMIN_CFG.maxAttempts) {
      const lockedUntil = now + ADMIN_CFG.lockMinutes * 60 * 1000;
      saveLockState({ attempts, lockedUntil });
      showLoginError(`Неверный пароль. Блокировка на ${ADMIN_CFG.lockMinutes} мин.`);
    } else {
      saveLockState({ attempts, lockedUntil: 0 });
      showLoginError(`Неверный пароль. Попыток осталось: ${ADMIN_CFG.maxAttempts - attempts}`);
    }
    document.getElementById('login-pw').value = '';
  }
}

function checkSession() {
  return sessionStorage.getItem(SESSION_KEY)?.startsWith('ok:') === true;
}

function logout() {
  sessionStorage.removeItem(SESSION_KEY);
  window.location.reload();
}

// Enter key on login
document.getElementById('login-pw').addEventListener('keydown', e => {
  if (e.key === 'Enter') attemptLogin();
});

// ════════════════════════════════════════════════
//   BOOT
// ════════════════════════════════════════════════
async function boot() {
  // Если мы здесь — Basic Auth уже пройден (сервер не отдаст страницу без него)
  // Показываем экран ввода пароля
  document.getElementById('login-screen').style.display = 'flex';

  // Если сессия ещё живёт — пропускаем форму
  if (checkSession()) {
    initAdminPanel();
    return;
  }
}

boot();

// ── Load admin data from localStorage / API ───────────
// ── Load admin data from localStorage / API ───────────
async function loadAdminData() {
  let apiData = null;
  try {
    const res = await fetch('/api/map/data');
    if (res.ok) {
      const data = await res.json();
      if (data && data.status === 'success') {
        apiData = data;
        console.log('✅ Loaded map data from FastAPI + PostgreSQL backend');
      }
    }
  } catch (err) {
    console.warn('⚠️ Backend /api/map/data unreachable, falling back to localStorage');
  }

  const baseOverrides = typeof OVERRIDES !== 'undefined' ? JSON.parse(JSON.stringify(OVERRIDES)) : {
    capitals: [], fortresses: [], ports: [], resources: [],
    renames: {}, leaders: {}, factions: {}, seaRoutes: [], terrainZones: [],
    terrainMultipliers: { default: 1.0, swamp: 2.0, mountain: 5.0, hills: 1.5, forest: 1.3, ocean: 1.0 }
  };

  adminData = {
    ...baseOverrides,
    ...(apiData?.overrides || {}),
    territories: (apiData?.territories && apiData.territories.length) ? apiData.territories : (baseOverrides.territories || []),
    terrainZones: (apiData?.terrainZones && apiData.terrainZones.length) ? apiData.terrainZones : (baseOverrides.terrainZones || []),
    terrainMultipliers: apiData?.terrainMultipliers || baseOverrides.terrainMultipliers || { default: 1.0, swamp: 2.0, mountain: 5.0, hills: 1.5, forest: 1.3, ocean: 1.0 }
  };

  if (!adminData.capitals || !adminData.capitals.length)     adminData.capitals   = [...(baseOverrides.capitals || [])];
  if (!adminData.fortresses || !adminData.fortresses.length) adminData.fortresses = [...(baseOverrides.fortresses || [])];
  if (!adminData.ports || !adminData.ports.length)           adminData.ports      = [...(baseOverrides.ports || [])];
  if (!adminData.seaRoutes || !adminData.seaRoutes.length)   adminData.seaRoutes  = [...(baseOverrides.seaRoutes || [])];
  if (!adminData.factions) adminData.factions = { ...(baseOverrides.factions || {}) };
  if (!adminData.leaders)  adminData.leaders  = { ...(baseOverrides.leaders || {}) };
  if (!adminData.renames)  adminData.renames  = { ...(baseOverrides.renames || {}) };
  if (!adminData.territories) adminData.territories = [...(typeof TERRITORIES !== 'undefined' ? TERRITORIES : [])];
  const dbWp = apiData?.overrides?.customWaypoints || apiData?.overrides?.customPoints || [];
  if (dbWp.length) adminData.customWaypoints = dbWp;
  if (!adminData.customWaypoints) adminData.customWaypoints = [];

  // Restore any local browser edits from localStorage if DB doesn't have them yet
  try {
    const localRaw = localStorage.getItem(STORAGE_KEY);
    if (localRaw) {
      const ad = JSON.parse(localRaw);
      if (!adminData.territories || !adminData.territories.length) if (ad.territories) adminData.territories = ad.territories;
      if (!Object.keys(adminData.factions || {}).length) if (ad.factions) Object.assign(adminData.factions, ad.factions);
      if (!Object.keys(adminData.leaders || {}).length)  if (ad.leaders)  Object.assign(adminData.leaders, ad.leaders);
      if (!Object.keys(adminData.renames || {}).length)  if (ad.renames)  Object.assign(adminData.renames, ad.renames);
      if (ad.customWaypoints && ad.customWaypoints.length) {
        if (!adminData.customWaypoints) adminData.customWaypoints = [];
        for (const cwp of ad.customWaypoints) {
          const idx = adminData.customWaypoints.findIndex(w => w.id === cwp.id);
          if (idx >= 0) adminData.customWaypoints[idx] = { ...adminData.customWaypoints[idx], ...cwp };
          else adminData.customWaypoints.push(cwp);
        }
      }
      if (!adminData.terrainZones || !adminData.terrainZones.length)       if (ad.terrainZones)    adminData.terrainZones = ad.terrainZones;
      if (ad.seaRoutes && ad.seaRoutes.length) adminData.seaRoutes = ad.seaRoutes;
      if (ad.mapSettings) adminData.mapSettings = { ...(adminData.mapSettings || {}), ...ad.mapSettings };
      if (ad.terrainMultipliers) adminData.terrainMultipliers = { ...(adminData.terrainMultipliers || {}), ...ad.terrainMultipliers };
    }
  } catch (e) {}

  if (adminData.seaRoutes && Array.isArray(adminData.seaRoutes)) {
    adminData.seaRoutes = adminData.seaRoutes.map(item => {
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

  mergeCustomWaypoints();
}

function mergeCustomWaypoints() {
  if (typeof MAP_DATA !== 'undefined' && MAP_DATA?.waypoints && adminData.customWaypoints?.length) {
    for (const cwp of adminData.customWaypoints) {
      if (cwp.worldX === undefined && cwp.imgX !== undefined) cwp.worldX = Math.round((cwp.imgX - ORIG_X + 0.5) * SCALE);
      if (cwp.worldZ === undefined && cwp.imgY !== undefined) cwp.worldZ = Math.round((cwp.imgY - ORIG_Z + 0.5) * SCALE);
      const idx = MAP_DATA.waypoints.findIndex(w => w.id === cwp.id);
      if (idx >= 0) MAP_DATA.waypoints[idx] = { ...MAP_DATA.waypoints[idx], ...cwp };
      else MAP_DATA.waypoints.push(cwp);
    }
  }
}

async function saveAdminData() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(adminData));
  try {
    const token = sessionStorage.getItem('tortuga_admin_token');
    const res = await fetch('/api/admin/save', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token ? 'Bearer ' + token : ''
      },
      body: JSON.stringify({
        overrides: {
          capitals: adminData.capitals,
          fortresses: adminData.fortresses,
          ports: adminData.ports,
          resources: adminData.resources,
          renames: adminData.renames,
          leaders: adminData.leaders,
          factions: adminData.factions,
          seaRoutes: adminData.seaRoutes,
          settings: adminData.mapSettings,
          customPoints: adminData.customWaypoints,
          customWaypoints: adminData.customWaypoints,
          terrainZones: adminData.terrainZones,
          terrainMultipliers: adminData.terrainMultipliers
        },
        territories: adminData.territories,
        terrainZones: adminData.terrainZones,
        terrainMultipliers: adminData.terrainMultipliers,
        token: token || ''
      })
    });
    if (res.ok) {
      console.log('✅ Synchronized with FastAPI + PostgreSQL backend');
    }
  } catch (err) {
    console.warn('⚠️ Server sync failed, saved locally:', err);
  }
}

// ── Helpers ──────────────────────────────────────
const toLL   = (ix, iy) => L.latLng(-iy, ix);
const wpLL   = wp => toLL(wp.imgX, wp.imgY);
const getWP  = id => MAP_DATA.waypoints.find(w => w.id === id);

function getWpType(id) {
  if (adminData.capitals?.includes(id))   return 'capital';
  if (adminData.fortresses?.includes(id)) return 'fortress';
  if (adminData.ports?.includes(id))      return 'port';
  if (adminData.resources?.includes(id))  return 'resource';
  return 'city';
}

function wpName(wp) { return adminData.renames?.[wp.id] || wp.name; }

// ════════════════════════════════════════════════
//   INIT ADMIN PANEL
// ════════════════════════════════════════════════
async function initAdminPanel() {
  document.getElementById('login-screen').style.display = 'none';
  document.getElementById('admin-panel').style.display = 'flex';
  document.getElementById('admin-panel').style.flexDirection = 'column';

  await loadAdminData();
  await initMaps();
  renderPointList();
  renderTerrList();
  renderSeaList();
  populateSeaSelects();
}

// ── Maps ─────────────────────────────────────────
async function initMaps() {
  const imgSize = await loadImageSize(MAP_IMAGE_PATH);
  const bounds = [[0, 0], [-imgSize.h, imgSize.w]];

  const mapCfg = adminData.mapSettings || OVERRIDES?.mapSettings || {};
  if (mapCfg.bgColor) {
    document.documentElement.style.setProperty('--map-bg', mapCfg.bgColor);
  }

  const minX = Math.max(0, Math.min(8192, mapCfg.minX ?? 0));
  const minY = Math.max(0, Math.min(8192, mapCfg.minY ?? 0));
  const maxX = Math.max(minX + 10, Math.min(8192, mapCfg.maxX ?? imgSize.w));
  const maxY = Math.max(minY + 10, Math.min(8192, mapCfg.maxY ?? imgSize.h));
  const bounded = (minX > 0 || minY > 0 || maxX < imgSize.w || maxY < imgSize.h);
  const viewBounds = bounded ? L.latLngBounds(toLL(minX, minY), toLL(maxX, maxY)) : null;

  // All maps share same bounds/image
  for (const id of ['adm-map-points','adm-map-territories','adm-map-sea','adm-map-settings']) {
    const key = id.replace('adm-map-','');
    const m = L.map(id, { crs: L.CRS.Simple, minZoom: -3, maxZoom: 4, zoomSnap: 0.25 });
    L.imageOverlay(MAP_IMAGE_PATH, bounds, { opacity: 1, interactive: false }).addTo(m);
    if (viewBounds && key !== 'settings') {
      m.setMaxBounds(viewBounds);
      m.options.maxBoundsViscosity = 1.0; // Rigid wall, zero throwback lag!
      m.fitBounds(viewBounds);
      try {
        const minZ = m.getBoundsZoom(viewBounds, false);
        m.setMinZoom(Math.max(-3, minZ));
      } catch(e) {}
    } else {
      m.setView([-1560, 860], -1);
    }
    maps[key] = m;
  }

  // Points map
  addMarkersToMap(maps.points);
  maps.points.on('click', onPointsMapClick);

  // Territories map
  terrLayer = L.layerGroup().addTo(maps.territories);
  terrainLayer = L.layerGroup();
  renderTerritoriesOnMap();
  renderTerrainOnMap();
  initDrawControl();

  // Sea map
  seaRouteLayer = L.layerGroup().addTo(maps.sea);
  addPortMarkersToSeaMap();
  renderSeaRoutesOnMap();
  maps.sea.on('click', onSeaMapClick);

  // Settings map
  if (maps.settings) {
    maps.settings.on('click', onSettingsMapClick);
    previewMapBounds();
  }
}

let waterGrid = null;
const W_GRID = 1024, H_GRID = 1024;

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
      if (a > 100 && b > r + 3 && b >= g - 35 && b > 30) {
        waterGrid[i] = 1;
      }
    }
  } catch(e) { console.warn('Failed to init water grid:', e); }
}

function findWaterPath(x1, y1, x2, y2) {
  if (!waterGrid) return [[x1, y1], [x2, y2]];
  
  const imgW = (typeof IMG_W !== 'undefined' && IMG_W) ? IMG_W : 5291;
  const imgH = (typeof IMG_H !== 'undefined' && IMG_H) ? IMG_H : 5067;
  const toGridX = (val) => Math.max(0, Math.min(W_GRID - 1, Math.floor((val / imgW) * W_GRID)));
  const toGridY = (val) => Math.max(0, Math.min(H_GRID - 1, Math.floor((val / imgH) * H_GRID)));
  const gx1 = toGridX(x1), gy1 = toGridY(y1);
  const gx2 = toGridX(x2), gy2 = toGridY(y2);

  const findNearestWater = (gx, gy) => {
    if (waterGrid[gy * W_GRID + gx] === 1) return [gx, gy];
    for (let r = 1; r <= 60; r++) {
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

  const open = [startIdx];
  const openSet = new Set([startIdx]);
  const dirs = [
    [0,-1,1], [0,1,1], [-1,0,1], [1,0,1],
    [-1,-1,1.414], [1,-1,1.414], [-1,1,1.414], [1,1,1.414]
  ];

  let found = false, iter = 0;
  while (open.length > 0 && iter++ < 60000) {
    let lowestIdx = 0, lowestF = fScore[open[0]];
    for (let i = 1; i < open.length; i++) {
      if (fScore[open[i]] < lowestF) { lowestF = fScore[open[i]]; lowestIdx = i; }
    }
    const curr = open[lowestIdx];
    if (curr === endIdx) { found = true; break; }

    open[lowestIdx] = open[open.length - 1];
    open.pop();
    openSet.delete(curr);
    closed[curr] = 1;

    const cx = curr % W_GRID, cy = Math.floor(curr / W_GRID);
    for (const [dx, dy, cost] of dirs) {
      const nx = cx + dx, ny = cy + dy;
      if (nx < 0 || nx >= W_GRID || ny < 0 || ny >= H_GRID) continue;
      const nIdx = ny * W_GRID + nx;
      if (closed[nIdx] || waterGrid[nIdx] === 0) continue;
      if (dx !== 0 && dy !== 0 && (waterGrid[cy * W_GRID + nx] === 0 || waterGrid[ny * W_GRID + cx] === 0)) continue;

      const tentG = gScore[curr] + cost;
      if (tentG < gScore[nIdx]) {
        cameFrom[nIdx] = curr;
        gScore[nIdx] = tentG;
        fScore[nIdx] = tentG + Math.hypot(ex - nx, ey - ny);
        if (!openSet.has(nIdx)) { open.push(nIdx); openSet.add(nIdx); }
      }
    }
  }

  if (!found) return [[x1, y1], [x2, y2]];

  const gridPath = [];
  let curr = endIdx;
  while (curr !== -1) {
    gridPath.push([curr % W_GRID, Math.floor(curr / W_GRID)]);
    if (curr === startIdx) break;
    curr = cameFrom[curr];
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
  return mapPath;
}

function loadImageSize(src) {
  return new Promise(resolve => {
    const img = new Image();
    img.onload = () => {
      initWaterGrid(img);
      if (typeof renderSeaRoutesOnMap === 'function') renderSeaRoutesOnMap();
      resolve({ w: img.naturalWidth, h: img.naturalHeight });
    };
    img.onerror = () => resolve({ w: IMG_W, h: IMG_H });
    img.src = src;
  });
}

// ── Points Map ────────────────────────────────────
function addMarkersToMap(m) {
  MAP_DATA.waypoints.forEach(wp => {
    if (wp.hidden) return;
    const marker = L.circleMarker(wpLL(wp), {
      radius: getWpType(wp.id) === 'capital' ? 6 : 4,
      color: wp.color, fillColor: wp.color, fillOpacity: 0.85, weight: 1,
    });
    marker.bindTooltip(wpName(wp), { sticky: false, direction: 'top', className: 'territory-tooltip' });
    marker.on('click', () => selectPoint(wp.id));
    marker.addTo(m);
  });
}

function startAddingNewPoint() {
  window.addingNewPoint = true;
  maps.points._container.style.cursor = 'crosshair';
  adminToast('Кликните на карте, где создать новую точку (крепость/ресурс)');
}

function onPointsMapClick(e) {
  const ix = Math.round(e.latlng.lng), iy = Math.round(-e.latlng.lat);
  if (window.addingNewPoint) {
    window.addingNewPoint = false;
    maps.points._container.style.cursor = '';
    const id = 'custom_' + Date.now().toString(36);
    const newWp = {
      id: id,
      name: "Новая крепость",
      imgX: ix,
      imgY: iy,
      worldX: Math.round((ix - ORIG_X + 0.5) * SCALE),
      worldZ: Math.round((iy - ORIG_Z + 0.5) * SCALE),
      type: "fortress",
      faction: "UNALIGNED",
      region: "Custom",
      regionName: "Пользовательские",
      custom: true
    };
    if (!adminData.customWaypoints) adminData.customWaypoints = [];
    adminData.customWaypoints.push(newWp);
    if (!adminData.fortresses) adminData.fortresses = [];
    adminData.fortresses.push(id);
    saveAdminData();
    mergeCustomWaypoints();
    addMarkersToMap(maps.points);
    renderPointList();
    selectPoint(id);
    adminToast('Новая точка создана: ' + id);
    return;
  }
  if (!pickingPosition || !selectedWP) return;
  document.getElementById('pe-x').value = ix;
  document.getElementById('pe-y').value = iy;
  pickingPosition = false;
  maps.points.off('click', onPointsMapClick);
  maps.points.on('click', onPointsMapClick);
  document.getElementById('pe-pick-hint').style.display = 'none';
  maps.points._container.style.cursor = '';
  adminToast('Позиция выбрана');
}

// ── Point List ────────────────────────────────────
function renderPointList() {
  const list = document.getElementById('pt-list');
  list.innerHTML = '';
  const q = document.getElementById('pt-search').value.trim().toLowerCase();
  const wps = MAP_DATA.waypoints.filter(wp => !wp.hidden
    && (q === '' || wpName(wp).toLowerCase().includes(q) || wp.id.toLowerCase().includes(q)));

  const typeIcons = { capital:'★', fortress:'❖', port:'◯', resource:'◈', city:'•' };
  wps.forEach(wp => {
    const type = getWpType(wp.id);
    const div = document.createElement('div');
    div.className = 'pt-item' + (selectedWP === wp.id ? ' selected' : '');
    div.innerHTML = `<div class="pt-dot" style="background:${wp.color}"></div><span class="pt-name">${wpName(wp)}</span><span class="pt-type">${typeIcons[type]||'•'}</span>`;
    div.addEventListener('click', () => selectPoint(wp.id));
    list.appendChild(div);
  });
}

function filterPointsList() { renderPointList(); }

function selectPoint(id) {
  selectedWP = id;
  const wp = getWP(id);
  if (!wp) return;

  renderPointList(); // re-render to update selection class

  const type = getWpType(id);
  document.getElementById('pe-id').textContent  = wp.id;
  document.getElementById('pe-name').value       = wpName(wp);
  document.getElementById('pe-type').value       = type;
  document.getElementById('pe-leader').value     = adminData.leaders?.[id] || '';
  document.getElementById('pe-faction').value    = adminData.factions?.[id] || '';
  document.getElementById('pe-x').value          = wp.imgX;
  document.getElementById('pe-y').value          = wp.imgY;
  document.getElementById('pt-editor').style.display = 'block';

  const delBtn = document.getElementById('pe-btn-delete');
  if (delBtn) delBtn.style.display = (wp.custom || adminData.customWaypoints?.some(w => w.id === id)) ? 'block' : 'none';

  // Pan map to point
  maps.points.flyTo(wpLL(wp), 1, { duration: 0.5 });
}

function pickPositionOnMap() {
  pickingPosition = true;
  document.getElementById('pe-pick-hint').style.display = 'block';
  maps.points._container.style.cursor = 'crosshair';
  adminToast('Кликни на карте для выбора позиции');
}

function savePointEdit() {
  const id   = document.getElementById('pe-id').textContent;
  const name = document.getElementById('pe-name').value.trim();
  const type = document.getElementById('pe-type').value;
  const leader = document.getElementById('pe-leader').value.trim();
  const newX = parseInt(document.getElementById('pe-x').value);
  const newY = parseInt(document.getElementById('pe-y').value);

  // Update renames
  const wp = getWP(id);
  if (name && name !== wp?.name) adminData.renames[id] = name;
  else delete adminData.renames[id];

  // Update type arrays
  const typeArrays = { capital: 'capitals', fortress: 'fortresses', port: 'ports', resource: 'resources' };
  Object.values(typeArrays).forEach(arr => {
    adminData[arr] = (adminData[arr] || []).filter(x => x !== id);
  });
  if (type !== 'city') {
    if (!adminData[typeArrays[type]]) adminData[typeArrays[type]] = [];
    adminData[typeArrays[type]].push(id);
  }

  // Update leader
  if (leader) adminData.leaders[id] = leader;
  else delete adminData.leaders[id];

  // Update faction
  const faction = document.getElementById('pe-faction').value;
  if (!adminData.factions) adminData.factions = {};
  if (faction) adminData.factions[id] = faction;
  else delete adminData.factions[id];

  if (!adminData.customWaypoints) adminData.customWaypoints = [];
  let idx = adminData.customWaypoints.findIndex(w => w.id === id);
  if (idx < 0) {
    adminData.customWaypoints.push({
      id: id,
      name: name || wp?.name || id,
      type: type,
      imgX: !isNaN(newX) ? newX : wp?.imgX,
      imgY: !isNaN(newY) ? newY : wp?.imgY,
      worldX: !isNaN(newX) ? Math.round((newX - ORIG_X + 0.5) * SCALE) : wp?.worldX,
      worldZ: !isNaN(newY) ? Math.round((newY - ORIG_Z + 0.5) * SCALE) : wp?.worldZ,
      faction: faction || wp?.faction || 'UNALIGNED',
      region: wp?.region || 'Custom',
      regionName: wp?.regionName || 'Пользовательские',
      custom: wp?.custom || false
    });
  } else {
    adminData.customWaypoints[idx].name = name || wp?.name;
    adminData.customWaypoints[idx].type = type;
    if (!isNaN(newX)) {
      adminData.customWaypoints[idx].imgX = newX;
      adminData.customWaypoints[idx].worldX = Math.round((newX - ORIG_X + 0.5) * SCALE);
    }
    if (!isNaN(newY)) {
      adminData.customWaypoints[idx].imgY = newY;
      adminData.customWaypoints[idx].worldZ = Math.round((newY - ORIG_Z + 0.5) * SCALE);
    }
    if (faction) adminData.customWaypoints[idx].faction = faction;
  }

  saveAdminData();
  mergeCustomWaypoints();
  addMarkersToMap(maps.points);
  renderPointList();
  adminToast(`✓ Сохранено: ${name || wp?.name}`);
}

function deleteCustomPoint() {
  if (!selectedWP) return;
  const id = selectedWP;
  if (!confirm(`Удалить точку ${id}?`)) return;
  
  if (adminData.customWaypoints) {
    adminData.customWaypoints = adminData.customWaypoints.filter(w => w.id !== id);
  }
  if (MAP_DATA?.waypoints) {
    MAP_DATA.waypoints = MAP_DATA.waypoints.filter(w => w.id !== id);
  }
  const typeArrays = ['capitals', 'fortresses', 'ports', 'resources'];
  typeArrays.forEach(arr => {
    adminData[arr] = (adminData[arr] || []).filter(x => x !== id);
  });
  delete adminData.renames?.[id];
  delete adminData.factions?.[id];
  delete adminData.leaders?.[id];

  saveAdminData();
  mergeCustomWaypoints();
  addMarkersToMap(maps.points);
  cancelPointEdit();
  adminToast('Точка удалена');
}

function cancelPointEdit() {
  selectedWP = null;
  document.getElementById('pt-editor').style.display = 'none';
  renderPointList();
}

// ── Territories ───────────────────────────────────
let drawnItems = null;

function normalizeToMultiPolygon(pts) {
  if (!pts || !pts.length) return [];
  if (typeof pts[0]?.[0]?.[0]?.[0] === 'number') return pts;
  if (typeof pts[0]?.[0]?.[0] === 'number') return pts.map(ring => [ring]);
  if (typeof pts[0]?.[0] === 'number') return [[pts]];
  return [];
}

function initDrawControl() {
  drawnItems = new L.FeatureGroup().addTo(maps.territories);
  drawControl = new L.Control.Draw({
    draw: {
      polygon: {
        allowIntersection: false,
        shapeOptions: { color: '#d2a555', fillOpacity: 0.15, weight: 2 },
      },
      rectangle: {
        shapeOptions: { color: '#d2a555', fillOpacity: 0.15, weight: 2 },
      },
      polyline: false, circle: false,
      circlemarker: false, marker: false,
    },
    edit: { featureGroup: drawnItems, remove: true },
  });
  maps.territories.addControl(drawControl);

  maps.territories.on(L.Draw.Event.CREATED, e => {
    const latlngs = e.layer.getLatLngs();
    const rings = Array.isArray(latlngs[0]) ? latlngs : [latlngs];
    const newLoop = rings[0].map(ll => [ll.lng, -ll.lat]);
    
    saveToHistory();
    const mode = document.querySelector('input[name="mw-mode"]:checked')?.value || (window.mwAddMode ? 'add' : 'new');
    pendingTerritoryPoints = applyTerritoryOperation(pendingTerritoryPoints, newLoop, mode);
    updateTerritoryPreview();
    
    if (mode === 'add') adminToast('Область добавлена');
    else if (mode === 'subtract') adminToast('Область вычтена');
    else adminToast('Полигон создан');
  });

  maps.territories.on(L.Draw.Event.EDITED, e => {
    saveToHistory();
    const editedPolys = [];
    drawnItems.eachLayer(layer => {
      const latlngs = layer.getLatLngs();
      const extractRings = (arr) => {
        if (!Array.isArray(arr) || !arr.length) return [];
        if (arr[0] && typeof arr[0].lat === 'number') {
          return [arr.map(ll => [ll.lng, -ll.lat])];
        }
        let res = [];
        for (const sub of arr) res.push(...extractRings(sub));
        return res;
      };
      const r = extractRings(latlngs);
      if (r.length) editedPolys.push(r);
    });
    pendingTerritoryPoints = editedPolys;
    updateTerritoryPreview();
    adminToast('Изменения сохранены');
  });

  maps.territories.on(L.Draw.Event.DELETED, e => {
    saveToHistory();
    const remainingPolys = [];
    drawnItems.eachLayer(layer => {
      const latlngs = layer.getLatLngs();
      const extractRings = (arr) => {
        if (!Array.isArray(arr) || !arr.length) return [];
        if (arr[0] && typeof arr[0].lat === 'number') {
          return [arr.map(ll => [ll.lng, -ll.lat])];
        }
        let res = [];
        for (const sub of arr) res.push(...extractRings(sub));
        return res;
      };
      const r = extractRings(latlngs);
      if (r.length) remainingPolys.push(r);
    });
    pendingTerritoryPoints = remainingPolys.length ? remainingPolys : null;
    updateTerritoryPreview();
    adminToast('🗑️ Выбранные фигуры удалены!');
  });
}

let editingTerritoryId = null;
let territoryHistory = [];

function saveToHistory() {
  if (pendingTerritoryPoints) {
    territoryHistory.push(JSON.parse(JSON.stringify(pendingTerritoryPoints)));
    if (territoryHistory.length > 20) territoryHistory.shift();
  }
}

function toTurfGeometry(pts) {
  const polys = normalizeToMultiPolygon(pts);
  const validPolys = [];
  for (const poly of polys) {
    const closedRings = [];
    for (const ring of poly) {
      if (!ring || ring.length < 3) continue;
      const closed = ring.map(p => [p[0], p[1]]);
      if (closed[0][0] !== closed[closed.length-1][0] || closed[0][1] !== closed[closed.length-1][1]) {
        closed.push([closed[0][0], closed[0][1]]);
      }
      if (closed.length >= 4) closedRings.push(closed);
    }
    if (closedRings.length) {
      try { validPolys.push(turf.polygon(closedRings)); } catch(e) {}
    }
  }
  if (!validPolys.length) return null;
  let combined = validPolys[0];
  for (let i = 1; i < validPolys.length; i++) {
    try { combined = turf.union(combined, validPolys[i]); } catch(e) {}
  }
  return combined;
}

function fromTurfGeometry(turfObj) {
  if (!turfObj) return null;
  const geom = turfObj.geometry || turfObj;
  if (geom.type === 'Polygon') {
    return [ geom.coordinates.map(ring => ring.slice(0, -1)) ];
  }
  if (geom.type === 'MultiPolygon') {
    return geom.coordinates.map(poly => poly.map(ring => ring.slice(0, -1)));
  }
  return null;
}

function applyTerritoryOperation(existingPts, newLoop, mode) {
  if (mode === 'new' || !existingPts) {
    return normalizeToMultiPolygon([newLoop]);
  }
  if (typeof turf === 'undefined') {
    const polys = normalizeToMultiPolygon(existingPts);
    if (mode === 'add') return [...polys, [newLoop]];
    return normalizeToMultiPolygon([newLoop]);
  }

  try {
    const existingGeom = toTurfGeometry(existingPts);
    const newGeom = toTurfGeometry([newLoop]);
    if (!existingGeom) return normalizeToMultiPolygon([newLoop]);
    if (!newGeom) return normalizeToMultiPolygon(existingPts);

    let resultGeom;
    if (mode === 'add') resultGeom = turf.union(existingGeom, newGeom);
    else if (mode === 'subtract') resultGeom = turf.difference(existingGeom, newGeom);
    else return normalizeToMultiPolygon([newLoop]);

    return fromTurfGeometry(resultGeom) || normalizeToMultiPolygon(existingPts);
  } catch(err) {
    console.warn('Turf boolean operation error:', err);
    adminToast('⚠️ Сложная геометрия, применено обычное добавление.');
    const polys = normalizeToMultiPolygon(existingPts);
    return [...polys, [newLoop]];
  }
}

function addTerritoryLoop(newLoop) {
  saveToHistory();
  pendingTerritoryPoints = applyTerritoryOperation(pendingTerritoryPoints, newLoop, 'add');
  updateTerritoryPreview();
}

function undoLastTerritoryPart() {
  if (!territoryHistory.length) {
    if (pendingTerritoryPoints) {
      clearTerritorySelection();
    } else {
      adminToast('↩️ История изменений пуста.');
    }
    return;
  }
  pendingTerritoryPoints = territoryHistory.pop();
  adminToast(`↩️ Шаг отменён. Осталось в истории: ${territoryHistory.length}`);
  updateTerritoryPreview();
}

function clearTerritorySelection() {
  pendingTerritoryPoints = null;
  editingTerritoryId = null;
  territoryHistory = [];
  if (magicWandActive) toggleMagicWand();
  document.getElementById('terr-form').style.display = 'none';
  document.getElementById('terr-draw-status').style.display = 'block';
  if (drawnItems) drawnItems.clearLayers();
  if (terrLayer) renderTerritoriesOnMap();
  adminToast('🗑️ Выделение сброшено.');
}

function updateTerritoryPreview() {
  if (!pendingTerritoryPoints) {
    document.getElementById('terr-form').style.display = 'none';
    document.getElementById('terr-draw-status').style.display = 'block';
    if (drawnItems) drawnItems.clearLayers();
    if (terrLayer) renderTerritoriesOnMap();
    return;
  }
  document.getElementById('terr-form').style.display = 'block';
  document.getElementById('terr-draw-status').style.display = 'none';
  
  const polys = normalizeToMultiPolygon(pendingTerritoryPoints);
  
  const countEl = document.getElementById('terr-part-count');
  if (countEl) countEl.textContent = polys.length;
  
  if (terrLayer) renderTerritoriesOnMap();
  
  if (drawnItems) {
    drawnItems.clearLayers();
    const faction = document.getElementById('terr-faction')?.value || 'NORTH';
    const color = FAC_COLORS[faction] || '#5ab8ff';
    
    polys.forEach(poly => {
      const lls = poly.map(ring => ring.map(([ix, iy]) => toLL(ix, iy)));
      L.polygon(lls, {
        color: color, weight: 2.5, dashArray: '6 6', fillOpacity: 0.35
      }).addTo(drawnItems);
    });
  }
}

function saveTerritory() {
  if (!pendingTerritoryPoints) return;
  const name = document.getElementById('terr-name').value.trim() || (currentTerritoryMode === 'terrain' ? 'Зона ландшафта' : 'Территория');
  const normPoints = normalizeToMultiPolygon(pendingTerritoryPoints);

  if (currentTerritoryMode === 'terrain') {
    const type = document.getElementById('terr-type')?.value || 'forest';
    if (!adminData.terrainZones) adminData.terrainZones = [];

    if (editingTerritoryId) {
      const idx = adminData.terrainZones.findIndex(t => t.id === editingTerritoryId);
      if (idx !== -1) {
        adminData.terrainZones[idx] = { id: editingTerritoryId, name, type, points: normPoints };
        adminToast(`✓ Зона "${name}" обновлена!`);
      } else {
        const id = 'trn_' + Date.now();
        adminData.terrainZones.push({ id, name, type, points: normPoints });
        adminToast(`✓ Зона "${name}" сохранена`);
      }
    } else {
      const id = 'trn_' + Date.now();
      adminData.terrainZones.push({ id, name, type, points: normPoints });
      adminToast(`✓ Зона "${name}" сохранена`);
    }
  } else {
    const faction = document.getElementById('terr-faction').value;
    const color   = FAC_COLORS[faction] || '#aaaaaa';
    if (!adminData.territories) adminData.territories = [];

    if (editingTerritoryId) {
      const idx = adminData.territories.findIndex(t => t.id === editingTerritoryId);
      if (idx !== -1) {
        adminData.territories[idx] = { id: editingTerritoryId, name, faction, color, points: normPoints };
        adminToast(`✓ Территория "${name}" обновлена!`);
      } else {
        const id = 'terr_' + Date.now();
        adminData.territories.push({ id, name, faction, color, points: normPoints });
        adminToast(`✓ Территория "${name}" сохранена`);
      }
    } else {
      const id = 'terr_' + Date.now();
      adminData.territories.push({ id, name, faction, color, points: normPoints });
      adminToast(`✓ Территория "${name}" сохранена`);
    }
  }

  pendingTerritoryPoints = null;
  editingTerritoryId = null;
  territoryHistory = [];
  saveAdminData();
  renderTerrList();
  if (drawnItems) drawnItems.clearLayers();
  renderTerritoriesOnMap();
  renderTerrainOnMap();
  document.getElementById('terr-form').style.display = 'none';
  document.getElementById('terr-draw-status').style.display = 'block';
  document.getElementById('terr-name').value = '';
}

function cancelTerrDraw() {
  clearTerritorySelection();
}

function deleteTerritory(id) {
  adminData.territories = (adminData.territories || []).filter(t => t.id !== id);
  saveAdminData(); renderTerrList(); renderTerritoriesOnMap();
  adminToast('Территория удалена');
}

function deleteTerrainZone(id) {
  adminData.terrainZones = (adminData.terrainZones || []).filter(t => t.id !== id);
  saveAdminData(); renderTerrList(); renderTerrainOnMap();
  adminToast('Зона удалена');
}

function editTerritory(id) {
  const t = (adminData.territories || []).find(x => x.id === id);
  if (!t) return;
  editingTerritoryId = t.id;
  territoryHistory = [];
  pendingTerritoryPoints = normalizeToMultiPolygon(t.points);
  
  document.getElementById('terr-name').value = t.name;
  if (t.faction) document.getElementById('terr-faction').value = t.faction;
  
  window.mwAddMode = true;
  const addRadio = document.querySelector('input[name="mw-mode"][value="add"]');
  if (addRadio) addRadio.checked = true;
  
  updateTerritoryPreview();
  adminToast(`Редактирование: "${t.name}"`);
}

function editTerrainZone(id) {
  const t = (adminData.terrainZones || []).find(x => x.id === id);
  if (!t) return;
  editingTerritoryId = t.id;
  territoryHistory = [];
  pendingTerritoryPoints = normalizeToMultiPolygon(t.points);
  
  document.getElementById('terr-name').value = t.name;
  if (t.type && document.getElementById('terr-type')) document.getElementById('terr-type').value = t.type;
  
  window.mwAddMode = true;
  const addRadio = document.querySelector('input[name="mw-mode"][value="add"]');
  if (addRadio) addRadio.checked = true;
  
  updateTerritoryPreview();
  adminToast(`Редактирование зоны: "${t.name}"`);
}

function switchTerritoryMode(mode) {
  currentTerritoryMode = mode;
  const btnFac = document.getElementById('terr-mode-btn-factions');
  const btnTerr = document.getElementById('terr-mode-btn-terrain');
  if (btnFac) btnFac.classList.toggle('active', mode === 'factions');
  if (btnTerr) btnTerr.classList.toggle('active', mode === 'terrain');

  const rowFac = document.getElementById('terr-row-faction');
  const rowTerr = document.getElementById('terr-row-terrain');
  if (rowFac) rowFac.style.display = mode === 'factions' ? 'block' : 'none';
  if (rowTerr) rowTerr.style.display = mode === 'terrain' ? 'block' : 'none';

  const listTitle = document.getElementById('terr-list-title');
  if (listTitle) listTitle.textContent = mode === 'factions' ? 'Существующие территории' : 'Существующие зоны ландшафта';

  const hintText = document.getElementById('terr-hint-text');
  if (hintText) {
    hintText.textContent = mode === 'factions'
      ? 'Нарисуй полигон на карте справа, затем выбери фракцию и сохрани.'
      : 'Нарисуй полигон на карте справа, выбери тип ландшафта и сохрани (влияет на скорость пути).';
  }

  // Toggle Leaflet layers
  if (maps.territories) {
    if (mode === 'factions') {
      if (terrainLayer && maps.territories.hasLayer(terrainLayer)) maps.territories.removeLayer(terrainLayer);
      if (terrLayer && !maps.territories.hasLayer(terrLayer)) maps.territories.addLayer(terrLayer);
    } else {
      if (terrLayer && maps.territories.hasLayer(terrLayer)) maps.territories.removeLayer(terrLayer);
      if (terrainLayer && !maps.territories.hasLayer(terrainLayer)) maps.territories.addLayer(terrainLayer);
    }
  }

  clearTerritorySelection();
  renderTerrList();
}

function renderTerrList() {
  const list = document.getElementById('terr-list');
  if (!list) return;
  list.innerHTML = '';

  if (currentTerritoryMode === 'terrain') {
    const terrainColors = { swamp: '#a5d6a7', mountain: '#b0bec5', hills: '#ffe082', forest: '#81c784', ocean: '#64b5f6' };
    const terrainNames = { swamp: '🍄 Болота', mountain: '⛰ Горы', hills: '丘 Холмы', forest: '🌲 Лес', ocean: '🌊 Океан' };
    (adminData.terrainZones || []).forEach(t => {
      const color = terrainColors[t.type] || '#81c784';
      const typeName = terrainNames[t.type] || t.type;
      const div = document.createElement('div');
      div.className = 'terr-item';
      div.innerHTML = `<div class="terr-swatch" style="background:${color}"></div><span class="terr-name">${t.name} <span class="terr-badge terr-badge-${t.type}">${typeName}</span></span><div style="display:flex;gap:4px;"><button class="adm-btn-sm" style="padding:2px 6px;background:#5ab8ff;color:#000;" onclick="editTerrainZone('${t.id}')" title="Редактировать вершины">✏️</button><button class="terr-del" onclick="deleteTerrainZone('${t.id}')">✕</button></div>`;
      list.appendChild(div);
    });
    if (!adminData.terrainZones?.length) list.innerHTML = '<div style="color:var(--tx-3);font-size:12px;padding:8px">Нет зон ландшафта</div>';
    return;
  }

  (adminData.territories || []).forEach(t => {
    const div = document.createElement('div');
    div.className = 'terr-item';
    div.innerHTML = `<div class="terr-swatch" style="background:${t.color}"></div><span class="terr-name">${t.name} <span style="opacity:.5;font-size:10px">(${t.faction})</span></span><div style="display:flex;gap:4px;"><button class="adm-btn-sm" style="padding:2px 6px;background:#5ab8ff;color:#000;" onclick="editTerritory('${t.id}')" title="Редактировать вершины / Довыделять">✏️</button><button class="terr-del" onclick="deleteTerritory('${t.id}')">✕</button></div>`;
    list.appendChild(div);
  });
  if (!adminData.territories?.length) list.innerHTML = '<div style="color:var(--tx-3);font-size:12px;padding:8px">Нет территорий</div>';
}

function renderTerritoriesOnMap() {
  if (!terrLayer) return;
  terrLayer.clearLayers();
  (adminData.territories || []).forEach(t => {
    if (t.id === editingTerritoryId && currentTerritoryMode === 'factions') return;
    const color = t.color || FAC_COLORS[t.faction] || '#aaaaaa';
    const polys = normalizeToMultiPolygon(t.points);
    polys.forEach(poly => {
      const lls = poly.map(ring => ring.map(([ix, iy]) => toLL(ix, iy)));
      L.polygon(lls, {
        color: color, weight: 1.5, opacity: 0.7,
        fillColor: color, fillOpacity: 0.2,
        className: 'no-outline',
      }).bindTooltip(t.name, { sticky: true, className: 'territory-tooltip' }).addTo(terrLayer);
    });
  });
}

function renderTerrainOnMap() {
  if (!terrainLayer) return;
  terrainLayer.clearLayers();
  const terrainColors = { swamp: '#a5d6a7', mountain: '#b0bec5', hills: '#ffe082', forest: '#81c784', ocean: '#64b5f6' };
  (adminData.terrainZones || []).forEach(t => {
    if (t.id === editingTerritoryId && currentTerritoryMode === 'terrain') return;
    const color = terrainColors[t.type] || '#81c784';
    const polys = normalizeToMultiPolygon(t.points);
    polys.forEach(poly => {
      const lls = poly.map(ring => ring.map(([ix, iy]) => toLL(ix, iy)));
      L.polygon(lls, {
        color: color, weight: 2.0, opacity: 0.8, dashArray: '4 4',
        fillColor: color, fillOpacity: 0.25,
        className: 'no-outline',
      }).bindTooltip(`${t.name} (${t.type})`, { sticky: true, className: 'territory-tooltip' }).addTo(terrainLayer);
    });
  });
}

// ── Magic Wand (Photoshop style) & Auto-Hull ─────────
let magicWandActive = false;
let _offCanvas = null, _offCtx = null, _offData = null, _offW = 0, _offH = 0;

async function getMapImageData() {
  if (_offData) return { data: _offData, w: _offW, h: _offH };
  return new Promise(resolve => {
    const img = new Image();
    img.crossOrigin = 'Anonymous';
    img.onload = () => {
      _offW = Math.round(img.naturalWidth / 2);
      _offH = Math.round(img.naturalHeight / 2);
      _offCanvas = document.createElement('canvas');
      _offCanvas.width = _offW;
      _offCanvas.height = _offH;
      _offCtx = _offCanvas.getContext('2d');
      _offCtx.drawImage(img, 0, 0, _offW, _offH);
      _offData = _offCtx.getImageData(0, 0, _offW, _offH).data;
      resolve({ data: _offData, w: _offW, h: _offH });
    };
    img.onerror = () => resolve(null);
    img.src = MAP_IMAGE_PATH;
  });
}

function toggleMagicWand() {
  magicWandActive = !magicWandActive;
  const btn = document.getElementById('btn-magic-wand');
  const box = document.getElementById('magic-wand-controls');
  if (magicWandActive) {
    if (btn) { btn.style.background = '#5ab8ff'; btn.style.color = '#0b101d'; }
    if (box) box.style.display = 'block';
    if (maps.territories) {
      maps.territories._container.style.cursor = 'crosshair';
      maps.territories.on('click', onMagicWandMapClick);
    }
    adminToast('Кликните по области на карте');
  } else {
    if (btn) { btn.style.background = ''; btn.style.color = ''; }
    if (box) box.style.display = 'none';
    if (maps.territories) {
      maps.territories._container.style.cursor = '';
      maps.territories.off('click', onMagicWandMapClick);
    }
  }
}

async function onMagicWandMapClick(e) {
  if (!magicWandActive) return;
  const ix = Math.round(e.latlng.lng);
  const iy = Math.round(-e.latlng.lat);
  
  adminToast('Поиск границ...');
  const imgData = await getMapImageData();
  if (!imgData) { adminToast('Ошибка чтения карты'); return; }

  const tol = parseInt(document.getElementById('mw-tol')?.value || 45);
  const cx = Math.max(0, Math.min(imgData.w - 1, Math.round(ix / 2)));
  const cy = Math.max(0, Math.min(imgData.h - 1, Math.round(iy / 2)));
  
  const startIdx = (cy * imgData.w + cx) * 4;
  const r0 = imgData.data[startIdx], g0 = imgData.data[startIdx+1], b0 = imgData.data[startIdx+2];

  const w = imgData.w, h = imgData.h, d = imgData.data;
  const vis = new Uint8Array(w * h);
  const q = [cx, cy];
  vis[cy * w + cx] = 1;
  
  let head = 0;
  const inRegion = new Uint8Array(w * h);
  let count = 0;

  while (head < q.length && count < 1500000) {
    const x = q[head++];
    const y = q[head++];
    const idx = (y * w + x) * 4;
    const r = d[idx], g = d[idx+1], b = d[idx+2];
    
    if (Math.hypot(r - r0, g - g0, b - b0) <= tol) {
      inRegion[y * w + x] = 1;
      count++;
      
      const nbs = [[x+1,y], [x-1,y], [x,y+1], [x,y-1]];
      for (const [nx, ny] of nbs) {
        if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
          const nIdx = ny * w + nx;
          if (!vis[nIdx]) {
            vis[nIdx] = 1;
            q.push(nx, ny);
          }
        }
      }
    }
  }

  if (count < 20) {
    adminToast('⚠️ Область слишком мала. Увеличь порог цвета.');
    return;
  }
  if (count >= 1500000) {
    adminToast('⚠️ Область слишком велика! Уменьши порог цвета.');
    return;
  }

  const boundary = [];
  for (let y = 1; y < h - 1; y += 2) {
    for (let x = 1; x < w - 1; x += 2) {
      if (inRegion[y * w + x]) {
        if (!inRegion[y * w + x + 1] || !inRegion[y * w + x - 1] || !inRegion[(y + 1) * w + x] || !inRegion[(y - 1) * w + x]) {
          boundary.push([x * 2, y * 2]);
        }
      }
    }
  }

  if (boundary.length < 3) {
    adminToast('⚠️ Не удалось построить контур.');
    return;
  }

  const polygon = orderBoundaryPoints(boundary, 64);
  saveToHistory();
  const mode = document.querySelector('input[name="mw-mode"]:checked')?.value || (window.mwAddMode ? 'add' : 'new');
  pendingTerritoryPoints = applyTerritoryOperation(pendingTerritoryPoints, polygon, mode);
  updateTerritoryPreview();

  if (mode === 'add') adminToast(`Контур добавлен (${polygon.length} точек)`);
  else if (mode === 'subtract') adminToast(`Область вычтена`);
  else adminToast(`Контур создан (${polygon.length} точек)`);
  
  if (mode === 'new' && magicWandActive) toggleMagicWand();
}

function orderBoundaryPoints(points, sectors = 64) {
  if (!points?.length) return [];
  let cx = 0, cy = 0;
  points.forEach(([x, y]) => { cx += x; cy += y; });
  cx /= points.length; cy /= points.length;

  const buckets = Array.from({ length: sectors }, () => []);
  for (const [x, y] of points) {
    let ang = Math.atan2(y - cy, x - cx);
    if (ang < 0) ang += Math.PI * 2;
    const idx = Math.min(sectors - 1, Math.floor((ang / (Math.PI * 2)) * sectors));
    buckets[idx].push([x, y]);
  }

  const result = [];
  for (let i = 0; i < sectors; i++) {
    if (buckets[i].length) {
      buckets[i].sort((a, b) => Math.hypot(b[0]-cx, b[1]-cy) - Math.hypot(a[0]-cx, a[1]-cy));
      result.push(buckets[i][0]);
    }
  }
  return result;
}

function getWpFaction(wp) {
  if (adminData.factions?.[wp.id]) return adminData.factions[wp.id];
  if (wp.region === 'North' || wp.id.includes('Winterfell')) return 'NORTH';
  if (wp.region === 'IronIslands' || wp.id.includes('Pyke')) return 'IRONBORN';
  if (wp.region === 'Dorne' || wp.id.includes('Sunspear')) return 'DORNE';
  if (wp.region === 'Reach' || wp.id.includes('Highgarden')) return 'REACH';
  if (wp.region === 'Westerlands' || wp.id.includes('CasterlyRock')) return 'WESTERLANDS';
  if (wp.region === 'Vale' || wp.id.includes('Eyrie')) return 'ARRYN';
  if (wp.region === 'Riverlands' || wp.id.includes('Riverrun')) return 'RIVERLANDS';
  if (wp.region === 'Stormlands' || wp.id.includes('StormsEnd')) return 'STORMLANDS';
  if (wp.region === 'Crownlands' || wp.id.includes('KingsLanding')) return 'CROWNLANDS';
  if (wp.region === 'Wall' || wp.id.includes('CastleBlack')) return 'NIGHT_WATCH';
  return wp.faction || 'UNALIGNED';
}

function autoHullTerritory() {
  const faction = document.getElementById('terr-faction')?.value || 'NORTH';
  const wps = (MAP_DATA?.waypoints || []).filter(w => !w.hidden && (adminData.factions?.[w.id] === faction || getWpFaction(w) === faction));
  
  if (wps.length < 3) {
    adminToast(`⚠️ У фракции "${faction}" меньше 3 точек на карте. Выбери другую фракцию.`);
    return;
  }

  const pts = wps.map(w => [w.imgX, w.imgY]);
  let cx = 0, cy = 0;
  pts.forEach(([x, y]) => { cx += x; cy += y; });
  cx /= pts.length; cy /= pts.length;

  const expanded = pts.map(([x, y]) => {
    const ang = Math.atan2(y - cy, x - cx);
    const dist = Math.hypot(x - cx, y - cy) + 140;
    return [Math.round(cx + Math.cos(ang) * dist), Math.round(cy + Math.sin(ang) * dist)];
  });

  const polygon = orderBoundaryPoints(expanded, 48);
  saveToHistory();
  const mode = document.querySelector('input[name="mw-mode"]:checked')?.value || (window.mwAddMode ? 'add' : 'new');
  pendingTerritoryPoints = applyTerritoryOperation(pendingTerritoryPoints, polygon, mode);
  updateTerritoryPreview();
  adminToast('Зона по точкам создана');
}

// ── Sea Routes ────────────────────────────────────
function populateSeaSelects() {
  const ports = (adminData.ports || []).map(id => getWP(id)).filter(Boolean);
  ports.sort((a, b) => a.name.localeCompare(b.name));
  ['sea-a', 'sea-b'].forEach(selId => {
    const sel = document.getElementById(selId);
    sel.innerHTML = ports.map(wp => `<option value="${wp.id}">${wpName(wp)}</option>`).join('');
  });
}

function addSeaRoute() {
  const a = document.getElementById('sea-a').value;
  const b = document.getElementById('sea-b').value;
  if (!a || !b || a === b) { adminToast('Выбери два разных порта'); return; }
  if (!adminData.seaRoutes) adminData.seaRoutes = [];
  // Avoid duplicates
  const exists = adminData.seaRoutes.some(r => (r[0]===a&&r[1]===b)||(r[0]===b&&r[1]===a));
  if (exists) { adminToast('Маршрут уже существует'); return; }
  adminData.seaRoutes.push([a, b]);
  saveAdminData(); renderSeaList(); renderSeaRoutesOnMap();
  adminToast(`✓ Морской маршрут добавлен`);
}

function deleteSeaRoute(i) {
  adminData.seaRoutes.splice(i, 1);
  saveAdminData(); renderSeaList(); renderSeaRoutesOnMap();
  adminToast('Маршрут удалён');
}

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

let editingSeaRouteIdx = null;
let editingSeaAnchors = [];
let seaEditPreviewLayer = null;

function renderSeaList() {
  const list = document.getElementById('sea-list');
  if (!list) return;
  list.innerHTML = '';
  (adminData.seaRoutes || []).forEach((item, i) => {
    const a = Array.isArray(item) ? item[0] : item.a;
    const b = Array.isArray(item) ? item[1] : item.b;
    const anchors = Array.isArray(item) ? item[2] : item.anchors;
    const wpA = getWP(a), wpB = getWP(b);
    const hasCustom = anchors && anchors.length > 0;
    const div = document.createElement('div');
    div.className = 'sea-item';
    div.innerHTML = `
      <span class="sea-ports">◯ ${wpName(wpA)||a} → ${wpName(wpB)||b} ${hasCustom ? '<span style="color:#00e5ff;font-size:11px">⚓ ('+anchors.length+' якоря)</span>' : ''}</span>
      <div style="display:flex; gap:4px;">
        <button class="adm-btn-sm" style="padding:2px 8px; background:#38d4ff; color:#000;" onclick="editSeaRoutePath(${i})" title="Настроить траекторию пути">✏️ Траектория</button>
        <button class="sea-del" onclick="deleteSeaRoute(${i})">✕</button>
      </div>`;
    list.appendChild(div);
  });
  if (!adminData.seaRoutes?.length) list.innerHTML = '<div style="color:var(--tx-3);font-size:12px;padding:8px">Нет морских маршрутов</div>';
}

function editSeaRoutePath(idx) {
  editingSeaRouteIdx = idx;
  const item = adminData.seaRoutes[idx];
  const anchors = Array.isArray(item) ? item[2] : item.anchors;
  editingSeaAnchors = anchors ? JSON.parse(JSON.stringify(anchors)) : [];

  const a = Array.isArray(item) ? item[0] : item.a;
  const b = Array.isArray(item) ? item[1] : item.b;
  const wpA = getWP(a), wpB = getWP(b);

  document.getElementById('sea-edit-card').style.display = 'block';
  document.getElementById('sea-edit-title').textContent = `${wpName(wpA)||a} ↔ ${wpName(wpB)||b}`;
  
  renderSeaAnchorsList();
  renderSeaRoutesOnMap();
  adminToast(`Редактирование пути: ${wpName(wpA)} ↔ ${wpName(wpB)}. Кликай по воде!`);
}

function onSeaMapClick(e) {
  if (editingSeaRouteIdx === null || editingSeaRouteIdx === undefined) return;
  const ix = Math.round(e.latlng.lng);
  const iy = Math.round(-e.latlng.lat);
  editingSeaAnchors.push([ix, iy]);
  renderSeaAnchorsList();
  renderSeaRoutesOnMap();
  adminToast('⚓ Якорь добавлен!');
}

function renderSeaAnchorsList() {
  const list = document.getElementById('sea-anchors-list');
  if (!list) return;
  list.innerHTML = '';
  if (!editingSeaAnchors.length) {
    list.innerHTML = '<div style="color:var(--tx-3);font-size:12px;padding:4px 0">Автотраектория (без ручных якорей). Кликни по морю на карте справа, чтобы добавить буй.</div>';
    return;
  }
  editingSeaAnchors.forEach(([ix, iy], i) => {
    const div = document.createElement('div');
    div.style.cssText = 'display:flex; align-items:center; justify-content:space-between; background:rgba(255,255,255,0.05); padding:4px 8px; border-radius:4px; font-size:12px;';
    div.innerHTML = `
      <span style="color:#00e5ff; font-weight:600;">⚓ #${i+1} (${ix}, ${iy})</span>
      <div style="display:flex; gap:4px;">
        <button class="adm-btn-sm" style="padding:1px 6px; background:#444;" onclick="moveSeaAnchor(${i}, -1)" title="Вверх">▲</button>
        <button class="adm-btn-sm" style="padding:1px 6px; background:#444;" onclick="moveSeaAnchor(${i}, 1)" title="Вниз">▼</button>
        <button class="terr-del" onclick="deleteSeaAnchor(${i})" title="Удалить">✕</button>
      </div>`;
    list.appendChild(div);
  });
}

function moveSeaAnchor(i, dir) {
  const ni = i + dir;
  if (ni < 0 || ni >= editingSeaAnchors.length) return;
  const tmp = editingSeaAnchors[i];
  editingSeaAnchors[i] = editingSeaAnchors[ni];
  editingSeaAnchors[ni] = tmp;
  renderSeaAnchorsList();
  renderSeaRoutesOnMap();
}

function deleteSeaAnchor(i) {
  editingSeaAnchors.splice(i, 1);
  renderSeaAnchorsList();
  renderSeaRoutesOnMap();
}

function saveSeaRoutePath() {
  if (editingSeaRouteIdx === null || editingSeaRouteIdx === undefined) return;
  const item = adminData.seaRoutes[editingSeaRouteIdx];
  if (!item) return;
  
  if (Array.isArray(item)) {
    if (editingSeaAnchors.length > 0) item[2] = editingSeaAnchors;
    else if (item.length > 2) item.splice(2, 1);
  } else {
    if (editingSeaAnchors.length > 0) item.anchors = editingSeaAnchors;
    else delete item.anchors;
  }
  
  cancelSeaRouteEdit();
  saveAdminData();
  renderSeaList();
  renderSeaRoutesOnMap();
  adminToast('✓ Траектория сохранена!');
}

function resetSeaRoutePath() {
  editingSeaAnchors = [];
  renderSeaAnchorsList();
  renderSeaRoutesOnMap();
  adminToast('Сброшено на авторасчёт (A*)');
}

function cancelSeaRouteEdit() {
  editingSeaRouteIdx = null;
  editingSeaAnchors = [];
  if (seaEditPreviewLayer) {
    seaEditPreviewLayer.clearLayers();
  }
  document.getElementById('sea-edit-card').style.display = 'none';
  renderSeaRoutesOnMap();
}

function addPortMarkersToSeaMap() {
  (adminData.ports || []).forEach(id => {
    const wp = getWP(id);
    if (!wp) return;
    const m = L.circleMarker(wpLL(wp), { radius: 7, color: '#90caf9', fillColor: '#5ab8ff', fillOpacity: 0.9, weight: 2 });
    m.bindTooltip(`◯ ${wpName(wp)}`, { sticky: false, className: 'territory-tooltip' });
    m.on('click', () => {
      const selA = document.getElementById('sea-a');
      const selB = document.getElementById('sea-b');
      if (!selA.value || selA.value === id || (selA.value && selB.value)) {
        selA.value = id;
        selB.value = '';
        adminToast(`◯ Порт A: ${wpName(wp)}`);
      } else {
        selB.value = id;
        adminToast(`◯ Порт B: ${wpName(wp)}`);
      }
    });
    m.addTo(maps.sea);
  });
}

function renderSeaRoutesOnMap() {
  if (!seaRouteLayer) return;
  seaRouteLayer.clearLayers();
  if (seaEditPreviewLayer) seaEditPreviewLayer.clearLayers();

  (adminData.seaRoutes || []).forEach((item, idx) => {
    const a = Array.isArray(item) ? item[0] : item.a;
    const b = Array.isArray(item) ? item[1] : item.b;
    const wpA = getWP(a), wpB = getWP(b);
    if (!wpA || !wpB) return;
    
    if (editingSeaRouteIdx !== null && editingSeaRouteIdx === idx) return;

    const curve = getSeaRouteCurve(item);
    const lls = curve.map(([ix, iy]) => toLL(ix, iy));
    const hasCustom = (Array.isArray(item) ? item[2] : item.anchors)?.length > 0;
    L.polyline(lls, {
      color: hasCustom ? '#00e5ff' : '#5ab8ff',
      weight: hasCustom ? 3 : 2,
      opacity: hasCustom ? 0.9 : 0.7,
      dashArray: hasCustom ? null : '10 5',
    }).bindTooltip(`${wpName(wpA)} ↔ ${wpName(wpB)}${hasCustom ? ' (своя траектория)' : ''}`, { sticky: true, className: 'territory-tooltip' })
      .addTo(seaRouteLayer);
  });

  if (editingSeaRouteIdx !== null && editingSeaRouteIdx !== undefined && maps.sea) {
    if (!seaEditPreviewLayer) seaEditPreviewLayer = L.layerGroup().addTo(maps.sea);
    else seaEditPreviewLayer.clearLayers();

    const item = adminData.seaRoutes[editingSeaRouteIdx];
    if (!item) return;
    const a = Array.isArray(item) ? item[0] : item.a;
    const b = Array.isArray(item) ? item[1] : item.b;
    const wpA = getWP(a), wpB = getWP(b);
    if (!wpA || !wpB) return;

    const tempItem = [a, b, editingSeaAnchors];
    const curve = getSeaRouteCurve(tempItem);
    const lls = curve.map(([ix, iy]) => toLL(ix, iy));

    L.polyline(lls, {
      color: '#00e5ff', weight: 4, opacity: 1.0,
    }).bindTooltip(`Редактируемый путь: ${wpName(wpA)} ↔ ${wpName(wpB)}`, { sticky: true }).addTo(seaEditPreviewLayer);

    editingSeaAnchors.forEach(([ix, iy], i) => {
      const marker = L.marker(toLL(ix, iy), {
        icon: L.divIcon({
          html: `<div style="background:#00e5ff; color:#000; font-weight:bold; font-size:11px; width:20px; height:20px; border-radius:50%; display:flex; align-items:center; justify-content:center; border:2px solid #fff; box-shadow:0 0 8px #00e5ff; cursor:pointer;">⚓</div>`,
          className: '', iconSize: [20, 20], iconAnchor: [10, 10]
        }),
        draggable: true,
        zIndexOffset: 1000
      }).addTo(seaEditPreviewLayer);

      marker.bindTooltip(`⚓ Якорь #${i+1}: (${ix}, ${iy})<br>Перетащи мышкой`, { sticky: true });
      marker.on('dragend', (e) => {
        const pos = e.target.getLatLng();
        editingSeaAnchors[i] = [Math.round(pos.lng), Math.round(-pos.lat)];
        renderSeaAnchorsList();
        renderSeaRoutesOnMap();
      });
    });
  }
}

// ── Tab switching ─────────────────────────────────
function switchTab(tab) {
  document.querySelectorAll('.adm-tab-content').forEach(el => el.classList.remove('active'));
  document.querySelectorAll('.adm-tab').forEach(el => el.classList.remove('active'));
  const targetContent = document.getElementById('tab-' + tab);
  if (targetContent) targetContent.classList.add('active');
  document.querySelectorAll('.adm-tab').forEach(el => {
    const txt = el.textContent.toLowerCase();
    if ((tab === 'points' && txt.includes('точк')) ||
        (tab === 'territories' && txt.includes('терр')) ||
        (tab === 'sea' && txt.includes('морск')) ||
        (tab === 'mapsettings' && txt.includes('настро')) ||
        (tab === 'editor' && txt.includes('редакт')) ||
        (tab === 'export' && txt.includes('экспорт'))) {
      el.classList.add('active');
    }
  });
  setTimeout(() => {
    const m = maps[tab];
    if (m) m.invalidateSize();
    if (tab === 'mapsettings') {
      loadMapSettingsUI();
      previewMapBounds();
    } else if (tab === 'editor') {
      loadEditorFile(document.getElementById('editor-file-select')?.value || 'overrides');
    }
  }, 50);
}

// ── Map Settings ──────────────────────────────────
function loadMapSettingsUI() {
  const cfg = adminData.mapSettings || OVERRIDES?.mapSettings || { minX: 0, minY: 0, maxX: 8192, maxY: 8192, bgColor: '#0f1c2d' };
  const minX = Math.max(0, Math.min(8192, cfg.minX ?? 0));
  const minY = Math.max(0, Math.min(8192, cfg.minY ?? 0));
  const maxX = Math.max(minX + 10, Math.min(8192, cfg.maxX ?? 8192));
  const maxY = Math.max(minY + 10, Math.min(8192, cfg.maxY ?? 8192));
  if (document.getElementById('cfg-min-x')) document.getElementById('cfg-min-x').value = minX;
  if (document.getElementById('cfg-min-y')) document.getElementById('cfg-min-y').value = minY;
  if (document.getElementById('cfg-max-x')) document.getElementById('cfg-max-x').value = maxX;
  if (document.getElementById('cfg-max-y')) document.getElementById('cfg-max-y').value = maxY;
  if (document.getElementById('cfg-bg-color')) document.getElementById('cfg-bg-color').value = cfg.bgColor || '#0f1c2d';
  if (document.getElementById('cfg-bg-hex')) document.getElementById('cfg-bg-hex').value = cfg.bgColor || '#0f1c2d';

  // Terrain multipliers
  const mults = adminData.terrainMultipliers || { default: 1.0, swamp: 2.0, mountain: 5.0, hills: 1.5, forest: 1.3, ocean: 1.0 };
  ['swamp', 'mountain', 'hills', 'forest'].forEach(type => {
    const el = document.getElementById('cfg-mult-' + type);
    const valEl = document.getElementById('val-mult-' + type);
    if (el && mults[type] !== undefined) el.value = mults[type];
    if (valEl && mults[type] !== undefined) valEl.textContent = Number(mults[type]).toFixed(1) + 'x';
  });
}

function saveMapSettings() {
  let minX = Math.max(0, Math.min(8192, parseInt(document.getElementById('cfg-min-x').value) || 0));
  let minY = Math.max(0, Math.min(8192, parseInt(document.getElementById('cfg-min-y').value) || 0));
  let maxX = Math.max(minX + 10, Math.min(8192, parseInt(document.getElementById('cfg-max-x').value) || 8192));
  let maxY = Math.max(minY + 10, Math.min(8192, parseInt(document.getElementById('cfg-max-y').value) || 8192));
  const bgColor = document.getElementById('cfg-bg-hex').value || '#0f1c2d';

  adminData.mapSettings = { minX, minY, maxX, maxY, bgColor };

  // Save terrain multipliers
  const swamp = parseFloat(document.getElementById('cfg-mult-swamp')?.value || 2.0);
  const mountain = parseFloat(document.getElementById('cfg-mult-mountain')?.value || 5.0);
  const hills = parseFloat(document.getElementById('cfg-mult-hills')?.value || 1.5);
  const forest = parseFloat(document.getElementById('cfg-mult-forest')?.value || 1.3);
  adminData.terrainMultipliers = { default: 1.0, swamp, mountain, hills, forest, ocean: 1.0 };

  saveAdminData();
  applyMapSettings(adminData.mapSettings);
  adminToast('Настройки карты и множители ландшафта сохранены');
}

function updateBgColorPreview(val) {
  document.getElementById('cfg-bg-color').value = val;
  document.getElementById('cfg-bg-hex').value = val;
  document.documentElement.style.setProperty('--map-bg', val);
}

function applyMapSettings(cfg) {
  if (!cfg) return;
  if (cfg.bgColor) {
    document.documentElement.style.setProperty('--map-bg', cfg.bgColor);
  }
  previewMapBounds();
}

function previewMapBounds() {
  if (!maps.settings) return;
  if (!window.boundsPreviewLayer) window.boundsPreviewLayer = L.layerGroup().addTo(maps.settings);
  window.boundsPreviewLayer.clearLayers();

  const minX = parseInt(document.getElementById('cfg-min-x').value) || 0;
  const minY = parseInt(document.getElementById('cfg-min-y').value) || 0;
  const maxX = parseInt(document.getElementById('cfg-max-x').value) || 8192;
  const maxY = parseInt(document.getElementById('cfg-max-y').value) || 8192;

  const rect = L.rectangle([toLL(minX, minY), toLL(maxX, maxY)], {
    color: '#5ab8ff', weight: 2, fillOpacity: 0.1, dashArray: '6, 6'
  });
  window.boundsPreviewLayer.addLayer(rect);
  maps.settings.fitBounds(rect.getBounds(), { padding: [20, 20] });
}

function resetMapBounds() {
  document.getElementById('cfg-min-x').value = 0;
  document.getElementById('cfg-min-y').value = 0;
  document.getElementById('cfg-max-x').value = 8192;
  document.getElementById('cfg-max-y').value = 8192;
  previewMapBounds();
}

function startBoundsSelection() {
  window.selectingBounds = true;
  window.boundsClickCount = 0;
  adminToast('Кликните левый верхний угол области на карте');
}

function onSettingsMapClick(e) {
  if (!window.selectingBounds) return;
  const x = Math.max(0, Math.min(8192, Math.round(e.latlng.lng)));
  const y = Math.max(0, Math.min(8192, Math.round(-e.latlng.lat)));

  if (window.boundsClickCount === 0) {
    window.boundsPoint1 = { x, y };
    window.boundsClickCount = 1;
    adminToast('Теперь кликните правый нижний угол');
  } else {
    const p1 = window.boundsPoint1;
    const minX = Math.min(p1.x, x);
    const minY = Math.min(p1.y, y);
    const maxX = Math.max(p1.x, x);
    const maxY = Math.max(p1.y, y);

    document.getElementById('cfg-min-x').value = minX;
    document.getElementById('cfg-min-y').value = minY;
    document.getElementById('cfg-max-x').value = maxX;
    document.getElementById('cfg-max-y').value = maxY;

    window.selectingBounds = false;
    previewMapBounds();
    adminToast('Область выбрана');
  }
}

// ── Export / Import ────────────────────────────────
function exportBackupJSON() {
  const backup = {
    timestamp: new Date().toISOString(),
    adminData: adminData
  };
  downloadFile(`tortuga_backup_${new Date().toISOString().slice(0,10)}.json`, JSON.stringify(backup, null, 2));
}

function importBackupJSON() {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = '.json,application/json';
  input.onchange = e => {
    const file = e.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = async event => {
      try {
        const parsed = JSON.parse(event.target.result);
        const dataToImport = parsed.adminData || parsed;
        adminData = { ...adminData, ...dataToImport };
        await saveAdminData();
        adminToast('Данные успешно импортированы! Перезагрузка...');
        setTimeout(() => window.location.reload(), 1000);
      } catch (err) {
        adminToast('Ошибка чтения файла JSON: ' + err.message);
      }
    };
    reader.readAsText(file);
  };
  input.click();
}

function importOnlySeaRoutes() {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = '.json,application/json';
  input.onchange = e => {
    const file = e.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = async event => {
      try {
        const parsed = JSON.parse(event.target.result);
        const dataToImport = parsed.adminData || parsed;
        if (!dataToImport.seaRoutes || !dataToImport.seaRoutes.length) {
          adminToast('⚠️ В выбранном файле нет морских маршрутов');
          return;
        }
        adminData.seaRoutes = dataToImport.seaRoutes;
        await saveAdminData();
        adminToast('✓ Морские пути успешно перенесены! Территории сохранены. Перезагрузка...');
        setTimeout(() => window.location.reload(), 1000);
      } catch (err) {
        adminToast('Ошибка чтения файла: ' + err.message);
      }
    };
    reader.readAsText(file);
  };
  input.click();
}

function exportOverrides() {
  const data = {
    mapSettings: adminData.mapSettings || { minX: 0, minY: 0, maxX: 8192, maxY: 8192, bgColor: '#0f1c2d' },
    customWaypoints: adminData.customWaypoints || [],
    capitals:   adminData.capitals   || [],
    fortresses: adminData.fortresses || [],
    ports:      adminData.ports      || [],
    resources:  adminData.resources  || [],
    renames:    adminData.renames    || {},
    leaders:    adminData.leaders    || {},
    factions:   adminData.factions   || {},
    seaRoutes:  adminData.seaRoutes  || [],
    terrainZones: adminData.terrainZones || [],
    terrainMultipliers: adminData.terrainMultipliers || { default: 1.0, swamp: 2.0, mountain: 5.0, hills: 1.5, forest: 1.3, ocean: 1.0 }
  };
  const content = `// ═══════════════════════════════════════════════
//  Tortuga Map — overrides.js
//  Сгенерировано Admin Panel ${new Date().toISOString()}
// ═══════════════════════════════════════════════

const OVERRIDES = ${JSON.stringify(data, null, 2)};
`;
  downloadFile('overrides.js', content);
}

// ── File & Config Editor ──────────────────────────
function loadEditorFile(val = 'overrides') {
  const textarea = document.getElementById('editor-code');
  const display = document.getElementById('editor-filename-display');
  const status = document.getElementById('editor-status');
  if (!textarea) return;
  if (status) status.textContent = '';

  if (val === 'overrides') {
    if (display) display.textContent = 'overrides.js';
    const data = {
      mapSettings: adminData.mapSettings || OVERRIDES?.mapSettings || { minX: 0, minY: 0, maxX: 8192, maxY: 8192, bgColor: '#0f1c2d' },
      customWaypoints: adminData.customWaypoints || OVERRIDES?.customWaypoints || [],
      capitals:   adminData.capitals   || [],
      fortresses: adminData.fortresses || [],
      ports:      adminData.ports      || [],
      resources:  adminData.resources  || [],
      renames:    adminData.renames    || {},
      leaders:    adminData.leaders    || {},
      factions:   adminData.factions   || {},
      seaRoutes:  adminData.seaRoutes  || [],
    };
    textarea.value = `// ═══════════════════════════════════════════════\n//  Tortuga Map — overrides.js\n//  Сгенерировано Admin Panel ${new Date().toISOString()}\n// ═══════════════════════════════════════════════\n\nconst OVERRIDES = ${JSON.stringify(data, null, 2)};\n`;
  } else if (val === 'admin_config') {
    if (display) display.textContent = 'admin_config.js';
    const cfg = typeof ADMIN_CFG !== 'undefined' ? ADMIN_CFG : {
      pwHash: "1994f8043856d2a24708391dad7a8841d6d8d84009b8a5857dcd1e3fc6f4c8a8",
      token: "t0rTuGaS3cr3tAdm1nAcc3ss2024K3y",
      maxAttempts: 3,
      lockMinutes: 10
    };
    textarea.value = `// ═══════════════════════════════════════════════\n//  Tortuga Map — admin_config.js\n// ═══════════════════════════════════════════════\n\nconst ADMIN_CFG = ${JSON.stringify(cfg, null, 2)};\n`;
  } else if (val === 'territories') {
    if (display) display.textContent = 'territories.js';
    const terrs = adminData.territories || (typeof TERRITORIES !== 'undefined' ? TERRITORIES : []);
    textarea.value = `// ═══════════════════════════════════════════════\n//  Tortuga Map — territories.js\n// ═══════════════════════════════════════════════\n\nconst TERRITORIES = ${JSON.stringify(terrs, null, 2)};\n`;
  } else if (val === 'raw_storage') {
    if (display) display.textContent = 'localStorage (tortuga_admin_data)';
    textarea.value = JSON.stringify(adminData, null, 2);
  }
}

function saveEditorFile() {
  const val = document.getElementById('editor-file-select')?.value || 'overrides';
  const text = document.getElementById('editor-code')?.value || '';
  const status = document.getElementById('editor-status');

  try {
    if (val === 'raw_storage') {
      const parsed = JSON.parse(text);
      adminData = parsed;
      saveAdminData();
    } else {
      const match = text.match(/const\s+[A-Z_]+\s*=\s*(\{[\s\S]*\}|\[[\s\S]*\])\s*;/);
      if (!match) throw new Error("Не найден корректный формат const NAME = {...};");
      const parsed = JSON.parse(match[1]);

      if (val === 'overrides') {
        if (parsed.mapSettings) adminData.mapSettings = parsed.mapSettings;
        if (parsed.customWaypoints) adminData.customWaypoints = parsed.customWaypoints;
        if (parsed.capitals) adminData.capitals = parsed.capitals;
        if (parsed.fortresses) adminData.fortresses = parsed.fortresses;
        if (parsed.ports) adminData.ports = parsed.ports;
        if (parsed.resources) adminData.resources = parsed.resources;
        if (parsed.renames) adminData.renames = parsed.renames;
        if (parsed.leaders) adminData.leaders = parsed.leaders;
        if (parsed.factions) adminData.factions = parsed.factions;
        if (parsed.seaRoutes) adminData.seaRoutes = parsed.seaRoutes;
        saveAdminData();
        mergeCustomWaypoints();
        addMarkersToMap(maps.points);
        renderPointList();
      } else if (val === 'territories') {
        adminData.territories = parsed;
        saveAdminData();
        renderTerritoriesOnMap();
        renderTerrList();
      } else if (val === 'admin_config') {
        Object.assign(ADMIN_CFG, parsed);
        adminToast('Конфиг безопасности обновлен');
      }
    }
    if (status) {
      status.style.color = '#a5d6a7';
      status.textContent = '✓ Изменения успешно применены и сохранены!';
    }
    adminToast('Изменения применены');
  } catch (e) {
    if (status) {
      status.style.color = '#ff8a80';
      status.textContent = '❌ Ошибка: ' + e.message;
    }
    adminToast('Ошибка: ' + e.message);
  }
}

function downloadEditorFile() {
  const val = document.getElementById('editor-file-select')?.value || 'overrides';
  const text = document.getElementById('editor-code')?.value || '';
  const filename = val === 'raw_storage' ? 'tortuga_admin_data.json' : `${val}.js`;
  downloadFile(filename, text);
}

function resetEditorFile() {
  const val = document.getElementById('editor-file-select')?.value || 'overrides';
  loadEditorFile(val);
  adminToast('Сброшено');
}

function exportTerritories() {
  const data = adminData.territories || [];
  const content = `// ═══════════════════════════════════════════════
//  Tortuga Map — territories.js
//  Сгенерировано Admin Panel ${new Date().toISOString()}
// ═══════════════════════════════════════════════

const TERRITORIES = ${JSON.stringify(data, null, 2)};
`;
  downloadFile('territories.js', content);
}

function resetAllData() {
  if (!confirm('Сбросить ВСЕ изменения из браузера? Это действие необратимо.')) return;
  localStorage.removeItem(STORAGE_KEY);
  adminToast('Данные сброшены. Перезагрузка...');
  setTimeout(() => window.location.reload(), 1200);
}

function downloadFile(name, content) {
  const a = document.createElement('a');
  a.href = URL.createObjectURL(new Blob([content], { type: 'text/javascript' }));
  a.download = name;
  a.click();
  URL.revokeObjectURL(a.href);
  adminToast(`⬇ ${name} скачан`);
}

// ── Toast ─────────────────────────────────────────
let _toastTimer = null;
function adminToast(msg, ms = 2500) {
  let el = document.getElementById('adm-toast');
  if (!el) {
    el = document.createElement('div');
    el.id = 'adm-toast';
    el.style.cssText = 'position:fixed;bottom:24px;right:24px;background:#1e273d;border:1px solid rgba(210,165,85,0.4);border-radius:10px;padding:10px 20px;font-size:13px;color:#d8d4cc;z-index:99999;box-shadow:0 4px 20px rgba(0,0,0,0.5);pointer-events:none;';
    document.body.appendChild(el);
  }
  el.textContent = msg;
  el.style.display = 'block';
  clearTimeout(_toastTimer);
  _toastTimer = setTimeout(() => { el.style.display = 'none'; }, ms);
}

// ── Keyboard: Enter on login ──────────────────────
// (already bound above)
