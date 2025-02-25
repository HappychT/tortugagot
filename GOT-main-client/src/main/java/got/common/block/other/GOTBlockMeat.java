package got.common.block.other;

import got.common.database.GOTCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GOTBlockMeat extends Block {

    public GOTBlockMeat() {
        super(Material.circuits);
        this.setCreativeTab(GOTCreativeTabs.tabFood);
        this.setHardness(0.35f);
        this.setStepSound(soundTypeGrass);
    }

}
