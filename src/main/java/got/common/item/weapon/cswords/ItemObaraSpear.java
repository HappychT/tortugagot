package got.common.item.weapon.cswords;

import got.common.database.GOTMaterial;
import got.common.item.weapon.GOTItemLegendarySword;
import got.common.item.weapon.GOTItemSword;

/** Копьё Обары Сэнд — блок 15/15, урон 7.5, скорость 65%, дальность 130%, умеет блочить, нельзя кинуть. */
public class ItemObaraSpear extends GOTItemLegendarySword {
	public ItemObaraSpear() {
		super(GOTMaterial.IRON, GOTItemSword.HitEffect.POISON);
		setWeaponDamage(7.5f);
	}
}
