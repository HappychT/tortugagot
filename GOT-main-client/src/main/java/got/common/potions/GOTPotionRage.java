package got.common.potions;

import net.minecraft.entity.SharedMonsterAttributes;

public class GOTPotionRage extends GOTCustomPotion {

    public GOTPotionRage(int id) {
        super(id, false, 14957584, "rage");
        func_111184_a(SharedMonsterAttributes.attackDamage, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.2D, 2);
    }

}
