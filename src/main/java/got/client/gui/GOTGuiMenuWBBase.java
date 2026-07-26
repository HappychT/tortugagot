package got.client.gui;

import got.client.GOTKeyHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;

public abstract class GOTGuiMenuWBBase extends GOTGuiScreenBase {
	public static RenderItem renderItem = new RenderItem();
	public int xSize = 200;
	public int ySize = 256;
	public int guiLeft;
	public int guiTop;
	public GuiButton goBack;

	@Override
	public void actionPerformed(GuiButton button) {
        if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
            if (button.displayString != null && button.displayString.equals(net.minecraft.util.StatCollector.translateToLocal("got.gui.menuButton"))) {
                got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
                brain.tutorial.client.TutorialClientState.tutorialProgress++; // Optimistic update
            } else {
                return; // Block other clicks
            }
        }
        if (button == goBack || button.id == 0 || (button.displayString != null && button.displayString.equals(net.minecraft.util.StatCollector.translateToLocal("got.gui.menuButton")))) {
            mc.displayGuiScreen(new GOTGuiMenu());
        }
		super.actionPerformed(button);
	}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
            int p = brain.tutorial.client.TutorialClientState.tutorialProgress;
            boolean isMap = this instanceof got.client.gui.GOTGuiMap;
            boolean isFactions = this instanceof got.client.gui.faction.GOTGuiFactions;
            boolean isFellowships = this instanceof got.client.gui.GOTGuiFellowships;
            boolean expected = (isMap && p == 3) || (isFactions && p == 27) || (isFellowships && p == 30);
            
            if (expected) {
                for (Object obj : buttonList) {
                    GuiButton btn = (GuiButton) obj;
                    if (btn.displayString != null && btn.displayString.equals(net.minecraft.util.StatCollector.translateToLocal("got.gui.menuButton"))) {
                        int alpha = (int)(Math.abs(Math.sin(System.currentTimeMillis() / 200.0)) * 255);
                        int borderColor = (alpha << 24) | 0xFFFF00; // Pulsating Yellow
                        drawFloatRect(btn.xPosition, btn.yPosition, btn.xPosition + btn.width, btn.yPosition + 1, borderColor);
                        drawFloatRect(btn.xPosition, btn.yPosition + btn.height - 1, btn.xPosition + btn.width, btn.yPosition + btn.height, borderColor);
                        drawFloatRect(btn.xPosition, btn.yPosition, btn.xPosition + 1, btn.yPosition + btn.height, borderColor);
                        drawFloatRect(btn.xPosition + btn.width - 1, btn.yPosition, btn.xPosition + btn.width, btn.yPosition + btn.height, borderColor);
                        org.lwjgl.opengl.GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    }
                }
            }
        }
    }

	@Override
	public void initGui() {
		super.initGui();
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}

	@Override
	public void keyTyped(char c, int i) {
        if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
            return; // Block ESC and menu key exit
        }
		if (i == GOTKeyHandler.keyBindingMenu.getKeyCode()) {
			mc.displayGuiScreen(new GOTGuiMenu());
			return;
		}
		super.keyTyped(c, i);
	}
}
