package got.client.render;

import got.client.model.customDecorations.WOWBarricadeModel;
import got.common.block.base.DecorationTileEntity;
import got.common.decorations.DecorationsRegister;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import java.lang.reflect.Field;
import java.util.Objects;

public class DecorationRenderer extends GeoBlockRenderer<DecorationTileEntity> {

    private TileEntity te;
    private Block bl;

    public DecorationRenderer() {
        super(new WOWBarricadeModel()); // We will override getGeoModelProvider(), so this is okay
    }

    public DecorationRenderer(Block bl) {
        super(new WOWBarricadeModel()); // We will override getGeoModelProvider(), so this is okay
        this.bl = bl;
    }

    @Override
    public AnimatedGeoModel<DecorationTileEntity> getGeoModelProvider() {
        TileEntity tile = this.te;
        if (tile != null && tile.getBlockType() != null) {
            GeoModelProvider<?> provider = Objects.requireNonNull(DecorationsRegister.findDecorationByBlock(tile.getBlockType())).getModel();
            if (provider != null) {
                return (AnimatedGeoModel<DecorationTileEntity>) provider;
            }
        }
        return super.getGeoModelProvider(); // Default to parent method if no specific model is found
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
        if (tile != null && tile.getBlockType() != null) {

            if ((DecorationsRegister.findDecorationByBlock(tile.getBlockType())) != null) {
                GeoModelProvider<?> provider = (DecorationsRegister.findDecorationByBlock(tile.getBlockType())).getModel();
                if (provider != null) {
                    try {
                        Field modelProviderField = GeoBlockRenderer.class.getDeclaredField("modelProvider");
                        modelProviderField.setAccessible(true); // Bypass the access control
                        modelProviderField.set(this, provider);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace(); // Handle exceptions appropriately
                    }
                }
            }

            if (this.bl != null) {
                GeoModelProvider<?> provider = Objects.requireNonNull(DecorationsRegister.findDecorationByBlock(bl)).getModel();
                if (provider != null) {
                    try {
                        Field modelProviderField = GeoBlockRenderer.class.getDeclaredField("modelProvider");
                        modelProviderField.setAccessible(true); // Bypass the access control
                        modelProviderField.set(this, provider);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace(); // Handle exceptions appropriately
                    }
                }
            }
            this.te = tile;
            super.render(tile, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
        }
    }


}
