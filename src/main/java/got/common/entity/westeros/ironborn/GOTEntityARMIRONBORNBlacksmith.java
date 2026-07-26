package got.common.entity.westeros.ironborn;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityARMIRONBORNBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityARMIRONBORNBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.IRONBORN;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.ARM;
    }
}
