package got.common.entity.westeros.dorne;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityMWDORNEBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityMWDORNEBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.DORNE;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.MW;
    }
}
