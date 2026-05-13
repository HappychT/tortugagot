package com.blocknpc;

import com.blocknpc.entity.EntityBlockNPC;
import com.blocknpc.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EnumCreatureType;

@Mod(modid = BlockTextureNPCMod.MODID, name = "Block Texture NPC", version = "1.0.0")
public class BlockTextureNPCMod {

    public static final String MODID = "blocktexnpc";

    @Instance(MODID)
    public static BlockTextureNPCMod instance;

    @SidedProxy(
            clientSide = "com.blocknpc.proxy.ClientProxy",
            serverSide = "com.blocknpc.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        EntityRegistry.registerModEntity(
                EntityBlockNPC.class, "BlockTextureNPC",
                1, instance, 64, 1, true
        );

        net.minecraft.world.biome.BiomeGenBase[] allBiomes =
                net.minecraft.world.biome.BiomeGenBase.getBiomeGenArray();
        java.util.List<net.minecraft.world.biome.BiomeGenBase> validBiomes =
                new java.util.ArrayList<net.minecraft.world.biome.BiomeGenBase>();
        for (net.minecraft.world.biome.BiomeGenBase b : allBiomes) {
            if (b != null) validBiomes.add(b);
        }
        EntityRegistry.addSpawn(
                EntityBlockNPC.class,
                5, 1, 3,
                EnumCreatureType.creature,
                validBiomes.toArray(new net.minecraft.world.biome.BiomeGenBase[0])
        );
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerRenderers();
    }
}
