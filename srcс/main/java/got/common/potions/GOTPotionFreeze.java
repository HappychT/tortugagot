package got.common.potions;

import got.common.GOTDamage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;

public class GOTPotionFreeze extends GOTCustomPotion {

    public GOTPotionFreeze(int id) {
        super(id, false, 3565458, "freeze");
        func_111184_a(SharedMonsterAttributes.movementSpeed, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448D, 2);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if(entity instanceof EntityPlayerMP) {
            GOTDamage.doFrostDamage((EntityPlayerMP) entity);
        }
    };

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
