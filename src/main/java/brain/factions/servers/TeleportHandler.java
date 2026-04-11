package brain.factions.servers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.common.database.GOTEffects;
import got.common.potions.GOTCustomPotion;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.CommandEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportHandler {

    private static final int TELEPORT_DELAY_SECONDS = 30;
    private static final double MOVEMENT_THRESHOLD_SQ = 0.25;
    private static final int PVPPOT = GOTEffects.combatLog.id;
    private static final Map<UUID, TeleportRequest> pendingTeleports = new ConcurrentHashMap<>();
    private static final List<String> BLOCKED_COMMANDS = Arrays.asList(
            "home",
            "spawn",
            "tpa",
            "tpaccept",
            "warp",
            "f home"
    );
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
        if (player.isPotionActive(PVPPOT)) {
            player.addChatMessage(new ChatComponentText("§cВы в пвп режиме, нельзя телепортироваться!"));
            return;
        }
        if (pendingTeleports.containsKey(player.getUniqueID())) {
            player.addChatMessage(new ChatComponentText("§cВы уже телепортируетесь!"));
            return;
        }

        TeleportRequest request = new TeleportRequest(player, target);
        pendingTeleports.put(player.getUniqueID(), request);
        player.addChatMessage(new ChatComponentText("§aНе двигайтесь " + TELEPORT_DELAY_SECONDS + " секунд, дабы телепортироваться!"));
    }

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        if (!(event.sender instanceof EntityPlayerMP)) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.sender;

        if (player.isPotionActive(PVPPOT)) {

            String commandName = event.command.getCommandName();

            if (BLOCKED_COMMANDS.contains(commandName)) {
                event.setCanceled(true);
                player.addChatMessage(new ChatComponentText("§cВы не можете использовать эту команду в пвп!"));
                return;
            }

            if (commandName.equals("f") && event.parameters.length > 0) {
                if (event.parameters[0].equalsIgnoreCase("home")) {
                    event.setCanceled(true);
                    player.addChatMessage(new ChatComponentText("§cВы не можете телепортироваться в пвп!"));
                }
            }
        }
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
            if (player.isPotionActive(PVPPOT)) {
                player.addChatMessage(new ChatComponentText("§cТелепортация отменена: получен негативный эффект."));
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