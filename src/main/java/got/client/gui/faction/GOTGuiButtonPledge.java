package got.client.gui.faction;

import java.util.*;
import org.lwjgl.opengl.GL11;
import got.client.GOTClientProxy;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GOTGuiButtonPledge extends GuiButton {
    public GOTGuiFactions parentGUI;
    public GOTFaction targetFaction;
    public boolean isPledgedToThisFaction;
    public boolean canPledge;
    public List<String> displayLines;
    private static final float ALIGNMENT_REQUIREMENT = 500.0f;
    private static final int ICON_WIDTH = 32;
    private static final int ICON_HEIGHT = 32;

    public GOTGuiButtonPledge(GOTGuiFactions gui, int i, int x, int y, GOTFaction faction) {
        super(i, x, y, ICON_WIDTH, ICON_HEIGHT, "");
        parentGUI = gui;
        targetFaction = faction;
        updatePledgeState();
    }

    public GOTGuiButtonPledge(GOTGuiFactions gui, int id, int x, int y, int width, int height, GOTFaction faction) {
        super(id, x, y, width, height, "");
        parentGUI = gui;
        targetFaction = faction;
        updatePledgeState();
    }


    public void updatePledgeState() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || targetFaction == null) {
            isPledgedToThisFaction = false;
            canPledge = false;
            enabled = false;
            displayLines = null;
            return;
        }
        GOTPlayerData pd = GOTLevelData.getData(mc.thePlayer);
        GOTFaction currentPledge = pd.getPledgeFaction();

        isPledgedToThisFaction = currentPledge == targetFaction;
        float alignment = pd.getAlignment(targetFaction);
        boolean hasEnoughAlignment = alignment >= ALIGNMENT_REQUIREMENT;

        canPledge = currentPledge == null;
        enabled = (canPledge && hasEnoughAlignment) || isPledgedToThisFaction;

        if (isPledgedToThisFaction) {
            setDisplayLines("§cПокинуть фракцию", "§7(" + targetFaction.factionName() + ")");
        } else if (canPledge) {
            if (hasEnoughAlignment) {
                setDisplayLines("§aВступить во фракцию", "§7(" + targetFaction.factionName() + ")");
            } else {
                setDisplayLines("§cНедостаточно репутации", "§7(Требуется: " + (int)ALIGNMENT_REQUIREMENT + ")");
            }
        } else {
            setDisplayLines("§cВы уже состоите", "§cв другой фракции");
            enabled = false;
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            mc.getTextureManager().bindTexture(GOTClientProxy.alignmentTexture);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

            field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;

            int u = 0;
            int v = 180;

            if (isPledgedToThisFaction) {
                u = field_146123_n ? 128 : 96;
            } else {
                if (enabled) {
                    u = field_146123_n ? 64 : 32;
                } else {
                    u = 0;
                }
            }

            drawTexturedModalRect(xPosition + (width - ICON_WIDTH) / 2, yPosition + (height - ICON_HEIGHT) / 2, u, v, ICON_WIDTH, ICON_HEIGHT);

            mouseDragged(mc, mouseX, mouseY);

            if (field_146123_n && displayLines != null && parentGUI != null) {
                float z = zLevel;
                parentGUI.drawHoveringTextPublic(displayLines, mouseX, mouseY);
                GL11.glDisable(2896);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                zLevel = z;
            }
        }
    }

    @Override
    public int getHoverState(boolean flag) {
        return !enabled ? 0 : (flag ? 2 : 1);
    }

    public void setDisplayLines(String... s) {
        displayLines = s == null ? null : Arrays.asList(s);
    }
}