package got.common.item.other;

import got.common.database.GOTCreativeTabs;
import net.minecraft.item.Item;

public class GOTItemFirstAidKit extends Item {
    public GOTItemFirstAidKit() {
        super();
        this.setCreativeTab(GOTCreativeTabs.tabMisc);
        this.setMaxStackSize(1);
    }
}