package got.common.item.other;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.GOTLevelData;
import got.common.database.GOTCreativeTabs;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class GOTItemFactionArmor extends GOTItemArmor {
    public GOTFaction faction;

    public GOTItemFactionArmor(ArmorMaterial material, int slotType) {
        this(material, slotType, "");
    }

    public GOTItemFactionArmor(ArmorMaterial material, int slotType, String s) {
        super(material, slotType, s);
        setCreativeTab(GOTCreativeTabs.tabCombat);
        extraName = s;
        slot = slotType;
    }
    public GOTItemFactionArmor setFactionArmor(GOTFaction fac) {
        this.faction = fac;
        return this;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer entityplayer, ItemStack itemstack) {
        if (!GOTEnchantmentHelper.hasEnchant(itemstack, GOTEnchantment.multifracConverter)) {
            if (GOTLevelData.getData(entityplayer).getPledgeFaction() != faction && entityplayer.getActivePotionEffect(Potion.weakness) == null)
                entityplayer.addPotionEffect(new PotionEffect(Potion.weakness.id, 200));
        }
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean show) {
        switch (faction) {
            case ARRYN:
                list.add(EnumChatFormatting.DARK_BLUE + StatCollector.translateToLocalFormatted("got.faction.ARRYN.name"));
                break;
            case DORNE:
                list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocalFormatted("got.faction.DORNE.name"));
                break;
            case DRAGONSTONE:
                list.add(EnumChatFormatting.DARK_RED + StatCollector.translateToLocalFormatted("got.faction.DRAGONSTONE.name"));
                break;
            case IRONBORN:
                list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocalFormatted("got.faction.IRONBORN.name"));
                break;
            case NORTH:
                list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("got.faction.NORTH.name"));
                break;
            case REACH:
                list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocalFormatted("got.faction.REACH.name"));
                break;
            case RIVERLANDS:
                list.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocalFormatted("got.faction.RIVERLANDS.name"));
                break;
            case STORMLANDS:
                list.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocalFormatted("got.faction.STORMLANDS.name"));
                break;
            case WESTERLANDS:
                list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("got.faction.WESTERLANDS.name"));
                break;
            default:
                break;
        }
    }
}
