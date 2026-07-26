#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import sys, io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8', errors='replace')
"""
Парсит GOTWaypoint.java и ru_RU.lang и генерирует waypoints.js для онлайн-карты.
Запускать из папки advmap:  python generate_waypoints.py
"""

import re
import json
import os

# Пути (относительно корня проекта)
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
PROJECT_ROOT = os.path.dirname(SCRIPT_DIR)

WAYPOINT_JAVA = os.path.join(PROJECT_ROOT, "src/main/java/got/common/world/map/GOTWaypoint.java")
LANG_RU = os.path.join(PROJECT_ROOT, "src/main/resources/assets/got/lang/ru_RU.lang")
OUTPUT_JS = os.path.join(SCRIPT_DIR, "data/waypoints.js")

# Масштаб координат (из GOTGenLayerWorld.java)
SCALE = 128       # 2^7
ORIGIN_X = 810    # map image X → world X
ORIGIN_Z = 730    # map image Y → world Z


def map_to_world_x(img_x):
    """Перевод imgX → мировые блоки X (из GOTWaypoint.mapToWorldX)"""
    return round((img_x - ORIGIN_X + 0.5) * SCALE)


def map_to_world_z(img_y):
    """Перевод imgY → мировые блоки Z (из GOTWaypoint.mapToWorldZ)"""
    return round((img_y - ORIGIN_Z + 0.5) * SCALE)


# ── Цвета/иконки фракций ─────────────────────────────────────────────────────
FACTION_COLORS = {
    "NIGHT_WATCH":   "#2c2c2c",
    "WILDLING":      "#6b4f2e",
    "NORTH":         "#4a6fa5",
    "ARRYN":         "#b8d4f0",
    "RIVERLANDS":    "#5a8c5a",
    "WESTERLANDS":   "#d4a017",
    "REACH":         "#2e7d32",
    "STORMLANDS":    "#546e7a",
    "CROWNLANDS":    "#9c1e1e",
    "DORNE":         "#c97a2e",
    "IRONBORN":      "#708090",
    "DRAGONSTONE":   "#7b1fa2",
    "WHITE_WALKER":  "#90caf9",
    "DOTHRAKI":      "#8b5e3c",
    "LHAZAR":        "#aed581",
    "MOSSOVY":       "#4caf50",
    "FREE":          "#ff9800",
    "UNALIGNED":     "#9e9e9e",
    "HOSTILE":       "#f44336",
    "GHISCAR":       "#ffd700",
    "ASSHAI":        "#212121",
    "VALYRIA":       "#880e4f",
    "YI_TI":         "#c62828",
    "SUMMER_ISLANDS":"#00bcd4",
    "JOGOS":         "#795548",
    "IBBEN":         "#607d8b",
    "SOTHORYOS":     "#1b5e20",
    "QARTH":         "#e91e63",
    "ULTHOS":        "#37474f",
}

REGION_NAMES_RU = {
    "ABOBA": "АБОБА",
    "NORTH": "Север",
    "ARRYN": "Долина Аррен",
    "ICE": "За Стеной",
    "RIVERLANDS": "Речные земли",
    "MOSSOVY": "Мосовия",
    "DRANGONSTONE": "Драконий Камень",
    "CROWNLANDS": "Королевские земли",
    "DORNE": "Дорн",
    "DOTHRAKI": "Дотракийское море",
    "IBBEN": "Иббен",
    "IRONBORN": "Железные острова",
    "LHAZAR": "Лхазар",
    "REACH": "Простор",
    "OCEAN": "Океан",
    "STORMLANDS": "Штормовые земли",
    "WESTERLANDS": "Запад",
    "ASSHAI": "Ассхай",
    "GHISCAR": "Гхискар",
    "JOGOS": "Джогос-Нхай",
    "VALYRIA": "Валирия",
    "FREE": "Вольные города",
    "QARTH": "Кварт",
    "SOTHORYOS": "Соторис",
    "ULTHOS": "Улхос",
    "YI_TI": "Йи Ти",
    "SUMMER_ISLANDS": "Летние острова",
    "AMOGUS": "???",
}


