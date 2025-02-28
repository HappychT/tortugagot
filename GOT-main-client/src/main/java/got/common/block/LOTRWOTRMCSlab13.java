package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.slab.GOTBlockSlabBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCSlab13 extends GOTBlockSlabBase {
	public LOTRWOTRMCSlab13(boolean flag) {
		super(flag, Material.rock, 8);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if ((j &= 7) == 0)
			return WOTRMCNewBlocks.proshloyebrickbrick.getIcon(i, 0);
		if (j == 1)
			return WOTRMCNewBlocks.proshloyebrickbrickaligned.getIcon(i, 0);
		if (j == 2)
			return WOTRMCNewBlocks.proshloyebrickdark.getIcon(i, 0);
		if (j == 3)
			return WOTRMCNewBlocks.proshloyebrickdetailedbrick.getIcon(i, 0);
		if (j == 4)
			return WOTRMCNewBlocks.proshloyebricklargebricks.getIcon(i, 0);
		if (j == 5)
			return WOTRMCNewBlocks.proshloyebrickplate.getIcon(i, 0);
		if (j == 6)
			return WOTRMCNewBlocks.proshloyebricksmallbrick.getIcon(i, 0);
		return WOTRMCNewBlocks.rednearharadborder.getIcon(i, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
	}
}
