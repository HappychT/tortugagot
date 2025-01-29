package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.wall.GOTBlockWallBase;
import got.common.database.GOTRegistry;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCWall6 extends GOTBlockWallBase {
	public LOTRWOTRMCWall6() {
		super(GOTRegistry.brick1, 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j == 0)
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
		if (j == 7)
			return WOTRMCNewBlocks.minasithilplate.getIcon(i, 0);
		if (j == 8)
			return WOTRMCNewBlocks.minasithilsmallbrick.getIcon(i, 0);
		if (j == 9)
			return WOTRMCNewBlocks.moredainborder.getIcon(i, 0);
		if (j == 10)
			return WOTRMCNewBlocks.moredainbrickaligned.getIcon(i, 0);
		if (j == 11)
			return WOTRMCNewBlocks.moredaindetailbrick.getIcon(i, 0);
		if (j == 12)
			return WOTRMCNewBlocks.morfalathrimborder.getIcon(i, 0);
		if (j == 13)
			return WOTRMCNewBlocks.morfalathrimfullblock.getIcon(i, 0);
		if (j == 14)
			return WOTRMCNewBlocks.nearharadbigbrick.getIcon(i, 0);
		if (j == 15)
			return WOTRMCNewBlocks.nearharadborder.getIcon(i, 0);
		return super.getIcon(i, j);
	}
}
