package got.client.gui.faction;

import got.client.gui.faction.pages.IPageRenderer;
import got.client.gui.utils.GuiApi;
import got.common.faction.GOTFaction;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FactionListRenderer implements IPageRenderer {

    private final GOTGuiFactions parent;
    private float currentScroll = 0.0F;
    private boolean isScrolling = false;

    private static final int BANNER_WIDTH = 80;
    private static final int BANNER_HEIGHT = 450;
    private static final int BANNER_GAP = 1;

    private GuiButton buttonFilterFactions;
    private GOTGuiFactions.FactionFilter currentFilter = GOTGuiFactions.FactionFilter.PLAYABLE;
    private List<GOTFaction> playableFactions = new ArrayList<>();
    private List<GOTFaction> otherFactions = new ArrayList<>();
    private List<GOTFaction> currentFactionList = new ArrayList<>();

    public FactionListRenderer(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        this.buttonFilterFactions = new GuiTexturedButton(4, 15, 15, 150, 36, "otherclans_button");
        if (currentFilter == GOTGuiFactions.FactionFilter.OTHER) {
            ((GuiTexturedButton)this.buttonFilterFactions).setTexture("clan_button");
            this.buttonFilterFactions.width = 100;
            this.buttonFilterFactions.height = 41;
        }
        buttonList.add(buttonFilterFactions);

        playableFactions.clear();
        otherFactions.clear();
        for (GOTFaction f : GOTFaction.values()) {
            if (f.isPlayableAlignmentFaction()) {
                if (GOTGuiFactions.PLAYABLE_FACTION_NAMES.contains(f.name())) {
                    playableFactions.add(f);
                } else {
                    otherFactions.add(f);
                }
            }
        }

        playableFactions.sort(Comparator.comparing(GOTFaction::factionName));
        otherFactions.sort(Comparator.comparing(GOTFaction::factionName));

        updateCurrentFactionList();
    }

    private void updateCurrentFactionList() {
        currentFactionList = (currentFilter == GOTGuiFactions.FactionFilter.PLAYABLE) ? playableFactions : otherFactions;
        if (GOTGuiFactions.currentFaction == null || !currentFactionList.contains(GOTGuiFactions.currentFaction)) {
            GOTGuiFactions.currentFaction = !currentFactionList.isEmpty() ? currentFactionList.get(0) : null;
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        buttonFilterFactions.visible = true;
        parent.mc.getTextureManager().bindTexture(new ResourceLocation("got", "textures/gui/faction/clansinfo_main_title.png"));
        int titleW = 200;
        int titleH = 34;
        parent.drawScaledCustomSizeModalRect(parent.getBaseWidth() / 2 - titleW/2, 10, 0, 0, 1292, 222, titleW, titleH, 1292.0F, 222.0F);

        // Координаты отрисовки
        int listX = 70;
        int listY = 50;
        int listWidth = parent.getBaseWidth() - 140; // Исправлено: 70 слева + 70 справа = 140
        int listHeight = parent.getBaseHeight() - 85;

        int totalContentWidth = currentFactionList.size() * (BANNER_WIDTH + BANNER_GAP) - BANNER_GAP;

        if (isScrolling) {
            if (Mouse.isButtonDown(0)) {
                int scrollBarWidth = listWidth;
                int scrollWidgetWidth = Math.max(20, (int) ((float) listWidth / (float) totalContentWidth * scrollBarWidth));
                float mousePos = scaledMouseX - listX - (scrollWidgetWidth / 2.0f);
                float maxScrollPos = scrollBarWidth - scrollWidgetWidth;
                this.currentScroll = MathHelper.clamp_float(mousePos / maxScrollPos, 0.0F, 1.0F);
            } else {
                isScrolling = false;
            }
        }

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(parent.getGuiLeft() + (int)(listX * parent.getScaleFactor()), parent.getGuiTop() + (int)(listY * parent.getScaleFactor()), (int)(listWidth * parent.getScaleFactor()), (int)(listHeight * parent.getScaleFactor()), true);

        int scrollOffsetX = 0;
        if (totalContentWidth > listWidth) {
            scrollOffsetX = (int) (this.currentScroll * (totalContentWidth - listWidth));
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < currentFactionList.size(); i++) {
            GOTFaction faction = currentFactionList.get(i);
            ResourceLocation factionTexture = new ResourceLocation("got", "textures/gui/factions/" + faction.codeName().toLowerCase() + ".png");

            int x = listX + i * (BANNER_WIDTH + BANNER_GAP) - scrollOffsetX;
            int y = listY + (listHeight - BANNER_HEIGHT) / 2;

            if (x + BANNER_WIDTH < listX || x > listX + listWidth) {
                continue;
            }

            parent.mc.getTextureManager().bindTexture(factionTexture);
            parent.drawScaledCustomSizeModalRect(x, y, 0, 0, BANNER_WIDTH, BANNER_HEIGHT, BANNER_WIDTH, BANNER_HEIGHT, (float)BANNER_WIDTH, (float)BANNER_HEIGHT);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (totalContentWidth > listWidth) {
            int scrollBarY = listY + listHeight + 5;
            int scrollBarWidth = listWidth;
            int scrollWidgetWidth = Math.max(20, (int) ((float) listWidth / (float) totalContentWidth * scrollBarWidth));

            Gui.drawRect(listX, scrollBarY, listX + scrollBarWidth, scrollBarY + 5, 0x80000000);
            int scrollWidgetX = listX + (int) (this.currentScroll * (scrollBarWidth - scrollWidgetWidth));
            Gui.drawRect(scrollWidgetX, scrollBarY, scrollWidgetX + scrollWidgetWidth, scrollBarY + 5, 0xFFC0C0C0);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        if (button == 0) {
            // ИСПРАВЛЕНО: Теперь клики проверяются в тех же координатах, где и рисуются!
            int listX = 70;
            int listY = 50;
            int listWidth = parent.getBaseWidth() - 140; // Аналогично исправлено
            int listHeight = parent.getBaseHeight() - 85;

            if (parent.isHover(listX, listY, listWidth, listHeight, scaledMouseX, scaledMouseY)) {
                int totalContentWidth = currentFactionList.size() * (BANNER_WIDTH + BANNER_GAP) - BANNER_GAP;
                int scrollOffsetX = (totalContentWidth > listWidth) ? (int) (this.currentScroll * (totalContentWidth - listWidth)) : 0;

                int effectiveMouseX = scaledMouseX - listX + scrollOffsetX;
                int index = effectiveMouseX / (BANNER_WIDTH + BANNER_GAP);

                if (index >= 0 && index < currentFactionList.size()) {
                    int x_start_banner = listX + index * (BANNER_WIDTH + BANNER_GAP) - scrollOffsetX;
                    if (scaledMouseX >= x_start_banner && scaledMouseX < x_start_banner + BANNER_WIDTH) {
                        parent.setCurrentFaction(currentFactionList.get(index), GOTGuiFactions.View.FACTION);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonFilterFactions) {
            currentFilter = currentFilter.toggle();
            if (buttonFilterFactions instanceof GuiTexturedButton) {
                GuiTexturedButton texBtn = (GuiTexturedButton) buttonFilterFactions;
                if (currentFilter == GOTGuiFactions.FactionFilter.PLAYABLE) {
                    texBtn.setTexture("otherclans_button");
                    texBtn.width = 150; texBtn.height = 36;
                } else {
                    texBtn.setTexture("clan_button");
                    texBtn.width = 100; texBtn.height = 41;
                }
            }
            updateCurrentFactionList();
        }
    }

    @Override
    public void handleMouseInput() {
        // ИСПРАВЛЕНО: Ширина списка для правильной работы колесика мыши
        int listWidth = parent.getBaseWidth() - 140;
        int totalContentWidth = currentFactionList.size() * (BANNER_WIDTH + BANNER_GAP) - BANNER_GAP;

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            if (totalContentWidth > listWidth) {
                float amount = (scroll > 0 ? -1 : 1) * 70.0f / (totalContentWidth - listWidth);
                this.currentScroll = MathHelper.clamp_float(this.currentScroll + amount, 0.0F, 1.0F);
            }
        }
    }

    @Override public void onOpened() { currentScroll = 0.0f; }
    @Override public void update() { }
    @Override public void keyTyped(char c, int key) { }
    @Override public void onGuiClosed() { }
    @Override public boolean isTextFieldFocused() { return false; }
}