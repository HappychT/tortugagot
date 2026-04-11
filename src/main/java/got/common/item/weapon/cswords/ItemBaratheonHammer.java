package got.common.item.weapon.cswords;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.item.weapon.GOTItemLegendaryHammer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemBaratheonHammer extends GOTItemLegendaryHammer {

    public ItemBaratheonHammer(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabStory);
    }



    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("");
        list.add(EnumChatFormatting.RED + "Критическая Атака" + EnumChatFormatting.WHITE + " – с прыжка сносит 25 прочности нагруднику.");
        list.add(EnumChatFormatting.DARK_RED + "Ярость" + EnumChatFormatting.WHITE + " – при убийстве игрока накладывает Силу и Скорость на 1 мин.");
        list.add("");
        list.add(EnumChatFormatting.GRAY + "Тот самый молот, которым Роберт разбил");
        list.add(EnumChatFormatting.GRAY + "грудь принцу Рейегару.");
    }

}