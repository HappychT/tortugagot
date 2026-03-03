package got.common.item.weapon.cswords;

import got.common.database.GOTMaterial;
import got.common.item.weapon.GOTItemLegendarySword;

/** Чёрное Пламя — урон 8, блок 32/32, скорость 64%, дальность 104%. */
public class ItemBlackfyreSword extends GOTItemLegendarySword {
	public ItemBlackfyreSword() {
		super(GOTMaterial.VALYRIAN_TOOL);
		setWeaponDamage(7.5f);
	}
}
