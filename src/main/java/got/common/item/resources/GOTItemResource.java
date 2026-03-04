package got.common.item.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class GOTItemResource extends Item {
    public GOTItemResource() {
        setCreativeTab(GOTCreativeTabs.tabResources);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean advanced) {
        list.add(EnumChatFormatting.GRAY + "Торговый ресурс");
    }
}
