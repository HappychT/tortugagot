package got.common.entity.westeros.riverlands;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityARMRIVERLANDSBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityARMRIVERLANDSBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.RIVERLANDS;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.ARM;
    }
}
