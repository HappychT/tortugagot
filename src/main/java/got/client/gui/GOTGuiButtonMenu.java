package got.client.gui;

import org.lwjgl.opengl.GL11;

import got.GOT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.WorldClient;

public class GOTGuiButtonMenu extends GuiButton {
	public GOTGuiMenu parentGUI;
	public Class<? extends GOTGuiMenuWBBase> menuScreenClass;
	public int menuKeyCode;

	public GOTGuiButtonMenu(GOTGuiMenu gui, int i, int x, int y, Class<? extends GOTGuiMenuWBBase> cls, String s, int key) {
		super(i, x, y, 32, 32, s);
		parentGUI = gui;
		menuScreenClass = cls;
		menuKeyCode = key;
	}

	public boolean canDisplayMenu() {
		if (menuScreenClass == GOTGuiMap.class) {
			WorldClient world = Minecraft.getMinecraft().theWorld;
			return world != null && world.getWorldInfo().getTerrainType() != GOT.worldTypeGOTClassic;
		}
		return true;
	}

	@Override
	public void drawButton(Minecraft mc, int i, int j) {
		if (visible) {
			mc.getTextureManager().bindTexture(GOTGuiMenu.menuIconsTexture);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			field_146123_n = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
			drawTexturedModalRect(xPosition, yPosition, 0 + (enabled ? 0 : width * 2) + (field_146123_n ? width : 0), id * height, width, height);
            
            if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
                int expected = brain.tutorial.client.TutorialClientState.getExpectedMenuButtonForProgress();
                if (expected == id) {
                    // Draw yellow pulsating border on the expected button
                    int alpha = (int)(Math.abs(Math.sin(System.currentTimeMillis() / 200.0)) * 255);
                    int borderColor = (alpha << 24) | 0xFFFF00; // Yellow
                    drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition, borderColor);
                    drawRect(xPosition - 1, yPosition + height, xPosition + width + 1, yPosition + height + 1, borderColor);
                    drawRect(xPosition - 1, yPosition, xPosition, yPosition + height, borderColor);
                    drawRect(xPosition + width, yPosition, xPosition + width + 1, yPosition + height, borderColor);
                    // Glow fill
                    int glowAlpha = (int)(Math.abs(Math.sin(System.currentTimeMillis() / 200.0)) * 40);
                    drawRect(xPosition, yPosition, xPosition + width, yPosition + height, (glowAlpha << 24) | 0xFFFF00);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                } else if (expected != -1) {
                    // Grey out non-target buttons with a dark overlay
                    drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xBB000000);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
            
			mouseDragged(mc, i, j);
		}
	}

	public GOTGuiMenuWBBase openMenu() {
		try {
			return menuScreenClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}