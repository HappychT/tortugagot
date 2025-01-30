package got.common.systems;

import got.common.item.tool.GOTItemAxe;
import got.common.item.weapon.*;
import got.common.registers.EffectRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;

import java.util.HashMap;
import java.util.Map;

public class GOTCoreBlockingSystem {
    private static final Map<Class<?>, WeaponBlockData> weaponBlockDataMap = new HashMap<>();

    public static void registerWeapon(Class<?> weaponClass, float leftBlockAngle, float rightBlockAngle, double staminaHitPercent, double staminaMissPercent) {
        weaponBlockDataMap.put(weaponClass, new WeaponBlockData(leftBlockAngle, rightBlockAngle, staminaHitPercent, staminaMissPercent));
    }

    public static void registerWeapons() {
        registerWeapon(GOTItemSword.class, 35.0f, 35.0f, 0.5, 0.3);
        registerWeapon(ItemSword.class, 35.0f, 35.0f, 0.5, 0.3);
        registerWeapon(GOTItemHammer.class, 20.0f, 20.0f, 0.8, 0.6);
        registerWeapon(GOTItemPolearm.class, 25.0f, 25.0f, 0.7, 0.6);
        registerWeapon(GOTItemAxe.class, 28.0f, 28.0f, 0.6, 0.4);
        registerWeapon(GOTItemDagger.class, 20.0f, 20.0f, 0.4, 0.2);
        registerWeapon(GOTItemDornePolearm.class, 10.0f, 90.0f, 1.0, 0.5);
    }

    public static WeaponBlockData getBlockData(Class<?> weaponClass, EntityPlayer player) {
        WeaponBlockData data = weaponBlockDataMap.getOrDefault(weaponClass, new WeaponBlockData(35.0f, 35.0f, 0.5, 0.3));
        if (player.isPotionActive(EffectRegister.exhaustion) || player.isPotionActive(Potion.digSlowdown)) {
            return new WeaponBlockData(data.getLeftBlockAngle() - 5.0f, data.getRightBlockAngle() - 5.0f, data.getStaminaHitPercent(), data.getStaminaMissPercent());
        }
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
            return leftBlockAngle;
        }

        public float getRightBlockAngle() {
            return rightBlockAngle;
        }

        public double getStaminaHitPercent() {
            return staminaHitPercent;
        }

        public double getStaminaMissPercent() {
            return staminaMissPercent;
        }
    }
}