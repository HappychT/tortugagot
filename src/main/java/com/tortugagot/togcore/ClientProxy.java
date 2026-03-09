package com.tortugagot.togcore;

import com.tortugagot.togcore.client.renderer.TOGRenderers;
import software.bernie.geckolib3.GeckoLib;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        TOGRenderers.init();
        GeckoLib.initialize();
    }
}
