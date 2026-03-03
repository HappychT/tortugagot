package got.common.enchant;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/** Привязка к душе — полное сохранение вещи при смерти и при ливе в бою. */
public class GOTEnchantmentSoulbound extends GOTEnchantment {

	public GOTEnchantmentSoulbound(String s) {
		super(s, GOTEnchantmentType.ANY);
		setValueModifier(2.0F);
		setBypassAnvilLimit();
	}

	@Override
	public String getDescription(ItemStack itemstack) {
		return StatCollector.translateToLocal("got.enchant." + enchantName + ".desc");
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
