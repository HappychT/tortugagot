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
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            stack.stackSize--;
        }

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new GOTEntityToxinBomb(world, player, stack));
        }

        return stack;
    }
}
