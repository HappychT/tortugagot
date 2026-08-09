package got.client.gui.faction;

import brain.factions.Faction;
import brain.factions.network.PacketInfoFactions;
import brain.factions.network.PacketMessage;
import brain.factions.servers.CollectionGoal;
import brain.factions.servers.CoreFaction;
import got.client.gui.GOTGuiMenu;
import got.client.gui.GOTGuiMenuWBBase;
import got.client.gui.faction.overlays.*;
import got.client.gui.faction.pages.*;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.text.SimpleDateFormat;
import java.util.*;

import static got.client.gui.faction.GOTGuiFactions.Page.getAvailablePages;

public class GOTGuiFactions extends GOTGuiMenuWBBase {

	public enum View { LIST, FACTION, MAP }
	public enum Page {
		FRONT, POLITICS, APPLICATIONS, PLAYER_LIST, WAR_COUNCIL, TITLE_HIERARCHY;
		public Page next(boolean isMember) {
			List<Page> pages = getAvailablePages(isMember);
			int index = pages.indexOf(this);
			return (index != -1 && index < pages.size() - 1) ? pages.get(index + 1) : this;
		}
		public Page prev(boolean isMember) {
			List<Page> pages = getAvailablePages(isMember);
			int index = pages.indexOf(this);
			return (index > 0) ? pages.get(index - 1) : this;
		}
		static List<Page> getAvailablePages(boolean isMember) {
			List<Page> pages = new ArrayList<>(Arrays.asList(FRONT));
			if (isMember) {
				pages.add(PLAYER_LIST);
			}
			return pages;
		}
	}
	public enum GroupStatus { NONE, PLAYER, CO_OWNER, OWNER }
	public enum Overlay {
		NONE, CREATE_COLLECTION, CREATE_TITLE, ASSIGN_TITLE, TREASURY_ACTION, POLITICS_PROPOSE,
		POLITICS_INCOMING_PROPOSALS, ADD_BARRACKS_PLAYER, INCREASE_BARRACKS_CAPACITY, SELECT_GOAL,
		DEPOSIT_PROVISIONS, COLLECTIONS_LIST, TREASURY_DETAILS, CONFIRM_LEAVE
	}
	enum FactionFilter {
		PLAYABLE("Другие фракции"), OTHER("Игровые фракции");
		private final String buttonText;
		FactionFilter(String text) { this.buttonText = text; }
		public String getButtonText() { return this.buttonText; }
		public FactionFilter toggle() { return this == PLAYABLE ? OTHER : PLAYABLE; }
	}

	public static ResourceLocation factionsTexture = new ResourceLocation("got:textures/gui/faction.png");
	private ResourceLocation currentBackgroundTexture = new ResourceLocation("got", "textures/gui/faction/background_main.png");
	public static GOTFaction currentFaction;
	private View currentView = View.LIST;
	private Page currentPage = Page.FRONT;
	private Overlay currentOverlay = Overlay.NONE;
	private boolean isOtherPlayer = false;
	private GroupStatus playerStatus = GroupStatus.NONE;
	private Faction factionDataCache;

	private final Map<Page, IPageRenderer> pageRenderers = new HashMap<>();
	private final Map<Overlay, IOverlayRenderer> overlayRenderers = new HashMap<>();
	private final FactionListRenderer factionListRenderer;
	public final MapView mapView;

	public CollectionGoal selectedCollectionGoal;
	public String selectedPlayerName;
	public Object contextMenuObject = null;
	public int contextMenuX, contextMenuY;
	public String diplomacyActionType = "";

	private GuiButton buttonBack, buttonPageNext, buttonPagePrev, buttonOpenMenu;
	private GOTGuiButtonPledge buttonPledge;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	private boolean hasSetInitialView = false;

	private final int baseWidth = 853;
	private final int baseHeight = 480;
	private float scaleFactor;

	public static final List<String> PLAYABLE_FACTION_NAMES = Arrays.asList(
			"NORTH", "IRONBORN", "RIVERLANDS", "WESTERLANDS", "ARRYN",
			"REACH", "DRAGONSTONE", "STORMLANDS", "DORNE"
	);

