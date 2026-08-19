package brain.tutorial.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.client.gui.GOTGuiFellowships;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TutorialGuiFellowshipsHighlight {

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

    private static GuiButton findButton(List buttonList, int id) {
        for (Object obj : buttonList) {
            if (obj instanceof GuiButton) {
                GuiButton btn = (GuiButton) obj;
                if (btn.id == id) return btn;
            }
        }
        return null;
    }

    private static void drawPointerToButton(List buttonList, int buttonId, float alpha, String description, int screenW, int screenH) {
        GuiButton target = findButton(buttonList, buttonId);
        if (target == null || !target.visible) return;

        drawGlowBorder(target.xPosition, target.yPosition, target.width, target.height, alpha, 2);

        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int panelW = 172;
        int padding = 6;
        int panelH = calculatePanelHeight(font, description, panelW - padding * 2, padding);

        int btnCenterX = target.xPosition + target.width / 2;
        int btnCenterY = target.yPosition + target.height / 2;

        int panelX = Math.max(10, Math.min(btnCenterX - panelW / 2, screenW - panelW - 10));
        int panelY;
        int lineStartY, lineEndY;

        if (btnCenterY > screenH / 2) {
            panelY = Math.max(10, target.yPosition - panelH - 20);
            lineStartY = target.yPosition - 2;
            lineEndY = panelY + panelH;
        } else {
            panelY = Math.min(screenH - panelH - 10, target.yPosition + target.height + 20);
            lineStartY = target.yPosition + target.height + 2;
            lineEndY = panelY;
        }

        int lineColor = ((int)(alpha * 180) << 24) | 0xFFC832;
        drawLine(btnCenterX, lineStartY, panelX + panelW / 2, lineEndY, lineColor);

        drawInfoPanel(font, description, panelX, panelY, alpha);
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

    private static String getStepDescription(int p) {
        return TutorialClientState.getSubStepSubtitle();
    }

    public static void drawHighlight(List buttonList, int xSize, int ySize, int guiLeft, int guiTop) {
        int p = TutorialClientState.tutorialProgress;
        
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        float alpha = 0.55f + 0.45f * (float)Math.sin(Minecraft.getSystemTime() / 300.0);
        
        String desc = getStepDescription(p);
        Minecraft mc = Minecraft.getMinecraft();
        int screenW = mc.displayWidth;
        int screenH = mc.displayHeight;
        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        screenW = sr.getScaledWidth();
        screenH = sr.getScaledHeight();
        
        if (p == 38) {
            drawPointerToButton(buttonList, 0, alpha, desc, screenW, screenH); // buttonCreate
        } else if (p == 39) {
            drawPointerToButton(buttonList, 1, alpha, desc, screenW, screenH); // buttonCreateThis
        } else if (p == 40) {
            drawPointerToArea(guiLeft + 8, guiTop + 30, xSize - 16, ySize - 60, alpha, desc, screenW, screenH);
        } else if (p == 41) {
            drawPointerToButton(buttonList, 2, alpha, desc, screenW, screenH); // buttonInvitePlayer
        } else if (p == 42) {
            drawPointerToButton(buttonList, 3, alpha, desc, screenW, screenH); // buttonInviteThis
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    public static boolean handleMouseClick(List buttonList, int mouseX, int mouseY, int button, int guiLeft, int guiTop, int xSize, int ySize, boolean fellowshipHovered) {
        int p = TutorialClientState.tutorialProgress;
        
        if (p == 38) {
            if (isButtonHovered(buttonList, 0, mouseX, mouseY)) {
                advance(); return true;
            }
            return false;
        } else if (p == 39) {
            if (isButtonHovered(buttonList, 1, mouseX, mouseY)) {
                advance(); return true;
            }
            if (isHoveringAnyOtherButton(buttonList, 1, mouseX, mouseY)) { return false; }
            return true; // Let them type — caller will forward to text field directly
        } else if (p == 40) {
            // Only advance if the player actually clicked on a fellowship entry, not empty space
            if (fellowshipHovered) {
                advance(); return true;
            }
            // If clicked inside the list area but nothing hovered, block (don't advance, don't do anything)
            if (mouseX >= guiLeft + 8 && mouseX <= guiLeft + xSize - 8 && mouseY >= guiTop + 30 && mouseY <= guiTop + ySize - 30) {
                return false; // Inside list area but no fellowship — block
            }
            return false; // Outside list area — block too
        } else if (p == 41) {
            if (isButtonHovered(buttonList, 2, mouseX, mouseY)) {
                advance(); return true;
            }
            return false;
        } else if (p == 42) {
            if (isButtonHovered(buttonList, 3, mouseX, mouseY)) {
                advance(); return true;
            }
            if (isHoveringAnyOtherButton(buttonList, 3, mouseX, mouseY)) { return false; }
            return true; // Let them type — caller will forward to text field directly
        }
        
        return true;
    }
    
    private static boolean isHoveringAnyOtherButton(List buttonList, int allowedId, int mouseX, int mouseY) {
        for (Object obj : buttonList) {
            if (obj instanceof GuiButton) {
                GuiButton btn = (GuiButton) obj;
                if (btn.id != allowedId && btn.visible) {
                    if (mouseX >= btn.xPosition && mouseY >= btn.yPosition && mouseX < btn.xPosition + btn.width && mouseY < btn.yPosition + btn.height) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isButtonHovered(List buttonList, int id, int mouseX, int mouseY) {
        for (Object obj : buttonList) {
            if (obj instanceof GuiButton) {
                GuiButton btn = (GuiButton) obj;
                if (btn.id == id && btn.visible && btn.enabled) {
                    if (mouseX >= btn.xPosition && mouseY >= btn.yPosition && mouseX < btn.xPosition + btn.width && mouseY < btn.yPosition + btn.height) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void advance() {
        got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
        TutorialClientState.tutorialProgress++;
    }

    public static boolean handleKeyTyped(char c, int key) {
        int p = TutorialClientState.tutorialProgress;
        if (p == 39 || p == 42) {
            if (key == 1) return false; // Block ESC from closing the overlay!
            return true; // Allow typing in text boxes
        }
        return false; // Block ESC
    }
}
