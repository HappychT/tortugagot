package got.common.entity.animal;

import net.minecraft.world.World;

public class GOTEntityBridleDothrakiStallion extends GOTEntityBridleHorse {
    private static final int[] BROWN_VARIANTS = new int[] {1, 2, 3, 6};

    public GOTEntityBridleDothrakiStallion(World world) {
        super(world);
    }

    @Override
    public void applyBridleAppearance() {
        setHorseVariant(BROWN_VARIANTS[worldObj.rand.nextInt(BROWN_VARIANTS.length)]);
    }
}