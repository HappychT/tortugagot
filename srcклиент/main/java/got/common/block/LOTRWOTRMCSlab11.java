package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.slab.GOTBlockSlabBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCSlab11 extends GOTBlockSlabBase {
	public LOTRWOTRMCSlab11(boolean flag) {
		super(flag, Material.rock, 8);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if ((j &= 7) == 0)
			return WOTRMCNewBlocks.logathrimlargebricks.getIcon(i, 0);
		if (j == 1)
			return WOTRMCNewBlocks.logathrimplate.getIcon(i, 0);
		if (j == 2)
			return WOTRMCNewBlocks.logathrimsmallbrick.getIcon(i, 0);
		if (j == 3)
			return WOTRMCNewBlocks.minasithilbrick.getIcon(i, 0);
		if (j == 4)
			return WOTRMCNewBlocks.minasithilbrickaligned.getIcon(i, 0);
		if (j == 5)
			return WOTRMCNewBlocks.minasithildetailedbrick.getIcon(i, 0);
		if (j == 6)
			return WOTRMCNewBlocks.minasithillargebricks.getIcon(i, 0);
		return WOTRMCNewBlocks.minasithilplate.getIcon(i, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
	}
}

