package got.common.item.weapon.cswords;

import got.common.database.GOTMaterial;
import got.common.item.weapon.GOTItemLegendaryGreatsword;

/**
 * Gregor Clegane's greatsword. Stats match Ice (Лёд) 1:1: damage 15, block 30/30, speed 67%, reach 150%.
 */
public class ItemGregorCleganeSword extends GOTItemLegendaryGreatsword {
	public ItemGregorCleganeSword() {
		super(GOTMaterial.IRON);
		setWeaponDamage(15.0f);
		setMaxDamage(10000);
	}
}
