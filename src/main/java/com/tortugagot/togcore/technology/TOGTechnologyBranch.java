package com.tortugagot.togcore.technology;

public enum TOGTechnologyBranch {
    CRAFTSMAN(1, "Ремесленник", 0x3D86FF),
    WARRIOR(2, "Воин", 0xD44848),
    GATHERER(4, "Собиратель", 0x3AA65A);

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
