package got.common.item.other;

import got.common.database.GOTEffects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GOTItemMugNightWatch extends GOTItemMug {
    public GOTItemMugNightWatch() {
        super(0.0f);
    }

    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        ItemStack result = super.onEaten(itemstack, world, entityplayer);
        if (!world.isRemote) {
            entityplayer.addPotionEffect(new PotionEffect(GOTEffects.nauseaResistance.id, 2400));
        }
        return result;
    }
}