	public GOTGuiFactions() {
		super();
		this.factionListRenderer = new FactionListRenderer(this);
		this.mapView = new MapView(this);

		pageRenderers.put(Page.FRONT, new FrontPage(this));
		pageRenderers.put(Page.POLITICS, new PoliticsPage(this));
		pageRenderers.put(Page.APPLICATIONS, new ApplicationsPage(this));
		pageRenderers.put(Page.PLAYER_LIST, new PlayerListPage(this));
		pageRenderers.put(Page.WAR_COUNCIL, new WarCouncilPage(this));
		pageRenderers.put(Page.TITLE_HIERARCHY, new TitleHierarchyPage(this));

		overlayRenderers.put(Overlay.CREATE_COLLECTION, new CreateCollectionOverlay(this));
		overlayRenderers.put(Overlay.CREATE_TITLE, new CreateTitleOverlay(this));
		overlayRenderers.put(Overlay.ASSIGN_TITLE, new AssignTitleOverlay(this));
		overlayRenderers.put(Overlay.TREASURY_ACTION, new TreasuryActionOverlay(this));
		overlayRenderers.put(Overlay.POLITICS_PROPOSE, new DiplomacyProposalOverlay(this));
		overlayRenderers.put(Overlay.POLITICS_INCOMING_PROPOSALS, new IncomingProposalsOverlay(this));
		overlayRenderers.put(Overlay.ADD_BARRACKS_PLAYER, new AddBarracksPlayerOverlay(this));
		overlayRenderers.put(Overlay.INCREASE_BARRACKS_CAPACITY, new IncreaseCapacityOverlay(this));
		overlayRenderers.put(Overlay.SELECT_GOAL, new SelectGoalOverlay(this));
		overlayRenderers.put(Overlay.DEPOSIT_PROVISIONS, new DepositProvisionsOverlay(this));
		overlayRenderers.put(Overlay.COLLECTIONS_LIST, new CollectionsListOverlay(this));
		overlayRenderers.put(Overlay.TREASURY_DETAILS, new TreasuryDetailsOverlay(this));
		overlayRenderers.put(Overlay.CONFIRM_LEAVE, new ConfirmLeaveOverlay(this));
	}

	@Override
	public void initGui() {
		super.initGui();
		this.xSize = (int)(this.width * 0.85f);
		this.ySize = (int)((float)this.xSize * ((float)baseHeight / (float)baseWidth));

		if(this.xSize > 1280) {
			this.xSize = 1280;
			this.ySize = 720;
		}

		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		this.scaleFactor = (float) this.xSize / (float) baseWidth;

		if (!hasSetInitialView) {
			GOTPlayerData pd = GOTLevelData.getData(this.mc.thePlayer);
			if (isOtherPlayer) {
				this.currentView = View.FACTION;
				this.currentPage = Page.FRONT;
			} else if (pd != null && pd.getPledgeFaction() != null && PLAYABLE_FACTION_NAMES.contains(pd.getPledgeFaction().codeName())) {
				currentFaction = pd.getPledgeFaction();
				this.currentView = View.FACTION;
			} else {
				this.currentView = View.LIST;
			}
			hasSetInitialView = true;
		}

		updateFactionDataCache();
		updateBackgroundTexture();

		this.buttonList.clear();

		int btnW = 170;
		int btnH = 39;
		int bottomButtonY = this.baseHeight - btnH - 15;

		this.buttonBack = new GuiTexturedButton(0, 60, bottomButtonY, btnW, btnH, "clanslist_button");
		this.buttonPagePrev = new GuiTexturedButton(1, 250, bottomButtonY - 3, 45, 45, "back_button");
		this.buttonPageNext = new GuiTexturedButton(2, this.baseWidth - 300, bottomButtonY - 3, 45, 45, "forward_button");
		this.buttonOpenMenu = new GuiTexturedButton(3, this.baseWidth / 2 - btnW/2, bottomButtonY, btnW, btnH, "main_button");

		this.buttonList.add(buttonBack);
		this.buttonList.add(buttonPagePrev);
		this.buttonList.add(buttonPageNext);
		this.buttonList.add(buttonOpenMenu);


		getActiveRenderer().initGui(this.buttonList);
		if (getActiveRenderer() instanceof IPageRenderer) {
			((IPageRenderer) getActiveRenderer()).onOpened();
		}

		if (this.buttonPledge != null) {
			this.buttonPledge.updatePledgeState();
		}
	}
	private void updateBackgroundTexture() {
		if (this.currentView == View.LIST) {
			this.currentBackgroundTexture = new ResourceLocation("got", "textures/gui/faction/clansinfo_menu2.png");
		} else if (this.currentView == View.FACTION) {
			this.currentBackgroundTexture = getFactionBackground(currentFaction);
		} else {
			this.currentBackgroundTexture = new ResourceLocation("got", "textures/gui/faction/background_main.png");
		}
	}

