package got.common.item.weapon;

import got.common.database.GOTMaterial;

/** Секира Селтигаров — урон 9.5, блок 25/25, скорость 45%, дальность 120%. */
public class GOTItemCeltigarAxe extends GOTItemLegendaryBattleaxe {
	public GOTItemCeltigarAxe() {
		super(GOTMaterial.VALYRIAN_TOOL);
		setWeaponDamage(9.5f);
	}
}
