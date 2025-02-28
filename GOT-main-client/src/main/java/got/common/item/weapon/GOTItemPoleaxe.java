package got.common.item.weapon;

import net.minecraft.item.Item;

public class GOTItemPoleaxe extends GOTItemBattleaxe {
    public GOTItemPoleaxe(Item.ToolMaterial material) {
        super(material);
        gotWeaponDamage -= 0.5f;
    }
}
