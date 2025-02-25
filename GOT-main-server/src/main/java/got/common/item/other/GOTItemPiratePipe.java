package got.common.item.other;

import got.common.database.GOTCreativeTabs;
import got.common.entity.other.GOTEntitySmokeRing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GOTItemPiratePipe extends Item {

    public GOTItemPiratePipe() {
        setMaxStackSize(1);
        setCreativeTab(GOTCreativeTabs.tabMisc);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 40;
    }

    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        itemstack.damageItem(1, entityplayer);
        if (entityplayer.canEat(false)) {
            entityplayer.getFoodStats().addStats(2, 0.3f);
        }
        if (!world.isRemote) {
            GOTEntitySmokeRing smoke = new GOTEntitySmokeRing(world, entityplayer);
            smoke.setSmokeColour(16);
            world.spawnEntityInWorld(smoke);
            entityplayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 2400));
        }
        world.playSoundAtEntity(entityplayer, "got:item.puff", 1.0f, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2f + 1.0f);
        return itemstack;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    public static int getSmokeColor(ItemStack itemstack) {
        if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("SmokeColour"))
            return itemstack.getTagCompound().getInteger("SmokeColour");
        return 0;
    }

    public static void setSmokeColor(ItemStack itemstack, int i) {
        if (itemstack.getTagCompound() == null) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        itemstack.getTagCompound().setInteger("SmokeColour", i);
    }
}