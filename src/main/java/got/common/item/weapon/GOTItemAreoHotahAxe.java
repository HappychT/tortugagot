package got.common.item.weapon;

import got.common.database.GOTMaterial;

/** Алебарда Арео Хотаха — урон 8.5, блок 20/20, скорость 52%, дальность 125%. */
public class GOTItemAreoHotahAxe extends GOTItemLegendaryBattleaxe {
	public GOTItemAreoHotahAxe() {
		super(GOTMaterial.IRON);
		setWeaponDamage(8.5f);
	}
}
