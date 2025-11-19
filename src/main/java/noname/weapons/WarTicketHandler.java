package noname.weapons;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class WarTicketHandler {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        handleTicketCheck(player);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        handleTicketCheck(player);
    }

    private void handleTicketCheck(EntityPlayerMP player) {
        if (!WarManager.isWarActive()) {
            return;
        }

        if (MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
            return;
        }

        if (!hasWarTicket(player)) {
            WarManager.banPlayerForWar(player);
        }
    }

    private boolean hasWarTicket(EntityPlayerMP player) {
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            if (player.inventory.mainInventory[i] != null &&
                    player.inventory.mainInventory[i].getItem() == RegItem.warTicket) {
                return true;
            }
        }
        return false;
    }
}


