package com.tortugagot.togcore.client;

import com.tortugagot.togcore.client.gui.TOGGuiTechnologyTree;
import net.minecraft.client.Minecraft;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public final class TOGClientTechnologyData {
    private static int masteryPoints;
    private static boolean synced;
    private static final Set<String> unlockedTechnologies = new LinkedHashSet<String>();

    private TOGClientTechnologyData() {
    }

    public static void setState(int points, Collection<String> unlocked) {
        masteryPoints = points;
        synced = true;
        unlockedTechnologies.clear();
        unlockedTechnologies.addAll(unlocked);
        if (Minecraft.getMinecraft().currentScreen instanceof TOGGuiTechnologyTree) {
            ((TOGGuiTechnologyTree) Minecraft.getMinecraft().currentScreen).refresh();
        }
    }

    public static int getMasteryPoints() {
        return masteryPoints;
    }

    public static boolean isSynced() {
        return synced;
    }

    public static boolean hasUnlocked(String id) {
        return unlockedTechnologies.contains(id);
    }

    public static int getUnlockedCount() {
        return unlockedTechnologies.size();
    }
}
