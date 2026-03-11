package got.common.enchant;

import got.common.item.GOTWeaponStats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GOTEnchantmentValyrianSeal extends GOTEnchantment {

    public GOTEnchantmentValyrianSeal(String s) {
        super(s, GOTEnchantmentType.values());
        setValueModifier(3.0F);
        setBypassAnvilLimit();
    }

    @Override
    public boolean canApply(ItemStack itemstack, boolean considering) {
        if (super.canApply(itemstack, considering))
            return true;
        return false;
    }

    @Override
    public String getDescription(ItemStack itemstack) {
        if (GOTWeaponStats.isMeleeWeapon(itemstack))
            return StatCollector.translateToLocalFormatted("got.enchant." + this.enchantName + ".desc.melee");

        return StatCollector.translateToLocalFormatted("got.enchant." + this.enchantName + ".desc.ranged");
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

    @Override
    public boolean isCompatibleWith(GOTEnchantment other) {
        return true;
    }
}