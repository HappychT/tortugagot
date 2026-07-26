package got.common.item.weapon.cswords;

import got.common.database.GOTMaterial;
import got.common.item.weapon.GOTItemLegendaryGreatsword;

/** Меч Григора Клигана — урон 12, блок как у Льда, скорость 25%, дальность 130%. */
public class ItemGregorCleganeSword extends GOTItemLegendaryGreatsword {
	public ItemGregorCleganeSword() {
		super(GOTMaterial.IRON);
		setWeaponDamage(12.0f);
		setMaxDamage(10000);
	}
}
