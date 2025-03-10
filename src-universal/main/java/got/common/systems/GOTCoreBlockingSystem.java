package got.common.systems;

import java.util.HashMap;
import java.util.Map;

import got.common.database.GOTEffects;
import got.common.item.tool.GOTItemAxe;
import got.common.item.weapon.GOTItemBattleaxe;
import got.common.item.weapon.GOTItemDagger;
import got.common.item.weapon.GOTItemDornePolearm;
import got.common.item.weapon.GOTItemHammer;
import got.common.item.weapon.GOTItemPolearm;
import got.common.item.weapon.GOTItemPoleaxe;
import got.common.item.weapon.GOTItemShieldPike;
import got.common.item.weapon.GOTItemShieldReachPike;
import got.common.item.weapon.GOTItemShieldRiverlandsTrident;
import got.common.item.weapon.GOTItemShieldSpear;
import got.common.item.weapon.GOTItemStormlandsHammer;
import got.common.item.weapon.GOTItemSword;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;

public class GOTCoreBlockingSystem {
    private static final Map<Class<?>, WeaponBlockData> weaponBlockDataMap = new HashMap<>();

    public static void registerWeapon(Class<?> weaponClass, float leftBlockAngle, float rightBlockAngle, double staminaHitPercent, double staminaMissPercent) {
        weaponBlockDataMap.put(weaponClass, new WeaponBlockData(leftBlockAngle, rightBlockAngle, staminaHitPercent, staminaMissPercent));
    }

    public static void registerWeapons() {
        registerWeapon(GOTItemSword.class, 35.0f, 35.0f, 6.0, 3.6);
        registerWeapon(ItemSword.class, 35.0f, 35.0f, 6.0, 3.6);

        registerWeapon(GOTItemHammer.class, 20.0f, 20.0f, 9.6, 7.2);
        registerWeapon(GOTItemStormlandsHammer.class, 20.0f, 20.0f, 9.6, 7.2);

        registerWeapon(GOTItemAxe.class, 28.0f, 28.0f, 7.2, 4.8);
        registerWeapon(GOTItemBattleaxe.class, 28.0f, 28.0f, 7.2, 4.8);

        registerWeapon(GOTItemDagger.class, 20.0f, 20.0f, 4.8, 2.4);

        registerWeapon(GOTItemDornePolearm.class, 10.0f, 90.0f, 12.0, 0.5);

        registerWeapon(GOTItemPoleaxe.class, 25.0f, 25.0f, 8.4, 7.2);
        registerWeapon(GOTItemPolearm.class, 25.0f, 25.0f, 8.4, 7.2);

        registerWeapon(GOTItemShieldSpear.class, 40.0f, 40.0f, 8.4, 7.2);
        registerWeapon(GOTItemShieldPike.class, 40.0f, 40.0f, 9.6, 7.2);
        registerWeapon(GOTItemShieldReachPike.class, 40.0f, 40.0f, 9.6, 7.2);
        registerWeapon(GOTItemShieldRiverlandsTrident.class, 40.0f, 40.0f, 9.6, 7.2);
    }

    public static WeaponBlockData getBlockData(Class<?> weaponClass, EntityPlayer player) {
        WeaponBlockData data = weaponBlockDataMap.getOrDefault(weaponClass, new WeaponBlockData(35.0f, 35.0f, 0.5, 0.3));
        if (player.isPotionActive(GOTEffects.exhaustion) || player.isPotionActive(Potion.digSlowdown))
            return new WeaponBlockData(data.getLeftBlockAngle() - 5.0f, data.getRightBlockAngle() - 5.0f, data.getStaminaHitPercent(), data.getStaminaMissPercent());
        return data;
    }

    public static class WeaponBlockData {
        private final float leftBlockAngle;
        private final float rightBlockAngle;
        private final double staminaHitPercent;
        private final double staminaMissPercent;

        public WeaponBlockData(float leftBlockAngle, float rightBlockAngle, double staminaHitPercent, double staminaMissPercent) {
            this.leftBlockAngle = leftBlockAngle;
            this.rightBlockAngle = rightBlockAngle;
            this.staminaHitPercent = staminaHitPercent;
            this.staminaMissPercent = staminaMissPercent;
        }

        public float getLeftBlockAngle() {
            return this.leftBlockAngle;
        }

        public float getRightBlockAngle() {
            return this.rightBlockAngle;
        }

        public double getStaminaHitPercent() {
            return this.staminaHitPercent;
        }

        public double getStaminaMissPercent() {
            return this.staminaMissPercent;
        }
    }
}