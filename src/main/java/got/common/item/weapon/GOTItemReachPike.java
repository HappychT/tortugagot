package got.common.item.weapon;

import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemReachPike extends GOTItemPolearmLong implements GOTFactionWeaponChecker{
    public GOTItemReachPike(Item.ToolMaterial material) {
        super(material);
    }
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocalFormatted("got.faction.REACH.name"));
    }
    @Override
    public GOTFaction getFaction() {
        return GOTFaction.REACH;
    }
}
