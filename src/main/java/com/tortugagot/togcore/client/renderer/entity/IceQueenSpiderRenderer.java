package com.tortugagot.togcore.client.renderer.entity;

import com.tortugagot.togcore.client.model.GeoModelBuilder;
import com.tortugagot.togcore.entity.IceQueenSpider;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class IceQueenSpiderRenderer extends GeoEntityRenderer<IceQueenSpider> {

    public IceQueenSpiderRenderer() {
        super(GeoModelBuilder.create("ice_queen_spider"));
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return this.modelProvider.getTextureLocation((IceQueenSpider) entity);
    }
}