	public static ResourceLocation getFactionBackground(GOTFaction faction) {
		if (faction == null) return new ResourceLocation("got", "textures/gui/faction/background_main.png");
		String codeName = faction.codeName().toLowerCase().replace(" ", "_");
		
		if (codeName.equals("martell")) codeName = "dorne";
		if (codeName.equals("nights_watch") || codeName.equals("nights watch")) codeName = "night_watch";
		if (codeName.equals("wildling")) codeName = "wilding";
		if (codeName.equals("high_power")) codeName = "tortuga";
		
		return new ResourceLocation("got", "textures/gui/faction/fac/" + codeName + ".png");
	}

	public static ResourceLocation getFactionBanner(GOTFaction faction) {
		if (faction == null) return new ResourceLocation("got", "textures/gui/faction/banner_bg.png");
		String codeName = faction.codeName().toLowerCase().replace(" ", "_");
		
		if (codeName.equals("martell") || codeName.equals("dorne")) return new ResourceLocation("got", "textures/gui/faction/fac/banner_martell_replaced.png");
		if (codeName.equals("night_watch") || codeName.equals("nights watch")) codeName = "nights_watch";
		if (codeName.equals("wilding")) codeName = "wildling";
		if (codeName.equals("high_power")) codeName = "tortuga";
		
		return new ResourceLocation("got", "textures/gui/faction/fac/banner_" + codeName + ".png");
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawWorldBackground(0);

		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft, guiTop, 0.0F);
		GL11.glScalef(scaleFactor, scaleFactor, 1.0F);

		int scaledMouseX = (int)((mouseX - guiLeft) / scaleFactor);
		int scaledMouseY = (int)((mouseY - guiTop) / scaleFactor);

		IGuiComponent activeRenderer = getActiveRenderer();
		IGuiComponent backgroundRenderer = (currentOverlay != Overlay.NONE) ? getActiveRendererWithoutOverlay() : activeRenderer;

		for(Object b : this.buttonList) {
			if (b instanceof GuiButton) ((GuiButton)b).visible = false;
		}

		drawPanel(0, 0, baseWidth, baseHeight);
		backgroundRenderer.drawScreen(mouseX, mouseY, scaledMouseX, scaledMouseY, partialTicks);

		if (currentOverlay != Overlay.NONE) {
			for(Object b : this.buttonList) {
				if (b instanceof GuiButton) ((GuiButton)b).visible = false;
			}
			drawRect(0, 0, baseWidth, baseHeight, 0x90000000);
			activeRenderer.drawScreen(mouseX, mouseY, scaledMouseX, scaledMouseY, partialTicks);
		}

		if (activeRenderer instanceof PlayerListPage) {
			((PlayerListPage)activeRenderer).drawPlayerContextMenuAfterButtons(mouseX, mouseY);
		}
		if (activeRenderer instanceof MapView) {
			((MapView)activeRenderer).drawDiplomacyContextMenuAfterButtons(mouseX, mouseY);
		}

		buttonBack.visible = (this.currentView != View.LIST) || isOtherPlayer;
		buttonPagePrev.visible = (this.currentView == View.FACTION && getAvailablePages(isPlayerMember()).size() > 1);
		buttonPageNext.visible = (this.currentView == View.FACTION && getAvailablePages(isPlayerMember()).size() > 1);
		buttonOpenMenu.visible = true;
		if (buttonPledge != null) {
			buttonPledge.visible = true;
		}

		for (Object obj : this.buttonList) {
			((GuiButton)obj).drawButton(this.mc, scaledMouseX, scaledMouseY);
		}

