package com.tortugagot.togcore.technology;

public enum TOGTechnologyBranch {
    CRAFTSMAN(1, "Ремесленник", 0x3D86FF),
    WARRIOR(2, "Воин", 0xD44848),
    GATHERER(4, "Собиратель", 0x3AA65A),
    MINER(8, "\u0428\u0430\u0445\u0442\u0451\u0440", 0xB58A4A),
    ENGINEER(16, "\u0418\u043D\u0436\u0435\u043D\u0435\u0440", 0x8F9AA8),
    COOKING(32, "\u041A\u0443\u043B\u0438\u043D\u0430\u0440\u0438\u044F", 0xDCA24A);

    private final int mask;
    private final String displayName;
    private final int color;

    TOGTechnologyBranch(int mask, String displayName, int color) {
        this.mask = mask;
        this.displayName = displayName;
        this.color = color;
    }

    public int getMask() {
        return mask;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getColor() {
        return color;
    }
}
