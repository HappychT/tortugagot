package noname.weapons.war;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import noname.weapons.RegItem;

import java.util.ArrayList;
import java.util.List;

public class WarTicketHandler {

    private int tickCounter = 0;
    private static final int CHECK_INTERVAL = 100;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!WarModeManager.getInstance().isWarModeActive()) {
            return;
        }

        tickCounter++;
        if (tickCounter < CHECK_INTERVAL) {
            return;
        }
        tickCounter = 0;

        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return;
        }

        List<EntityPlayerMP> playersToBan = new ArrayList<>();

        for (Object obj : server.getConfigurationManager().playerEntityList) {
            if (!(obj instanceof EntityPlayerMP)) {
                continue;
            }

            EntityPlayerMP player = (EntityPlayerMP) obj;

            if (isPlayerOP(player)) {
                continue;
            }

            if (!hasWarTicket(player)) {
                playersToBan.add(player);
            }
        }

        for (EntityPlayerMP player : playersToBan) {
            banPlayer(player);
        }
    }

    private boolean isPlayerOP(EntityPlayerMP player) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return false;
        }
        return server.getConfigurationManager().func_152596_g(player.getGameProfile());
    }

    private boolean hasWarTicket(EntityPlayerMP player) {
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (stack != null && stack.getItem() == RegItem.warTicket) {
                return true;
            }
        }

        for (int i = 0; i < player.inventory.armorInventory.length; i++) {
            ItemStack stack = player.inventory.armorInventory[i];
            if (stack != null && stack.getItem() == RegItem.warTicket) {
                return true;
            }
        }

        return false;
    }

    private void banPlayer(EntityPlayerMP player) {
        String reason = "Вы потратили последнюю жизнь, ожидайте окончания ивента.";

        player.playerNetServerHandler.kickPlayerFromServer(reason);

        System.out.println("[WarMode] Игрок " + player.getCommandSenderName() +
                " исключен: нет военного билета");

        MinecraftServer server = MinecraftServer.getServer();
        if (server != null) {
            server.getConfigurationManager().sendChatMsg(new ChatComponentText(
                    EnumChatFormatting.RED + "☠ " + player.getCommandSenderName() +
                            " потерял последнюю жизнь и выбыл из ивента!"));
        }
    }
}
