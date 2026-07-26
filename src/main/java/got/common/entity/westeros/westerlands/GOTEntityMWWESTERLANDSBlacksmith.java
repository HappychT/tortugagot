package got.common.entity.westeros.westerlands;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityMWWESTERLANDSBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityMWWESTERLANDSBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.WESTERLANDS;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.MW;
    }
}
