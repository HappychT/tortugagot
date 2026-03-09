package com.tortugagot.togcore.client.model;

import com.tortugagot.togcore.TogCore;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public abstract class GeoModelBuilder {

    private GeoModelBuilder() {
    }

    public static <T extends IAnimatable> AnimatedGeoModel<T> create(String id) {
        return new AnimatedGeoModel<T>() {

            @Override
            public ResourceLocation getAnimationFileLocation(T t) {
                if (t instanceof Entity) {
                    return TogCore.id("animations/entity/" + id + ".animation.json");
                }
                if (t instanceof ItemArmor) {
                    return TogCore.id("animations/armor/" + id + ".animation.json");
                }
                if (t instanceof Item) {
                    return TogCore.id("animations/item/" + id + ".animation.json");
                }
                return TogCore.id("animations/entity/" + id + ".animation.json");
            }

            @Override
            public ResourceLocation getModelLocation(T t) { // В Geckolib3 это getModelResource
                if (t instanceof Entity) {
                    return TogCore.id("geo/entity/" + id + ".geo.json");
                }
                if (t instanceof ItemArmor) {
                    return TogCore.id("geo/armor/" + id + ".geo.json");
                }
                if (t instanceof Item) {
                    return TogCore.id("geo/item/" + id + ".geo.json");
                }
                return TogCore.id("geo/entity/" + id + ".geo.json");
            }

            @Override
            public ResourceLocation getTextureLocation(T t) { // В Geckolib3 это getTextureResource
                if (t instanceof Entity) {
                    return TogCore.id("textures/entity/" + id + ".png");
                }
                if (t instanceof ItemArmor) {
                    return TogCore.id("textures/armor/" + id + ".png");
                }
                if (t instanceof Item) {
                    return TogCore.id("textures/item/" + id + ".png");
                }
                return TogCore.id("textures/entity/" + id + ".png");
            }
        };
    }
}