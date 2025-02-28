package got.common.item.weapon;

import got.common.database.GOTMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class GOTItemDamnedCaptainSaber extends GOTItemSword {

    public GOTItemDamnedCaptainSaber() {
        super(GOTMaterial.CORSAIR);
        this.gotWeaponDamage = 8.0f;
        setMaxDamage(7000);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if(entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.confusion.id, 2 * 20));
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
