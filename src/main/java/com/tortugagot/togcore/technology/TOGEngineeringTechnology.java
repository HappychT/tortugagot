package com.tortugagot.togcore.technology;

import got.common.database.GOTRegistry;
import got.common.item.weapon.GOTItemSiegeCrossbow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import noname.weapons.entity.EntityBalista;
import noname.weapons.entity.EntityCatapult;
import noname.weapons.entity.EntityTribushet;

public final class TOGEngineeringTechnology {
    private static final float MASTER_RELOAD_FACTOR = 0.75F;

    private TOGEngineeringTechnology() {
    }

    public static boolean hasApprenticeEngineer(EntityPlayer player) {
        return TOGTechnologyLocks.has(player, TOGTechnologyLocks.APPRENTICE_ENGINEER);
    }

    public static boolean hasEngineer(EntityPlayer player) {
        return TOGTechnologyLocks.has(player, TOGTechnologyLocks.ENGINEER);
    }

    public static boolean hasMasterEngineer(EntityPlayer player) {
        return TOGTechnologyLocks.has(player, TOGTechnologyLocks.MASTER_ENGINEER);
    }

    public static boolean canUseRepairKit(EntityPlayer player) {
        return hasApprenticeEngineer(player);
    }

    public static boolean canUseSiegeWeapon(EntityPlayer player, Entity entity) {
        return !isRestrictedSiegeWeapon(entity) || hasEngineer(player);
    }

    public static boolean canUseSiegeCrossbow(EntityPlayer player) {
        return hasMasterEngineer(player);
    }

    public static int getReloadTime(float standardReloadTime, EntityPlayer player) {
        return getReloadTime(standardReloadTime, hasMasterEngineer(player));
    }

    static int getReloadTime(float standardReloadTime, boolean masterEngineer) {
        if (standardReloadTime <= 0.0F) {
            return 0;
        }
        float reloadTime = masterEngineer ? standardReloadTime * MASTER_RELOAD_FACTOR : standardReloadTime;
        return Math.max(1, (int) Math.ceil(reloadTime));
    }

    public static void notifyRepairKitBlocked(EntityPlayer player) {
        TOGTechnologyNotifier.notifyBlocked(player, "engineer:repair_kit", "использовать ремонтный набор инженера", "не открыта технология для использования ремонтного набора", TOGTechnologyLocks.APPRENTICE_ENGINEER);
    }

    public static void notifySiegeWeaponBlocked(EntityPlayer player, Entity entity) {
        TOGTechnologyNotifier.notifyBlocked(player, "engineer:siege_weapon:" + getEntityKey(entity), "использовать осадное орудие", "не открыта технология для управления этим осадным орудием", TOGTechnologyLocks.ENGINEER);
    }

    public static void notifySiegeCrossbowBlocked(EntityPlayer player) {
        TOGTechnologyNotifier.notifyBlocked(player, "engineer:siege_crossbow", "использовать осадный арбалет", "не открыта технология для использования осадного арбалета", TOGTechnologyLocks.MASTER_ENGINEER);
    }

    static boolean isRestrictedSiegeWeapon(Entity entity) {
        return entity != null && isRestrictedSiegeWeaponClass(entity.getClass());
    }

    static boolean isRestrictedSiegeWeaponClass(Class entityClass) {
        return entityClass != null && (EntityBalista.class.isAssignableFrom(entityClass)
                || EntityCatapult.class.isAssignableFrom(entityClass)
                || EntityTribushet.class.isAssignableFrom(entityClass));
    }

    static boolean isSiegeCrossbowItem(Item item) {
        return item != null && (item == GOTRegistry.siegeCrossbow || item instanceof GOTItemSiegeCrossbow);
    }

    private static String getEntityKey(Entity entity) {
        return entity != null ? entity.getClass().getName() : "unknown";
    }
}
