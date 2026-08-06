package com.tortugagot.togcore.network;

import com.tortugagot.togcore.TogCore;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public final class TOGPacketHandler {
    public static SimpleNetworkWrapper networkWrapper;
    private static int id;

    private TOGPacketHandler() {
    }

    public static void register() {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(TogCore.MODID);
        networkWrapper.registerMessage(TOGPacketTechnologySync.Handler.class, TOGPacketTechnologySync.class, id++, Side.CLIENT);
        networkWrapper.registerMessage(TOGPacketPassiveCounter.Handler.class, TOGPacketPassiveCounter.class, id++, Side.CLIENT);
        networkWrapper.registerMessage(TOGPacketTechnologyUnlock.Handler.class, TOGPacketTechnologyUnlock.class, id++, Side.SERVER);
        networkWrapper.registerMessage(TOGPacketTechnologyRequest.Handler.class, TOGPacketTechnologyRequest.class, id++, Side.SERVER);
    }
}
