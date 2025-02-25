package got.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.block.wall.GOTBlockWallBase;
import got.common.database.GOTRegistry;
import net.minecraft.util.IIcon;

public class LOTRWOTRMCWall7 extends GOTBlockWallBase {
	public LOTRWOTRMCWall7() {
		super(GOTRegistry.brick1, 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j == 0)
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
		if (j == 7)
			return WOTRMCNewBlocks.rednearharadborder.getIcon(i, 0);
		if (j == 8)
			return WOTRMCNewBlocks.rednearharadbrickaligned.getIcon(i, 0);
		if (j == 9)
			return WOTRMCNewBlocks.rednearharaddetailedbrick.getIcon(i, 0);
		if (j == 10)
			return WOTRMCNewBlocks.southernorocarnibrick.getIcon(i, 0);
		if (j == 11)
			return WOTRMCNewBlocks.southernorocarnibrickaligned.getIcon(i, 0);
		if (j == 12)
			return WOTRMCNewBlocks.southernorocarnilargebricks.getIcon(i, 0);
		if (j == 13)
			return WOTRMCNewBlocks.southernorocarniplate.getIcon(i, 0);
		if (j == 14)
			return WOTRMCNewBlocks.southernorocarnismallbrick.getIcon(i, 0);
		if (j == 15)
			return WOTRMCNewBlocks.taiyobubrick.getIcon(i, 0);
		return super.getIcon(i, j);
	}
}

