package got.common.util;

import net.minecraft.entity.player.EntityPlayer;

public class ZoneLib {

    public static boolean isPlayerInZone(EntityPlayer player, Zone zone) {
        int x = (int) Math.floor(player.posX);
        int y = (int) Math.floor(player.posY);
        int z = (int) Math.floor(player.posZ);

        return zone.isWithin(x, y, z);
    }


}
