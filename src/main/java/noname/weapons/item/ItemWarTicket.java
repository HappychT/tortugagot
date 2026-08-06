package noname.weapons.item;

import got.common.database.GOTCreativeTabs;
import net.minecraft.item.Item;

public class ItemWarTicket extends Item {

    public ItemWarTicket() {
        setUnlocalizedName("war_ticket");
        setTextureName("got:millitary_card");
        setMaxStackSize(1);
        setMaxDamage(3);
        setCreativeTab(GOTCreativeTabs.tabMisc);
    }
}
