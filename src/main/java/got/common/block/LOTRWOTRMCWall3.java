package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.wall.GOTBlockWallBase;
import got.common.database.GOTRegistry;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCWall3 extends GOTBlockWallBase {
	public LOTRWOTRMCWall3() {
		super(GOTRegistry.brick1, 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j == 0)
			return WOTRMCNewBlocks.dorwinionlargebrick.getIcon(i, 0);
		if (j == 1)
			return WOTRMCNewBlocks.dorwinionpanel.getIcon(i, 0);
		if (j == 2)
			return WOTRMCNewBlocks.dwarvenbrick.getIcon(i, 0);
		if (j == 3)
			return WOTRMCNewBlocks.dwarvenbrickaligned.getIcon(i, 0);
		if (j == 4)
			return WOTRMCNewBlocks.dwarvendetailedbrick.getIcon(i, 0);
		if (j == 5)
			return WOTRMCNewBlocks.dwarvenlargebricks.getIcon(i, 0);
		if (j == 6)
			return WOTRMCNewBlocks.dwarvenplate.getIcon(i, 0);
		if (j == 7)
			return WOTRMCNewBlocks.dwarvensmallbrick.getIcon(i, 0);
		if (j == 8)
			return WOTRMCNewBlocks.ereborbrick.getIcon(i, 0);
		if (j == 9)
			return WOTRMCNewBlocks.ereborbrickaligned.getIcon(i, 0);
		if (j == 10)
			return WOTRMCNewBlocks.erebordetailedbrick.getIcon(i, 0);
		if (j == 11)
			return WOTRMCNewBlocks.ereborlargebricks.getIcon(i, 0);
		if (j == 12)
			return WOTRMCNewBlocks.ereborplate.getIcon(i, 0);
		if (j == 13)
			return WOTRMCNewBlocks.ereborsmallbrick.getIcon(i, 0);
		if (j == 14)
			return WOTRMCNewBlocks.framsburgbrick.getIcon(i, 0);
		if (j == 15)
			return WOTRMCNewBlocks.framsburgbrickaligned.getIcon(i, 0);
		return super.getIcon(i, j);
	}
}
