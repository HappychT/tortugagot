package got.client.gui.faction;

import brain.factions.Faction;
import brain.factions.network.PacketInfoFactions;
import brain.factions.servers.CollectionGoal;
import got.client.gui.GOTGuiMenu;
import got.client.gui.GOTGuiMenuWBBase;
import got.client.gui.faction.overlays.*;
import got.client.gui.faction.pages.*;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

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
			List<Page> pages = new ArrayList<>(Arrays.asList(FRONT, POLITICS));
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
			} else if (pd.getPledgeFaction() != null && PLAYABLE_FACTION_NAMES.contains(pd.getPledgeFaction().codeName())) {
				currentFaction = pd.getPledgeFaction();
				this.currentView = View.FACTION;
			} else {
				this.currentView = View.LIST;
			}
			hasSetInitialView = true;
		}

		updateFactionDataCache();
		this.buttonList.clear();

		int bottomButtonY = this.baseHeight - 35;
		this.buttonBack = new GuiCustomButton(0, 15, bottomButtonY, 100, 20, "К списку фракций");
		this.buttonPagePrev = new GuiCustomButton(1, 120, bottomButtonY, 20, 20, "<");
		this.buttonPageNext = new GuiCustomButton(2, this.baseWidth - 145, bottomButtonY, 20, 20, ">");
		this.buttonOpenMenu = new GuiCustomButton(3, this.baseWidth / 2 - 75, bottomButtonY, 150, 20, "Главное меню");

		this.buttonList.add(buttonBack);
		this.buttonList.add(buttonPagePrev);
		this.buttonList.add(buttonPageNext);
		this.buttonList.add(buttonOpenMenu);

		getActiveRenderer().initGui(this.buttonList);
		if (getActiveRenderer() instanceof IPageRenderer) {
			((IPageRenderer) getActiveRenderer()).onOpened();
		}
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

		for (Object obj : this.buttonList) {
			((GuiButton)obj).drawButton(this.mc, scaledMouseX, scaledMouseY);
		}

		GL11.glPopMatrix();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		int scaledMouseX = (int)((mouseX - guiLeft) / scaleFactor);
		int scaledMouseY = (int)((mouseY - guiTop) / scaleFactor);

		getActiveRenderer().mouseClicked(mouseX, mouseY, scaledMouseX, scaledMouseY, button);

		if (button == 0) {
			for (Object obj : new ArrayList<>(this.buttonList)) {
				GuiButton guiButton = (GuiButton) obj;
				if (guiButton.mousePressed(this.mc, scaledMouseX, scaledMouseY)) {
					guiButton.func_146113_a(this.mc.getSoundHandler());
					this.actionPerformed(guiButton);
				}
			}
		}
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
		this.mc.getTextureManager().bindTexture(factionsTexture);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);

		drawScaledCustomSizeModalRect(x, y, 0, 0, 1920, 1080, width, height, 1920.0F, 1080.0F);

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private IGuiComponent getActiveRenderer() {
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

		if (button == buttonOpenMenu) {
			mc.displayGuiScreen(new GOTGuiMenu());
			return;
		}
		if (button == this.buttonBack) {
			if (isOtherPlayer) {
				mc.displayGuiScreen(null);
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

		getActiveRenderer().actionPerformed(button);
	}

	@Override
	public void keyTyped(char c, int key) {
		IGuiComponent renderer = getActiveRenderer();

		if (key == 1) { // ESC
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
			super.keyTyped(c, key);
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
	}

	private void updateFactionDataCache() {
		this.factionDataCache = (currentFaction != null) ? PacketInfoFactions.getFactions().get(currentFaction.codeName()) : null;
		this.playerStatus = calculatePlayerStatus();
	}

	public boolean isHover(int x, int y, int w, int h, int scaledMouseX, int scaledMouseY) {
		return scaledMouseX >= x && scaledMouseX < x + w && scaledMouseY >= y && scaledMouseY < y + h;
	}

	public void drawCenteredString(String text, int x, int y, int color) {
		this.getFontRenderer().drawString(text, x - this.getFontRenderer().getStringWidth(text) / 2, y, color);
	}

	public void drawString(String text, int x, int y, int color) {
		getFontRenderer().drawString(text, x, y, color);
	}

	public void drawTooltip(List<String> text, int x, int y) {
		func_146283_a(text, x, y);
	}

	private GroupStatus calculatePlayerStatus() {
		if (getFactionData() != null && mc.thePlayer != null) {
			String playerName = mc.thePlayer.getCommandSenderName();
			if (getFactionData().getLeaderName().equals(playerName)) return GroupStatus.OWNER;
			if (getFactionData().getAssistantName().equals(playerName)) return GroupStatus.CO_OWNER;
			if (getFactionData().getPlayers().containsKey(playerName)) return GroupStatus.PLAYER;
		}
		return GroupStatus.NONE;
	}

	public GroupStatus getPlayerStatus() { return playerStatus; }
	public Faction getFactionData() { return factionDataCache; }

	public boolean isPlayerMember() {
		return playerStatus == GroupStatus.PLAYER || playerStatus == GroupStatus.CO_OWNER || playerStatus == GroupStatus.OWNER;
	}
	public void refresh() {
		this.initGui();
	}
	public IPageRenderer getPage(Page type) {
		return pageRenderers.get(type);
	}

	public void drawButtonHoveringText(List<String> text, int x, int y) {
		this.func_146283_a(text, x, y);
	}

	public boolean isPlayerLeader() {
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