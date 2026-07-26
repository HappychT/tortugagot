package got.client.gui;

import java.util.ArrayList;

import got.client.gui.faction.GOTGuiFactions;
import org.lwjgl.opengl.GL11;

import got.client.GOTTickHandlerClient;
import got.common.GOTDimension;
import got.common.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

public class GOTGuiMenu extends GOTGuiScreenBase {
	public static ResourceLocation menuIconsTexture = new ResourceLocation("got:textures/gui/menu_icons.png");
	public static Class<? extends GOTGuiMenuWBBase> lastMenuScreen = null;
	public boolean sentCheckPacket = false;

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 999) {
			got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialRestart());
			this.mc.displayGuiScreen(null);
			return;
		}
		
		GOTGuiMenuWBBase screen;
		if (button instanceof GOTGuiButtonMenu && (screen = ((GOTGuiButtonMenu) button).openMenu()) != null) {
			if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
                int p = brain.tutorial.client.TutorialClientState.tutorialProgress;
                
                if (p == 0 && button.id == 2) { // Achievements (Skip)
                    got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
                    brain.tutorial.client.TutorialClientState.tutorialProgress++;
                    return;
                } else if (p == 1 && button.id == 3) { // Map (Open)
                    got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
                    brain.tutorial.client.TutorialClientState.tutorialProgress++;
                } else if (p >= 4 && p <= 35 && button.id == 4) { // Factions (Open)
                    if (p == 4) {
                        got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
                        brain.tutorial.client.TutorialClientState.tutorialProgress++;
                    }
                } else if (p == 36 && button.id == 8) { // Languages (Skip)
                    got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
                    brain.tutorial.client.TutorialClientState.tutorialProgress++;
                    return;
                } else if (p == 37 && button.id == 6) { // Fellowships (Open)
                    got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
                    brain.tutorial.client.TutorialClientState.tutorialProgress++;
                } else if (p == 43 && button.id == 7) { // Titles (Skip)
                    got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
                    brain.tutorial.client.TutorialClientState.tutorialProgress++;
                    return;
                } else if (p == 44 && button.id == 5) { // Shields/Attributes (Skip)
                    got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
                    brain.tutorial.client.TutorialClientState.tutorialProgress++;
                    return;
                } else if (p == 45 && button.id == 1) { // Options (Skip) -> Finish Stage 3!
                    got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
                    brain.tutorial.client.TutorialClientState.tutorialProgress++;
                    return;
                } else {
                    return; // Block clicks on wrong buttons
                }
			}
			mc.displayGuiScreen(screen);
			lastMenuScreen = screen.getClass();
			return;
		}
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		String title = StatCollector.translateToLocalFormatted("got.gui.menu", GOTDimension.getCurrentDimension(mc.theWorld).getDimensionName());
		fontRendererObj.drawStringWithShadow(title, width / 2 - fontRendererObj.getStringWidth(title) / 2, height / 2 - 80, 16777215);
		super.drawScreen(i, j, f);
		for (Object obj : buttonList) {
			GOTGuiButtonMenu button;
			if (obj instanceof GOTGuiButtonMenu && (button = (GOTGuiButtonMenu) obj).func_146115_a() && button.displayString != null) {
				float z = zLevel;
				drawCreativeTabHoveringText(button.displayString, i, j);
				GL11.glDisable(2896);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				zLevel = z;
			}
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		GOTGuiMenu.resetLastMenuScreen();

		if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
			int p = brain.tutorial.client.TutorialClientState.tutorialProgress;
			// Handle return from submenus
			if (p == 3) { // Returned from Map
				got.common.network.GOTPacketHandler.networkWrapper.sendToServer(new brain.tutorial.network.GOTPacketTutorialAdvance());
				brain.tutorial.client.TutorialClientState.tutorialProgress++;
			} else if (p == 35) { // Returned from Factions
				// Do nothing, already advanced
			} else if (p == 42) { // Returned from Fellowships
				// Do nothing, already advanced
			}

			int targetBtnId = -1;
			if (p == 1) targetBtnId = 3;
			if (p == 4) targetBtnId = 4;
			if (p == 37) targetBtnId = 6;

			if (targetBtnId != -1) {
			}
		}

			int midX = width / 2;
			int midY = height / 2;
			buttonList.add(new GOTGuiButtonMenu(this, 2, 0, 0, GOTGuiAchievements.class, StatCollector.translateToLocal("got.gui.achievements"), 30));
			buttonList.add(new GOTGuiButtonMenu(this, 3, 0, 0, GOTGuiMap.class, StatCollector.translateToLocal("got.gui.map"), 50));
			buttonList.add(new GOTGuiButtonMenu(this, 4, 0, 0, GOTGuiFactions.class, StatCollector.translateToLocal("got.gui.factions"), 33));
			buttonList.add(new GOTGuiButtonMenu(this, 8, 0, 0, GOTGuiLanguages.class, StatCollector.translateToLocal("got.gui.languages"), 20));

			buttonList.add(new GOTGuiButtonMenu(this, 6, 0, 0, GOTGuiFellowships.class, StatCollector.translateToLocal("got.gui.fellowships"), 25));
			buttonList.add(new GOTGuiButtonMenu(this, 7, 0, 0, GOTGuiTitles.class, StatCollector.translateToLocal("got.gui.titles"), 20));
			buttonList.add(new GOTGuiButtonMenu(this, 5, 0, 0, GOTGuiShields.class, StatCollector.translateToLocal("got.gui.atribute"), 31));
			buttonList.add(new GOTGuiButtonMenu(this, 1, 0, 0, GOTGuiOptions.class, StatCollector.translateToLocal("got.gui.options"), 24));
			ArrayList<GOTGuiButtonMenu> menuButtons = new ArrayList<>();
			for (Object obj : buttonList) {
				if (obj instanceof GOTGuiButtonMenu) {
					GOTGuiButtonMenu button = (GOTGuiButtonMenu) obj;
					button.enabled = button.canDisplayMenu();
					menuButtons.add(button);
				}
			}
			int numButtons = menuButtons.size();
			int numTopRowButtons = (numButtons - 1) / 2 + 1;
			int numBtmRowButtons = numButtons - numTopRowButtons;
			int topRowLeft = midX - (numTopRowButtons * 32 + (numTopRowButtons - 1) * 10) / 2;
			int btmRowLeft = midX - (numBtmRowButtons * 32 + (numBtmRowButtons - 1) * 10) / 2;
			for (int l = 0; l < numButtons; ++l) {
				GOTGuiButtonMenu button2 = menuButtons.get(l);
				if (l < numTopRowButtons) {
					button2.xPosition = topRowLeft + l * 42;
					button2.yPosition = midY - 5 - 32;
				} else {
					button2.xPosition = btmRowLeft + (l - numTopRowButtons) * 42;
					button2.yPosition = midY + 5;
				}
			}
			
			
			buttonList.add(new net.minecraft.client.gui.GuiButton(999, width - 210, 10, 200, 20, "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0443\u0441\u0442\u0438\u0442\u044c \u043e\u0431\u0443\u0447\u0435\u043d\u0438\u0435"));
	}

	@Override
	public void keyTyped(char c, int i) {
		for (Object obj : buttonList) {
			if (obj instanceof GOTGuiButtonMenu) {
				GOTGuiButtonMenu button = (GOTGuiButtonMenu) obj;
				if (button.visible && button.enabled && button.menuKeyCode >= 0 && i == button.menuKeyCode) {
					actionPerformed(button);
					return;
				}
			}
		}
		super.keyTyped(c, i);
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int i, int j) {
		super.setWorldAndResolution(mc, i, j);
		if (mc.thePlayer != null) {
			GOTPacketCheckMenuPrompt packet;
			if (!sentCheckPacket) {
				GOTTickHandlerClient.renderMenuPrompt = false;
				packet = new GOTPacketCheckMenuPrompt(GOTPacketMenuPrompt.Type.MENU);
				GOTPacketHandler.networkWrapper.sendToServer(packet);
				sentCheckPacket = true;
			}
		}
	}

	public static GuiScreen openMenu(EntityPlayer entityplayer) {
		if (lastMenuScreen != null) {
			try {
				return lastMenuScreen.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new GOTGuiMenu();
	}

	public static void resetLastMenuScreen() {
		lastMenuScreen = null;
	}
}
