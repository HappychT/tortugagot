package brain.tutorial.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.client.gui.faction.GOTGuiFactions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TutorialGuiFactionsHighlight {

    // Защита от повторного авто-advance при повторном открытии GUI
    private static boolean closingInProgress = false;

    public static boolean isButtonAllowed(int buttonId) {
        if (!TutorialClientState.isTutorialActive || TutorialClientState.tutorialStage != 3) return true;
        int[] validButtons = {4, 9, 7, 16, 17, 150, 151, 152, 101, 0, 1, 22, 11, 12, 201, 19, 6, 5, 50};
        for (int b : validButtons) if (buttonId == b) return true;
        return false;
    }

    // ========== BEAUTIFUL HIGHLIGHT RENDERING ==========

    /**
     * Draws a glowing border around a rectangular area (pulsating).
     */
    private static void drawGlowBorder(int x, int y, int w, int h, float alpha, int thickness) {
        int r = 255, g = 200, b = 50;
        int color = ((int)(alpha * 200) << 24) | (r << 16) | (g << 8) | b;
        int glowColor = ((int)(alpha * 80) << 24) | (r << 16) | (g << 8) | b;

        // Outer glow (larger, more transparent)
        Gui.drawRect(x - thickness - 2, y - thickness - 2, x + w + thickness + 2, y - thickness, glowColor);
        Gui.drawRect(x - thickness - 2, y + h + thickness, x + w + thickness + 2, y + h + thickness + 2, glowColor);
        Gui.drawRect(x - thickness - 2, y - thickness, x - thickness, y + h + thickness, glowColor);
        Gui.drawRect(x + w + thickness, y - thickness, x + w + thickness + 2, y + h + thickness, glowColor);

        // Inner bright border
        Gui.drawRect(x - thickness, y - thickness, x + w + thickness, y, color);           // top
        Gui.drawRect(x - thickness, y + h, x + w + thickness, y + h + thickness, color);   // bottom
        Gui.drawRect(x - thickness, y, x, y + h, color);                                   // left
        Gui.drawRect(x + w, y, x + w + thickness, y + h, color);                           // right
    }

    /**
     * Draws a line between two points using small rectangles.
     */
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

    /**
     * Draws an info panel at the given position with a description text.
     * Returns the panel bounds [x, y, w, h].
     */
    private static void drawInfoPanel(FontRenderer font, String text, int panelX, int panelY, float alpha) {
        int padding = 6;
        int maxWidth = 160;

        // Word-wrap the text
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

        // Background
        int bgColor = ((int)(alpha * 220) << 24) | 0x0A0A14;
        Gui.drawRect(panelX, panelY, panelX + panelW, panelY + panelH, bgColor);

        // Border
        int borderColor = ((int)(alpha * 180) << 24) | 0xFFC832;
        Gui.drawRect(panelX, panelY, panelX + panelW, panelY + 1, borderColor);
        Gui.drawRect(panelX, panelY + panelH - 1, panelX + panelW, panelY + panelH, borderColor);
        Gui.drawRect(panelX, panelY, panelX + 1, panelY + panelH, borderColor);
        Gui.drawRect(panelX + panelW - 1, panelY, panelX + panelW, panelY + panelH, borderColor);

        // Corner accents (small bright squares at corners)
        int accentColor = ((int)(alpha * 255) << 24) | 0xFFD700;
        Gui.drawRect(panelX, panelY, panelX + 3, panelY + 3, accentColor);
        Gui.drawRect(panelX + panelW - 3, panelY, panelX + panelW, panelY + 3, accentColor);
        Gui.drawRect(panelX, panelY + panelH - 3, panelX + 3, panelY + panelH, accentColor);
        Gui.drawRect(panelX + panelW - 3, panelY + panelH - 3, panelX + panelW, panelY + panelH, accentColor);

        // Text
        int textColor = ((int)(alpha * 255) << 24) | 0xE8C840;
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        for (int i = 0; i < lines.size(); i++) {
            font.drawStringWithShadow(lines.get(i), panelX + padding, panelY + padding + i * lineHeight, textColor);
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
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

    /**
     * Main method: draw a glow on the target button + a line + an info panel with the step description.
     */
    private static void drawPointerToButton(GOTGuiFactions gui, int buttonId, float alpha, String description) {
        GuiButton target = findButton(gui, buttonId);
        if (target == null || !target.visible) return;

        drawGlowBorder(target.xPosition, target.yPosition, target.width, target.height, alpha, 2);

        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int panelW = 172;
        int padding = 6;
        int panelH = calculatePanelHeight(font, description, panelW - padding * 2, padding);

        int btnCenterX = target.xPosition + target.width / 2;
        int btnCenterY = target.yPosition + target.height / 2;

        int panelX = Math.max(10, Math.min(btnCenterX - panelW / 2, gui.getBaseWidth() - panelW - 10));
        int panelY;
        int lineStartY, lineEndY;

        if (btnCenterY > gui.getBaseHeight() / 2) {
            panelY = Math.max(10, target.yPosition - panelH - 20);
            lineStartY = target.yPosition - 2;
            lineEndY = panelY + panelH;
        } else {
            panelY = Math.min(gui.getBaseHeight() - panelH - 10, target.yPosition + target.height + 20);
            lineStartY = target.yPosition + target.height + 2;
            lineEndY = panelY;
        }

        int lineColor = ((int)(alpha * 180) << 24) | 0xFFC832;
        drawLine(btnCenterX, lineStartY, panelX + panelW / 2, lineEndY, lineColor);

        drawInfoPanel(font, description, panelX, panelY, alpha);
    }

    /**
     * Draws a glow on an area + line + info panel.
     */
    private static void drawPointerToArea(GOTGuiFactions gui, int x, int y, int w, int h, float alpha, String description) {
        drawGlowBorder(x, y, w, h, alpha, 2);

        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int panelW = 172;
        int padding = 6;
        int panelH = calculatePanelHeight(font, description, panelW - padding * 2, padding);

        int centerX = x + w / 2;
        int centerY = y + h / 2;

        int panelX = Math.max(10, Math.min(centerX - panelW / 2, gui.getBaseWidth() - panelW - 10));
        int panelY;
        int lineStartY, lineEndY;

        if (centerY > gui.getBaseHeight() / 2) {
            panelY = Math.max(10, y - panelH - 20);
            lineStartY = y - 2;
            lineEndY = panelY + panelH;
        } else {
            panelY = Math.min(gui.getBaseHeight() - panelH - 10, y + h + 20);
            lineStartY = y + h + 2;
            lineEndY = panelY;
        }

        int lineColor = ((int)(alpha * 180) << 24) | 0xFFC832;
        drawLine(centerX, lineStartY, panelX + panelW / 2, lineEndY, lineColor);

        drawInfoPanel(font, description, panelX, panelY, alpha);
    }

    // ========== STEP DESCRIPTIONS ==========

    private static String getStepDescription(int p) {
        return TutorialClientState.getSubStepSubtitle();
    }

    // ========== MAIN DRAW ==========

    public static void drawHighlight(GOTGuiFactions gui, int mouseX, int mouseY) {
        int p = TutorialClientState.tutorialProgress;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        float alpha = 0.55f + 0.45f * (float) Math.sin(Minecraft.getSystemTime() / 300.0);

        String desc = getStepDescription(p);

        if (p == 5) {
            drawPointerToButton(gui, 4, alpha, desc);
        } else if (p == 6) {
            if (gui.getActiveRenderer() instanceof got.client.gui.faction.FactionListRenderer) {
                got.client.gui.faction.FactionListRenderer listRenderer = (got.client.gui.faction.FactionListRenderer) gui.getActiveRenderer();
                int[] bounds = listRenderer.getFactionBounds("HIGH_POWER");
                if (bounds != null) {
                    drawPointerToArea(gui, bounds[0], bounds[1], bounds[2], bounds[3], alpha, desc + " " + TutorialTextsClient.get("gui.factions.tortuga"));
                } else {
                    FontRenderer font = Minecraft.getMinecraft().fontRenderer;
                    drawInfoPanel(font, desc + " " + TutorialTextsClient.get("gui.factions.scroll_tortuga"), gui.getBaseWidth() / 2 - 86, 20, alpha);
                }
            }
        } else if (p == 7) {
            int plateWidth = 250;
            int plateHeight = 172;
            int plateX = (gui.getBaseWidth() - plateWidth) / 2;
            int plateY = 162;
            drawPointerToArea(gui, plateX, plateY, plateWidth, plateHeight, alpha, desc);
        } else if (p == 8) {
            drawPointerToArea(gui, gui.getBaseWidth() / 2 - 100, 365, 200, 25, alpha, desc);
        } else if (p == 9) {
            drawPointerToButton(gui, 9, alpha, desc);
        } else if (p == 10) {
            int rightButtonsX = gui.getBaseWidth() - 233;
            drawPointerToArea(gui, rightButtonsX, 60, 168, 282, alpha, desc);
        } else if (p == 11) {
            drawPointerToButton(gui, 7, alpha, desc);
        } else if (p == 12) {
            drawPointerToButton(gui, 16, alpha, desc);
        } else if (p == 13 || p == 14) {
            drawPointerToButton(gui, 17, alpha, desc);
        } else if (p == 15) {
            drawPointerToButton(gui, 17, alpha, desc);
        } else if (p == 16) {
            if (gui.getCurrentPage() != got.client.gui.faction.GOTGuiFactions.Page.TITLE_HIERARCHY) {
                drawPointerToButton(gui, 17, alpha, TutorialTextsClient.get("gui.factions.press_hierarchy"));
            } else {
                int listX = 60, listY = 50, listWidth = gui.getBaseWidth() - 120, listHeight = 12 * 22;
                drawPointerToArea(gui, listX, listY, listWidth, listHeight, alpha, desc);
            }
        } else if (p == 17) {
            drawPointerToButton(gui, 150, alpha, desc); // Save button in Title Hierarchy
        } else if (p == 18) {
            if (gui.getPage(GOTGuiFactions.Page.PLAYER_LIST) instanceof got.client.gui.faction.pages.PlayerListPage) {
                got.client.gui.faction.pages.PlayerListPage page = (got.client.gui.faction.pages.PlayerListPage) gui.getPage(GOTGuiFactions.Page.PLAYER_LIST);
                int[] bounds = page.getPlayerRowBounds(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
                if (bounds != null) {
                    drawPointerToArea(gui, bounds[0], bounds[1], bounds[2], bounds[3], alpha, desc + " " + TutorialTextsClient.get("gui.factions.rmb_nickname"));
                } else {
                    FontRenderer font = Minecraft.getMinecraft().fontRenderer;
                    drawInfoPanel(font, desc + " " + TutorialTextsClient.get("gui.factions.scroll_down"), gui.getBaseWidth() / 2 - 86, 20, alpha);
                }
            } else {
                drawPointerToArea(gui, 15, 50, gui.getBaseWidth() - 30, gui.getBaseHeight() - 70, alpha, desc);
            }
            if (gui.contextMenuObject != null) advance();
        } else if (p == 19) {
            drawPointerToButton(gui, 101, alpha, desc);
        } else if (p == 20) {
            int width = 220, height = 180;
            int overlayX = gui.getBaseWidth() / 2, overlayY = gui.getBaseHeight() / 2;
            int left = overlayX - width / 2, top = overlayY - height / 2;
            drawPointerToArea(gui, left, top, width, height, alpha, desc);
            
            if (gui.getCurrentOverlay() != GOTGuiFactions.Overlay.ASSIGN_TITLE) {
                advance();
            }
        } else if (p == 21) {
            if (gui.getCurrentPage() == got.client.gui.faction.GOTGuiFactions.Page.FRONT) {
                advance();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glPopMatrix();
                return;
            }
            GuiButton back = findButton(gui, 0);
            GuiButton prev = findButton(gui, 1);
            if (back != null && back.visible) drawGlowBorder(back.xPosition, back.yPosition, back.width, back.height, alpha, 2);
            if (prev != null && prev.visible) drawGlowBorder(prev.xPosition, prev.yPosition, prev.width, prev.height, alpha, 2);
            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            drawInfoPanel(font, desc, gui.getBaseWidth() / 2 - 86, 20, alpha);
        } else if (p == 22) {
            if (gui.getCurrentView() == GOTGuiFactions.View.LIST) {
                drawPointerToArea(gui, 60, 50, gui.getBaseWidth() - 120, gui.getBaseHeight() - 100, alpha, desc);
            } else {
                drawPointerToButton(gui, 22, alpha, desc);
            }
        } else if (p == 23) {
            drawPointerToButton(gui, 11, alpha, desc);
        } else if (p == 24) {
            int ox = gui.getBaseWidth() / 2, oy = gui.getBaseHeight() / 2;
            drawGlowBorder(ox - 150, oy - 75, 300, 150, alpha, 2);
            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            drawInfoPanel(font, desc, ox + 160, oy - 50, alpha); // Side guide
        } else if (p == 25) {
            int width = 350, height = 250;
            int x = (gui.getBaseWidth() - width) / 2, y = (gui.getBaseHeight() - height) / 2;
            int listX = x + 15, listY = y + 45, listWidth = width - 30, listHeight = height - 60;
            drawPointerToArea(gui, listX, listY, listWidth, listHeight, alpha, desc);
        } else if (p == 26) {
            drawPointerToButton(gui, 201, alpha, desc);
        } else if (p == 27) {
            drawPointerToButton(gui, 19, alpha, desc);
        } else if (p == 28) {
            int width = 400, height = 300;
            int ox = gui.getBaseWidth() / 2, oy = gui.getBaseHeight() / 2;
            drawPointerToArea(gui, ox - width / 2, oy - height / 2, width, height, alpha, desc + " " + TutorialTextsClient.get("gui.factions.press_esc_close"));
        } else if (p == 29) {
            GuiButton sethome = findButton(gui, 6);
            GuiButton tp = findButton(gui, 5);
            if (sethome != null && sethome.visible) drawGlowBorder(sethome.xPosition, sethome.yPosition, sethome.width, sethome.height, alpha, 2);
            if (tp != null && tp.visible) drawGlowBorder(tp.xPosition, tp.yPosition, tp.width, tp.height, alpha, 2);
            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            drawInfoPanel(font, desc, gui.getBaseWidth() - 180, gui.getBaseHeight() / 2, alpha);
        } else if (p == 30) {
            // Подсвечиваем кнопку Дипломатии (ID 11) аналогично кнопкам Дом/Телепорт, вход заблокирован
            drawPointerToButton(gui, 11, alpha, desc);
        } else if (p == 31) {
            // Подсвечиваем кнопку Военный совет (ID 50), игрок должен кликнуть на неё
            drawPointerToButton(gui, 50, alpha, desc);
        } else if (p == 32) {
            // Если открылась карта (игрок нажал кнопку войны/союза) — сразу возвращаем в военный совет
            if (gui.getCurrentView() == got.client.gui.faction.GOTGuiFactions.View.MAP) {
                gui.setCurrentView(got.client.gui.faction.GOTGuiFactions.View.FACTION);
            }
            // Внутри Военного совета — просто инфопанель, никаких нажатий не требуется
            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            drawInfoPanel(font, desc, gui.getBaseWidth() / 2 - 86, gui.getBaseHeight() / 2 - 30, alpha);
        } else if (p == 33) {
            // Закрываем фракции и переходим сразу на p==36 (Языки)
            // Шаги 34 и 35 были «закройте меню» — их пропускаем
            if (!closingInProgress) {
                closingInProgress = true;
                advance(); // 33 → 34
                advance(); // 34 → 35
                advance(); // 35 → 36 (Языки)
                gui.mc.displayGuiScreen(new got.client.gui.GOTGuiMenu());
            }
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
            return;
        } else if (p == 34 || p == 35) {
            // Запасной вариант: если GUI ещё открыт на 34/35, додвигаемся до 36
            if (!closingInProgress) {
                closingInProgress = true;
                while (TutorialClientState.tutorialProgress < 36) {
                    advance();
                }
                gui.mc.displayGuiScreen(new got.client.gui.GOTGuiMenu());
            }
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
            return;
        } else {
            // Сбрасываем флаг если мы на раннем шаге
            closingInProgress = false;
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    // ========== MOUSE CLICK HANDLING (unchanged logic) ==========

    public static boolean handleMouseClick(GOTGuiFactions gui, int mouseX, int mouseY, int button) {
        int p = TutorialClientState.tutorialProgress;

        if (p == 5) {
            if (isButtonHovered(gui, 4, mouseX, mouseY)) { advance(); return true; }
            return false;
        } else if (p == 6) {
            if (gui.getActiveRenderer() instanceof got.client.gui.faction.FactionListRenderer) {
                got.client.gui.faction.FactionListRenderer listRenderer = (got.client.gui.faction.FactionListRenderer) gui.getActiveRenderer();
                int[] bounds = listRenderer.getFactionBounds("HIGH_POWER");

                // Клик точно по баннеру HIGH_POWER (и он виден) → advance + передать клик
                if (bounds != null && isHighPowerVisible(listRenderer, gui)
                        && gui.isHover(bounds[0], bounds[1], bounds[2], bounds[3], mouseX, mouseY)) {
                    if (button == 0) { advance(); }
                    return true; // пропустить → FactionListRenderer выполнит setCurrentFaction
                }

                // Клик в зону списка (другая фракция, или HIGH_POWER вне экрана) → строго блокируем
                int listX = 70, listY = 50;
                int listW = gui.getBaseWidth() - 140, listH = gui.getBaseHeight() - 85;
                if (gui.isHover(listX, listY, listW, listH, mouseX, mouseY)) { return false; } // заблокировать клик по чужим фракциям
            }
            return false;
        } else if (p == 7) {
            advance(); return false;
        } else if (p == 8) {
            advance(); return false;
        } else if (p == 9) {
            if (isButtonHovered(gui, 9, mouseX, mouseY)) { advance(); return true; }
            return false;
        } else if (p == 10) {
            advance(); return false;
        } else if (p == 11) {
            if (isButtonHovered(gui, 7, mouseX, mouseY)) { advance(); return true; }
            return false;
        } else if (p == 12) {
            if (isButtonHovered(gui, 16, mouseX, mouseY)) { advance(); return true; }
            return false;
        } else if (p == 13 || p == 14) {
            if (gui.getCurrentOverlay() == got.client.gui.faction.GOTGuiFactions.Overlay.CREATE_TITLE) {
                if (isButtonHovered(gui, 17, mouseX, mouseY)) { 
                    got.client.gui.faction.overlays.CreateTitleOverlay overlay = (got.client.gui.faction.overlays.CreateTitleOverlay) gui.getActiveRenderer();
                    String name = overlay.titleNameField.getText().trim();
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(name) && !name.contains("!")) {
                        brain.factions.Faction faction = brain.factions.network.PacketMessage.getCurrentFaction(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
                        if (faction == null || !faction.getTitles().containsKey(name)) {
                            advance(); advance(); 
                        }
                    }
                    return true; 
                }
            }
            if (isHoveringAnyOtherButton(gui, 17, mouseX, mouseY)) { return false; }
            return true; // Let them type
        } else if (p == 15) {
            if (gui.getCurrentPage() == got.client.gui.faction.GOTGuiFactions.Page.PLAYER_LIST && gui.getCurrentOverlay() == got.client.gui.faction.GOTGuiFactions.Overlay.NONE) {
                if (isButtonHovered(gui, 17, mouseX, mouseY)) { advance(); return true; }
            }
            return false;
        } else if (p == 16) {
            if (gui.getCurrentPage() != got.client.gui.faction.GOTGuiFactions.Page.TITLE_HIERARCHY) {
                if (isButtonHovered(gui, 17, mouseX, mouseY)) { return true; } // Allow recovery
                return false;
            }
            GuiButton btnUp = findButton(gui, 151);
            GuiButton btnDown = findButton(gui, 152);
            if (btnUp != null && btnUp.visible && gui.isHover(btnUp.xPosition, btnUp.yPosition, btnUp.width, btnUp.height, mouseX, mouseY)) {
                if (button == 0) { advance(); }
                return true; 
            }
            if (btnDown != null && btnDown.visible && gui.isHover(btnDown.xPosition, btnDown.yPosition, btnDown.width, btnDown.height, mouseX, mouseY)) {
                if (button == 0) { advance(); }
                return true; 
            }
            int listX = 60, listY = 50, listWidth = gui.getBaseWidth() - 120, listHeight = 12 * 22;
            if (gui.isHover(listX, listY, listWidth, listHeight, mouseX, mouseY)) {
                return true; // allow interacting with the list
            }
            return false; // block everything else
        } else if (p == 17) {
            GuiButton btn = findButton(gui, 150);
            if (btn != null && btn.visible && gui.isHover(btn.xPosition, btn.yPosition, btn.width, btn.height, mouseX, mouseY)) {
                if (button == 0) { advance(); }
                return true; 
            }
            return false;
        } else if (p == 18) {
            if (gui.getPage(GOTGuiFactions.Page.PLAYER_LIST) instanceof got.client.gui.faction.pages.PlayerListPage) {
                got.client.gui.faction.pages.PlayerListPage page = (got.client.gui.faction.pages.PlayerListPage) gui.getPage(GOTGuiFactions.Page.PLAYER_LIST);
                int[] bounds = page.getPlayerRowBounds(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
                if (bounds != null && gui.isHover(bounds[0], bounds[1], bounds[2], bounds[3], mouseX, mouseY)) {
                    return true;
                }
            }
            return false;
        } else if (p == 19) {
            GuiButton btn = findButton(gui, 101); // Context menu set title button
            if (btn != null && btn.visible && gui.isHover(btn.xPosition, btn.yPosition, btn.width, btn.height, mouseX, mouseY)) {
                if (button == 0) { advance(); }
                return true;
            }
            if (gui.isHover(15, 50, gui.getBaseWidth() - 30, gui.getBaseHeight() - 70, mouseX, mouseY)) {
                return true; // allow re-opening the context menu
            }
            return false;
        } else if (p == 20) {
            int width = 220, height = 180;
            int overlayX = gui.getBaseWidth() / 2, overlayY = gui.getBaseHeight() / 2;
            int left = overlayX - width / 2, top = overlayY - height / 2;
            if (gui.isHover(left, top, width, height, mouseX, mouseY)) {
                return true; 
            }
            return false;
        } else if (p == 21) {
            if (isButtonHovered(gui, 0, mouseX, mouseY)) { advance(); return true; }
            if (isButtonHovered(gui, 1, mouseX, mouseY)) {
                if (gui.getCurrentPage() == GOTGuiFactions.Page.POLITICS) { advance(); }
                return true;
            }
            return false;
        } else if (p == 22) {
            if (gui.getCurrentView() == GOTGuiFactions.View.LIST) { return true; }
            if (isButtonHovered(gui, 22, mouseX, mouseY)) { advance(); return true; }
            return false;
        } else if (p == 23) {
            if (isButtonHovered(gui, 11, mouseX, mouseY)) { advance(); return true; }
            return false;
        } else if (p == 24) {
            if (isButtonHovered(gui, 12, mouseX, mouseY)) { advance(); return true; }
            if (gui.getCurrentOverlay() != GOTGuiFactions.Overlay.CREATE_COLLECTION) {
                if (isButtonHovered(gui, 11, mouseX, mouseY)) return true;
            }
            if (isHoveringAnyOtherButton(gui, 12, mouseX, mouseY)) { return false; }
            int ox = gui.getBaseWidth() / 2, oy = gui.getBaseHeight() / 2;
            if (gui.isHover(ox - 150, oy - 75, 300, 150, mouseX, mouseY)) { return true; }
            return false;
        } else if (p == 25) {
            int width = 350, height = 250;
            int x = (gui.getBaseWidth() - width) / 2, y = (gui.getBaseHeight() - height) / 2;
            int listX = x + 15, listY = y + 45, listWidth = width - 30, listHeight = height - 60;
            if (gui.isHover(listX, listY, listWidth, listHeight, mouseX, mouseY)) {
                if (button == 0) { advance(); }
                return true;
            }
            return false;
        } else if (p == 26) {
            if (isButtonHovered(gui, 201, mouseX, mouseY)) { advance(); return true; }
            return false;
        } else if (p == 27) {
            if (isButtonHovered(gui, 19, mouseX, mouseY)) { advance(); return true; }
            if (gui.getCurrentOverlay() != GOTGuiFactions.Overlay.TREASURY_ACTION) {
                if (isButtonHovered(gui, 201, mouseX, mouseY)) return true;
            }
            if (isHoveringAnyOtherButton(gui, 19, mouseX, mouseY)) { return false; }
            int ox = gui.getBaseWidth() / 2, oy = gui.getBaseHeight() / 2;
            if (gui.isHover(ox - 125, oy - 50, 250, 100, mouseX, mouseY)) { return true; }
            return false;
        } else if (p == 28) {
            int width = 400, height = 300;
            int ox = gui.getBaseWidth() / 2, oy = gui.getBaseHeight() / 2;
            int left = ox - width / 2, top = oy - height / 2;
            if (gui.isHover(left, top, width, height, mouseX, mouseY)) {
                return true; 
            }
            return false;
        } else if (p == 29) {
            if (button == 0) { advance(); }
            return false;
        } else if (p == 30) {
            // return false блокирует ВСЕ кнопки (включая кнопку Дипломатии ID 11)
            // advance на ЛКМ — точно как p==29 (кнопки Дом/Телепорт)
            if (button == 0) { advance(); }
            return false;
        } else if (p == 31) {
            // Нажать Военный совет (ID 50)
            if (isButtonHovered(gui, 50, mouseX, mouseY)) { advance(); return true; }
            return false;
        } else if (p == 32) {
            // Внутри совета — инфопанель, ЛКМ = advance
            if (button == 0) { advance(); }
            return false;
        } else if (p == 33) {
            // авто-advance в drawHighlight, клик ничего не делает
            return false;
        } else if (p == 34) {
            // авто-advance + закрытие в drawHighlight
            return false;
        }

        return true;
    }

    // ========== KEY HANDLING ==========

    public static boolean handleKeyTyped(GOTGuiFactions gui, char c, int key) {
        int p = TutorialClientState.tutorialProgress;
        if (p == 28 && key == 1) {
            gui.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
            advance();
            return false; // Consume ESC so it doesn't close the entire GUI!
        }
        if ((p == 32 || p == 33 || p == 34) && key == 1) {
            // На последних шагах фракций ESC не закрывает GUI, drawHighlight сам закроет
            return false;
        }
        
        if (key == 1) {
            return false; // Block all other ESC presses!
        }
        return true;
    }

    // ========== UTILITIES ==========

    private static GuiButton findButton(GOTGuiFactions gui, int id) {
        for (Object obj : gui.getButtonList()) {
            if (obj instanceof GuiButton) {
                GuiButton btn = (GuiButton) obj;
                if (btn.id == id) return btn;
            }
        }
        return null;
    }

    private static boolean isButtonHovered(GOTGuiFactions gui, int id, int mouseX, int mouseY) {
        GuiButton btn = findButton(gui, id);
        if (btn == null || !btn.visible || !btn.enabled) return false;
        return mouseX >= btn.xPosition && mouseY >= btn.yPosition
                && mouseX < btn.xPosition + btn.width && mouseY < btn.yPosition + btn.height;
    }

    private static boolean isHoveringAnyOtherButton(GOTGuiFactions gui, int allowedId, int mouseX, int mouseY) {
        for (Object obj : gui.getButtonList()) {
            if (obj instanceof GuiButton) {
                GuiButton btn = (GuiButton) obj;
                if (btn.id != allowedId && btn.visible) {
                    if (mouseX >= btn.xPosition && mouseY >= btn.yPosition
                            && mouseX < btn.xPosition + btn.width && mouseY < btn.yPosition + btn.height) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    // Проверяет, что баннер HIGH_POWER реально виден в viewport списка фракций
    private static boolean isHighPowerVisible(got.client.gui.faction.FactionListRenderer listRenderer, GOTGuiFactions gui) {
        int[] bounds = listRenderer.getFactionBounds("HIGH_POWER");
        if (bounds == null) return false;
        int listX = 70;
        int listWidth = gui.getBaseWidth() - 140;
        // Баннер виден если хотя бы частично попадает в [listX, listX+listWidth]
        return bounds[0] + bounds[2] > listX && bounds[0] < listX + listWidth;
    }

    private static void advance() {
        got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
        TutorialClientState.tutorialProgress++;
    }
}
