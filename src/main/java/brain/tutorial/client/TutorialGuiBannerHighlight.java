package brain.tutorial.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.client.gui.GOTGuiBanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TutorialGuiBannerHighlight {

    // ========== BEAUTIFUL HIGHLIGHT RENDERING ==========

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

    private static int calculatePanelHeight(FontRenderer font, String text, int maxWidth, int padding) {
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        int lines = 0;
        for (String word : words) {
            if (currentLine.length() > 0 && font.getStringWidth(currentLine + " " + word) > maxWidth) {
                lines++;
                currentLine = new StringBuilder(word);
            } else {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            }
        }
        if (currentLine.length() > 0) lines++;
        return lines * 11 + padding * 2;
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

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        for (int i = 0; i < lines.size(); i++) {
            font.drawStringWithShadow(lines.get(i), panelX + padding, panelY + padding + i * lineHeight, 0xFFE8C840);
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private static void drawPointerToArea(int x, int y, int w, int h, float alpha, String description, int screenW, int screenH) {
        drawGlowBorder(x, y, w, h, alpha, 2);

        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int panelW = 172;
        int padding = 6;
        int panelH = calculatePanelHeight(font, description, panelW - padding * 2, padding);

        int centerX = x + w / 2;
        int centerY = y + h / 2;

        int panelX = Math.max(10, Math.min(centerX - panelW / 2, screenW - panelW - 10));
        int panelY;
        int lineStartY, lineEndY;

        if (centerY > screenH / 2) {
            panelY = Math.max(10, y - panelH - 20);
            lineStartY = y - 2;
            lineEndY = panelY + panelH;
        } else {
            panelY = Math.min(screenH - panelH - 10, y + h + 20);
            lineStartY = y + h + 2;
            lineEndY = panelY;
        }

        int lineColor = ((int)(alpha * 180) << 24) | 0xFFC832;
        drawLine(centerX, lineStartY, panelX + panelW / 2, lineEndY, lineColor);

        drawInfoPanel(font, description, panelX, panelY, alpha);
    }

    private static void drawPointerToButton(GuiButton target, float alpha, String description, int screenW, int screenH) {
        if (target == null || !target.visible) return;
        drawPointerToArea(target.xPosition, target.yPosition, target.width, target.height, alpha, description, screenW, screenH);
    }

    private static String getStepDescription() {
        return TutorialClientState.getSubStepSubtitle();
    }

    public static void drawHighlight(GOTGuiBanner gui) {
        if (TutorialClientState.tutorialStage != 4 || (TutorialClientState.tutorialProgress != 2 && TutorialClientState.tutorialProgress != 3)) return;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        float alpha = 0.55f + 0.45f * (float)Math.sin(Minecraft.getSystemTime() / 300.0);
        
        Minecraft mc = Minecraft.getMinecraft();
        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int screenW = sr.getScaledWidth();
        int screenH = sr.getScaledHeight();

        String desc = getStepDescription();

        if (!gui.theBanner.isPlayerSpecificProtection()) {
            // Need to switch mode
            drawPointerToButton(gui.buttonMode, alpha, TutorialTextsClient.get("gui.banner.switch_mode"), screenW, screenH);
        } else {
            // Need to type the fellowship
            if (gui.allowedPlayers != null && gui.allowedPlayers.length > 1) {
                GuiTextField targetField = gui.allowedPlayers[1];
                if (targetField != null) {
                    if (targetField.getText().startsWith("f/") && targetField.getText().length() > 2) {
                        drawPointerToArea(targetField.xPosition, targetField.yPosition, targetField.width, targetField.height, alpha, TutorialTextsClient.get("gui.banner.press_esc"), screenW, screenH);
                    } else {
                        drawPointerToArea(targetField.xPosition, targetField.yPosition, targetField.width, targetField.height, alpha, desc, screenW, screenH);
                    }
                }
            }
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    public static boolean handleMouseClick(GOTGuiBanner gui, int mouseX, int mouseY, int button) {
        if (TutorialClientState.tutorialStage != 4 || (TutorialClientState.tutorialProgress != 2 && TutorialClientState.tutorialProgress != 3)) return false;

        if (!gui.theBanner.isPlayerSpecificProtection()) {
            // Only allow clicking the buttonMode
            if (gui.buttonMode != null && isHovering(gui.buttonMode.xPosition, gui.buttonMode.yPosition, gui.buttonMode.width, gui.buttonMode.height, mouseX, mouseY)) {
                return false; // let it pass
            }
            return true; // block everything else
        } else {
            // Mode is correct. Allow clicking the text field
            if (gui.allowedPlayers != null && gui.allowedPlayers.length > 1) {
                GuiTextField targetField = gui.allowedPlayers[1];
                if (targetField != null && isHovering(targetField.xPosition, targetField.yPosition, targetField.width, targetField.height, mouseX, mouseY)) {
                    return false; // let them click the text box
                }
            }
            return true; // block clicking anything else
        }
    }

    public static boolean handleKeyTyped(GOTGuiBanner gui, char c, int key) {
        if (TutorialClientState.tutorialStage != 4 || (TutorialClientState.tutorialProgress != 2 && TutorialClientState.tutorialProgress != 3)) return false;
        
        // We MUST allow ESC (key == 1) so they can close and save the GUI!
        if (key == 1) return false;
        
        // If they haven't switched to whitelist mode, block typing
        if (!gui.theBanner.isPlayerSpecificProtection()) return true;
        
        return false;
    }

    private static boolean isHovering(int x, int y, int w, int h, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}
