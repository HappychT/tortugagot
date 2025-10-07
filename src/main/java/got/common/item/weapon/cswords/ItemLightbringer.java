package got.common.item.weapon.cswords;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.item.weapon.GOTItemGreatsword;
import got.common.network.GOTPacketHandler;
import got.common.network.PacketSyncWeaponHitCount;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemLightbringer extends GOTItemGreatsword {

    private static final String NBT_HIT_COUNT = "LightbringerHits";

    public ItemLightbringer(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabCombat);
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitEntity, EntityLivingBase user) {
        itemstack.damageItem(1, user);

        if (!user.worldObj.isRemote && user instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) user;

            NBTTagCompound nbt = itemstack.getTagCompound();
            if (nbt == null) {
                nbt = new NBTTagCompound();
                itemstack.setTagCompound(nbt);
            }

            int hitCount = nbt.getInteger(NBT_HIT_COUNT);
            hitCount++;

            if (hitCount >= 3) {
                hitEntity.setFire(5);
                spawnFlameParticles(hitEntity.worldObj, hitEntity);
                hitCount = 0;
            }

            nbt.setInteger(NBT_HIT_COUNT, hitCount);

            GOTPacketHandler.networkWrapper.sendTo(new PacketSyncWeaponHitCount(this, hitCount), player);
        }

        return true;
    }


    private void spawnFlameParticles(World world, Entity entity) {
        if (world instanceof net.minecraft.world.WorldServer) {
            net.minecraft.world.WorldServer worldServer = (net.minecraft.world.WorldServer) world;

            worldServer.func_147487_a("flame",
                    entity.posX, entity.posY + entity.height / 2.0D, entity.posZ,
                    30,
                    entity.width / 2.0D, entity.height / 2.0D, entity.width / 2.0D,
                    0.0D);

            worldServer.func_147487_a("largesmoke",
                    entity.posX, entity.posY + entity.height / 2.0D, entity.posZ,
                    15,
                    entity.width / 2.0D, entity.height / 2.0D, entity.width / 2.0D,
                    0.0D);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        super.addInformation(itemstack, entityplayer, list, flag);
        list.add(EnumChatFormatting.GRAY + "");
        list.add(EnumChatFormatting.GRAY + "Легендарный меч, выкованный в крови любимой,");
        list.add(EnumChatFormatting.GRAY + "предвещающий пришествие обещанного Принца.");
        list.add(EnumChatFormatting.GRAY + "");
        list.add(EnumChatFormatting.RED + "Драконье Пламя" + EnumChatFormatting.WHITE + " – каждая третья атака поджигает цель.");

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemstack, int pass) {
        return true;
    }
}