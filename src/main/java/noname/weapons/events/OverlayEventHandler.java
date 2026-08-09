package noname.weapons.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import noname.weapons.render.ReloadBalistaRender;
import noname.weapons.render.ReloadRenderer;
import noname.weapons.render.ReloadTribushetRender;
import org.lwjgl.input.Mouse;

public class OverlayEventHandler {
    private final ReloadRenderer renderer = new ReloadRenderer();
    private final ReloadTribushetRender renderer2 = new ReloadTribushetRender();
    private final ReloadBalistaRender renderer3 = new ReloadBalistaRender();

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            renderer.render();
            renderer2.render();
            renderer3.render();
        }
    }

}
