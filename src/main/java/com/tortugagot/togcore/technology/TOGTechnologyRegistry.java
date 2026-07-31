package com.tortugagot.togcore.technology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TOGTechnologyRegistry {
    private static final List<TOGTechnology> TECHNOLOGIES = new ArrayList<TOGTechnology>();
    private static final Map<String, TOGTechnology> BY_ID = new LinkedHashMap<String, TOGTechnology>();
    private static final int COL_1 = 92;
    private static final int COL_2 = 124;
    private static final int COL_3 = 156;
    private static final int COL_4 = 188;
    private static final int COL_5 = 220;
    private static final int COL_6 = 252;
    private static final int COL_7 = 284;
    private static final int COL_8 = 316;
    private static final int ROW_CRAFTSMAN = 6;
    private static final int ROW_CRAFTSMAN_SHARED = 34;
    private static final int ROW_CRAFTSMAN_EXTRA = 34;
    private static final int ROW_CLAYMORE = 36;
    private static final int ROW_SWORD = 56;
    private static final int ROW_TWO_HANDED = 76;
    private static final int ROW_DAGGER = 94;
    private static final int ROW_POLEARM = 114;
    private static final int ROW_PIKE = 134;
    private static final int ROW_GLAIVE = 154;
    private static final int ROW_SHIELD = 164;
    private static final int ROW_HAMMER = 144;
    private static final int ROW_AXE = 182;
    private static final int ROW_RANGED = 200;
    private static final int ROW_BOW = 200;
    private static final int ROW_CROSSBOW = 216;
    private static final int ROW_GATHERER = 234;
    private static final int ROW_HUNTER = 254;
    private static final int ROW_FARMER = 274;

    static {
        add("strong_hands", "Сильные руки", "Общий узел между ветками Ремесленника и Воина. Открывает дальнейшую прокачку ремесленных технологий и владения физическим оружием.", "Общий узел между ветками Ремесленника и Воина. Открывает дальнейшую прокачку ремесленных технологий и владения физическим оружием.", "Нельзя открывать следующие связанные уровни Ремесленника и физические воинские владения.", 10, mask(TOGTechnologyBranch.CRAFTSMAN, TOGTechnologyBranch.WARRIOR), COL_1, ROW_CRAFTSMAN_SHARED);
        add("keen_eye", "Зоркий глаз", "Общий узел между ветками Воина и Собирателя. Открывает владение луком и арбалетом, а также дальнейшую прокачку Собирателя, Охотника и Фермера.", "Общий узел между ветками Воина и Собирателя. Открывает владение луком и арбалетом, а также дальнейшую прокачку Собирателя, Охотника и Фермера.", "Нельзя открывать лук, арбалет и следующие связанные технологии Собирателя.", 10, mask(TOGTechnologyBranch.WARRIOR, TOGTechnologyBranch.GATHERER), COL_1, ROW_RANGED);

        add("craftsman_i", "Ремесленник I", "Открывает получение стали: переплавка железа вместе с углём в доменной печи даёт сталь.", "Открывает получение стали: переплавка железа вместе с углём в доменной печи даёт сталь.", "Железо и уголь расходуются при переплавке, но сталь не получается.", 20, mask(TOGTechnologyBranch.CRAFTSMAN), COL_2, ROW_CRAFTSMAN, "strong_hands");
        add("craftsman_ii", "Ремесленник II", "Открывает крафт оружия Вестероса из стали: меча, кинжала, копья, пики, секиры, топора и молота.", "Открывает крафт оружия Вестероса из стали: меча, кинжала, копья, пики, секиры, топора и молота.", "Оружие Вестероса не создаётся; ингредиенты при заблокированном крафте не расходуются.", 30, mask(TOGTechnologyBranch.CRAFTSMAN), COL_3, ROW_CRAFTSMAN, "craftsman_i");
        add("craftsman_iii", "Ремесленник III", "Открывает создание стальной пластины из четырёх единиц стали.", "Открывает создание стальной пластины из четырёх единиц стали.", "Стальная пластина не создаётся; ингредиенты не расходуются.", 40, mask(TOGTechnologyBranch.CRAFTSMAN), COL_4, ROW_CRAFTSMAN, "craftsman_ii");
        add("craftsman_iv", "Ремесленник IV", "Открывает крафт полного комплекта брони Вестероса из стальных пластин.", "Открывает крафт полного комплекта брони Вестероса из стальных пластин.", "Броня Вестероса не создаётся; ингредиенты не расходуются.", 50, mask(TOGTechnologyBranch.CRAFTSMAN), COL_5, ROW_CRAFTSMAN, "craftsman_iii");
        add("craftsman_v", "Ремесленник V", "Открывает создание легированной стали, необходимой для дальнейшего производства закалённых материалов.", "Открывает создание легированной стали, необходимой для дальнейшего производства закалённых материалов.", "Легированная сталь не получается.", 60, mask(TOGTechnologyBranch.CRAFTSMAN), COL_6, ROW_CRAFTSMAN, "craftsman_iv");
        add("craftsman_vi", "Ремесленник VI", "Открывает получение закалённой стальной пластины путём переплавки стальной пластины вместе с легированной сталью.", "Открывает получение закалённой стальной пластины путём переплавки стальной пластины вместе с легированной сталью.", "Материалы и топливо расходуются, но закалённая стальная пластина не получается.", 70, mask(TOGTechnologyBranch.CRAFTSMAN), COL_7, ROW_CRAFTSMAN, "craftsman_v");
        add("craftsman_vii", "Ремесленник VII", "Открывает крафт фракционной брони из закалённых стальных пластин: North, Ironborn, Riverlands, Arryn, Westerlands, Reach, Dorne, Crownlands, Night Watch, Stormlands и Dragonstone.", "Открывает крафт фракционной брони из закалённых стальных пластин: North, Ironborn, Riverlands, Arryn, Westerlands, Reach, Dorne, Crownlands, Night Watch, Stormlands и Dragonstone.", "Фракционная броня не создаётся; ингредиенты не расходуются.", 80, mask(TOGTechnologyBranch.CRAFTSMAN), COL_8, ROW_CRAFTSMAN, "craftsman_vi");
        add("smelter", "Плавильщик", "Открывает возможность использовать и создавать плавильню.", "Открывает возможность использовать и создавать плавильню.", "Нельзя создать и полноценно использовать плавильню.", 30, mask(TOGTechnologyBranch.CRAFTSMAN), COL_8, ROW_CRAFTSMAN_EXTRA, "craftsman_vi");

        add("sword_mastery", "Владение мечом", "Позволяет полноценно использовать одноручные мечи.", "Позволяет полноценно использовать одноручные мечи.", "Без этого навыка, при взятие в руки любой вид меча на игрока должно накладываться \"Неумелый Воин\"", 25, mask(TOGTechnologyBranch.WARRIOR), COL_2, ROW_SWORD, "strong_hands");
        add("claymore_mastery", "Владение клейморами", "Позволяет полноценно использовать клейморы.", "Позволяет полноценно использовать клейморы.", "Без этого навыка, при взятие в руки любой вид клейморов на игрока должно накладываться \"Неумелый Воин\"", 20, mask(TOGTechnologyBranch.WARRIOR), COL_5, ROW_CLAYMORE, "sword_mastery");
        add("two_handed_sword_mastery", "Владение двуручными мечами", "Позволяет полноценно использовать двуручные мечи.", "Позволяет полноценно использовать двуручные мечи.", "Без этого навыка, при взятие в руки любой вид двуручных мечей на игрока должно накладываться \"Неумелый Воин\"", 20, mask(TOGTechnologyBranch.WARRIOR), COL_5, ROW_TWO_HANDED, "sword_mastery");
        add("dagger_mastery", "Владение кинжалами", "Позволяет полноценно использовать кинжалы.", "Позволяет полноценно использовать кинжалы.", "Без этого навыка, при взятие в руки любой вид кинжалов на игрока должно накладываться \"Неумелый Воин\"", 20, mask(TOGTechnologyBranch.WARRIOR), COL_2, ROW_DAGGER, "strong_hands");
        add("polearm_mastery", "Владение древковым оружием", "Открывает дальнейшее изучение копий, пик и глеф.", "Открывает дальнейшее изучение копий, пик и глеф.", "Технология только открывает доступ к следующим", 10, mask(TOGTechnologyBranch.WARRIOR), COL_2, ROW_POLEARM, "strong_hands");
        add("spear_mastery", "Владение копьями", "Позволяет полноценно использовать копья, включая версии копья со щитом при наличии владения щитами.", "Позволяет полноценно использовать копья, включая версии копья со щитом при наличии владения щитами.", "Без этого навыка, при взятие в руки любой вид копий на игрока должно накладываться \"Неумелый Воин\"", 20, mask(TOGTechnologyBranch.WARRIOR), COL_5, ROW_POLEARM, "polearm_mastery");
        add("pike_mastery", "Владение пиками", "Позволяет полноценно использовать пики, включая версии пики со щитом при наличии владения щитами.", "Позволяет полноценно использовать пики, включая версии пики со щитом при наличии владения щитами.", "Без этого навыка, при взятие в руки любой вид пик на игрока должно накладываться \"Неумелый Воин\"", 20, mask(TOGTechnologyBranch.WARRIOR), COL_5, ROW_PIKE, "polearm_mastery");
        add("glaive_mastery", "Владение глефами", "Позволяет полноценно использовать глефы и аналогичное древковое оружие этого типа.", "Позволяет полноценно использовать глефы и аналогичное древковое оружие этого типа.", "Без этого навыка, при взятие в руки любой вид глеф на игрока должно накладываться \"Неумелый Воин\"", 20, mask(TOGTechnologyBranch.WARRIOR), COL_5, ROW_GLAIVE, "polearm_mastery");
        add("shield_mastery", "Владение щитами", "Позволяет использовать щиты, а также варианты копий и пик со щитом при наличии соответствующего владения оружием.", "Позволяет использовать щиты, а также варианты копий и пик со щитом при наличии соответствующего владения оружием.", "Без этого навыка, при взятие в руки любой вид оружия со щитом или щит на игрока должно накладываться \"Неумелый Воин\"", 20, mask(TOGTechnologyBranch.WARRIOR), COL_2, ROW_SHIELD, "strong_hands");
        add("axe_mastery", "Владение топорами и секирами", "Позволяет полноценно использовать боевые топоры и секиры.", "Позволяет полноценно использовать боевые топоры и секиры.", "Без этого навыка, при взятие в руки любой вид топоров или секир на игрока должно накладываться \"Неумелый Воин\"", 30, mask(TOGTechnologyBranch.WARRIOR), COL_2, ROW_AXE, "strong_hands");
        add("hammer_mastery", "Владение молотами", "Позволяет полноценно использовать боевые молоты.", "Позволяет полноценно использовать боевые молоты.", "Без этого навыка, при взятие в руки любой вид молота на игрока должно накладываться \"Неумелый Воин\"", 25, mask(TOGTechnologyBranch.WARRIOR), COL_2, ROW_HAMMER, "strong_hands");
        add("bow_mastery", "Владение луками", "Позволяет полноценно использовать луки.", "Позволяет полноценно использовать луки.", "Без этого навыка, при взятие в руки любой вид луков на игрока должно накладываться \"Неумелый Стрелок\"", 25, mask(TOGTechnologyBranch.WARRIOR), COL_2, ROW_BOW, "keen_eye");
        add("crossbow_mastery", "Владение арбалетами", "Позволяет полноценно использовать арбалеты.", "Позволяет полноценно использовать арбалеты.", "Без этого навыка, при взятие в руки любой вид арбалетов на игрока должно накладываться \"Неумелый Стрелок\"", 25, mask(TOGTechnologyBranch.WARRIOR), COL_4, ROW_CROSSBOW, "keen_eye");

        add("gatherer_i", "Собиратель I", "Открывает сбор ягод, семян и ягодных кустов.", "Открывает сбор ягод, семян и ягодных кустов.", "Растение или куст уничтожается, но соответствующий предмет не выпадает.", 15, mask(TOGTechnologyBranch.GATHERER), COL_2, ROW_GATHERER, "keen_eye");
        add("gatherer_ii", "Собиратель II", "Открывает сбор трав.", "Открывает сбор трав.", "Трава уничтожается, но предмет не выпадает.", 30, mask(TOGTechnologyBranch.GATHERER), COL_3, ROW_GATHERER, "gatherer_i");
        add("gatherer_iii", "Собиратель III", "Открывает сбор цветов.", "Открывает сбор цветов.", "Цветок уничтожается, но предмет не выпадает.", 15, mask(TOGTechnologyBranch.GATHERER), COL_4, ROW_GATHERER, "gatherer_ii");
        add("hunter_i", "Охотник I", "Открывает полноценное получение кожи, меха и рогов с животных.", "Открывает полноценное получение кожи, меха и рогов с животных.", "Животное погибает, но заблокированные охотничьи ресурсы(кожа, мех, рога) не выпадают. Мясо выпадает всегда в единичном варианте.", 15, mask(TOGTechnologyBranch.GATHERER), COL_2, ROW_HUNTER, "keen_eye");
        add("hunter_ii", "Охотник II", "Увеличивает количество мяса, выпадающего с животных.", "Увеличивает количество мяса, выпадающего с животных.", "Дополнительное мясо не выпадает.", 20, mask(TOGTechnologyBranch.GATHERER), COL_3, ROW_HUNTER, "hunter_i");
        add("hunter_iii", "Охотник III", "Увеличивает количество кожи и меха, а также открывает получение шкур.", "Увеличивает количество кожи и меха, а также открывает получение шкур.", "Дополнительная кожа и мех не выпадают; шкуры недоступны.", 25, mask(TOGTechnologyBranch.GATHERER), COL_4, ROW_HUNTER, "hunter_ii");
        add("farmer_i", "Фермер I", "Открывает вспахивание земли и высадку пшеницы.", "Открывает вспахивание земли и высадку пшеницы.", "Земля не вспахивается; игрок получает объяснение причины в чат.", 15, mask(TOGTechnologyBranch.GATHERER), COL_2, ROW_FARMER, "keen_eye");
        add("farmer_ii", "Фермер II", "Открывает выращивание и сбор картофеля, моркови и репы.", "Открывает выращивание и сбор картофеля, моркови и репы.", "Перечисленные культуры нельзя полноценно выращивать и собирать.", 30, mask(TOGTechnologyBranch.GATHERER), COL_3, ROW_FARMER, "farmer_i");
        add("farmer_iii", "Фермер III", "Открывает остальные сельскохозяйственные культуры, кроме табака.", "Открывает остальные сельскохозяйственные культуры, кроме табака.", "Остальные культуры нельзя полноценно выращивать и собирать.", 45, mask(TOGTechnologyBranch.GATHERER), COL_4, ROW_FARMER, "farmer_ii");
        add("farmer_iv", "Фермер IV", "Открывает выращивание и сбор табака.", "Открывает выращивание и сбор табака.", "Табак нельзя полноценно выращивать и собирать.", 50, mask(TOGTechnologyBranch.GATHERER), COL_5, ROW_FARMER, "farmer_iii");
    }

    private TOGTechnologyRegistry() {
    }

    private static void add(String id, String name, String description, String unlocks, String lockedEffect, int baseCost, int branchMask, int x, int y, String... prerequisites) {
        TOGTechnology technology = new TOGTechnology(id, name, description, unlocks, lockedEffect, baseCost, branchMask, x, y, prerequisites);
        TECHNOLOGIES.add(technology);
        BY_ID.put(id, technology);
    }

    private static int mask(TOGTechnologyBranch... branches) {
        int mask = 0;
        for (TOGTechnologyBranch branch : branches) {
            mask |= branch.getMask();
        }
        return mask;
    }

    public static List<TOGTechnology> getAll() {
        return Collections.unmodifiableList(TECHNOLOGIES);
    }

    public static TOGTechnology get(String id) {
        return BY_ID.get(id);
    }
}
