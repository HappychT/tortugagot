package got.common.item.weapon;

import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemShieldRiverlandsTrident extends GOTItemShieldPike implements GOTFactionWeaponChecker{

    public GOTItemShieldRiverlandsTrident(Item.ToolMaterial material) {
        super(material);
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocalFormatted("got.faction.RIVERLANDS.name"));
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.RIVERLANDS;
    }
}
