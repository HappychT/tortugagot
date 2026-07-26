package got.common.entity.westeros.reach;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityRWREACHBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityRWREACHBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.REACH;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.RW;
    }
}
