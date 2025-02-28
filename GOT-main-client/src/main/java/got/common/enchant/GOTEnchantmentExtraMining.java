package got.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GOTEnchantmentExtraMining extends GOTEnchantment{

    public GOTEnchantmentExtraMining(String s) {
        super(s, GOTEnchantmentType.PICKAXE);
    }

    @Override
    public String getDescription(ItemStack itemstack) {
        return StatCollector.translateToLocalFormatted("got.enchant.extraMining.desc");
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
