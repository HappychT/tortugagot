package got.common.item.weapon;

import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemNorthGreatSword extends GOTItemGreatsword implements GOTFactionWeaponChecker{
    public GOTItemNorthGreatSword(Item.ToolMaterial material) {
        super(material);
    }
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("got.faction.NORTH.name"));
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.NORTH;
    }
}
