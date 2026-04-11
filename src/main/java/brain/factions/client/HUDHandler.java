package brain.factions.client;

import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class HUDHandler {
    @SubscribeEvent
    public void onRenderText(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;

        if (FactionStructureManager.structureSlots == null) return;

        int px = (int) Math.floor(mc.thePlayer.posX);
        int pz = (int) Math.floor(mc.thePlayer.posZ);

        for (FactionStructureSlot slot : FactionStructureManager.structureSlots) {
            if (slot.category == FactionStructureSlot.StructureCategory.FORTRESS && slot.ownerFactionID != null) {
                int radius = 15 + ((slot.level > 0 ? slot.level : 1) - 1) * 10;
                
                if (Math.abs(px - slot.xCoord) <= radius && Math.abs(pz - slot.zCoord) <= radius) {
                    FontRenderer fr = mc.fontRenderer;
                    String text = "§6Территория: §e" + slot.name;
                    String text2 = "§7Уровень крепости: §a" + slot.level;
                    fr.drawStringWithShadow(text, 5, 5, 0xFFFFFF);
                    fr.drawStringWithShadow(text2, 5, 15, 0xFFFFFF);
                    break;
                }
            }
        }
    }
}