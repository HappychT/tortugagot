package got.common.item.weapon;

import net.minecraft.item.*;

public class GOTItemPolearm extends GOTItemSword {
	public GOTItemPolearm(Item.ToolMaterial material) {
		super(material);
		gotWeaponDamage += 0.5f;
	}

}
