package noname.weapons.item;

import got.common.database.GOTCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemWarTicket extends Item {

    public ItemWarTicket() {
        setUnlocalizedName("war_ticket");
        setTextureName("got:war_ticket");
        setMaxStackSize(64);
        setCreativeTab(GOTCreativeTabs.tabMisc);
    }
}


