package got.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GOTEnchantmentSecondBreathBuff extends GOTEnchantment {

    public GOTEnchantmentSecondBreathBuff(String s) {
        super(s, GOTEnchantmentType.ARMOR_HEAD);
    }

    @Override
    public String getDescription(ItemStack paramItemStack) {
        return StatCollector.translateToLocalFormatted("got.enchant.secondBreathBuff.desc");
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
