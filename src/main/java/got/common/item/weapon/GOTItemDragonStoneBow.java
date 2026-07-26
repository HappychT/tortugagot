package got.common.item.weapon;

import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemDragonStoneBow extends GOTItemBow implements GOTFactionWeaponChecker{
    public GOTItemDragonStoneBow(Item.ToolMaterial material, double d) {
        super(material, d);
    }
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.DARK_RED + StatCollector.translateToLocalFormatted("got.faction.DRAGONSTONE.name"));
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.DRAGONSTONE;
    }
}
