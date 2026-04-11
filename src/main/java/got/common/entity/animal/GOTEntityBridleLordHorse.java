package got.common.entity.animal;

import net.minecraft.world.World;

public class GOTEntityBridleLordHorse extends GOTEntityBridleHorse {
    public GOTEntityBridleLordHorse(World world) {
        super(world);
    }

    @Override
    public void applyBridleAppearance() {
    }

    @Override
    public int getTotalArmorValue() {
        return 5;
    }
}
