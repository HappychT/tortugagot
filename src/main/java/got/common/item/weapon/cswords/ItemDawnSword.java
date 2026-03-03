package got.common.item.weapon.cswords;

import got.common.database.GOTMaterial;
import got.common.item.weapon.GOTItemLegendarySword;

/** Рассвет — урон 8.5, блок 35/35, скорость 55%, дальность 110%. */
public class ItemDawnSword extends GOTItemLegendarySword {
	public ItemDawnSword() {
		super(GOTMaterial.IRON);
		setIsGlowing();
		setWeaponDamage(8.5f);
	}
}
