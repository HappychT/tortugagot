package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.wall.GOTBlockWallBase;
import got.common.database.GOTRegistry;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCWall8 extends GOTBlockWallBase {
	public LOTRWOTRMCWall8() {
		super(GOTRegistry.brick1, 7);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j == 0)
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
}
