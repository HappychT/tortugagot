package com.tortugagot.togcore.technology;

import got.common.database.GOTRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public final class TOGBrewingTechnology {
    public static final String BLOCKED_MESSAGE = "\u0412\u044B \u043D\u0435 \u043C\u043E\u0436\u0435\u0442\u0435 \u043F\u0440\u0438\u0433\u043E\u0442\u043E\u0432\u0438\u0442\u044C \u044D\u0442\u043E\u0442 \u043D\u0430\u043F\u0438\u0442\u043E\u043A. \u0421\u043D\u0430\u0447\u0430\u043B\u0430 \u043E\u0442\u043A\u0440\u043E\u0439\u0442\u0435 \u0442\u0435\u0445\u043D\u043E\u043B\u043E\u0433\u0438\u044E \u201C\u041F\u0438\u0432\u043E\u0432\u0430\u0440\u0435\u043D\u0438\u0435\u201D";

    private TOGBrewingTechnology() {
    }

    public static boolean isRestrictedDrink(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        return isRestrictedDrink(stack.getItem());
    }

    public static boolean isRestrictedDrink(Item item) {
        return item != null && (item == GOTRegistry.mugSourMilk
                || item == GOTRegistry.mugMapleBeer
                || item == GOTRegistry.mugPlumKvass
                || item == GOTRegistry.mugWhisky
                || item == GOTRegistry.mugEthanol);
    }

    public static boolean canBrew(EntityPlayer player, ItemStack result) {
        return !isRestrictedDrink(result) || TOGTechnologyLocks.has(player, TOGTechnologyLocks.BREWING);
    }

    public static boolean blockIfNeeded(EntityPlayer player, ItemStack result) {
        if (canBrew(player, result)) {
            return false;
        }
        notifyBlocked(player);
        return true;
    }

    public static void notifyBlocked(EntityPlayer player) {
        if (player != null && !player.worldObj.isRemote) {
            player.addChatMessage(new ChatComponentText(BLOCKED_MESSAGE));
        }
    }
}
