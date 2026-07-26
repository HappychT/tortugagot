package got.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;

public class GOTAutoRespawnHandler {
    private boolean pendingRespawn;

    public GOTAutoRespawnHandler() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!(event.gui instanceof GuiGameOver) || mc.theWorld == null) {
            return;
        }

        if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            return;
        }

        event.setCanceled(true);
        event.gui = null;
        pendingRespawn = true;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!pendingRespawn) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) {
            pendingRespawn = false;
            return;
        }

        mc.thePlayer.respawnPlayer();
        mc.displayGuiScreen(null);

        pendingRespawn = false;
    }
}
