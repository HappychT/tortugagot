package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.slab.GOTBlockSlabBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCSlab14 extends GOTBlockSlabBase {
	public LOTRWOTRMCSlab14(boolean flag) {
		super(flag, Material.rock, 8);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if ((j &= 7) == 0)
			return WOTRMCNewBlocks.rednearharadbrickaligned.getIcon(i, 0);
		if (j == 1)
			return WOTRMCNewBlocks.rednearharaddetailedbrick.getIcon(i, 0);
		if (j == 2)
			return WOTRMCNewBlocks.southernorocarnibrick.getIcon(i, 0);
		if (j == 3)
			return WOTRMCNewBlocks.southernorocarnibrickaligned.getIcon(i, 0);
		if (j == 4)
			return WOTRMCNewBlocks.southernorocarnilargebricks.getIcon(i, 0);
		if (j == 5)
			return WOTRMCNewBlocks.southernorocarniplate.getIcon(i, 0);
		if (j == 6)
			return WOTRMCNewBlocks.southernorocarnismallbrick.getIcon(i, 0);
		return WOTRMCNewBlocks.taiyobubrick.getIcon(i, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
	}
}
