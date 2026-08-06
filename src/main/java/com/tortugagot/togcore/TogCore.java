package com.tortugagot.togcore;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tortugagot.togcore.network.TOGPacketHandler;
import com.tortugagot.togcore.registry.TOGEntityRegistry;
import com.tortugagot.togcore.registry.TOGItemRegistry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = TogCore.MODID, version = "1.0.0", name = "Tortuga GoT Core-mod", acceptedMinecraftVersions = "[1.7.10]")
public class TogCore {

    public static final String MODID = "togcore";
    public static final Logger LOG = LogManager.getLogger(MODID);
    @Mod.Instance(value = "togcore")
    public static TogCore instance;

    @SidedProxy(clientSide = "com.tortugagot.togcore.ClientProxy", serverSide = "com.tortugagot.togcore.CommonProxy")
    public static CommonProxy proxy;

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        TOGPacketHandler.register();
        TOGItemRegistry.registerItems();
        proxy.preInit(event);
        TOGEntityRegistry.registerEntities();
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        proxy.registerRenderers();
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
