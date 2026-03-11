package got.common.systems;

import java.util.HashMap;
import java.util.Map;

import got.common.database.GOTEffects;
import got.common.item.tool.GOTItemAxe;
import got.common.item.weapon.*;
import got.common.item.weapon.cswords.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;

public class GOTCoreBlockingSystem {
    private static final Map<Class<?>, WeaponBlockData> weaponBlockDataMap = new HashMap<>();

    public static void registerWeapon(Class<?> weaponClass, float leftBlockAngle, float rightBlockAngle, double staminaHitPercent, double staminaAttackPercent) {
        weaponBlockDataMap.put(weaponClass, new WeaponBlockData(leftBlockAngle, rightBlockAngle, staminaHitPercent, staminaAttackPercent));
    }

    public static void registerWeapons() {
        registerWeapon(GOTItemNorthGreatSword.class, 35.0f, 35.0f, 2, 2.5);
        registerWeapon(ItemHeartsbaneSword.class, 35.0f, 35.0f, 2.0, 2.5);
        registerWeapon(ItemBrightroarSword.class, 35.0f, 35.0f, 2.0, 2.5);
        registerWeapon(ItemDawnSword.class, 35.0f, 35.0f, 2.0, 2.5);
        registerWeapon(ItemLongclawSword.class, 32.0f, 32.0f, 1.8, 2.3);
        registerWeapon(ItemBlackfyreSword.class, 32.0f, 32.0f, 1.8, 2.3);
        registerWeapon(ItemSunspear.class, 15.0f, 15.0f, 3.0, 3.5);
        registerWeapon(ItemObaraSpear.class, 15.0f, 15.0f, 3.0, 3.5);
        registerWeapon(GOTItemCeltigarAxe.class, 25.0f, 25.0f, 3.0, 3.5);
        registerWeapon(GOTItemAreoHotahAxe.class, 20.0f, 20.0f, 3.0, 3.5);
        registerWeapon(GOTItemVictarionAxe.class, 28.0f, 28.0f, 2.0, 2.5);
        registerWeapon(GOTItemTyrionAxe.class, 20.0f, 20.0f, 1.5, 1.8);
        registerWeapon(ItemGendryHammer.class, 20.0f, 20.0f, 2.5, 3.0);
        registerWeapon(GOTItemSword.class, 30.0f, 30.0f, 1.5, 1.8);
        registerWeapon(ItemSword.class, 30.0f, 30.0f, 1.5, 1.8);
        registerWeapon(GOTItemArrynClaymore.class, 32.0f, 32.0f, 1.5, 1.8);
        registerWeapon(ItemLightbringer.class, 35.0f, 35.0f, 1.8, 2.0);
        registerWeapon(ItemNightKingSword.class, 35.0f, 35.0f, 2.5, 3.0);
        registerWeapon(ItemIceStarkSword.class, 35.0f, 35.0f, 2.0, 2.5);
        registerWeapon(ItemPoisonedSandBlade.class, 15.0f, 90.0f, 1.5, 2.2);
        registerWeapon(ItemSyrioForelSword.class, 30.0f, 30.0f, 1.5, 1.8);
        registerWeapon(ItemStunningHammer.class, 20.0f, 20.0f, 2, 2.5);
        registerWeapon(ItemDecoyClaymore.class, 38.0f, 38.0f, 1.8, 2.0);

        registerWeapon(ItemBaratheonHammer.class, 15.0f, 15.0f, 3, 3.5);
        registerWeapon(GOTItemHammer.class, 20.0f, 20.0f, 2.4, 3.6);
        registerWeapon(GOTItemStormlandsHammer.class, 20.0f, 20.0f, 2.4, 3.6);
        
        registerWeapon(GOTItemIronBornAxe.class, 28.0f, 28.0f, 1.8, 2.4);
        registerWeapon(GOTItemAxe.class, 28.0f, 28.0f, 1.8, 2.4);
        registerWeapon(GOTItemBattleaxe.class, 28.0f, 28.0f, 1.8, 2.4);

        registerWeapon(GOTItemDagger.class, 20.0f, 20.0f, 1.2, 1.2);

        registerWeapon(GOTItemDornePolearm.class, 10.0f, 90.0f, 3.0, 3.0);

        registerWeapon(GOTItemPoleaxe.class, 25.0f, 25.0f, 2.1, 3.6);
        registerWeapon(GOTItemPolearm.class, 25.0f, 25.0f, 2.1, 3.6);
        registerWeapon(GOTItemShieldSpear.class, 40.0f, 40.0f, 2.1, 3.6);
        registerWeapon(GOTItemShieldPike.class, 40.0f, 40.0f, 2.4, 3.6);
        registerWeapon(GOTItemShieldReachPike.class, 40.0f, 40.0f, 2.4, 3.6);
        registerWeapon(GOTItemShieldRiverlandsTrident.class, 40.0f, 40.0f, 2.4, 3.6);
        registerWeapon(GOTItemValyrianShieldHalbert.class, 40.0f, 40.0f, 2.4, 3.7);
        registerWeapon(GOTItemValyrianShieldSpear.class, 40.0f, 40.0f, 2.4, 3.6);
    }

    public static WeaponBlockData getBlockData(Class<?> weaponClass, EntityPlayer player) {
        WeaponBlockData data = weaponBlockDataMap.getOrDefault(weaponClass, new WeaponBlockData(30.0f, 30.0f, 6.0, 3.6));
        if (player.isPotionActive(GOTEffects.exhaustion) || player.isPotionActive(Potion.digSlowdown))
            return new WeaponBlockData(data.getLeftBlockAngle() - 5.0f, data.getRightBlockAngle() - 5.0f, data.getStaminaHitPercent(), data.getStaminaAttackPercent());
        return data;
    }

    public static class WeaponBlockData {
        private final float leftBlockAngle;
        private final float rightBlockAngle;
        private final double staminaHitPercent;
        private final double staminaAttackPercent;

        public WeaponBlockData(float leftBlockAngle, float rightBlockAngle, double staminaHitPercent, double staminaAttackPercent) {
            this.leftBlockAngle = leftBlockAngle;
            this.rightBlockAngle = rightBlockAngle;
            this.staminaHitPercent = staminaHitPercent;
            this.staminaAttackPercent = staminaAttackPercent;
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

        public double getStaminaAttackPercent() {
            return this.staminaAttackPercent;
        }
    }
}