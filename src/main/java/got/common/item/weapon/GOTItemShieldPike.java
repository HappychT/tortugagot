package got.common.item.weapon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class GOTItemShieldPike extends GOTItemPolearm{

    public GOTItemShieldPike(Item.ToolMaterial material) {
        super(material);
        this.bFull3D = true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        entityplayer.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.2D);
        return itemstack;
    }
    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int useTick) {
        entityplayer.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.0D);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("got.enchant.shield"));
    }
}

