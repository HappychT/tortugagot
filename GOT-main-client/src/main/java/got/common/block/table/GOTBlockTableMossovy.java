package got.common.block.table;

import got.common.faction.GOTFaction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GOTBlockTableMossovy extends GOTBlockCraftingTable {
	public GOTBlockTableMossovy() {
		super(Material.wood, GOTFaction.HIGH_POWER, 88);
		setStepSound(Block.soundTypeWood);
	}
}
