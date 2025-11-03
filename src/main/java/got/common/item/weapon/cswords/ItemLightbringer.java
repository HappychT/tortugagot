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
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("");
        list.add(EnumChatFormatting.RED + "Драконье Пламя" + EnumChatFormatting.WHITE + " – каждая третья атака поджигает цель.");
        list.add("");
        list.add(EnumChatFormatting.GRAY + "Легендарный меч, выкованный в крови любимой,");
        list.add(EnumChatFormatting.GRAY + "предвещающий пришествие обещанного Принца.");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemstack, int pass) {
        return true;
    }
}