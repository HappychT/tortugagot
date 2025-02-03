package got.common.potions;

import net.minecraft.entity.EntityLivingBase;

public class GOTPotionNauseaResistance extends GOTCustomPotion {

    public GOTPotionNauseaResistance(int id) {
        super(id, false, 8999288, "nauseaResistance");
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {}

}