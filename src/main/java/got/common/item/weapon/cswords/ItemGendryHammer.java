package got.common.item.weapon.cswords;

import got.common.database.GOTMaterial;
import got.common.item.weapon.GOTItemLegendaryHammer;

/** Молот Джендри Баратеона — урон 8, блок 20/20, скорость 45%, дальность 100%, откидывание +1. */
public class ItemGendryHammer extends GOTItemLegendaryHammer {
	public ItemGendryHammer() {
		super(GOTMaterial.IRON);
		setWeaponDamage(8.0f);
	}
}
