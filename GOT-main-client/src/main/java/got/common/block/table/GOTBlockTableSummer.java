package got.common.block.table;

import got.common.faction.GOTFaction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GOTBlockTableSummer extends GOTBlockCraftingTable {
	public GOTBlockTableSummer() {
		super(Material.wood, GOTFaction.HIGH_POWER, 25);
		setStepSound(Block.soundTypeWood);
	}
}
