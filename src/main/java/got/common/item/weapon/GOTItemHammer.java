package got.common.item.weapon;

import net.minecraft.item.*;

public class GOTItemHammer extends GOTItemSword {
	public GOTItemHammer(Item.ToolMaterial material) {
		super(material);
		gotWeaponDamage += 1.5f;
	}

}
