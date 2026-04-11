package got.common.entity.animal;

import net.minecraft.world.World;

public class GOTEntityBridlePlotva extends GOTEntityBridleHorse {
    public GOTEntityBridlePlotva(World world) {
        super(world);
    }

    @Override
    public void applyBridleAppearance() {
        setHorseVariant(3);
    }
}
