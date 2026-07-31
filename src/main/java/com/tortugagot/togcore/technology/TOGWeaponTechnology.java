package com.tortugagot.togcore.technology;

import com.tortugagot.togcore.TogCore;
import got.common.database.GOTRegistry;
import got.common.item.weapon.GOTItemCrossbow;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public final class TOGWeaponTechnology {
    public static final float UNSKILLED_WARRIOR_DAMAGE_FACTOR = 0.9F;
    public static final float UNSKILLED_WARRIOR_SPEED_FACTOR = 0.9F;

    private TOGWeaponTechnology() {
    }

    public static String getRequiredTechnology(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        Item item = stack.getItem();
        if (item == GOTRegistry.westerosSword) {
            return TOGTechnologyLocks.SWORD_MASTERY;
        }
        if (item == GOTRegistry.westerosDagger || item == GOTRegistry.westerosDaggerPoisoned) {
            return TOGTechnologyLocks.DAGGER_MASTERY;
        }
        if (item == GOTRegistry.westerosSpear || item == GOTRegistry.shieldWestorSpear) {
            return TOGTechnologyLocks.SPEAR_MASTERY;
        }
        if (item == GOTRegistry.westerosPike || item == GOTRegistry.shieldWestorPike) {
            return TOGTechnologyLocks.PIKE_MASTERY;
        }
        if (item == GOTRegistry.westerosPolearm || item == GOTRegistry.battleaxeWestros) {
            return TOGTechnologyLocks.AXE_MASTERY;
        }
        if (item == GOTRegistry.westerosHammer) {
            return TOGTechnologyLocks.HAMMER_MASTERY;
        }
        if (item instanceof GOTItemCrossbow) {
            return TOGTechnologyLocks.CROSSBOW_MASTERY;
        }
        if (item instanceof ItemBow || item == Items.bow) {
            return TOGTechnologyLocks.BOW_MASTERY;
        }
        return null;
    }

    public static boolean isTrackedWeapon(ItemStack stack) {
        return getRequiredTechnology(stack) != null;
    }

    public static boolean isMeleeWeapon(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        Item item = stack.getItem();
        return item == GOTRegistry.westerosSword
                || item == GOTRegistry.westerosDagger
                || item == GOTRegistry.westerosDaggerPoisoned
                || item == GOTRegistry.westerosSpear
                || item == GOTRegistry.shieldWestorSpear
                || item == GOTRegistry.westerosPike
                || item == GOTRegistry.shieldWestorPike
                || item == GOTRegistry.westerosPolearm
                || item == GOTRegistry.battleaxeWestros
                || item == GOTRegistry.westerosHammer;
    }

    public static boolean isBow(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemBow && !(stack.getItem() instanceof GOTItemCrossbow);
    }

    public static boolean isCrossbow(ItemStack stack) {
        return stack != null && stack.getItem() instanceof GOTItemCrossbow;
    }

    public static boolean hasRequiredTechnology(EntityPlayer player, ItemStack stack) {
        String technologyId = getRequiredTechnology(stack);
        if (technologyId == null) {
            return true;
        }
        if (player == null) {
            return false;
        }
        if (player.worldObj != null && player.worldObj.isRemote) {
            return TogCore.proxy.hasClientTechnology(technologyId);
        }
        return TOGTechnologyLocks.has(player, technologyId);
    }

    public static boolean hasRequiredTechnologyClient(ItemStack stack) {
        String technologyId = getRequiredTechnology(stack);
        return technologyId == null || TogCore.proxy.hasClientTechnology(technologyId);
    }

    public static boolean hasUnskilledWarrior(EntityPlayer player, ItemStack stack) {
        return isMeleeWeapon(stack) && !hasRequiredTechnology(player, stack);
    }

    public static boolean hasUnskilledArcherClient(ItemStack stack) {
        return (isBow(stack) || isCrossbow(stack)) && !hasRequiredTechnologyClient(stack);
    }
}
