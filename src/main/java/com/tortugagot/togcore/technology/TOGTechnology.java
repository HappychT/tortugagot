package com.tortugagot.togcore.technology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TOGTechnology {
    private static final int UNLOCK_COST_GROWTH_PER_TECHNOLOGY = 5;

    private final String id;
    private final String name;
    private final String description;
    private final String unlocks;
    private final String lockedEffect;
    private final int baseCost;
    private final int branchMask;
    private final int x;
    private final int y;
    private final List<String> prerequisites;

    public TOGTechnology(String id, String name, String description, String unlocks, String lockedEffect, int baseCost, int branchMask, int x, int y, String... prerequisites) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unlocks = unlocks;
        this.lockedEffect = lockedEffect;
        this.baseCost = baseCost;
        this.branchMask = branchMask;
        this.x = x;
        this.y = y;
        this.prerequisites = new ArrayList<String>();
        if (prerequisites != null) {
            Collections.addAll(this.prerequisites, prerequisites);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUnlocks() {
        return unlocks;
    }

    public String getLockedEffect() {
        return lockedEffect;
    }

    public int getCost() {
        return baseCost;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public int getUnlockCost(int alreadyUnlockedCount) {
        return baseCost + (alreadyUnlockedCount + 1) * UNLOCK_COST_GROWTH_PER_TECHNOLOGY;
    }

    public int getBranchMask() {
        return branchMask;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<String> getPrerequisites() {
        return Collections.unmodifiableList(prerequisites);
    }

    public boolean hasBranch(TOGTechnologyBranch branch) {
        return (branchMask & branch.getMask()) != 0;
    }

    public int getDisplayColor() {
        int count = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        for (TOGTechnologyBranch branch : TOGTechnologyBranch.values()) {
            if (hasBranch(branch)) {
                int color = branch.getColor();
                r += color >> 16 & 0xFF;
                g += color >> 8 & 0xFF;
                b += color & 0xFF;
                count++;
            }
        }
        if (count == 0) {
            return 0x888888;
        }
        return r / count << 16 | g / count << 8 | b / count;
    }
}
