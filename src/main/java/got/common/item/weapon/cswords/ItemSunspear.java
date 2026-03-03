package got.common.item.weapon.cswords;

import got.common.database.GOTMaterial;
import got.common.item.weapon.GOTItemLegendarySword;
import got.common.item.weapon.GOTItemSword;

/** Копьё Оберина Мартелла — блок 15/15, урон 7.5, скорость 65%, дальность 130%, умеет блочить, нельзя кинуть. */
public class ItemSunspear extends GOTItemLegendarySword {
	public ItemSunspear() {
		super(GOTMaterial.IRON, GOTItemSword.HitEffect.POISON);
		setWeaponDamage(7.5f);
	}
}
