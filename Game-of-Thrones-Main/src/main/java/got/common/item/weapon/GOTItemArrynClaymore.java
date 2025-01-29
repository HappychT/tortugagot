package got.common.item.weapon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemArrynClaymore extends GOTItemSword {
    public GOTItemArrynClaymore(Item.ToolMaterial material) {
        super(material);
        gotWeaponDamage += 3;
    }
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.DARK_BLUE + StatCollector.translateToLocalFormatted("got.faction.ARRYN.name"));
    }
}