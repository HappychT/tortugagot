package got.common.block.slab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class GOTBlockSlabIce  extends GOTBlockSlabBase {
    public GOTBlockSlabIce(boolean flag) {
        super(flag, Material.ice, 1);
        setHardness(0.6f);
        setStepSound(Block.soundTypeGlass);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public IIcon getIcon(int i, int j) {
        return Blocks.ice.getIcon(i, 0);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconregister) {
    }


}
