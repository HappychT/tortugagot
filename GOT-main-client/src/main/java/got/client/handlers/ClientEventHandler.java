package got.client.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.client.GOTClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    private final Minecraft mc = Minecraft.getMinecraft();


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderHUD(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            EntityPlayer player = mc.thePlayer;
            if (player != null && player.getHeldItem() != null) {
                Item heldItem = player.getHeldItem().getItem();

                Integer hitCount = GOTClientProxy.weaponHitCounts.get(heldItem);

                if (hitCount != null) {
                    String text = "Комбо: " + hitCount + " / 3";
                    int color = 0xFFFFFF;

                    int x = event.resolution.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(text) / 2;
                    int y = event.resolution.getScaledHeight() - 55;

                    mc.fontRenderer.drawStringWithShadow(text, x, y, color);
                }
            }
        }
    }
}