package got.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GOTEnchantmentRestBuff extends GOTEnchantment {

    public GOTEnchantmentRestBuff(String s) {
        super(s, GOTEnchantmentType.ARMOR_FEET);
    }

    @Override
    public String getDescription(ItemStack paramItemStack) {
        return StatCollector.translateToLocalFormatted("got.enchant.restBuff.desc");
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
