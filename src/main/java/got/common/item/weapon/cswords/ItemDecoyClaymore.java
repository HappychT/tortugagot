package got.common.item.weapon.cswords;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import got.common.item.weapon.GOTItemArrynClaymore;
import got.common.item.weapon.GOTItemGreatsword;
import got.common.network.GOTPacketHandler;
import got.common.network.PacketSyncWeaponHitCount;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemDecoyClaymore extends GOTItemGreatsword {

    private static final String NBT_KEY = "DecoyClaymoreHits";

    public ItemDecoyClaymore(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabStory);
        setWeaponDamage(7.5F);
    }



    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("");
        list.add(EnumChatFormatting.LIGHT_PURPLE + "Обманный Удар" + EnumChatFormatting.WHITE + " – крит без прыжка, но при прыжке слабость врагу.");
        list.add(EnumChatFormatting.DARK_PURPLE + "Заморозка стамины" + EnumChatFormatting.WHITE + " – каждый 3-й удар блокирует выносливость.");
        list.add("");
        list.add(EnumChatFormatting.GRAY + "Меч, манипулирующий ходом боя.");
    }
}