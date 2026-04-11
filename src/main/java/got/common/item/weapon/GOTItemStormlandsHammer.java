package got.common.item.weapon;

import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemStormlandsHammer extends GOTItemHammer implements GOTFactionWeaponChecker{
    public GOTItemStormlandsHammer(Item.ToolMaterial material) {
        super(material);
    }
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocalFormatted("got.faction.STORMLANDS.name"));
    }
    @Override
    public GOTFaction getFaction() {
        return GOTFaction.STORMLANDS;
    }
}
