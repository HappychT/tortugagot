package got.common.item.potions;

import got.common.entity.potion.GOTEntityToxinBomb;
import got.common.item.weapon.GOTItemThrowingBomb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GOTItemToxinBomb extends GOTItemThrowingBomb {

    public GOTItemToxinBomb(PotionEffect... list) {
        super(new double[] {5, 5, 5}, 2.0f, list);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int timeLeft) {
        int j = this.getMaxItemUseDuration(itemstack) - timeLeft;

        if (j < 30) {
            return;
        }

        if (!entityplayer.capabilities.isCreativeMode) {
            --itemstack.stackSize;
        }

        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            GOTEntityToxinBomb bomb = new GOTEntityToxinBomb(world, entityplayer, itemstack);

            bomb.setThrowableHeading(bomb.motionX, bomb.motionY, bomb.motionZ, 0.7F, 1.0F);

            world.spawnEntityInWorld(bomb);
        }
    }
}