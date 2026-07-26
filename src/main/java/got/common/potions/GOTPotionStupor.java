package got.common.potions;

import net.minecraft.entity.SharedMonsterAttributes;

public class GOTPotionStupor extends GOTCustomPotion {

    public GOTPotionStupor(int id) {
        super(id, true, 0x5C4033, "stupor");
        func_111184_a(SharedMonsterAttributes.movementSpeed,
            "B5D63560-3E97-4E5B-9B0A-1F7C2E5A8B3D", -1.0D, 2);
    }
}
