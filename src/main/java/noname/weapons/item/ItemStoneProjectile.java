package noname.weapons.item;

import got.common.database.GOTCreativeTabs;
import net.minecraft.item.Item;

public class ItemStoneProjectile extends Item {
    
    public ItemStoneProjectile() {
        this.setUnlocalizedName("stone_projectile");
        this.setTextureName("got:stone_projectile");
        this.setCreativeTab(GOTCreativeTabs.tabsWaepons);
        this.setMaxStackSize(64);
    }
}

