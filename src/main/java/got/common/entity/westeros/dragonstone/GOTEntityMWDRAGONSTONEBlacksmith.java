package got.common.entity.westeros.dragonstone;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.world.World;

public class GOTEntityMWDRAGONSTONEBlacksmith extends GOTEnchaldBlacksmith {

    public GOTEntityMWDRAGONSTONEBlacksmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.DRAGONSTONE;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.MW;
    }
}
