package com.tortugagot.togcore;

import com.tortugagot.togcore.command.TOGCommandTechnology;
import com.tortugagot.togcore.item.TOGMasteryItemEvents;
import com.tortugagot.togcore.recipe.TOGRecipeRegistry;
import com.tortugagot.togcore.technology.TOGTechnologyEvents;
import com.tortugagot.togcore.technology.TOGWeaponSkillEvents;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collection;

public class CommonProxy {
    private final TOGTechnologyEvents technologyEvents = new TOGTechnologyEvents();
    private final TOGMasteryItemEvents masteryItemEvents = new TOGMasteryItemEvents();
    private final TOGWeaponSkillEvents weaponSkillEvents = new TOGWeaponSkillEvents();

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(technologyEvents);
        MinecraftForge.EVENT_BUS.register(masteryItemEvents);
        MinecraftForge.EVENT_BUS.register(weaponSkillEvents);
        FMLCommonHandler.instance().bus().register(technologyEvents);
        FMLCommonHandler.instance().bus().register(masteryItemEvents);
        FMLCommonHandler.instance().bus().register(weaponSkillEvents);
        TOGRecipeRegistry.registerRecipes();
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new TOGCommandTechnology());
    }

    public void registerRenderers() {
    }

    public void receiveTechnologySync(int masteryPoints, Collection<String> unlockedTechnologies) {
    }

    public boolean hasClientTechnology(String id) {
        return false;
    }
}
