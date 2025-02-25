package got.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GOTEnchantmentRangedSpeed extends GOTEnchantment {

    public float rangedSpeed;

    public GOTEnchantmentRangedSpeed(String s, float i) {
        super(s, GOTEnchantmentType.RANGED_LAUNCHER);
        rangedSpeed = i;
        setValueModifier(rangedSpeed * 2.0f);
    }

    @Override
    public String getDescription(ItemStack itemstack) {
        return StatCollector.translateToLocalFormatted("got.enchant.rangedSpeed.desc", formatMultiplicative(rangedSpeed));
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

}
