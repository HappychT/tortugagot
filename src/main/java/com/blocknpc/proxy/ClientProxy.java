package com.blocknpc.proxy;

import com.blocknpc.client.renderer.RenderBlockNPC;
import com.blocknpc.entity.EntityBlockNPC;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(
            EntityBlockNPC.class,
            new RenderBlockNPC()
        );
    }
}
