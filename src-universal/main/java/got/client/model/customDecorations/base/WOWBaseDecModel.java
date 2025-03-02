package got.client.model.customDecorations.base;


import got.common.block.base.DecorationTileEntity;
import got.common.decorations.DecorationsRegister;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.Objects;

public class WOWBaseDecModel extends AnimatedGeoModel<DecorationTileEntity> {

    private ResourceLocation geoLoc;
    public WOWBaseDecModel(ResourceLocation geoLoc) {
        this.geoLoc = geoLoc;
    }

    @Override
    public ResourceLocation getModelLocation(DecorationTileEntity dec) {
        return geoLoc;
    }

    @Override
    public ResourceLocation getTextureLocation(DecorationTileEntity dec) {
        return Objects.requireNonNull(DecorationsRegister.findDecorationByModel(this)).getTex();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DecorationTileEntity dec) {
        return null;
    }
}
