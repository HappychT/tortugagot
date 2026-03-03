package got.common.item.weapon;

import got.common.database.GOTMaterial;

/** Топор Виктариона Грейджоя — урон 8.5, блок 28/28, скорость 60%, дальность 96%. */
public class GOTItemVictarionAxe extends GOTItemLegendaryBattleaxe {
	public GOTItemVictarionAxe() {
		super(GOTMaterial.IRON);
		setWeaponDamage(8.5f);
	}
}
