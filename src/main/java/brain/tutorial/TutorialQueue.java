package brain.tutorial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import java.util.LinkedList;
import java.util.UUID;
import got.common.network.GOTPacketHandler;
import brain.tutorial.network.GOTPacketTutorialQueue;

public class TutorialQueue {

    private static final TutorialQueue INSTANCE = new TutorialQueue();

    public static TutorialQueue getInstance() {
        return INSTANCE;
    }

    private final LinkedList<UUID> queue = new LinkedList<>();
    private long lastAdmitTime = 0;

    private TutorialQueue() {}

    public void addToQueue(EntityPlayer player) {
        UUID uuid = player.getUniqueID();
        if (!queue.contains(uuid)) {
            queue.add(uuid);
        }
        
        // TP to limbo
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP mp = (EntityPlayerMP) player;
            if (mp.dimension != TutorialConfig.limbo_dimension) {
                WorldServer targetWorld = mp.mcServer.worldServerForDimension(TutorialConfig.limbo_dimension);
                if (targetWorld != null) {
                    mp.mcServer.getConfigurationManager().transferPlayerToDimension(mp, TutorialConfig.limbo_dimension, new brain.tutorial.world.TutorialTeleporter(targetWorld));
                }
            }
            mp.playerNetServerHandler.setPlayerLocation(TutorialConfig.limbo_x + 0.5, TutorialConfig.limbo_y, TutorialConfig.limbo_z + 0.5, mp.rotationYaw, mp.rotationPitch);
        }
        
        broadcastQueueState();
    }

    public void removeFromQueue(UUID uuid) {
        if (queue.remove(uuid)) {
            broadcastQueueState();
        }
    }

    public void tick() {
        if (queue.isEmpty()) return;

        long currentTime = System.currentTimeMillis();
        // interval is in seconds, so multiply by 1000
        if (currentTime - lastAdmitTime >= TutorialConfig.queue_interval * 1000L) {
            UUID nextPlayerId = queue.poll();
            EntityPlayer player = getPlayerByUUID(nextPlayerId);
            if (player != null) {
                lastAdmitTime = currentTime;
                // Notify the player they are leaving the queue
                if (player instanceof EntityPlayerMP) {
                    GOTPacketHandler.networkWrapper.sendTo(new GOTPacketTutorialQueue(false, 0, queue.size()), (EntityPlayerMP) player);
                }
                
                // Start the tutorial for them
                TutorialManager.getInstance().startCombat(player);
            }
            broadcastQueueState();
        }
    }

    private EntityPlayer getPlayerByUUID(UUID uuid) {
        for (Object obj : net.minecraft.server.MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer p = (EntityPlayer) obj;
                if (p.getUniqueID().equals(uuid)) {
                    return p;
                }
            }
        }
        return null;
    }

    private void broadcastQueueState() {
        int total = queue.size();
        int pos = 1;
        for (UUID uuid : queue) {
            EntityPlayer player = getPlayerByUUID(uuid);
            if (player instanceof EntityPlayerMP) {
                GOTPacketHandler.networkWrapper.sendTo(new GOTPacketTutorialQueue(true, pos, total), (EntityPlayerMP) player);
            }
            pos++;
        }
    }
}
