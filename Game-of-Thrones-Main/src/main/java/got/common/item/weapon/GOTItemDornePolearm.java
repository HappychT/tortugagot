package got.common.item.weapon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemDornePolearm extends GOTItemPolearm{
    public GOTItemDornePolearm(ToolMaterial material) {
        super(material);
    }
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocalFormatted("got.faction.DORNE.name"));
    }
}
