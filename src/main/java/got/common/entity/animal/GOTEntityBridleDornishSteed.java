package got.common.entity.animal;

import net.minecraft.world.World;

public class GOTEntityBridleDornishSteed extends GOTEntityBridleHorse {
    public GOTEntityBridleDornishSteed(World world) {
        super(world);
    }

    @Override
    public void applyBridleAppearance() {
        setHorseVariant(4 | 2 << 8);
    }
}