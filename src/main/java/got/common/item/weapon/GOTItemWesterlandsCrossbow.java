package got.common.item.weapon;

import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemWesterlandsCrossbow extends GOTItemCrossbow implements GOTFactionWeaponChecker{
    public GOTItemWesterlandsCrossbow(Item.ToolMaterial material) {
        super(material);
        boltDamageFactor = 1.05d;
    }
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("got.faction.WESTERLANDS.name"));
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.WESTERLANDS;
    }
}
