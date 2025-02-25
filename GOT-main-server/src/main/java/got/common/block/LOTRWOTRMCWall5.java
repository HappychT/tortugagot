package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.wall.GOTBlockWallBase;
import got.common.database.GOTRegistry;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCWall5 extends GOTBlockWallBase {
	public LOTRWOTRMCWall5() {
		super(GOTRegistry.brick1, 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j == 0)
			return WOTRMCNewBlocks.jinzhunbrickaligned.getIcon(i, 0);
		if (j == 1)
			return WOTRMCNewBlocks.jinzhundetailedbrick.getIcon(i, 0);
		if (j == 2)
			return WOTRMCNewBlocks.jinzhunlargebricks.getIcon(i, 0);
		if (j == 3)
			return WOTRMCNewBlocks.jinzhunplate.getIcon(i, 0);
		if (j == 4)
			return WOTRMCNewBlocks.jinzhunsmallbrick.getIcon(i, 0);
		if (j == 5)
			return WOTRMCNewBlocks.khandlargebricks.getIcon(i, 0);
		if (j == 6)
			return WOTRMCNewBlocks.khandplate.getIcon(i, 0);
		if (j == 7)
			return WOTRMCNewBlocks.khazaddumbrick.getIcon(i, 0);
		if (j == 8)
			return WOTRMCNewBlocks.khazaddumbrickaligned.getIcon(i, 0);
		if (j == 9)
			return WOTRMCNewBlocks.khazaddumdetailedbrick.getIcon(i, 0);
		if (j == 10)
			return WOTRMCNewBlocks.khazaddumlargebricks.getIcon(i, 0);
		if (j == 11)
			return WOTRMCNewBlocks.khazaddumplate.getIcon(i, 0);
		if (j == 12)
			return WOTRMCNewBlocks.khazaddumsmallbrick.getIcon(i, 0);
		if (j == 13)
			return WOTRMCNewBlocks.logathrimbrick.getIcon(i, 0);
		if (j == 14)
			return WOTRMCNewBlocks.logathrimbrickaligned.getIcon(i, 0);
		if (j == 15)
			return WOTRMCNewBlocks.logathrimdetailedbrick.getIcon(i, 0);
		return super.getIcon(i, j);
	}
}
