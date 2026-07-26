// ═══════════════════════════════════════════════
//  Tortuga Map — overrides.js
//  Хранит типы точек, морские маршруты, переопределения
//  Редактируется через Admin Panel
// ═══════════════════════════════════════════════

const OVERRIDES = {
  // Настройки карты (фон и границы области видимости)
  mapSettings: {
    bgColor: "#0f1c2d",
    minX: 0,
    minY: 0,
    maxX: 8192,
    maxY: 8192
  },

  // Пользовательские (созданные вручную) точки на карте
  customWaypoints: [],

  // Столицы (задаются вручную/через админку)
  capitals: [
    "KingsLanding", "Winterfell", "CasterlyRock", "Highgarden",
    "Dragonstone", "Sunspear", "Pyke", "Eyrie", "Riverrun",
    "StormEnd", "OldTown"
  ],

  // Крепости (задаются через админку)
  fortresses: [
    "CastleBlack", "Harrenhal", "Dreadfort", "Twins",
    "Nightfort", "ShadowTower"
  ],

  // Порты — из GOTWaypoint.java: всё с "port/Port" + острова и прибрежные города
  ports: [
    "Lannisport", "Lordsport", "Ryamsport", "SweetportSound",
    "Seagard", "WhiteHarbour", "Oldtown", "Gulltown",
    "Barrowtown", "Saltpans", "Maidenpool", "KingsLanding",
    "Dragonstone", "Sunspear", "PlankyTown", "EastWatch",
    "Sisterton", "WeepingTown", "StarfishHarbor", "Spicetown",
    "Driftmark", "Pyke", "TenTowers",
    "Crakehall", "OldOak", "HewettTown", "Bandallon",
    "SunHouse", "Starfall", "Hellholt", "Saltshore",
    "Greenstone", "RainHouse", "StormsEnd", "EvenfallHall",
    "FlintsFinger", "VictarionLanding"
  ],

  // Ресурсные точки — задаются вручную через админку
  resources: [],

  // Переименования: { id: "новое имя" }
  renames: {},

  // Лидеры точек: { id: "Имя лидера" }
  leaders: {},

  // Фракции точек (при захвате/выставлении лидера): { id: "FACTION_ID" }
  factions: {},

  // Морские маршруты между портами (граф adjacency)
  // Каждая пара — прямой морской путь вдоль берега (без пересечения суши)
  seaRoutes: [
    // Железные острова и западный залив
    ["Pyke", "Lordsport"],
    ["TenTowers", "Lordsport"],
    ["Lordsport", "Seagard"],
    ["Lordsport", "Lannisport"],
    ["Pyke", "Seagard"],
    ["Seagard", "FlintsFinger"],
    ["FlintsFinger", "VictarionLanding"],
    ["VictarionLanding", "Barrowtown"],

    // Западное побережье (от Ланниспорта до Староместа)
    ["Lannisport", "Crakehall"],
    ["Crakehall", "OldOak"],
    ["OldOak", "HewettTown"],
    ["HewettTown", "Bandallon"],
    ["Bandallon", "Oldtown"],
    ["Lannisport", "HewettTown"],
    ["HewettTown", "Ryamsport"],
    ["Oldtown", "Ryamsport"],
    ["Oldtown", "StarfishHarbor"],
    ["Ryamsport", "StarfishHarbor"],

    // Южное побережье (Дорн и Арбор — огибая берег по морю)
    ["Ryamsport", "SunHouse"],
    ["StarfishHarbor", "SunHouse"],
    ["SunHouse", "Starfall"],
    ["Starfall", "Hellholt"],
    ["Hellholt", "Saltshore"],
    ["Saltshore", "PlankyTown"],
    ["PlankyTown", "Sunspear"],
    
    // Восточное побережье (от Дорна до Королевской Гавани и Долины)
    ["Sunspear", "WeepingTown"],
    ["WeepingTown", "Greenstone"],
    ["Greenstone", "RainHouse"],
    ["RainHouse", "StormsEnd"],
    ["StormsEnd", "EvenfallHall"],
    ["EvenfallHall", "Dragonstone"],
    ["EvenfallHall", "KingsLanding"],
    ["KingsLanding", "Dragonstone"],
    ["KingsLanding", "Driftmark"],
    ["Dragonstone", "Driftmark"],
    ["Dragonstone", "Spicetown"],
    ["Driftmark", "Spicetown"],
    ["Dragonstone", "SweetportSound"],
    ["Dragonstone", "Gulltown"],
    ["Dragonstone", "Maidenpool"],
    ["KingsLanding", "Maidenpool"],
    ["Maidenpool", "Saltpans"],
    ["Saltpans", "Gulltown"],
    ["SweetportSound", "Gulltown"],
    ["Gulltown", "Sisterton"],
    ["Sisterton", "WhiteHarbour"],
    ["WhiteHarbour", "EastWatch"]
  ],

  // Трудные территории (ландшафт: болота, горы, холмы, лес, океан)
  terrainZones: [],

  // Множители времени пути для типов ландшафта (1.0 = обычный, >1.0 = замедление)
  terrainMultipliers: {
    default: 1.0,
    swamp: 2.0,
    mountain: 5.0,
    hills: 1.5,
    forest: 1.3,
    ocean: 1.0
  }
};
