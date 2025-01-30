package got.common.item.weapon;

import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.faction.GOTFaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GOTItemShieldReachPike extends GOTItemShieldPike implements GOTFactionWeaponChecker {

    public GOTItemShieldReachPike(Item.ToolMaterial material) {
        super(material);
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean show) {
        list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocalFormatted("got.faction.REACH.name"));
    }

//    @Override
//    public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer entityplayer, Entity entity) {
//        GOTLevelData.getData(entityplayer);
//        if (!GOTEnchantmentHelper.hasEnchant(itemstack, GOTEnchantment.getEnchantmentByName("multifracConverter"))) {
//            return GOTLevelData.getData(entityplayer).getPledgeFaction() != GOTFaction.REACH;
//        }
//        return false;
//    }
}
