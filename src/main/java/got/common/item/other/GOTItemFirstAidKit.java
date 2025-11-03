package got.common.item.other;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class GOTItemFirstAidKit extends Item {
    public GOTItemFirstAidKit() {
        super();
        this.setCreativeTab(GOTCreativeTabs.tabMisc);
        this.setMaxStackSize(1);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add(EnumChatFormatting.DARK_AQUA + "Увеличивает лечение от бинтов");
        list.add(EnumChatFormatting.YELLOW + "Достаточно хранить в инвентаре");
    }
}