def parse_lang(lang_file):
    """Читает .lang файл и возвращает словарь key → value"""
    names = {}
    with open(lang_file, encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if "=" in line and not line.startswith("#"):
                key, _, val = line.partition("=")
                names[key.strip()] = val.strip()
    return names


def parse_waypoints_java(java_file):
    """
    Парсит enum-значения GOTWaypoint из Java-файла.
    Поддерживает форматы:
      Name(Region.X, GOTFaction.Y, imgX, imgY)
      Name(Region.X, GOTFaction.Y, imgX, imgY, true)
    """
    waypoints = []
    with open(java_file, encoding="utf-8") as f:
        content = f.read()

    # Найти блок enum-значений (до первых полей/методов)
    # Паттерн: идентификатор(Region.X, GOTFaction.Y, int, int[, bool])
    pattern = re.compile(
        r"^\s*(?://.*\n\s*)*"          # опциональный комментарий
        r"([A-Z][A-Za-z0-9_]*)"        # имя вейпоинта
        r"\s*\(\s*"
        r"Region\.([A-Z_]+)"           # регион
        r"\s*,\s*"
        r"GOTFaction\.([A-Z_]+)"       # фракция
        r"\s*,\s*"
        r"(-?\d+)"                     # imgX
        r"\s*,\s*"
        r"(-?\d+)"                     # imgY
        r"(?:\s*,\s*(true|false))?"    # isHidden (опционально)
        r"\s*\)",
        re.MULTILINE
    )

    # Ищем только в части до первого блока методов (до первого "public int")
    enum_section_match = re.search(r"^public enum GOTWaypoint.*?^(?:\tpublic Region region;)",
                                   content, re.MULTILINE | re.DOTALL)
    search_area = enum_section_match.group(0) if enum_section_match else content

    seen = set()
    for m in pattern.finditer(search_area):
        name = m.group(1)
        region = m.group(2)
        faction = m.group(3)
        img_x = int(m.group(4))
        img_y = int(m.group(5))
        hidden = m.group(6) == "true" if m.group(6) else False

        if name in seen:
            continue
        seen.add(name)

        world_x = map_to_world_x(img_x)
        world_z = map_to_world_z(img_y)

        waypoints.append({
            "id": name,
            "imgX": img_x,
            "imgY": img_y,
            "worldX": world_x,
            "worldZ": world_z,
            "region": region,
            "faction": faction,
            "hidden": hidden,
        })

    return waypoints


def main():
    print(f"Reading waypoints from: {WAYPOINT_JAVA}")
    waypoints = parse_waypoints_java(WAYPOINT_JAVA)
    print(f"  Found waypoints: {len(waypoints)}")

    print(f"Reading localization from: {LANG_RU}")
    lang = parse_lang(LANG_RU)

    # Добавить русские названия
    for wp in waypoints:
        lang_key = f"got.wp.{wp['id']}"
        wp["name"] = lang.get(lang_key, wp["id"])
        wp["color"] = FACTION_COLORS.get(wp["faction"], "#9e9e9e")
        wp["regionName"] = REGION_NAMES_RU.get(wp["region"], wp["region"])

    # Создать папку data/ если нет
    os.makedirs(os.path.dirname(OUTPUT_JS), exist_ok=True)

    # Сформировать список регионов
    regions = sorted(set(wp["region"] for wp in waypoints))
    regions_data = [
        {"id": r, "name": REGION_NAMES_RU.get(r, r)}
        for r in regions
    ]

    # Список фракций
    factions = sorted(set(wp["faction"] for wp in waypoints))
    factions_data = [
        {"id": f, "color": FACTION_COLORS.get(f, "#9e9e9e")}
        for f in factions
    ]

    output = {
        "waypoints": waypoints,
        "regions": regions_data,
        "factions": factions_data,
        "meta": {
            "scale": SCALE,
            "originX": ORIGIN_X,
            "originZ": ORIGIN_Z,
            "total": len(waypoints),
        }
    }

    # Записать как JS-файл (export для браузера)
    with open(OUTPUT_JS, "w", encoding="utf-8") as f:
        f.write("// Авто-сгенерировано из GOTWaypoint.java + ru_RU.lang\n")
        f.write("// НЕ РЕДАКТИРОВАТЬ ВРУЧНУЮ — запустите generate_waypoints.py\n\n")
        f.write("const MAP_DATA = ")
        json.dump(output, f, ensure_ascii=False, indent=2)
        f.write(";\n")

    print(f"Written to: {OUTPUT_JS}")
    print(f"  Waypoints: {len(waypoints)}, regions: {len(regions)}, factions: {len(factions)}")

    # Показать пример
    print("\nSample (first 3 waypoints):")
    for wp in waypoints[:3]:
        print(f"  {wp['id']} -> '{wp['name']}' [{wp['region']}] X={wp['worldX']} Z={wp['worldZ']}")


if __name__ == "__main__":
    main()
