package got.common.item.other;


import got.common.database.GOTCreativeTabs;
import net.minecraft.item.Item;

public class GOTItemGauzeSet extends Item {
    public GOTItemGauzeSet() {
        super();
        this.setCreativeTab(GOTCreativeTabs.tabMisc);
        this.setMaxStackSize(1);
    }
}