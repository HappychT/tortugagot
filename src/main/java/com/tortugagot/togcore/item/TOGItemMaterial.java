package com.tortugagot.togcore.item;

import com.tortugagot.togcore.TogCore;
import got.common.database.GOTCreativeTabs;
import net.minecraft.item.Item;

public class TOGItemMaterial extends Item {
    public TOGItemMaterial(String name, String textureName) {
        setMaxStackSize(64);
        setCreativeTab(GOTCreativeTabs.tabMaterials);
        setUnlocalizedName(TogCore.MODID + ":" + name);
        setTextureName(textureName);
    }
}
