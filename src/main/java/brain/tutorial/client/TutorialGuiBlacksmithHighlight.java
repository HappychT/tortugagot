package brain.tutorial.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.client.gui.GOTGuiFactionBlacksmith;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TutorialGuiBlacksmithHighlight {

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
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < lines.size(); i++) {
            font.drawStringWithShadow(lines.get(i), panelX + padding, panelY + padding + i * lineHeight, ((int)(alpha * 255) << 24) | 0xE8C840);
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

    public static void drawHighlight(GOTGuiFactionBlacksmith gui) {
        if (TutorialClientState.tutorialStage != 6) return;
        
        int prog = TutorialClientState.tutorialProgress;
        if (prog == 0 || prog == 4 || prog == 8) return; // Waiting for player to open the GUI

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        float alpha = 0.55f + 0.45f * (float)Math.sin(Minecraft.getSystemTime() / 300.0);
        
        Minecraft mc = Minecraft.getMinecraft();
        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int screenW = sr.getScaledWidth();
        int screenH = sr.getScaledHeight();

        String desc = TutorialClientState.getSubStepSubtitle();
        
        int guiLeft = (gui.width - 512) / 2; // Assuming guiLeft calculation
        int guiTop = (gui.height - 512) / 2;

        java.util.List buttons = cpw.mods.fml.common.ObfuscationReflectionHelper.getPrivateValue(net.minecraft.client.gui.GuiScreen.class, gui, "buttonList", "field_146292_n");

        if (gui.isBuyingSlot) {
            GuiButton yesBtn = null;
            if (buttons != null) {
                for (Object obj : buttons) {
                    GuiButton gb = (GuiButton) obj;
                    if (gb.id == 103) {
                        yesBtn = gb;
                        break;
                    }
                }
            }
            if (yesBtn != null && yesBtn.visible) {
                drawPointerToArea(yesBtn.xPosition, yesBtn.yPosition, yesBtn.width, yesBtn.height, alpha, desc, screenW, screenH);
            }
        } else if ((prog == 2 || prog == 6) && buttons != null && !buttons.isEmpty()) {
            // Need to select an enchant. Highlight one of the buttons.
            GuiButton btn = null;
            for (Object obj : buttons) {
                GuiButton gb = (GuiButton) obj;
                if (gb.id < 100 && gb.visible && gb.enabled) {
                    btn = gb;
                    break;
                }
            }
            if (btn == null) {
                btn = (GuiButton) buttons.get(0);
            }
            if (btn != null && btn.visible) {
                drawPointerToArea(btn.xPosition, btn.yPosition, btn.width, btn.height, alpha, desc, screenW, screenH);
            }
        } else if (prog == 1 || prog == 5) {
            // Put armor/sword in slot
            Slot slot = (Slot) gui.inventorySlots.inventorySlots.get(0); // The central slot for item
            if (slot != null) {
                int sx = guiLeft + slot.xDisplayPosition;
                int sy = guiTop + slot.yDisplayPosition;
                drawPointerToArea(sx, sy, 16, 16, alpha, desc, screenW, screenH);
            }
        } else if ((prog == 3 || prog == 7) && buttons != null) {
            // Press reforge button (ID 100)
            GuiButton btn = null;
            for (Object obj : buttons) {
                GuiButton gb = (GuiButton) obj;
                if (gb.id == 100) {
                    btn = gb;
                    break;
                }
            }
            if (btn != null && btn.visible) {
                drawPointerToArea(btn.xPosition, btn.yPosition, btn.width, btn.height, alpha, desc, screenW, screenH);
            }
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }
    
    public static boolean handleMouseClick(GOTGuiFactionBlacksmith gui, int mouseX, int mouseY, int button) {
        if (TutorialClientState.tutorialStage != 6) return false;
        int prog = TutorialClientState.tutorialProgress;
        
        // Progress 0 or 4: they just opened it. We can auto-advance.
        if (prog == 0 || prog == 4 || prog == 8) {
            return false;
        }
        
        int guiLeft = (gui.width - 512) / 2;
        int guiTop = (gui.height - 512) / 2;
        
        if (prog == 1 || prog == 5) {
            java.util.List buttons = cpw.mods.fml.common.ObfuscationReflectionHelper.getPrivateValue(net.minecraft.client.gui.GuiScreen.class, gui, "buttonList", "field_146292_n");
            // Allow clicking enchant buttons. (id < 23)
            if (buttons != null) {
                for (Object obj : buttons) {
                    GuiButton gb = (GuiButton) obj;
                    if (gb.id < 100 && gb.visible) {
                        if (mouseX >= gb.xPosition && mouseX <= gb.xPosition + gb.width &&
                            mouseY >= gb.yPosition && mouseY <= gb.yPosition + gb.height) {
                            return false; // let it pass
                        }
                    }
                }
            }
            // Allow inventory clicks just in case
            return false; // Let's not fully block them so they don't get stuck.
        } else if (prog == 2 || prog == 6) {
            return false;
        } else if (prog == 3 || prog == 7) {
            return false;
        }
        return false;
    }

    public static void drawAnvilHighlight(got.client.gui.GOTGuiAnvil gui) {
        if (TutorialClientState.tutorialStage != 6) return;
        int prog = TutorialClientState.tutorialProgress;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        float alpha = 0.55f + 0.45f * (float)Math.sin(Minecraft.getSystemTime() / 300.0);
        
        Minecraft mc = Minecraft.getMinecraft();
        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int screenW = sr.getScaledWidth();
        int screenH = sr.getScaledHeight();

        String desc = TutorialClientState.getSubStepSubtitle();
        
        int guiLeft = (gui.width - 176) / 2;
        int guiTop = (gui.height - 198) / 2;

        if (false) {
            // Reforge armor: Highlight Slot 0, Slot 2, and buttonReforge
            Slot slot0 = (Slot) gui.inventorySlots.inventorySlots.get(0);
            if (slot0 != null && !slot0.getHasStack()) {
                drawPointerToArea(guiLeft + slot0.xDisplayPosition, guiTop + slot0.yDisplayPosition, 16, 16, alpha, desc, screenW, screenH);
            } else {
                Slot slot2 = (Slot) gui.inventorySlots.inventorySlots.get(2);
                if (slot2 != null && !slot2.getHasStack()) {
                    drawPointerToArea(guiLeft + slot2.xDisplayPosition, guiTop + slot2.yDisplayPosition, 16, 16, alpha, desc, screenW, screenH);
                } else {
                    java.util.List buttons = cpw.mods.fml.common.ObfuscationReflectionHelper.getPrivateValue(net.minecraft.client.gui.GuiScreen.class, gui, "buttonList", "field_146292_n");
                    if (buttons != null && !buttons.isEmpty()) {
                        GuiButton btn = (GuiButton) buttons.get(0); // buttonReforge
                        if (btn != null && btn.visible) {
                            drawPointerToArea(btn.xPosition, btn.yPosition, btn.width, btn.height, alpha, desc, screenW, screenH);
                        }
                    }
                }
            }
        } else if (prog >= 5 && prog <= 7) {
            // Enchant sword: Highlight Slot 0, Slot 1, and Slot 3 (output)
            Slot slot0 = (Slot) gui.inventorySlots.inventorySlots.get(0);
            if (slot0 != null && (!slot0.getHasStack() || slot0.getStack().getItem() != got.common.database.GOTRegistry.bronzeSword)) {
                drawPointerToArea(guiLeft + slot0.xDisplayPosition, guiTop + slot0.yDisplayPosition, 16, 16, alpha, TutorialClientState.getRawText("substep.6.5", desc), screenW, screenH);
            } else {
                Slot slot1 = (Slot) gui.inventorySlots.inventorySlots.get(1);
                if (slot1 != null && !slot1.getHasStack()) {
                    drawPointerToArea(guiLeft + slot1.xDisplayPosition, guiTop + slot1.yDisplayPosition, 16, 16, alpha, TutorialClientState.getRawText("substep.6.6", desc), screenW, screenH);
                } else {
                    Slot slot2 = (Slot) gui.inventorySlots.inventorySlots.get(2);
                    if (slot2 != null && !slot2.getHasStack()) {
                        drawPointerToArea(guiLeft + slot2.xDisplayPosition, guiTop + slot2.yDisplayPosition, 16, 16, alpha, TutorialClientState.getRawText("substep.6.6_2", "Положите легированную сталь в нижний слот"), screenW, screenH);
                    } else {
                        Slot slot3 = (Slot) gui.inventorySlots.inventorySlots.get(3);
                        if (slot3 != null && slot3.getHasStack()) {
                            drawPointerToArea(guiLeft + slot3.xDisplayPosition, guiTop + slot3.yDisplayPosition, 16, 16, alpha, TutorialClientState.getRawText("substep.6.7", desc), screenW, screenH);
                        }
                    }
                }
            }
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    public static boolean handleAnvilMouseClick(got.client.gui.GOTGuiAnvil gui, int mouseX, int mouseY, int button) {
        if (TutorialClientState.tutorialStage != 6) return false;
        return false;
    }
}
