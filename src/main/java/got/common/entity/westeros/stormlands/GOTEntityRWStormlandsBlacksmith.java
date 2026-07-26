package got.common.entity.westeros.stormlands;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityRWStormlandsBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityRWStormlandsBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.STORMLANDS;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.RW;
    }
}
