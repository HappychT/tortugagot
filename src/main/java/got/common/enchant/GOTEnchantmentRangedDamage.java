package got.common.enchant;

import got.common.item.weapon.GOTItemCrossbow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GOTEnchantmentRangedDamage extends GOTEnchantment {
	public float damageFactor;

	public GOTEnchantmentRangedDamage(String s, float damage) {
		super(s, GOTEnchantmentType.RANGED_LAUNCHER);
		damageFactor = damage;
		if (damageFactor > 1.0F) {
			setValueModifier(damageFactor * 2.0F);
		} else {
			setValueModifier(damageFactor);
		}
	}

	@Override
	public boolean canApply(ItemStack itemstack, boolean considering) {
		if (this == GOTEnchantment.rangedStrong2) {
			Item item = itemstack != null ? itemstack.getItem() : null;
			return item instanceof ItemBow || item instanceof GOTItemCrossbow;
		}
		return super.canApply(itemstack, considering);
	}

	@Override
	public String getDescription(ItemStack itemstack) {
		return StatCollector.translateToLocalFormatted("got.enchant.rangedDamage.desc", formatMultiplicative(damageFactor));
	}

	@Override
	public boolean isBeneficial() {
		return damageFactor >= 1.0F;
	}
}
