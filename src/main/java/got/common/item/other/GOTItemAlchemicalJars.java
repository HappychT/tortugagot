package got.common.item.other;


import got.common.database.GOTCreativeTabs;
import net.minecraft.item.Item;

public class GOTItemAlchemicalJars extends Item {
    public GOTItemAlchemicalJars() {
        super();
        this.setCreativeTab(GOTCreativeTabs.tabMisc);
        this.setMaxStackSize(1);
    }
}