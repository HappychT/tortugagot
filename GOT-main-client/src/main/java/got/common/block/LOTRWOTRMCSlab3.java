package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.slab.GOTBlockSlabBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCSlab3 extends GOTBlockSlabBase {
	public LOTRWOTRMCSlab3(boolean flag) {
		super(flag, Material.rock, 8);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if ((j &= 7) == 0)
			return WOTRMCNewBlocks.bluedwarfborder.getIcon(i, 0);
		if (j == 1)
			return WOTRMCNewBlocks.bluedwarfsmallbrick.getIcon(i, 0);
		if (j == 2)
			return WOTRMCNewBlocks.cargonborder.getIcon(i, 0);
		if (j == 3)
			return WOTRMCNewBlocks.cargonlargebrick.getIcon(i, 0);
		if (j == 4)
			return WOTRMCNewBlocks.cargonsmallbrick.getIcon(i, 0);
		if (j == 5)
			return WOTRMCNewBlocks.cerinrimbrick.getIcon(i, 0);
		if (j == 6)
			return WOTRMCNewBlocks.cerinrimdetailedbrick.getIcon(i, 0);
		return WOTRMCNewBlocks.cerinrimlargebricks.getIcon(i, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
	}
}
