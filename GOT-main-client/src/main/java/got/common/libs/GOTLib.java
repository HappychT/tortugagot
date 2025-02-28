package got.common.libs;

import got.common.item.GOTWeaponStats;
import net.minecraft.entity.player.EntityPlayer;

public class GOTLib {

    public static int getAttackCooldown(EntityPlayer ply) {
        return GOTWeaponStats.getAttackTimePlayer(ply.getHeldItem()) - 4;
    }

}
