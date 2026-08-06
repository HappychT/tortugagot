package com.tortugagot.togcore.recipe;

import com.tortugagot.togcore.TogCore;
import com.tortugagot.togcore.technology.TOGTechnologyLocks;
import com.tortugagot.togcore.technology.TOGTechnologyNotifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class TOGRecipeTechnologyShapelessOre implements IRecipe {
    private final ShapelessOreRecipe recipe;
    private final String[] requiredTechnologyIds;

    public TOGRecipeTechnologyShapelessOre(ItemStack result, Object[] recipeParams, String... requiredTechnologyIds) {
        recipe = new ShapelessOreRecipe(result, recipeParams);
        this.requiredTechnologyIds = requiredTechnologyIds;
    }

    public static TOGRecipeTechnologyShapelessOre shapeless(ItemStack result, Object[] recipeParams, String... requiredTechnologyIds) {
        return new TOGRecipeTechnologyShapelessOre(result, recipeParams, requiredTechnologyIds);
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        if (!recipe.matches(inventory, world)) {
            return false;
        }
        if (world == null) {
            return true;
        }
        if (world.isRemote) {
            return hasClientTechnologies();
        }
        EntityPlayer player = TOGRecipePlayerResolver.findPlayer(inventory);
        String missingTechnologyId = getMissingServerTechnology(player);
        if (missingTechnologyId != null) {
            TOGTechnologyNotifier.notifyBlocked(player, "craft:" + getResultKey() + ":" + missingTechnologyId, "создать " + getResultName(), "не открыта технология для этого крафта", missingTechnologyId);
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        return recipe.getCraftingResult(inventory);
    }

    @Override
    public int getRecipeSize() {
        return recipe.getRecipeSize();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return recipe.getRecipeOutput();
    }

    private boolean hasClientTechnologies() {
        for (String technologyId : requiredTechnologyIds) {
            if (!TogCore.proxy.hasClientTechnology(technologyId)) {
                return false;
            }
        }
        return true;
    }

    private String getMissingServerTechnology(EntityPlayer player) {
        for (String technologyId : requiredTechnologyIds) {
            if (!TOGTechnologyLocks.has(player, technologyId)) {
                return technologyId;
            }
        }
        return null;
    }

    private String getResultName() {
        ItemStack output = recipe.getRecipeOutput();
        return output != null ? output.getDisplayName() : "предмет";
    }

    private String getResultKey() {
        ItemStack output = recipe.getRecipeOutput();
        if (output == null || output.getItem() == null) {
            return "unknown";
        }
        return Item.getIdFromItem(output.getItem()) + ":" + output.getItemDamage();
    }
}
