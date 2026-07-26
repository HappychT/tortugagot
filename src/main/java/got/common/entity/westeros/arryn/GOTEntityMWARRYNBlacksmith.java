package got.common.entity.westeros.arryn;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityMWARRYNBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityMWARRYNBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.ARRYN;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.MW;
    }
}
