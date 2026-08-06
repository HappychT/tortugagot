package com.tortugagot.togcore.registry;

import com.tortugagot.togcore.item.TOGItemMasteryPoints;
import com.tortugagot.togcore.item.TOGItemMaterial;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public final class TOGItemRegistry {
    private static final String MASTERY_POINT_TEXTURE = "got:valyrian_book";

    public static final Item masteryPoint1 = new TOGItemMasteryPoints("mastery_point_1", 1, MASTERY_POINT_TEXTURE);
    public static final Item masteryPoint10 = new TOGItemMasteryPoints("mastery_point_10", 10, MASTERY_POINT_TEXTURE);
    public static final Item masteryPoint100 = new TOGItemMasteryPoints("mastery_point_100", 100, MASTERY_POINT_TEXTURE);
    public static final Item steel = new TOGItemMaterial("steel", "togcore:steel");
    public static final Item steelPlate = new TOGItemMaterial("steel_plate", "togcore:steel_plate");
    public static final Item hardenedSteelPlate = new TOGItemMaterial("hardened_steel_plate", "togcore:hardened_steel_plate");

    private TOGItemRegistry() {
    }

    public static void registerItems() {
        registerItem(masteryPoint1, "mastery_point_1");
        registerItem(masteryPoint10, "mastery_point_10");
        registerItem(masteryPoint100, "mastery_point_100");
        registerItem(steel, "steel");
        registerItem(steelPlate, "steel_plate");
        registerItem(hardenedSteelPlate, "hardened_steel_plate");
    }

    private static void registerItem(Item item, String name) {
        GameRegistry.registerItem(item, name);
    }
}
