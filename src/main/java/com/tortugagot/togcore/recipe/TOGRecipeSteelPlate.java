package com.tortugagot.togcore.recipe;

import com.tortugagot.togcore.TogCore;
import com.tortugagot.togcore.registry.TOGItemRegistry;
import com.tortugagot.togcore.technology.TOGTechnologyLocks;
import com.tortugagot.togcore.technology.TOGTechnologyNotifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class TOGRecipeSteelPlate implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        if (!matchesSteelSquare(inventory)) {
            return false;
        }
        if (world == null) {
            return true;
        }
        if (world.isRemote) {
            return TogCore.proxy.hasClientTechnology(TOGTechnologyLocks.STEEL_PLATE);
        }
        EntityPlayer player = TOGRecipePlayerResolver.findPlayer(inventory);
        if (!TOGTechnologyLocks.has(player, TOGTechnologyLocks.STEEL_PLATE)) {
            TOGTechnologyNotifier.notifyBlocked(player, "craft:steel_plate", "создать стальную пластину", "не открыта технология для этого крафта", TOGTechnologyLocks.STEEL_PLATE);
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        return getRecipeOutput().copy();
    }

    @Override
    public int getRecipeSize() {
        return 4;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(TOGItemRegistry.steelPlate);
    }

    private boolean matchesSteelSquare(InventoryCrafting inventory) {
        int size = inventory.getSizeInventory();
        if (size == 4) {
            return isSteelOnlyInSlots(inventory, 0, 1, 2, 3);
        }
        if (size == 9) {
            return isSteelOnlyInSlots(inventory, 0, 1, 3, 4)
                    || isSteelOnlyInSlots(inventory, 1, 2, 4, 5)
                    || isSteelOnlyInSlots(inventory, 3, 4, 6, 7)
                    || isSteelOnlyInSlots(inventory, 4, 5, 7, 8);
        }
        return false;
    }

    private boolean isSteelOnlyInSlots(InventoryCrafting inventory, int a, int b, int c, int d) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            boolean shouldContainSteel = i == a || i == b || i == c || i == d;
            if (stack == null) {
                if (shouldContainSteel) {
                    return false;
                }
                continue;
            }
            if (!shouldContainSteel || stack.getItem() != TOGItemRegistry.steel) {
                return false;
            }
        }
        return true;
    }
}
