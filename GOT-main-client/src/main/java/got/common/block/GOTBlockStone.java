package got.common.block;

import got.common.database.GOTCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GOTBlockStone extends Block {
    public GOTBlockStone() {
        super(Material.rock);
        setCreativeTab(GOTCreativeTabs.tabBlock);
        setHardness(1.5f);
        setResistance(10.0f);
        setStepSound(Block.soundTypeStone);
    }
}
