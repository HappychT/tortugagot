package got.client.model.customDecorations;


import got.common.block.base.DecorationTileEntity;
import got.common.decorations.DecorationsRegister;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.Objects;

public class WOWBarricadeModel extends AnimatedGeoModel<DecorationTileEntity> {

    public WOWBarricadeModel() {

    }

    @Override
    public ResourceLocation getModelLocation(DecorationTileEntity dec) {
        return new ResourceLocation("got", "geo/dungeon_barricade.geo.json");
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
