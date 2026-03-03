package got.common.item.weapon.cswords;

import got.common.database.GOTMaterial;
import got.common.item.weapon.GOTItemLegendarySword;

/** Губитель Сердец — урон 8.5, блок 35/35, скорость 55%, дальность 110%. */
public class ItemHeartsbaneSword extends GOTItemLegendarySword {
	public ItemHeartsbaneSword() {
		super(GOTMaterial.VALYRIAN_TOOL);
		setWeaponDamage(8.5f);
	}
}
