package brain.tutorial.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.client.gui.GOTGuiMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TutorialGuiMapHighlight {

    private static void drawGlowBorder(int x, int y, int w, int h, float alpha, int thickness) {
        int r = 255, g = 200, b = 50;
        int color = ((int)(alpha * 200) << 24) | (r << 16) | (g << 8) | b;
        int glowColor = ((int)(alpha * 80) << 24) | (r << 16) | (g << 8) | b;

        Gui.drawRect(x - thickness - 2, y - thickness - 2, x + w + thickness + 2, y - thickness, glowColor);
        Gui.drawRect(x - thickness - 2, y + h + thickness, x + w + thickness + 2, y + h + thickness + 2, glowColor);
        Gui.drawRect(x - thickness - 2, y - thickness, x - thickness, y + h + thickness, glowColor);
        Gui.drawRect(x + w + thickness, y - thickness, x + w + thickness + 2, y + h + thickness, glowColor);

        Gui.drawRect(x - thickness, y - thickness, x + w + thickness, y, color);
        Gui.drawRect(x - thickness, y + h, x + w + thickness, y + h + thickness, color);
        Gui.drawRect(x - thickness, y, x, y + h, color);
        Gui.drawRect(x + w, y, x + w + thickness, y + h, color);
    }

    private static void drawLine(int x1, int y1, int x2, int y2, int color) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float steps = Math.max(Math.abs(dx), Math.abs(dy));
        if (steps == 0) return;
        float stepX = dx / steps;
        float stepY = dy / steps;
        for (int i = 0; i <= (int) steps; i += 2) {
            int px = x1 + (int)(stepX * i);
            int py = y1 + (int)(stepY * i);
            Gui.drawRect(px, py, px + 2, py + 2, color);
        }
    }

    private static void drawInfoPanel(FontRenderer font, String text, int panelX, int panelY, float alpha) {
        int padding = 6;
        int maxWidth = 160;

        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            if (currentLine.length() > 0 && font.getStringWidth(currentLine + " " + word) > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            }
        }
        if (currentLine.length() > 0) lines.add(currentLine.toString());

        int lineHeight = 11;
        int panelW = maxWidth + padding * 2;
        int panelH = lines.size() * lineHeight + padding * 2;

        int bgColor = ((int)(alpha * 220) << 24) | 0x0A0A14;
        Gui.drawRect(panelX, panelY, panelX + panelW, panelY + panelH, bgColor);

        int borderColor = ((int)(alpha * 180) << 24) | 0xFFC832;
        Gui.drawRect(panelX, panelY, panelX + panelW, panelY + 1, borderColor);
        Gui.drawRect(panelX, panelY + panelH - 1, panelX + panelW, panelY + panelH, borderColor);
        Gui.drawRect(panelX, panelY, panelX + 1, panelY + panelH, borderColor);
        Gui.drawRect(panelX + panelW - 1, panelY, panelX + panelW, panelY + panelH, borderColor);

        int accentColor = ((int)(alpha * 255) << 24) | 0xFFD700;
        Gui.drawRect(panelX, panelY, panelX + 3, panelY + 3, accentColor);
        Gui.drawRect(panelX + panelW - 3, panelY, panelX + panelW, panelY + 3, accentColor);
        Gui.drawRect(panelX, panelY + panelH - 3, panelX + 3, panelY + panelH, accentColor);
        Gui.drawRect(panelX + panelW - 3, panelY + panelH - 3, panelX + panelW, panelY + panelH, accentColor);

        int textColor = ((int)(alpha * 255) << 24) | 0xE8C840;
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        for (int i = 0; i < lines.size(); i++) {
            font.drawStringWithShadow(lines.get(i), panelX + padding, panelY + padding + i * lineHeight, textColor);
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private static void drawPointerToArea(GOTGuiMap gui, int x, int y, int w, int h, float alpha, String description) {
        drawGlowBorder(x, y, w, h, alpha, 2);

        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int panelW = 172;
        int panelX = Math.max(10, Math.min(x + w / 2 - panelW / 2, gui.width - panelW - 10));
        int panelY = y > gui.height / 2 ? Math.max(10, y - 60) : Math.min(gui.height - 60, y + h + 20);

        int lineColor = ((int)(alpha * 180) << 24) | 0xFFC832;
        drawLine(x + w / 2, y > gui.height / 2 ? y - 2 : y + h + 2, panelX + panelW / 2, y > gui.height / 2 ? panelY + 40 : panelY, lineColor);

        drawInfoPanel(font, description, panelX, panelY, alpha);
    }

    public static void drawHighlight(GOTGuiMap gui, int mouseX, int mouseY) {
        int p = TutorialClientState.tutorialProgress;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        float alpha = 0.55f + 0.45f * (float) Math.sin(Minecraft.getSystemTime() / 300.0);

        String desc = TutorialClientState.getSubStepSubtitle();

        if (p == 2) {
            // "На карте есть путевые точки. Приблизьте карту (колесиком мыши), чтобы рассмотреть их."
            // "Также на карте обозначены: все регионы, биомы, моря Вестероса."
            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            drawInfoPanel(font, desc + " " + TutorialTextsClient.get("gui.map.regions_info"), gui.width / 2 - 86, 20, alpha);
            
            // Text for waypoint is handled automatically by the map's default tooltip logic when hovering
        } else if (p == 3) {
            // Кнопка создания путевой точки: widgetAddCWP
            if (gui.widgetAddCWP != null) {
                try {
                    java.lang.reflect.Field mapXMinF = got.client.gui.GOTGuiMap.class.getDeclaredField("mapXMin");
                    java.lang.reflect.Field mapYMinF = got.client.gui.GOTGuiMap.class.getDeclaredField("mapYMin");
                    java.lang.reflect.Field mapWidthF = got.client.gui.GOTGuiMap.class.getDeclaredField("mapWidth");
                    java.lang.reflect.Field mapHeightF = got.client.gui.GOTGuiMap.class.getDeclaredField("mapHeight");
                    mapXMinF.setAccessible(true); mapYMinF.setAccessible(true);
                    mapWidthF.setAccessible(true); mapHeightF.setAccessible(true);
                    
                    int mapXMin = mapXMinF.getInt(gui);
                    int mapYMin = mapYMinF.getInt(gui);
                    int mapWidth = mapWidthF.getInt(gui);
                    int mapHeight = mapHeightF.getInt(gui);
                    
                    int wx = mapXMin + gui.widgetAddCWP.getMapXPos(mapWidth);
                    int wy = mapYMin + gui.widgetAddCWP.getMapYPos(mapHeight);
                    int w = gui.widgetAddCWP.width;
                    
                    drawPointerToArea(gui, wx, wy, w, w, alpha, desc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    public static boolean handleMouseClick(GOTGuiMap gui, int mouseX, int mouseY, int button) {
        int p = TutorialClientState.tutorialProgress;

        if (p == 2) {
            if (button == 0) {
                advance();
                return true;
            }
            return false; // Only allow clicking to advance, or maybe scrolling
        } else if (p == 3) {
            // The map will be closed by pressing ESC, handled in GOTGuiMap or default logic.
            // Disable clicks
            return true;
        }

        return true;
    }

    private static void advance() {
        got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
        TutorialClientState.tutorialProgress++;
    }
}
