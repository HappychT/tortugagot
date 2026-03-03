package got.common.item.weapon;

import got.common.database.GOTMaterial;

/** Топор Тириона Ланнистера — урон 6, блок 20/20, скорость 77%, дальность 77%. */
public class GOTItemTyrionAxe extends GOTItemLegendaryBattleaxe {
	public GOTItemTyrionAxe() {
		super(GOTMaterial.IRON);
		setWeaponDamage(6.0f);
	}
}
