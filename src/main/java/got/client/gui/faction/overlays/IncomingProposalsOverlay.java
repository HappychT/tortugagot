package got.client.gui.faction.overlays;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.client.gui.utils.GuiApi;
import got.client.utils.UtilO;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class IncomingProposalsOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private GuiButton buttonAccept, buttonDecline, buttonCancel;
    private float proposalListScroll = 0.0f;
    private List<Faction.Proposal> proposals = new ArrayList<>();
    private Faction.Proposal selectedProposal;

    public IncomingProposalsOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        buttonAccept = new GuiCustomButton(52, overlayX - 102, overlayY + 70, 100, 20, "Принять");
        buttonDecline = new GuiCustomButton(53, overlayX + 2, overlayY + 70, 100, 20, "Отклонить");
        buttonCancel = new GuiCustomButton(54, overlayX - 50, overlayY + 95, 100, 20, "Закрыть");

        buttonList.add(buttonAccept);
        buttonList.add(buttonDecline);
        buttonList.add(buttonCancel);

        Faction factionData = parent.getFactionData();
        if (factionData != null) {
            proposals = new ArrayList<>(factionData.getProposals().values());
        } else {
            proposals.clear();
        }
        proposalListScroll = 0.0f;
        this.selectedProposal = null;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        UtilO.drawRoundedRectangle(overlayX - 160, overlayY - 90, 320, 220, 7, new Color(10, 10, 10, 220).getRGB(), 0);
        parent.drawCenteredString("Входящие предложения", overlayX, overlayY - 80, 0xFFFFFF);

        buttonCancel.visible = true;
        buttonAccept.visible = buttonDecline.visible = (this.selectedProposal != null);

        if (!proposals.isEmpty()) {
            int listX = overlayX - 150;
            int listY = overlayY - 60;
            int listWidth = 300;
            int listHeight = 120;
            int totalContentHeight = proposals.size() * 22;

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GuiApi.glScissor(listX, listY, listWidth, listHeight, false);

            int scrollOffset = (totalContentHeight > listHeight) ? (int)(this.proposalListScroll * (totalContentHeight - listHeight)) : 0;

            for (int i = 0; i < proposals.size(); i++) {
                int drawY = listY + i * 22 - scrollOffset;
                if (drawY + 22 < listY || drawY > listY + listHeight) continue;

                Faction.Proposal proposal = proposals.get(i);
                int bgColor = 0;
                if (proposal == this.selectedProposal) {
                    bgColor = 0x80FFFFFF;
                } else if (parent.isHover(listX, drawY, listWidth, 20, scaledMouseX, scaledMouseY)) {
                    bgColor = 0x50FFFFFF;
                }
                if(bgColor != 0) Gui.drawRect(listX, drawY, listX + listWidth, drawY + 20, bgColor);

                String text = String.format("§e%s§f от §b%s§f. Цена: §6%d",
                        proposal.getRelationType().getDisplayName(), proposal.getFromFactionID(), proposal.getCost());
                parent.drawString(text, listX + 5, drawY + 6, 0xFFFFFF);
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

        } else {
            parent.drawCenteredString("§7Нет входящих предложений", overlayX, overlayY, 0xAAAAAA);
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (this.selectedProposal == null && (button == buttonAccept || button == buttonDecline)) return;

        if (button == buttonAccept) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.respondToProposal(this.selectedProposal.getFromFactionID(), true));
            this.selectedProposal = null;
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.POLITICS_INCOMING_PROPOSALS, true);
        } else if (button == buttonDecline) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.respondToProposal(this.selectedProposal.getFromFactionID(), false));
            this.selectedProposal = null;
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.POLITICS_INCOMING_PROPOSALS, true);
        } else if (button == buttonCancel) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        if (button == 0 && !proposals.isEmpty()) {
            int listX = parent.getBaseWidth() / 2 - 150;
            int listY = parent.getBaseHeight() / 2 - 60;
            int listWidth = 300;
            int listHeight = 120;

            if (parent.isHover(listX, listY, listWidth, listHeight, scaledMouseX, scaledMouseY)) {
                int totalContentHeight = proposals.size() * 22;
                int scrollOffset = (totalContentHeight > listHeight) ? (int)(proposalListScroll * (totalContentHeight - listHeight)) : 0;
                int index = (scaledMouseY - listY + scrollOffset) / 22;
                if (index >= 0 && index < proposals.size()) {
                    this.selectedProposal = proposals.get(index);
                }
            }
        }
    }

    @Override
    public void handleMouseInput() {
        int k = Mouse.getEventDWheel();
        if (k != 0) {
            int listHeight = 120;
            int totalContentHeight = proposals.size() * 22;
            if (totalContentHeight > listHeight) {
                float scrollAmount = (k > 0 ? -1 : 1) * 20.0f / (totalContentHeight - listHeight);
                proposalListScroll = MathHelper.clamp_float(proposalListScroll + scrollAmount, 0.0f, 1.0f);
            }
        }
    }

    @Override public void keyTyped(char c, int key) {}
    @Override public void update() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}