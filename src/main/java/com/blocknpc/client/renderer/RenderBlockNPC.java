package com.blocknpc.client.renderer;

import com.blocknpc.client.model.ModelBlockNPC;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBlockNPC extends RenderLiving {

    public RenderBlockNPC() {
        super(new ModelBlockNPC(), 0.5f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TextureMap.locationBlocksTexture;
    }
}