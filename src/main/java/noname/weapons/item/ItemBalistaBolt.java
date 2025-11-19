package noname.weapons.item;

import got.common.database.GOTCreativeTabs;
import net.minecraft.item.Item;

public class ItemBalistaBolt extends Item {

    public ItemBalistaBolt() {
        this.setUnlocalizedName("balista_bolt");
        this.setTextureName("got:balista_bolt");
        this.setCreativeTab(GOTCreativeTabs.tabsWaepons);
        this.setMaxStackSize(64);
    }
}