        if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
            brain.tutorial.client.TutorialGuiFactionsHighlight.drawHighlight(this, scaledMouseX, scaledMouseY);
        }

		GL11.glPopMatrix();

		if (activeRenderer instanceof MapView) {
			((MapView)activeRenderer).drawTooltips(mouseX, mouseY, scaledMouseX, scaledMouseY);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		int scaledMouseX = (int)((mouseX - guiLeft) / scaleFactor);
		int scaledMouseY = (int)((mouseY - guiTop) / scaleFactor);

        if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
            if (!brain.tutorial.client.TutorialGuiFactionsHighlight.handleMouseClick(this, scaledMouseX, scaledMouseY, button)) {
                return; // Blocked by tutorial
            }
        }

		// 1. СНАЧАЛА проверяем клики по кнопкам!
		if (button == 0) {
			for (Object obj : new ArrayList<>(this.buttonList)) {
				GuiButton guiButton = (GuiButton) obj;

				if (guiButton.mousePressed(this.mc, scaledMouseX, scaledMouseY)) {
					guiButton.func_146113_a(this.mc.getSoundHandler());
					this.actionPerformed(guiButton);
					return;
				}
			}
		}

		getActiveRenderer().mouseClicked(mouseX, mouseY, scaledMouseX, scaledMouseY, button);
	}

	public void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float textureWidth, float textureHeight) {
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)x, (double)(y + height), (double)this.zLevel, (double)(u * f), (double)((v + (float)vHeight) * f1));
		tessellator.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1));
		tessellator.addVertexWithUV((double)(x + width), (double)y, (double)this.zLevel, (double)((u + (float)uWidth) * f), (double)(v * f1));
		tessellator.addVertexWithUV((double)x, (double)y, (double)this.zLevel, (double)(u * f), (double)(v * f1));
		tessellator.draw();
	}

	public void drawPanel(int x, int y, int width, int height) {
		this.mc.getTextureManager().bindTexture(this.currentBackgroundTexture);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
		drawScaledCustomSizeModalRect(x, y, 0, 0, 1920, 1080, width, height, 1920.0F, 1080.0F);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public IGuiComponent getActiveRenderer() {
		if (currentOverlay != Overlay.NONE && overlayRenderers.containsKey(currentOverlay)) {
			return overlayRenderers.get(currentOverlay);
		}
		if (currentView == View.FACTION && pageRenderers.containsKey(currentPage)) {
			return pageRenderers.get(currentPage);
		}
		if (currentView == View.LIST) {
			return factionListRenderer;
		}
		if (currentView == View.MAP) {
			return mapView;
		}
		return pageRenderers.get(Page.FRONT);
	}

	public IOverlayRenderer getOverlay(Overlay type) {
		return overlayRenderers.get(type);
	}

	private IGuiComponent getActiveRendererWithoutOverlay() {
		if (currentView == View.FACTION) return pageRenderers.getOrDefault(currentPage, pageRenderers.get(Page.FRONT));
		if (currentView == View.LIST) return factionListRenderer;
		if (currentView == View.MAP) return mapView;
		return pageRenderers.get(Page.FRONT);
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (!button.enabled) return;

		if (brain.tutorial.client.TutorialClientState.isTutorialActive) {
			if (!brain.tutorial.client.TutorialGuiFactionsHighlight.isButtonAllowed(button.id)) {
				return;
			}
		}

		if (button == buttonOpenMenu) {
			mc.displayGuiScreen(new GOTGuiMenu());
			return;
		}
		if (button == this.buttonBack) {
			if (isOtherPlayer) {
				mc.displayGuiScreen(null);
			} else if (this.currentView == View.FACTION && this.currentPage != Page.FRONT) {
				setCurrentPage(Page.FRONT);
			} else if (this.currentView != View.LIST) {
				setCurrentView(View.LIST);
			}
			return;
		}
		if (button == buttonPagePrev) {
			setCurrentPage(currentPage.prev(isPlayerMember()));
			return;
		}
		if (button == buttonPageNext) {
			setCurrentPage(currentPage.next(isPlayerMember()));
			return;
		}

		if (button instanceof GOTGuiButtonPledge) {
			GOTGuiButtonPledge pledgeButton = (GOTGuiButtonPledge) button;
			if (pledgeButton.enabled) {
				if (pledgeButton.isPledgedToThisFaction) {
					// Покинуть фракцию — через overlay подтверждения
					setCurrentOverlay(Overlay.CONFIRM_LEAVE, true);
				} else {
					boolean isTutorial = brain.tutorial.client.TutorialClientState.isTutorialActive
							&& brain.tutorial.client.TutorialClientState.tutorialStage == 3;
					if (isTutorial) {
						// В туториале — прямое вступление (без системы заявок, сервер сам добавит в players)
						brain.factions.servers.CoreFaction.brainChannel.sendToServer(
							new brain.factions.network.PacketMessage("tutorialJoin#" + pledgeButton.targetFaction.codeName())
						);
					} else {
						// В обычном режиме — подача заявки через brain-систему
						brain.factions.servers.CoreFaction.brainChannel.sendToServer(
							new brain.factions.network.PacketMessage("sendApplication#" + pledgeButton.targetFaction.codeName())
						);
					}
					pledgeButton.updatePledgeState();
				}
			}
			return;
		}

		getActiveRenderer().actionPerformed(button);
	}

	@Override
	public void keyTyped(char c, int key) {
        if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
            if (!brain.tutorial.client.TutorialGuiFactionsHighlight.handleKeyTyped(this, c, key)) {
                return; // Blocked by tutorial
            }
        }
		IGuiComponent renderer = getActiveRenderer();

		if (key == 1) {
			if (currentOverlay != Overlay.NONE) {
				if (currentOverlay == Overlay.TREASURY_DETAILS) {
					setCurrentOverlay(Overlay.COLLECTIONS_LIST, true);
				} else {
					setCurrentOverlay(Overlay.NONE, true);
				}
				return;
			}
			if (contextMenuObject != null) {
				contextMenuObject = null;
				return;
			}
		}

		renderer.keyTyped(c, key);

		if (!renderer.isTextFieldFocused()) {
			if (key == 1 || key == this.mc.gameSettings.keyBindInventory.getKeyCode())
			{
				this.mc.thePlayer.closeScreen();
			}
		}
	}


	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		getActiveRenderer().handleMouseInput();
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		getActiveRenderer().update();
		if (buttonPledge != null) {
			buttonPledge.updatePledgeState();
		}
	}

	private void updateFactionDataCache() {
		this.factionDataCache = (currentFaction != null) ? PacketInfoFactions.getFactions().get(currentFaction.codeName()) : null;
		this.playerStatus = calculatePlayerStatus();
	}

	public boolean isHover(int x, int y, int w, int h, int scaledMouseX, int scaledMouseY) {
		return scaledMouseX >= x && scaledMouseX < x + w && scaledMouseY >= y && scaledMouseY < y + h;
	}

	public void drawCenteredString(String text, int x, int y, int color) {
		float scale = 1.3f;
		GL11.glPushMatrix();
		int scaledX = (int) (x / scale);
		int scaledY = (int) (y / scale);
		GL11.glScalef(scale, scale, 1.0f);
		this.getFontRenderer().drawString(text, scaledX - this.getFontRenderer().getStringWidth(text) / 2, scaledY, color);
		GL11.glPopMatrix();
	}

	public void drawString(String text, int x, int y, int color) {
		float scale = 1.3f;

		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, 1.0f);

		int scaledX = (int) (x / scale);
		int scaledY = (int) (y / scale);

		getFontRenderer().drawString(text, scaledX, scaledY, color);

		GL11.glPopMatrix();
	}


	public void drawTooltip(List<String> text, int x, int y) {
		drawHoveringTextPublic(text, x, y);
	}

	public void drawHoveringTextPublic(List<String> textLines, int x, int y) {
		if (!textLines.isEmpty()) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int tooltipTextWidth = 0;

			for (String s : textLines) {
				int textWidth = this.fontRendererObj.getStringWidth(s);
				if (textWidth > tooltipTextWidth) {
					tooltipTextWidth = textWidth;
				}
			}

			int screenX = x + 12;
			int screenY = y - 12;
			int tooltipHeight = 8;

			if (textLines.size() > 1) {
				tooltipHeight += 2 + (textLines.size() - 1) * 10;
			}

			if (screenX + tooltipTextWidth + 4 > this.width) {
				screenX -= 28 + tooltipTextWidth;
			}

			if (screenY + tooltipHeight + 6 > this.height) {
				screenY = this.height - tooltipHeight - 6;
			}
			if (screenY < 0) {
				screenY = 0;
			}

			this.zLevel = 300.0F;
			int backgroundColor = -267386864;
			this.drawGradientRect(screenX - 3, screenY - 4, screenX + tooltipTextWidth + 3, screenY - 3, backgroundColor, backgroundColor);
			this.drawGradientRect(screenX - 3, screenY + tooltipHeight + 3, screenX + tooltipTextWidth + 3, screenY + tooltipHeight + 4, backgroundColor, backgroundColor);
			this.drawGradientRect(screenX - 3, screenY - 3, screenX + tooltipTextWidth + 3, screenY + tooltipHeight + 3, backgroundColor, backgroundColor);
			this.drawGradientRect(screenX - 4, screenY - 3, screenX - 3, screenY + tooltipHeight + 3, backgroundColor, backgroundColor);
			this.drawGradientRect(screenX + tooltipTextWidth + 3, screenY - 3, screenX + tooltipTextWidth + 4, screenY + tooltipHeight + 3, backgroundColor, backgroundColor);
			int borderColorStart = 1347420415;
			int borderColorEnd = (borderColorStart & 16711422) >> 1 | borderColorStart & -16777216;
			this.drawGradientRect(screenX - 3, screenY - 3 + 1, screenX - 3 + 1, screenY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
			this.drawGradientRect(screenX + tooltipTextWidth + 2, screenY - 3 + 1, screenX + tooltipTextWidth + 3, screenY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
			this.drawGradientRect(screenX - 3, screenY - 3, screenX + tooltipTextWidth + 3, screenY - 3 + 1, borderColorStart, borderColorStart);
			this.drawGradientRect(screenX - 3, screenY + tooltipHeight + 2, screenX + tooltipTextWidth + 3, screenY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

			GL11.glPushMatrix();
			GL11.glTranslatef(0, 0, 300);

			for (int i = 0; i < textLines.size(); ++i) {
				String line = textLines.get(i);
				this.fontRendererObj.drawStringWithShadow(line, screenX, screenY, -1);
				if (i == 0) {
					screenY += 2;
				}
				screenY += 10;
			}
			GL11.glPopMatrix();

			this.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}

	private GroupStatus calculatePlayerStatus() {
		if (getFactionData() != null && mc.thePlayer != null) {
			String playerName = mc.thePlayer.getCommandSenderName();
			if (playerName == null) return GroupStatus.NONE;
			Faction factionData = getFactionData();
			if (factionData.getLeaderName() != null && factionData.getLeaderName().equals(playerName)) return GroupStatus.OWNER;
			if (factionData.getAssistantName() != null && factionData.getAssistantName().equals(playerName)) return GroupStatus.CO_OWNER;
			if (factionData.getPlayers() != null && factionData.getPlayers().containsKey(playerName)) return GroupStatus.PLAYER;
		}
		return GroupStatus.NONE;
	}

	public GroupStatus getPlayerStatus() { 
        if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) {
            return GroupStatus.OWNER;
        }
        return playerStatus; 
    }
	public Faction getFactionData() { return factionDataCache; }

	public boolean isPlayerMember() {
        if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) return true;
		return playerStatus == GroupStatus.PLAYER || playerStatus == GroupStatus.CO_OWNER || playerStatus == GroupStatus.OWNER;
	}
	public void refresh() {
		this.initGui();
	}
	public IPageRenderer getPage(Page type) {
		return pageRenderers.get(type);
	}

	public void drawButtonHoveringText(List<String> text, int x, int y) {
		drawHoveringTextPublic(text, x, y);
	}

	public boolean isPlayerLeader() {
        if (brain.tutorial.client.TutorialClientState.isTutorialActive && brain.tutorial.client.TutorialClientState.tutorialStage == 3) return true;
		return playerStatus == GroupStatus.CO_OWNER || playerStatus == GroupStatus.OWNER;
	}

	public void setOtherPlayer(GOTFaction faction) {
		this.isOtherPlayer = true;
		setCurrentFaction(faction, View.FACTION);
	}

	public void setCurrentView(View view) {
		this.currentView = view;
		this.currentPage = Page.FRONT;
		initGui();
	}

	public void setCurrentPage(Page page) {
		this.currentPage = page;
		initGui();
	}

	public void setCurrentOverlay(Overlay overlay, boolean reinit) {
		this.currentOverlay = overlay;
		if(reinit) {
			initGui();
		}
	}

	public void setCurrentFaction(GOTFaction faction, View view) {
		currentFaction = faction;
		this.currentView = view;
		updateFactionDataCache();
		initGui();
	}

	public View getCurrentView() {
		return currentView;
	}

	public Page getCurrentPage() {
		return currentPage;
	}

	public Overlay getCurrentOverlay() {
		return currentOverlay;
	}

	public float getScaleFactor() {
		return this.scaleFactor;
	}

	public int getBaseWidth() { return this.baseWidth; }
	public int getBaseHeight() { return this.baseHeight; }
	public int getGuiLeft() { return this.guiLeft; }
	public int getGuiTop() { return this.guiTop; }
	public int getXSize() { return this.xSize; }
	public int getYSize() { return this.ySize; }
	public SimpleDateFormat getDateFormat() { return this.dateFormat; }

	public net.minecraft.client.gui.FontRenderer getFontRenderer() { return this.fontRendererObj; }

	public List<GuiButton> getButtonList() { return this.buttonList; }
}