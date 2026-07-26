package got.common.entity.westeros.reach;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityMWREACHBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityMWREACHBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.REACH;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.MW;
    }
}
