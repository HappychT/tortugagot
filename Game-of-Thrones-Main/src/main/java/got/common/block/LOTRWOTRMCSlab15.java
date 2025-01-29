package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.slab.GOTBlockSlabBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCSlab15 extends GOTBlockSlabBase {
	public LOTRWOTRMCSlab15(boolean flag) {
		super(flag, Material.rock, 7);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if ((j &= 7) == 0)
			return WOTRMCNewBlocks.taiyobubrickaligned.getIcon(i, 0);
		if (j == 1)
			return WOTRMCNewBlocks.taiyobudetailedbrick.getIcon(i, 0);
		if (j == 2)
			return WOTRMCNewBlocks.taiyobulargebricks.getIcon(i, 0);
		if (j == 3)
			return WOTRMCNewBlocks.taiyobuplate.getIcon(i, 0);
		if (j == 4)
			return WOTRMCNewBlocks.taiyobusmallbrick.getIcon(i, 0);
		if (j == 5)
			return WOTRMCNewBlocks.woodelfborder.getIcon(i, 0);
		if (j == 6)
			return WOTRMCNewBlocks.woodelffullbrick.getIcon(i, 0);
		return super.getIcon(i, j);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
	}
}
