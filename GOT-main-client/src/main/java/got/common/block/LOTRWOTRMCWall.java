package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.wall.GOTBlockWallBase;
import got.common.database.GOTRegistry;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCWall extends GOTBlockWallBase {
	public LOTRWOTRMCWall() {
		super(GOTRegistry.brick1, 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j == 0)
			return WOTRMCNewBlocks.avariBlock2.getIcon(i, 0);
		if (j == 1)
			return WOTRMCNewBlocks.avariBorder.getIcon(i, 0);
		if (j == 2)
			return WOTRMCNewBlocks.avariFullblock.getIcon(i, 0);
		if (j == 3)
			return WOTRMCNewBlocks.avariDarkBlock2.getIcon(i, 0);
		if (j == 4)
			return WOTRMCNewBlocks.avariDarkBorder.getIcon(i, 0);
		if (j == 5)
			return WOTRMCNewBlocks.avariDarkFullblock.getIcon(i, 0);
		if (j == 6)
			return WOTRMCNewBlocks.aeornthrimlargebrick.getIcon(i, 0);
		if (j == 7)
			return WOTRMCNewBlocks.aeornthrimroughlargebrick.getIcon(i, 0);
		if (j == 8)
			return WOTRMCNewBlocks.aeornthrimsmallbrick.getIcon(i, 0);
		if (j == 9)
			return WOTRMCNewBlocks.angmardetailedbrick.getIcon(i, 0);
		if (j == 10)
			return WOTRMCNewBlocks.angmarlargebricks.getIcon(i, 0);
		if (j == 11)
			return WOTRMCNewBlocks.angmarsmallbrick.getIcon(i, 0);
		if (j == 12)
			return WOTRMCNewBlocks.balchothbrick.getIcon(i, 0);
		if (j == 13)
			return WOTRMCNewBlocks.balchothlargebricks.getIcon(i, 0);
		if (j == 14)
			return WOTRMCNewBlocks.balchothsmallbrick.getIcon(i, 0);
		if (j == 15)
			return WOTRMCNewBlocks.bluedwarfbigbrick.getIcon(i, 0);
		return super.getIcon(i, j);
	}
}


