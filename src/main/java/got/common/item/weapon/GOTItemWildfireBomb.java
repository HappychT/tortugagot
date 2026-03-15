package got.common.item.weapon;

import got.common.database.GOTCreativeTabs;
import got.common.entity.other.GOTEntityWildfireBomb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GOTItemWildfireBomb extends Item {

    public GOTItemWildfireBomb() {
        this.setMaxStackSize(16);
        this.setCreativeTab(GOTCreativeTabs.tabCombat);
        this.setUnlocalizedName("gotWildfireBomb");
        this.setTextureName("got:wildfire_bomb");
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        player.setItemInUse(stack, getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeLeft) {
        int j = this.getMaxItemUseDuration(stack) - timeLeft;

        if (j < 30) {
            return;
        }

        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            GOTEntityWildfireBomb bomb = new GOTEntityWildfireBomb(world, player);

            bomb.setThrowableHeading(bomb.motionX, bomb.motionY, bomb.motionZ, 1.5F, 1.0F);

            world.spawnEntityInWorld(bomb);
        }
    }
}