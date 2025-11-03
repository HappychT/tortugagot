package got.common.handlers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BandageCooldownHandler {

    private static final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    private static final long TICKS_PER_SECOND = 20;

    public static void setCooldown(EntityPlayer player, int seconds) {
        long endTime = player.worldObj.getTotalWorldTime() + (long) seconds * TICKS_PER_SECOND;
        cooldowns.put(player.getUniqueID(), endTime);
    }

    public static long getCooldown(EntityPlayer player) {
        if (!cooldowns.containsKey(player.getUniqueID())) {
            return 0;
        }
        long endTime = cooldowns.get(player.getUniqueID());
        long currentTime = player.worldObj.getTotalWorldTime();

        if (currentTime >= endTime) {
            cooldowns.remove(player.getUniqueID());
            return 0;
        }

        return (endTime - currentTime) / TICKS_PER_SECOND;
    }

    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {

        }
    }


}