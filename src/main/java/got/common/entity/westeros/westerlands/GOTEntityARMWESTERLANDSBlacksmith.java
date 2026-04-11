package got.common.entity.westeros.westerlands;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityARMWESTERLANDSBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityARMWESTERLANDSBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.WESTERLANDS;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.ARM;
    }
}
