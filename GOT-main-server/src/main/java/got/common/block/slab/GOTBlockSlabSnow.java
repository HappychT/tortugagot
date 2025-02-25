package got.common.block.slab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import java.util.Random;

public class GOTBlockSlabSnow  extends GOTBlockSlabBase {
    public GOTBlockSlabSnow (boolean flag) {
        super(flag, Material.sand, 1);
        setHardness(0.6f);
        setStepSound(Block.soundTypeSnow);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public IIcon getIcon(int i, int j) {
        return Blocks.snow.getIcon(i, 0);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconregister) {
    }


}