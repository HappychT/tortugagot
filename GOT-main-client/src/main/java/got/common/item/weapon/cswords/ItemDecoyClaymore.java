package got.common.item.weapon.cswords;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import got.common.item.weapon.GOTItemArrynClaymore;
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

public class ItemDecoyClaymore extends GOTItemArrynClaymore {

    private static final String NBT_KEY = "DecoyClaymoreHits";

    public ItemDecoyClaymore(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabCombat);
        setWeaponDamage(9.0F);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);

        if (!attacker.worldObj.isRemote && attacker instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) attacker;

            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt == null) {
                nbt = new NBTTagCompound();
                stack.setTagCompound(nbt);
            }

            int count = nbt.getInteger(NBT_KEY);
            count++;

            if (count >= 3) {
                target.addPotionEffect(new PotionEffect(GOTEffects.staminaLock.id, 80, 0));
                count = 0;
            }

            nbt.setInteger(NBT_KEY, count);
            GOTPacketHandler.networkWrapper.sendTo(new PacketSyncWeaponHitCount(this, count), player);
        }

        return true;
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