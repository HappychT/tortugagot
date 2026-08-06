package com.tortugagot.togcore.recipe;

import com.tortugagot.togcore.TogCore;
import com.tortugagot.togcore.registry.TOGItemRegistry;
import com.tortugagot.togcore.technology.TOGTechnologyLocks;
import com.tortugagot.togcore.technology.TOGTechnologyNotifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import noname.weapons.RegItem;

public class TOGRecipeBallistaBolt implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        if (!matchesBallistaBoltPattern(inventory)) {
            return false;
        }
        if (world == null) {
            return true;
        }
        if (world.isRemote) {
            return TogCore.proxy.hasClientTechnology(TOGTechnologyLocks.APPRENTICE_ENGINEER);
        }
        EntityPlayer player = TOGRecipePlayerResolver.findPlayer(inventory);
        if (!TOGTechnologyLocks.has(player, TOGTechnologyLocks.APPRENTICE_ENGINEER)) {
            TOGTechnologyNotifier.notifyBlocked(player, "craft:ballista_bolt", "создать снаряд для баллисты", "не открыта технология для этого крафта", TOGTechnologyLocks.APPRENTICE_ENGINEER);
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack output = getRecipeOutput();
        return output != null ? output.copy() : null;
    }

    @Override
    public int getRecipeSize() {
        return 5;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return RegItem.balistaBolt != null ? new ItemStack(RegItem.balistaBolt) : null;
    }

    public static void consumeExtraSteel(ItemStack craftingResult, IInventory inventory) {
    }

    static boolean matchesBallistaBoltPattern(InventoryCrafting inventory) {
        if (inventory == null || inventory.getSizeInventory() != 9) {
            return false;
        }
        ItemStack[] stacks = new ItemStack[9];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = inventory.getStackInSlot(i);
        }
        return matchesBallistaBoltPattern(stacks, TOGItemRegistry.steel, Items.stick);
    }

    public static boolean matchesBallistaBoltPattern(ItemStack[] stacks, Item steelItem, Item stickItem) {
        if (stacks == null || stacks.length != 9) {
            return false;
        }
        return isEmpty(stacks[0])
                && hasItem(stacks[1], steelItem, 1)
                && hasItem(stacks[2], steelItem, 1)
                && isEmpty(stacks[3])
                && hasItem(stacks[4], stickItem, 1)
                && hasItem(stacks[5], steelItem, 1)
                && hasItem(stacks[6], stickItem, 1)
                && isEmpty(stacks[7])
                && isEmpty(stacks[8]);
    }

    private static boolean hasItem(ItemStack stack, Item item, int minAmount) {
        return stack != null && item != null && stack.getItem() == item && stack.stackSize >= minAmount;
    }

    private static boolean isEmpty(ItemStack stack) {
        return stack == null || stack.stackSize <= 0;
    }
}
