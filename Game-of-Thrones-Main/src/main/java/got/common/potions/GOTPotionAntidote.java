package got.common.potions;

import got.common.database.GOTEffects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class GOTPotionAntidote extends GOTCustomPotion {

    public GOTPotionAntidote(int id) {
        super(id, false, 4960121, "antidote");
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        entity.removePotionEffect(Potion.poison.id);
        entity.removePotionEffect(GOTEffects.killingPoison.id);
    }

    @Override
    public boolean isReady(int tick, int level) {
        return true;
    }
}
