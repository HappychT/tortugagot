package got.common.item.weapon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemWesterlandsCrossbow extends GOTItemCrossbow{
    public GOTItemWesterlandsCrossbow(Item.ToolMaterial material) {
        super(material);
    }
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("got.faction.WESTERLANDS.name"));
    }
}
