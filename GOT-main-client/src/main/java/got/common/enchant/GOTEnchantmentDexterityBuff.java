package got.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GOTEnchantmentDexterityBuff extends GOTEnchantment {

    public GOTEnchantmentDexterityBuff(String s) {
        super(s, GOTEnchantmentType.ARMOR_BODY);
    }

    @Override
    public String getDescription(ItemStack paramItemStack) {
        return StatCollector.translateToLocalFormatted("got.enchant.dexterityBuff.desc");
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
