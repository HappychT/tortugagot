package com.tortugagot.togcore;

import com.tortugagot.togcore.client.TOGClientTechnologyData;
import com.tortugagot.togcore.client.TOGClientPassiveCounterData;
import com.tortugagot.togcore.client.TOGWeaponSkillClientEvents;
import com.tortugagot.togcore.client.renderer.TOGRenderers;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;
import software.bernie.geckolib3.GeckoLib;

import java.util.Collection;

public class ClientProxy extends CommonProxy {
    private final TOGWeaponSkillClientEvents weaponSkillClientEvents = new TOGWeaponSkillClientEvents();

    @Override
    public void registerRenderers() {
        TOGRenderers.init();
        GeckoLib.initialize();
        FMLCommonHandler.instance().bus().register(weaponSkillClientEvents);
        MinecraftForge.EVENT_BUS.register(weaponSkillClientEvents);
    }

    @Override
    public void receiveTechnologySync(int masteryPoints, Collection<String> unlockedTechnologies) {
        TOGClientTechnologyData.setState(masteryPoints, unlockedTechnologies);
    }

    @Override
    public void receivePassiveCounterUpdate(String label, int count, int threshold, boolean ready) {
        TOGClientPassiveCounterData.setCounter(label, count, threshold, ready);
    }

    @Override
    public boolean hasClientTechnology(String id) {
        return TOGClientTechnologyData.isSynced() && TOGClientTechnologyData.hasUnlocked(id);
    }
}
