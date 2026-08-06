package com.tortugagot.togcore.technology;

import com.tortugagot.togcore.TogCore;
import got.common.database.GOTMaterial;
import got.common.database.GOTRegistry;
import got.common.item.GOTMaterialFinder;
import got.common.item.GOTWeaponStats;
import got.common.item.other.GOTItemMountArmor;
import got.common.item.weapon.GOTItemBow;
import got.common.item.weapon.GOTItemCrossbow;
import got.common.item.weapon.GOTItemSarbacane;
import got.common.item.weapon.GOTItemThrowingAxe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public final class TOGSmithingTechnology {
    private TOGSmithingTechnology() {
    }

    public static boolean hasExperiencedSmith(EntityPlayer player) {
        return hasTechnology(player, TOGTechnologyLocks.EXPERIENCED_SMITH);
    }

    public static boolean hasMasterSmith(EntityPlayer player) {
        return hasTechnology(player, TOGTechnologyLocks.MASTER_SMITH);
    }

    public static boolean hasLegendarySmith(EntityPlayer player) {
        return hasTechnology(player, TOGTechnologyLocks.LEGENDARY_SMITH);
    }

    public static int applyExperiencedSmithReforgeDiscount(EntityPlayer player, int materialCost) {
        return applyExperiencedSmithReforgeDiscount(hasExperiencedSmith(player), materialCost);
    }

    public static int applyMasterSmithTemplateDiscount(EntityPlayer player, int alloySteelCost) {
        return applyMasterSmithTemplateDiscount(hasMasterSmith(player), alloySteelCost);
    }

    public static boolean canUseValyrianWeapon(EntityPlayer player, ItemStack stack) {
        return canUseValyrianSmithingItem(player, stack);
    }

    public static boolean canUseValyrianSmithingItem(EntityPlayer player, ItemStack stack) {
        return canUseValyrianSmithingItem(hasLegendarySmith(player), stack);
    }

    static int applyExperiencedSmithReforgeDiscount(boolean experiencedSmith, int materialCost) {
        return applyHalfDiscount(experiencedSmith, materialCost);
    }

    static int applyMasterSmithTemplateDiscount(boolean masterSmith, int alloySteelCost) {
        return applyHalfDiscount(masterSmith, alloySteelCost);
    }

    static boolean canUseValyrianWeapon(boolean legendarySmith, ItemStack stack) {
        return canUseValyrianSmithingItem(legendarySmith, stack);
    }

    static boolean canUseValyrianSmithingItem(boolean legendarySmith, ItemStack stack) {
        return !isValyrianSmithingItem(stack) || legendarySmith;
    }

    public static void notifyValyrianBlocked(EntityPlayer player, String key, String action) {
        TOGTechnologyNotifier.notifyBlocked(player, key, action, "не открыта технология для работы с валирийским снаряжением", TOGTechnologyLocks.LEGENDARY_SMITH);
    }

    public static boolean isValyrianSmithingItem(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        return isValyrianToolOrWeapon(stack) || isValyrianArmor(stack) || isValyrianChisel(stack);
    }

    public static boolean isValyrianWeapon(ItemStack stack) {
        return stack != null && stack.getItem() != null && isWeaponLike(stack) && isValyrianToolOrWeapon(stack);
    }

    public static boolean isValyrianArmor(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        Item item = stack.getItem();
        if (item instanceof ItemArmor) {
            return isValyrianArmorMaterial(((ItemArmor) item).getArmorMaterial());
        }
        if (item instanceof GOTItemMountArmor) {
            return isValyrianArmorMaterial(((GOTItemMountArmor) item).getMountArmorMaterial());
        }
        return false;
    }

    public static boolean isValyrianToolOrWeapon(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        Item item = stack.getItem();
        if (item instanceof GOTItemCrossbow) {
            return isValyrianMaterial(((GOTItemCrossbow) item).getCrossbowMaterial());
        }
        if (item instanceof GOTItemBow) {
            return isValyrianMaterial(((GOTItemBow) item).bowMaterial);
        }
        if (item instanceof GOTItemThrowingAxe) {
            return isValyrianMaterial(((GOTItemThrowingAxe) item).getMaterial());
        }
        if (item instanceof GOTMaterialFinder) {
            return isValyrianMaterial(((GOTMaterialFinder) item).getMaterial());
        }
        if (item instanceof ItemTool) {
            return isValyrianMaterialName(((ItemTool) item).getToolMaterialName());
        }
        if (item instanceof ItemSword) {
            return isValyrianMaterialName(((ItemSword) item).getToolMaterialName());
        }
        return false;
    }

    private static boolean isValyrianChisel(ItemStack stack) {
        return stack != null && stack.getItem() == GOTRegistry.valyrianChisel;
    }

    private static boolean hasTechnology(EntityPlayer player, String technologyId) {
        if (player == null) {
            return false;
        }
        if (player.worldObj != null && player.worldObj.isRemote) {
            return TogCore.proxy.hasClientTechnology(technologyId);
        }
        return TOGTechnologyLocks.has(player, technologyId);
    }

    private static boolean isWeaponLike(ItemStack stack) {
        Item item = stack.getItem();
        return GOTWeaponStats.isMeleeWeapon(stack) || item instanceof ItemBow || item instanceof GOTItemCrossbow || item instanceof GOTItemSarbacane;
    }

    private static boolean isValyrianMaterial(Item.ToolMaterial material) {
        return material == GOTMaterial.VALYRIAN_TOOL
                || material == GOTMaterial.VALYRIAN_TOOL2
                || material != null && isValyrianMaterialName(material.name());
    }

    private static boolean isValyrianArmorMaterial(ItemArmor.ArmorMaterial material) {
        return material == GOTMaterial.VALYRIAN
                || material == GOTMaterial.VALYRIAN_CHAINMAIL
                || material != null && isValyrianArmorMaterialName(material.name());
    }

    private static boolean isValyrianMaterialName(String materialName) {
        return "GOT_VALYRIAN_TOOL".equals(materialName)
                || "GOT_VALYRIAN_TOOL2".equals(materialName)
                || "VALYRIAN_STEEL".equals(materialName);
    }

    private static boolean isValyrianArmorMaterialName(String materialName) {
        return "GOT_VALYRIAN".equals(materialName)
                || "GOT_VALYRIAN_CHAINMAIL".equals(materialName)
                || "VALYRIAN_STEEL".equals(materialName);
    }

    private static int ceilHalf(int value) {
        return value / 2 + value % 2;
    }

    private static int applyHalfDiscount(boolean unlocked, int cost) {
        if (cost <= 0 || !unlocked) {
            return cost;
        }
        return Math.max(1, ceilHalf(cost));
    }
}
