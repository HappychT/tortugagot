package com.tortugagot.togcore.recipe;

import com.tortugagot.togcore.registry.TOGItemRegistry;
import com.tortugagot.togcore.technology.TOGTechnologyLocks;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import noname.weapons.RegItem;

public final class TOGRecipeRegistry {
    private static boolean registered;

    private TOGRecipeRegistry() {
    }

    public static void registerRecipes() {
        if (registered) {
            return;
        }
        registered = true;

        OreDictionary.registerOre("ingotSteel", new ItemStack(TOGItemRegistry.steel));
        OreDictionary.registerOre("plateSteel", new ItemStack(TOGItemRegistry.steelPlate));
        OreDictionary.registerOre("plateHardenedSteel", new ItemStack(TOGItemRegistry.hardenedSteelPlate));

        GameRegistry.addRecipe(new TOGRecipeSteelPlate());
        GameRegistry.addRecipe(new TOGRecipeBallistaBolt());
        GameRegistry.addRecipe(TOGRecipeTechnologyShapedOre.shaped(new ItemStack(RegItem.stoneProjectile), new Object[] {
                "XXX",
                "XXX",
                "XXX",
                Character.valueOf('X'), Blocks.cobblestone
        }, TOGTechnologyLocks.APPRENTICE_ENGINEER));
    }
}
