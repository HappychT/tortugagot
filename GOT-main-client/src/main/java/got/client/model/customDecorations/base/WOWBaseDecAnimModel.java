package got.client.model.customDecorations.base;


import got.common.block.base.DecorationTileEntity;
import got.common.decorations.DecorationsRegister;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class WOWBaseDecAnimModel extends WOWBaseDecModel {

    private ResourceLocation geoLoc;
    public WOWBaseDecAnimModel(ResourceLocation geoLoc) {
        super(geoLoc);
    }



    @Override
    public ResourceLocation getAnimationFileLocation(DecorationTileEntity dec) {

        return Objects.requireNonNull(DecorationsRegister.findDecorationByModel(this)).getAnim();

    }
}
