package got.common.entity.animal;

import net.minecraft.world.World;

public abstract class GOTEntityBridleHorse extends GOTEntityHorse {
    protected GOTEntityBridleHorse(World world) {
        super(world);
    }

    public abstract void applyBridleAppearance();
}
