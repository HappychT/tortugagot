package got.common.entity.animal;

import net.minecraft.world.World;

public class GOTEntityBridleSerogriv extends GOTEntityBridleHorse {
    public GOTEntityBridleSerogriv(World world) {
        super(world);
    }

    @Override
    public void applyBridleAppearance() {
        setHorseVariant(0);
    }
}