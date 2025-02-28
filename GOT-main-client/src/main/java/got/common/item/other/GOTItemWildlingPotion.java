package got.common.item.other;

import got.common.database.GOTCreativeTabs;
import got.common.database.GOTEffects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GOTItemWildlingPotion extends Item {

    public GOTItemWildlingPotion() {
        setMaxStackSize(1);
        setCreativeTab(GOTCreativeTabs.tabMisc);
        setContainerItem(Items.glass_bottle);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.drink;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 32;
    }

    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!world.isRemote) {
            entityplayer.addPotionEffect(new PotionEffect(GOTEffects.frostResistance.id, 5 * 60 * 20));
        }
        return !entityplayer.capabilities.isCreativeMode ? new ItemStack(Items.glass_bottle) : itemstack;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }
}
