package brain.factions.servers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportHandler {

    private static final int TELEPORT_DELAY_SECONDS = 30;
    private static final double MOVEMENT_THRESHOLD_SQ = 0.25;

    private static final Map<UUID, TeleportRequest> pendingTeleports = new ConcurrentHashMap<>();

    private static class TeleportRequest {
        final EntityPlayerMP player;
        final Location target;
        final double startX, startY, startZ;
        final long teleportTime;

        TeleportRequest(EntityPlayerMP player, Location target) {
            this.player = player;
            this.target = target;
            this.startX = player.posX;
            this.startY = player.posY;
            this.startZ = player.posZ;
            this.teleportTime = System.currentTimeMillis() + (TELEPORT_DELAY_SECONDS * 1000L);
        }
    }


    public static void startTeleport(EntityPlayerMP player, Location target) {
        if (pendingTeleports.containsKey(player.getUniqueID())) {
            player.addChatMessage(new ChatComponentText("§cВы уже телепортируетесь!"));
            return;
        }

        TeleportRequest request = new TeleportRequest(player, target);
        pendingTeleports.put(player.getUniqueID(), request);
        player.addChatMessage(new ChatComponentText("§aНе двигайтесь " + TELEPORT_DELAY_SECONDS + " секунд, дабы телепортироваться!"));
    }


    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        pendingTeleports.entrySet().removeIf(entry -> {
            TeleportRequest request = entry.getValue();
            EntityPlayerMP player = request.player;

            if (player.getServerForPlayer() == null) {
                return true;
            }

            if (player.getDistanceSq(request.startX, request.startY, request.startZ) > MOVEMENT_THRESHOLD_SQ) {
                player.addChatMessage(new ChatComponentText("§cТелепортация отменена, так как вы двигались."));
                return true;
            }

            if (System.currentTimeMillis() >= request.teleportTime) {
                FreeTeleporter.sendToDimensionWithoutPortal(player, request.target.getWorldID(), request.target.getX(), request.target.getY(), request.target.getZ());
                return true;
            }

            return false;
        });
    }
}