package got.common.item.weapon.cswords;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.item.weapon.GOTItemHammer;
import got.common.network.GOTPacketHandler;
import got.common.network.PacketSyncWeaponHitCount;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemStunningHammer extends GOTItemHammer {

    private static final String NBT_KEY = "StunningHammerHits";

    public ItemStunningHammer(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabStory);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("");
        list.add(EnumChatFormatting.DARK_GRAY + "Утомление" + EnumChatFormatting.WHITE + " – каждая 3-я атака снижает скорость врага.");
        list.add(EnumChatFormatting.RED + "Доп. урон" + EnumChatFormatting.WHITE + " – +2 HP при атаке утомленного врага.");
        list.add("");
        list.add(EnumChatFormatting.GRAY + "Молот, ломающий сопротивление противника.");
    }
}