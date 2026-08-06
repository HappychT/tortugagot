package com.tortugagot.togcore.technology;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class TOGTechnologyLocks {
    public static final String STEEL = "craftsman_i";
    public static final String STEEL_PLATE = "craftsman_iii";
    public static final String ALLOY_STEEL = "craftsman_v";
    public static final String HARDENED_STEEL_PLATE = "craftsman_vi";
    public static final String FACTION_ARMOR = "craftsman_vii";
    public static final String EXPERIENCED_SMITH = "experienced_smith";
    public static final String MASTER_SMITH = "master_smith";
    public static final String LEGENDARY_SMITH = "legendary_smith";
    public static final String EXPERIENCED_MINER = "experienced_miner";
    public static final String MASTER_MINER = "master_miner";
    public static final String LEGENDARY_MINER = "legendary_miner";
    public static final String APPRENTICE_ENGINEER = "apprentice_engineer";
    public static final String ENGINEER = "engineer";
    public static final String MASTER_ENGINEER = "master_engineer";
    public static final String COOKING_I = "cooking_i";
    public static final String COOKING_II = "cooking_ii";
    public static final String COOKING_III = "cooking_iii";
    public static final String BREWING = "brewing";
    public static final String SMELTER = "smelter";
    public static final String WESTEROS_WEAPONS = "craftsman_ii";
    public static final String SWORD_MASTERY = "sword_mastery";
    public static final String CLAYMORE_MASTERY = "claymore_mastery";
    public static final String TWO_HANDED_SWORD_MASTERY = "two_handed_sword_mastery";
    public static final String DAGGER_MASTERY = "dagger_mastery";
    public static final String POLEARM_MASTERY = "polearm_mastery";
    public static final String SPEAR_MASTERY = "spear_mastery";
    public static final String PIKE_MASTERY = "pike_mastery";
    public static final String GLAIVE_MASTERY = "glaive_mastery";
    public static final String SHIELD_MASTERY = "shield_mastery";
    public static final String AXE_MASTERY = "axe_mastery";
    public static final String HAMMER_MASTERY = "hammer_mastery";
    public static final String BOW_MASTERY = "bow_mastery";
    public static final String CROSSBOW_MASTERY = "crossbow_mastery";
    public static final String BOW_ARROW_SAVE_I = "bow_arrow_save_i";
    public static final String BOW_STAMINA = "bow_stamina";
    public static final String BOW_ARROW_SAVE_II = "bow_arrow_save_ii";
    public static final String BOW_PASSIVE = "bow_passive";
    public static final String CROSSBOW_BOLT_SAVE_I = "crossbow_bolt_save_i";
    public static final String CROSSBOW_STAMINA = "crossbow_stamina";
    public static final String CROSSBOW_BOLT_SAVE_II = "crossbow_bolt_save_ii";
    public static final String CROSSBOW_PASSIVE = "crossbow_passive";
    public static final String GATHERER_BERRIES = "gatherer_i";
    public static final String GATHERER_HERBS = "gatherer_ii";
    public static final String GATHERER_FLOWERS = "gatherer_iii";
    public static final String FARMER_WHEAT = "farmer_i";
    public static final String FARMER_BASIC_CROPS = "farmer_ii";
    public static final String FARMER_ADVANCED_CROPS = "farmer_iii";
    public static final String FARMER_TOBACCO = "farmer_iv";
    public static final String HUNTER_BASIC = "hunter_i";
    public static final String HUNTER_MORE_MEAT = "hunter_ii";
    public static final String HUNTER_ADVANCED = "hunter_iii";
    private static final double FORGE_USE_RANGE_SQ = 64.0D;

    private TOGTechnologyLocks() {
    }

    public static boolean has(EntityPlayer player, String technologyId) {
        if (player == null) {
            return false;
        }
        TOGTechnologyPlayerData data = TOGTechnologyPlayerData.get(player);
        if (data == null) {
            TOGTechnologyPlayerData.register(player);
            data = TOGTechnologyPlayerData.get(player);
        }
        return data != null && data.hasUnlocked(technologyId);
    }

    public static boolean isUnlockedNearby(World world, int x, int y, int z, String technologyId) {
        if (world == null || world.isRemote) {
            return true;
        }
        for (Object obj : world.playerEntities) {
            if (!(obj instanceof EntityPlayer)) {
                continue;
            }
            EntityPlayer player = (EntityPlayer) obj;
            if (player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= FORGE_USE_RANGE_SQ && has(player, technologyId)) {
                return true;
            }
        }
        return false;
    }
}
