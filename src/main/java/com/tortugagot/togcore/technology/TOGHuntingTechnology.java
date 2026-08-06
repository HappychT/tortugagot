package com.tortugagot.togcore.technology;

import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class TOGHuntingTechnology {
    private TOGHuntingTechnology() {
    }

    public static boolean isAnimal(EntityLivingBase entity) {
        return entity instanceof EntityAnimal || entity instanceof EntityWaterMob || entity.getClass().getName().startsWith("got.common.entity.animal.");
    }

    public static DropType getDropType(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return DropType.ALLOWED;
        }
        if (isMeat(stack)) {
            return DropType.MEAT;
        }
        if (isHideOrTrophy(stack)) {
            return DropType.HIDE_OR_TROPHY;
        }
        if (isBasicResource(stack)) {
            return DropType.BASIC_RESOURCE;
        }
        return DropType.ALLOWED;
    }

    public static boolean isLimitedByAdvancedHunter(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.leather || item == GOTRegistry.fur || item == GOTRegistry.lionFur;
    }

    public static String technologyName(String technologyId) {
        return TOGTechnologyNotifier.technologyName(technologyId);
    }

    public static String stackKey(ItemStack stack) {
        return Item.getIdFromItem(stack.getItem()) + ":" + stack.getItemDamage();
    }

    private static boolean isMeat(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.beef || item == Items.cooked_beef || item == Items.porkchop || item == Items.cooked_porkchop || item == Items.chicken || item == Items.cooked_chicken || item == Items.fish || item == Items.cooked_fished || item == Items.rotten_flesh || item == GOTRegistry.muttonRaw || item == GOTRegistry.muttonCooked || item == GOTRegistry.deerRaw || item == GOTRegistry.deerCooked || item == GOTRegistry.camelRaw || item == GOTRegistry.camelCooked || item == GOTRegistry.lionRaw || item == GOTRegistry.lionCooked || item == GOTRegistry.rabbitRaw || item == GOTRegistry.rabbitCooked || item == GOTRegistry.rhinoRaw || item == GOTRegistry.rhinoCooked || item == GOTRegistry.elephantRaw || item == GOTRegistry.elephantCooked || item == GOTRegistry.zebraRaw || item == GOTRegistry.zebraCooked || item == GOTRegistry.walrusLardRaw || item == GOTRegistry.walrusLardCooked;
    }

    private static boolean isBasicResource(ItemStack stack) {
        Item item = stack.getItem();
        if (item == Items.dye && stack.getItemDamage() == 0) {
            return true;
        }
        return item == Items.leather || item == Items.feather || item == Items.bone || item == Item.getItemFromBlock(Blocks.wool) || item == GOTRegistry.fur || item == GOTRegistry.lionFur || item == GOTRegistry.horn || item == GOTRegistry.rhinoHorn || item == GOTRegistry.gemsbokHorn || item == GOTRegistry.whiteBisonHorn || item == GOTRegistry.swanFeather || item == GOTRegistry.scorpionTail || item == GOTRegistry.termite || item == GOTRegistry.rabbitPaw || item == GOTRegistry.bundleFeathers || item == GOTRegistry.bundleFur || item == GOTRegistry.bundleHornsClaws || item == GOTRegistry.tannedLeather;
    }

    private static boolean isHideOrTrophy(ItemStack stack) {
        Item item = stack.getItem();
        return item == GOTRegistry.gemsbokHide || item == GOTRegistry.bearRug || item == GOTRegistry.lionRug || item == GOTRegistry.wargCloak;
    }

    public enum DropType {
        ALLOWED,
        MEAT,
        BASIC_RESOURCE,
        HIDE_OR_TROPHY
    }
}
