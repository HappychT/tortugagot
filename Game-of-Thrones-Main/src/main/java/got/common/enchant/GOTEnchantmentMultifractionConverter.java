package got.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GOTEnchantmentMultifractionConverter extends GOTEnchantment{

    public GOTEnchantmentMultifractionConverter(String s) {
        super(s, new GOTEnchantmentType[] {GOTEnchantmentType.FACTION_ARMOR, GOTEnchantmentType.FACTION_WEAPON});
    }

    @Override
    public String getDescription(ItemStack itemstack) {
        return StatCollector.translateToLocalFormatted("got.enchant.multifracConverter.desc");
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
