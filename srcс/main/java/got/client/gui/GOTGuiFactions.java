package got.client.gui;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.math.IntMath;

import brain.factions.CoreFaction;
import brain.factions.Faction;
import brain.factions.network.PacketInfoFactions;
import brain.factions.network.PacketMessage;
import got.GOT;
import got.client.*;
import got.client.gui.GOTGuiFactions.GroupStatus;
import got.client.gui.playerlist.PlayerLayer;
import got.client.gui.utils.GuiApi;
import got.client.gui.utils.GuiScrollingList;
import got.common.*;
import got.common.faction.*;
import got.common.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.common.UsernameCache;

public class GOTGuiFactions extends GOTGuiMenuWBBase {
	public static ResourceLocation factionsTexture = new ResourceLocation("got:textures/gui/factions.png");
	public static ResourceLocation factionsTextureFull = new ResourceLocation("got:textures/gui/factions_full.png");
	public static GOTDimension currentDimension;
	public static GOTDimension prevDimension;
	public static GOTDimension.DimensionRegion currentRegion;
	public static GOTDimension.DimensionRegion prevRegion;
	public static List<GOTFaction> currentFactionList;
	public static int maxAlignmentsDisplayed = 1;
	public static Page currentPage = Page.FRONT;
	public static int maxDisplayedAlliesEnemies = 10;
	public int currentFactionIndex = 0;
	public int prevFactionIndex = 0;
	public GOTFaction currentFaction;
	public int pageY = 46;
	public int pageWidth = 256;
	public int pageHeight = 128;
	public int pageBorderLeft = 16;
	public int pageBorderTop = 12;
	public int pageMapX = 159;
	public int pageMapY = 22;
	public int pageMapSize = 80;
	public GOTGuiMap mapDrawGui;
	public GuiButton buttonRegions;
	public GuiButton buttonPagePrev;
	public GuiButton buttonPageNext;
	public GuiButton buttonFactionMap;
	public GOTGuiButtonPledge buttonPledge;
	public GOTGuiButtonPledge buttonPledgeConfirm;
	public GOTGuiButtonPledge buttonPledgeRevoke;
	public float currentScroll;
	public boolean isScrolling;
	public boolean wasMouseDown;
	public int scrollBarWidth;
	public int scrollBarHeight;
	public int scrollBarX;
	public int scrollBarY;
	public int scrollBarBorder;
	public int scrollWidgetWidth;
	public int scrollWidgetHeight;
	public GOTGuiScrollPane scrollPaneAlliesEnemies;
	public int scrollAlliesEnemiesX;
	public int numDisplayedAlliesEnemies;
	public List currentAlliesEnemies;
	public boolean isOtherPlayer;
	public String otherPlayerName;
	public Map<GOTFaction, Float> playerAlignmentMap;
	public boolean isPledging;
	public boolean isUnpledging;
	
	public static GuiScrollingList<PlayerLayer> playerList;
	public static GuiScrollingList<PlayerLayer> applicationList;
	
	public static GroupStatus status = GroupStatus.Player;
	public static boolean isEdit;
	public static int MX;
	public static int MY;
	protected static boolean isClicked;
	public static GuiTextField prefixField;
	
	public GOTGuiFactions() {
		xSize = pageWidth;
		currentScroll = 0.0f;
		isScrolling = false;
		scrollBarWidth = 240;
		scrollBarHeight = 14;
		scrollBarX = xSize / 2 - scrollBarWidth / 2;
		scrollBarY = 180;
		scrollBarBorder = 1;
		scrollWidgetWidth = 17;
		scrollWidgetHeight = 12;
		scrollPaneAlliesEnemies = new GOTGuiScrollPane(7, 7).setColors(5521198, 8019267);
		scrollAlliesEnemiesX = 138;
		isOtherPlayer = false;
		isPledging = false;
		isUnpledging = false;
		mapDrawGui = new GOTGuiMap();
		
		playerList = new GuiScrollingList<>(0);
		applicationList = new GuiScrollingList<>(0);
		isEdit = false;
	}
	

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button == buttonRegions) {
				
				List<GOTDimension.DimensionRegion> regionList = GOTGuiFactions.currentDimension.dimensionRegions;
				if (!regionList.isEmpty()) {
					int i = regionList.indexOf(currentRegion);
					++i;
					i = IntMath.mod(i, regionList.size());
					currentRegion = regionList.get(i);
					updateCurrentDimensionAndFaction();
					setCurrentScrollFromFaction();
					scrollPaneAlliesEnemies.resetScroll();
					isPledging = false;
					isUnpledging = false;
				}
			} else if (button.enabled && button == goBack) {
				mc.displayGuiScreen(new GOTGuiMenu());
			} else if (button == buttonPagePrev) {
				Page newPage = currentPage.prev();
				if (newPage != null) {
					currentPage = newPage;
					scrollPaneAlliesEnemies.resetScroll();
					isPledging = false;
					isUnpledging = false;
				}
			} else if (button == buttonPageNext) {
				Page newPage = currentPage.next();
				if (newPage != null) {
					currentPage = newPage;
					scrollPaneAlliesEnemies.resetScroll();
					isPledging = false;
					isUnpledging = false;
				}
			} else if (button == buttonFactionMap) {
				GOTGuiMap factionGuiMap = new GOTGuiMap();
				factionGuiMap.setControlZone(currentFaction);
				mc.displayGuiScreen(factionGuiMap);
			} else if (button == buttonPledge) {
				if (GOTLevelData.getData(mc.thePlayer).isPledgedTo(currentFaction)) {
					isUnpledging = true;
				} else {
					isPledging = true;
				}
			} else if (button == buttonPledgeConfirm) {
				GOTPacketPledgeSet packet = new GOTPacketPledgeSet(currentFaction);
			//	GOTPacketHandler.networkWrapper.sendToServer(packet);
				EntityClientPlayerMP entityplayer = Minecraft.getMinecraft().thePlayer;
				GOTPlayerData pd = GOTLevelData.getData(entityplayer);
				GOTFaction fac = packet.pledgeFac;
				if (fac == null) {
					GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("quet"));

				} else if (pd.canPledgeTo(fac) && pd.canMakeNewPledge()) {
					GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("sendApplication#" + fac.codeName()));
				}
				isPledging = false;
			} else if (button == buttonPledgeRevoke) {
				GOTPacketPledgeSet packet = new GOTPacketPledgeSet(null);
				GOTPacketHandler.networkWrapper.sendToServer(packet);
				EntityClientPlayerMP entityplayer = Minecraft.getMinecraft().thePlayer;
				GOTPlayerData pd = GOTLevelData.getData(entityplayer);
				GOTFaction fac = packet.pledgeFac;
				if (fac == null) {
					GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("quet"));

				} else if (pd.canPledgeTo(fac) && pd.canMakeNewPledge()) {
					GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("sendApplication#" + fac.codeName()));
				}
				isUnpledging = false;
				mc.displayGuiScreen(null);
			} else {
				super.actionPerformed(button);
			}
		}
	}

	public boolean canScroll() {
		return true;
	}

	public void drawButtonHoveringText(List list, int i, int j) {
		func_146283_a(list, i, j);
	}
	
	public boolean isApplication() {
		for(PlayerLayer f : applicationList.getElements()) {
			if(f.getPlayerName().equals(mc.thePlayer.getDisplayName())) {
				return true;
			}
		}
		return false;
	}
	@Override
	public void drawScreen(int i, int j, float f) {
		MX = i;
		MY = j;
		List desc;
		int stringWidth;
		GOTPlayerData clientPD = GOTLevelData.getData(mc.thePlayer);
	
		boolean mouseOverWarCrimes = false;
		if (!isPledging && !isUnpledging) {
			buttonPagePrev.enabled = currentPage.prev() != null;
			buttonPageNext.enabled = currentPage.next() != null;
			buttonFactionMap.enabled = currentPage != Page.RANKS &&  currentPage != Page.Playerlist && currentFaction.isPlayableAlignmentFaction() && GOTDimension.getCurrentDimension(mc.theWorld) == currentFaction.factionDimension;
			buttonFactionMap.visible = buttonFactionMap.enabled;
			if (!GOTFaction.controlZonesEnabled(mc.theWorld)) {
				buttonFactionMap.enabled = false;
				buttonFactionMap.visible = false;
			}
			if (!isOtherPlayer && currentPage == Page.FRONT) {
				if (clientPD.isPledgedTo(currentFaction)) {
					buttonPledge.isBroken = buttonPledge.func_146115_a();
					buttonPledge.enabled = true;
					buttonPledge.visible = true;
			//		buttonPledge.setDisplayLines(StatCollector.translateToLocal("got.gui.factions.unpledge"));
					buttonPledge.setDisplayLines("Покинуть фракцию");
				} else {
					buttonPledge.isBroken = false;
					buttonPledge.visible = clientPD.getPledgeFaction() == null && currentFaction.isPlayableAlignmentFaction() && clientPD.getAlignment(currentFaction) >= 0.0f;
					buttonPledge.enabled = buttonPledge.visible && clientPD.hasPledgeAlignment(currentFaction);
					
					if(!isApplication()) {
					//	buttonPledge.enabled = true;
						String desc1 = StatCollector.translateToLocal("got.gui.factions.pledge");
						String desc2 = StatCollector.translateToLocalFormatted("got.gui.factions.pledgeReq", GOTAlignmentValues.formatAlignForDisplay(currentFaction.getPledgeAlignment()));
						buttonPledge.setDisplayLines("Подать заявку", desc2);
					} else {
						buttonPledge.enabled = false;
						GL11.glTranslated(0, 0, 100);
						GL11.glColor4f(1, 1, 1, 1);
						fontRendererObj.drawString("Заявка отправлена", guiLeft +50, guiTop + pageHeight/2+80, new Color(220, 220, 220, 150).getRGB());
						GL11.glTranslated(0, 0, -100);	
					}
				}
			} else {
				buttonPledge.enabled = false;
				buttonPledge.visible = false;
			}
			buttonPledgeConfirm.enabled = false;
			buttonPledgeConfirm.visible = false;
			buttonPledgeRevoke.enabled = false;
			buttonPledgeRevoke.visible = false;
		} else {
			buttonPagePrev.enabled = false;
			buttonPageNext.enabled = false;
			buttonFactionMap.enabled = false;
			buttonFactionMap.visible = false;
			buttonPledge.enabled = false;
			buttonPledge.visible = false;
			if (isPledging) {
				buttonPledgeConfirm.visible = true;
				buttonPledgeConfirm.enabled = clientPD.canMakeNewPledge() && clientPD.canPledgeTo(currentFaction);
				buttonPledgeConfirm.setDisplayLines("Подать заявку");
				buttonPledgeRevoke.enabled = false;
				buttonPledgeRevoke.visible = false;
			} else if (isUnpledging) {
				buttonPledgeConfirm.enabled = false;
				buttonPledgeConfirm.visible = false;
				buttonPledgeRevoke.enabled = true;
				buttonPledgeRevoke.visible = true;
				buttonPledgeRevoke.setDisplayLines(StatCollector.translateToLocal("got.gui.factions.unpledge"));
			}
		}
		setupScrollBar(i, j);
		drawDefaultBackground();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		if (useFullPageTexture()) {
			mc.getTextureManager().bindTexture(factionsTextureFull);
		} else {
			mc.getTextureManager().bindTexture(factionsTexture);
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.drawTexturedModalRect(guiLeft, guiTop + pageY, 0, 0, pageWidth, pageHeight);
		if (currentRegion != null && GOTGuiFactions.currentDimension.dimensionRegions.size() > 1) {
			buttonRegions.displayString = currentRegion.getRegionName();
			buttonRegions.enabled = true;
			buttonRegions.visible = true;
		} else {
			buttonRegions.displayString = "";
			buttonRegions.enabled = false;
			buttonRegions.visible = false;
		}
		if (currentFaction != null) {
			float alignment = isOtherPlayer && playerAlignmentMap != null ? playerAlignmentMap.get(currentFaction) : clientPD.getAlignment(currentFaction);
			int x = guiLeft + xSize / 2;
			int y = guiTop;
			GOTTickHandlerClient.renderAlignmentBar(alignment, isOtherPlayer, currentFaction, x, y, true, false, true, true);
			String s = currentFaction.factionSubtitle();
			this.drawCenteredString(s, x, y += fontRendererObj.FONT_HEIGHT + 22, 16777215);
			if (!useFullPageTexture() && currentPage != Page.Playerlist) {
				if (currentFaction.factionMapInfo != null) {
					GOTMapRegion mapInfo = currentFaction.factionMapInfo;
					int mapX = mapInfo.mapX;
					int mapY = mapInfo.mapY;
					int mapR = mapInfo.radius;
					int xMin = guiLeft + pageMapX;
					int xMax = xMin + pageMapSize;
					int yMin = guiTop + pageY + pageMapY;
					int yMax = yMin + pageMapSize;
					int mapBorder = 1;
					Gui.drawRect(xMin - mapBorder, yMin - mapBorder, xMax + mapBorder, yMax + mapBorder, -16777216);
					float zoom = (float) pageMapSize / (float) (mapR * 2);
					float zoomExp = (float) Math.log(zoom) / (float) Math.log(2.0);
					mapDrawGui.setFakeMapProperties(mapX, mapY, zoom, zoomExp, zoom);
					int[] statics = GOTGuiMap.setFakeStaticProperties(pageMapSize, pageMapSize, xMin, xMax, yMin, yMax);
					mapDrawGui.enableZoomOutWPFading = false;
					boolean sepia = GOTConfig.enableSepiaMap;
					mapDrawGui.renderMapAndOverlay(sepia, 1.0f, true);
					GOTGuiMap.setFakeStaticProperties(statics[0], statics[1], statics[2], statics[3], statics[4], statics[5]);
				}
				int wcX = guiLeft + pageMapX + 3;
				int wcY = guiTop + pageY + pageMapY + pageMapSize + 5;
				int wcWidth = 8;
				mc.getTextureManager().bindTexture(factionsTexture);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				if (currentFaction.approvesWarCrimes) {
					this.drawTexturedModalRect(wcX, wcY, 33, 142, wcWidth, wcWidth);
				} else {
					this.drawTexturedModalRect(wcX, wcY, 41, 142, wcWidth, wcWidth);
				}
				if (i >= wcX && i < wcX + wcWidth && j >= wcY && j < wcY + wcWidth) {
					mouseOverWarCrimes = true;
				}
			}
			x = guiLeft + pageBorderLeft;
			y = guiTop + pageY + pageBorderTop;
		
			if (!isPledging && !isUnpledging) {
				int index;
				switch (currentPage) {
				case ALLIES:
				case ENEMIES:
					int avgBgColor = GOTTextures.computeAverageFactionPageColor(factionsTexture, 20, 20, 120, 80);
					int[] minMax = scrollPaneAlliesEnemies.getMinMaxIndices(currentAlliesEnemies, numDisplayedAlliesEnemies);
					for (index = minMax[0]; index <= minMax[1]; ++index) {
						Object listObj = currentAlliesEnemies.get(index);
						if (listObj instanceof GOTFactionRelations.Relation) {
							GOTFactionRelations.Relation rel = (GOTFactionRelations.Relation) listObj;
							s = StatCollector.translateToLocalFormatted("got.gui.factions.relationHeader", rel.getDisplayName());
							fontRendererObj.drawString(s, x, y, 8019267);
						} else if (listObj instanceof GOTFaction) {
							GOTFaction fac = (GOTFaction) listObj;
							s = StatCollector.translateToLocalFormatted("got.gui.factions.list", fac.factionName());
							fontRendererObj.drawString(s, x, y, GOTTextures.findContrastingColor(fac.getFactionColor(), avgBgColor));
						}
						y += fontRendererObj.FONT_HEIGHT;
					}
					break;
				case FRONT:
					if (isOtherPlayer) {
						s = StatCollector.translateToLocalFormatted("got.gui.factions.pageOther", otherPlayerName);
						fontRendererObj.drawString(s, x, y, 8019267);
						y += fontRendererObj.FONT_HEIGHT * 2;
					}
					String alignmentInfo = StatCollector.translateToLocal("got.gui.factions.alignment");
					fontRendererObj.drawString(alignmentInfo, x, y, 8019267);
					String alignmentString = GOTAlignmentValues.formatAlignForDisplay(alignment);
					GOTTickHandlerClient.drawAlignmentText(fontRendererObj, x += fontRendererObj.getStringWidth(alignmentInfo) + 5, y, alignmentString, 1.0f);

					x = guiLeft + pageBorderLeft;
					GOTFactionRank curRank = currentFaction.getRank(alignment);
					String rankName = curRank.getFullNameWithGender(clientPD);
					fontRendererObj.drawString(rankName, x, y += fontRendererObj.FONT_HEIGHT, 8019267);
					y += fontRendererObj.FONT_HEIGHT * 2;
					if (!isOtherPlayer) {
						GOTFactionData factionData = clientPD.getFactionData(currentFaction);
						if (alignment >= 0.0f) {
							float conq;
							s = StatCollector.translateToLocalFormatted("got.gui.factions.data.enemiesKilled", factionData.getEnemiesKilled());
							fontRendererObj.drawString(s, x, y, 8019267);
							s = StatCollector.translateToLocalFormatted("got.gui.factions.data.trades", factionData.getTradeCount());
							fontRendererObj.drawString(s, x, y += fontRendererObj.FONT_HEIGHT, 8019267);
							s = StatCollector.translateToLocalFormatted("got.gui.factions.data.hires", factionData.getHireCount());
							fontRendererObj.drawString(s, x, y += fontRendererObj.FONT_HEIGHT, 8019267);
							s = StatCollector.translateToLocalFormatted("got.gui.factions.data.miniquests", factionData.getMiniQuestsCompleted());
							fontRendererObj.drawString(s, x, y += fontRendererObj.FONT_HEIGHT, 8019267);
							y += fontRendererObj.FONT_HEIGHT;
							if (clientPD.isPledgedTo(currentFaction) && (conq = factionData.getConquestEarned()) != 0.0f) {
								int conqInt = Math.round(conq);
								s = StatCollector.translateToLocalFormatted("got.gui.factions.data.conquest", conqInt);
								fontRendererObj.drawString(s, x, y, 8019267);
								y += fontRendererObj.FONT_HEIGHT;
							}
						}
						if (alignment <= 0.0f) {
							s = StatCollector.translateToLocalFormatted("got.gui.factions.data.npcsKilled", factionData.getNPCsKilled());
							fontRendererObj.drawString(s, x, y, 8019267);
							y += fontRendererObj.FONT_HEIGHT;
						}
						if (buttonPledge.visible && clientPD.isPledgedTo(currentFaction)) {
							s = StatCollector.translateToLocal("got.gui.factions.pledged");
							int px = buttonPledge.xPosition + buttonPledge.width + 8;
							int py = buttonPledge.yPosition + buttonPledge.height / 2 - fontRendererObj.FONT_HEIGHT / 2;
							fontRendererObj.drawString("Вы в этой фракции", px, py, 16711680);
							
							GuiApi.drawRect( width/2, buttonPledge.yPosition + 15, 15, 15, (isHover( width/2, buttonPledge.yPosition + 15, 15, 15)) ?new Color(0, 0, 0, 30).getRGB() :new Color(0, 0, 0, 40).getRGB() );
							GL11.glColor4f(1, 1, 1, 1);
							GuiApi.drawTexturedQuadFitBLEND(new ResourceLocation("got", "textures/icons/home.png"), width/2, buttonPledge.yPosition + 15, 15, 15);
							if (isHover(width / 2, buttonPledge.yPosition + 15, 15, 15)) {
								GOTTickHandlerClient.drawAlignmentText(fontRendererObj, MX+10, MY+10, "Телепортация домой", 1.0f);
								if(isClicked(width / 2, buttonPledge.yPosition + 15, 15, 15)) {
									CoreFaction.brainChannel.sendToServer(new PacketMessage("home"));
									Minecraft.getMinecraft().displayGuiScreen(null);
									setClicked(false);
								}
							}
							if(status != GroupStatus.Player) {
								GuiApi.drawRect( buttonPledge.xPosition+buttonPledge.width + 8, buttonPledge.yPosition + 21, 64, 10, (isHover( buttonPledge.xPosition+buttonPledge.width + 8, buttonPledge.yPosition + 21, 64, 10)) ?new Color(0, 0, 0, 30).getRGB() :new Color(0, 0, 0, 40).getRGB() );
								GL11.glColor4f(1, 1, 1, 1);
								GuiApi.drawScaleText("Установить точку дома фракции", buttonPledge.xPosition+buttonPledge.width + 11, buttonPledge.yPosition + 24, 0.5f, true, 8019267);
								if(isClicked( buttonPledge.xPosition+buttonPledge.width + 8, buttonPledge.yPosition + 21, 64, 10)) {
									CoreFaction.brainChannel.sendToServer(new PacketMessage("setHome"));
									setClicked(false);
								}
							}
						}
						
					}
					break;
				case RANKS:
					GOTFactionRank curRank1 = currentFaction.getRank(clientPD);
					int[] minMax1 = scrollPaneAlliesEnemies.getMinMaxIndices(currentAlliesEnemies, numDisplayedAlliesEnemies);
					for (index = minMax1[0]; index <= minMax1[1]; ++index) {
						Object listObj = currentAlliesEnemies.get(index);
						if (listObj instanceof String) {
							s = (String) listObj;
							fontRendererObj.drawString(s, x, y, 8019267);
						} else if (listObj instanceof GOTFactionRank) {
							GOTFactionRank rank = (GOTFactionRank) listObj;
							String rankName1 = rank.getShortNameWithGender(clientPD);
							String rankAlign = GOTAlignmentValues.formatAlignForDisplay(rank.alignment);
							if (rank == GOTFactionRank.RANK_ENEMY) {
								rankAlign = "-";
							}
							boolean hiddenRankName = false;
							if (!clientPD.isPledgedTo(currentFaction) && rank.alignment > currentFaction.getPledgeAlignment() && rank.alignment > currentFaction.getRankAbove(curRank1).alignment) {
								hiddenRankName = true;
							}
							if (hiddenRankName) {
								rankName1 = StatCollector.translateToLocal("got.gui.factions.rank?");
							}
							s = StatCollector.translateToLocalFormatted("got.gui.factions.listRank", rankName1, rankAlign);
							if (rank == curRank1) {
								GOTTickHandlerClient.drawAlignmentText(fontRendererObj, x, y, s, 1.0f);
							} else {
								fontRendererObj.drawString(s, x, y, 8019267);
							}
						}
						y += fontRendererObj.FONT_HEIGHT;
					}
					break;
					
					
				case Playerlist:
					if (status != GroupStatus.Player) {
						fontRendererObj.drawString("Заяки [" + applicationList.getList().size()+"]" ,x + pageWidth/2 + 40, y, 8019267);
						applicationList.drawScreen(MX, MY, j);
					}
					fontRendererObj.drawString("Участники " + playerList.getList().size() ,x , y, 8019267);
					playerList.drawScreen(MX, MY, f);
				
					GL11.glPushMatrix();
					GL11.glEnable(GL11.GL_SCISSOR_TEST);
					GuiApi.glScissor(x, y+10, width*4, pageHeight - 40, false);
					for (int k = 0; k < playerList.getList().size() ; k++) {
						playerList.getList().get(k).draw(this, x, y + 10 +10*k - playerList.getScrollOffset(), 115, 10);
						if(isClicked( x, y + 10 +10*k - playerList.getScrollOffset(), 115, 10) && !prefixField.getVisible()) {
							prefixEditName = playerList.getElement(k).getPlayerName();
							prefixField.setText(getPrefix(PacketInfoFactions.getFactions().get(currentFaction.codeName()), playerList.getElement(k).getPlayerName()));
							prefixField.setVisible(true);
							setClicked(false);
						}
					}
					renderPrefixSet();
					if (status != GroupStatus.Player ) {
						for (int k = 0; k < applicationList.getList().size() ; k++) {
							applicationList.getList().get(k).drawApplicationPlayer(this, x + pageWidth/2 + 15, y + 10 +10*k - applicationList.getScrollOffset(), 115, 5);
						}
					}
					// Draw Hover List
					for (int k = 0; k < playerList.getList().size() ; k++) {
						playerList.getList().get(k).drawHover(this, x, y + 10 +10*k - playerList.getScrollOffset(), 115, 5);
					}
					GL11.glDisable(GL11.GL_SCISSOR_TEST);
					// Draw Edit Button
			
					if (status != GroupStatus.Player) {
						GuiApi.drawRect(x + pageWidth / 2 + 16, y + pageHeight - 28, 6, 6,(isEdit) ? new Color(255, 255, 255, 180).getRGB(): new Color(255, 255, 255, 80).getRGB());
						GL11.glColor4f(1, 1, 1, 1);
						GuiApi.drawTexturedQuadFitBLEND(new ResourceLocation("got", "textures/icons/edit.png"), x + pageWidth / 2 + 16, y + pageHeight - 28, 6, 6);
						if (isHover(x + pageWidth / 2 + 16, y + pageHeight - 28, 6, 6)) {
							GOTTickHandlerClient.drawAlignmentText(fontRendererObj, MX, MY, "Редактировать", 1.0f);
							if (isClicked(x + pageWidth / 2 + 16, y + pageHeight - 28, 6,6)) {
								isEdit = (isEdit == false ? true : false);
								setClicked(false);
							}
						}
					}
					
					GL11.glPopMatrix();
				
					break;
			
				}
				if (scrollPaneAlliesEnemies.hasScrollBar) {
					scrollPaneAlliesEnemies.drawScrollBar();
				}
			} else {
				int stringWidth2 = pageWidth - pageBorderLeft * 2;
				ArrayList<String> displayLines = new ArrayList<>();
				if (isPledging) {
					if (clientPD.canMakeNewPledge()) {
						if (clientPD.canPledgeTo(currentFaction)) {
							String desc2 = StatCollector.translateToLocalFormatted("got.gui.factions.pledgeDesc1", currentFaction.factionName());
							displayLines.addAll(fontRendererObj.listFormattedStringToWidth(desc2, stringWidth2));
							displayLines.add("");
							desc2 = StatCollector.translateToLocalFormatted("got.gui.factions.pledgeDesc2");
							displayLines.addAll(fontRendererObj.listFormattedStringToWidth(desc2, stringWidth2));
						}
					} else {
						GOTFaction brokenPledge = clientPD.getBrokenPledgeFaction();
						String brokenPledgeName = brokenPledge == null ? StatCollector.translateToLocal("got.gui.factions.pledgeUnknown") : brokenPledge.factionName();
						String desc3 = StatCollector.translateToLocalFormatted("got.gui.factions.pledgeBreakCooldown", currentFaction.factionName(), brokenPledgeName);
						displayLines.addAll(fontRendererObj.listFormattedStringToWidth(desc3, stringWidth2));
						displayLines.add("");
						GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
						mc.getTextureManager().bindTexture(factionsTexture);
						this.drawTexturedModalRect(guiLeft + pageWidth / 2 - 97, guiTop + pageY + 56, 0, 240, 194, 16);
						float cdFrac = (float) clientPD.getPledgeBreakCooldown() / (float) clientPD.getPledgeBreakCooldownStart();
						this.drawTexturedModalRect(guiLeft + pageWidth / 2 - 75, guiTop + pageY + 60, 22, 232, MathHelper.ceiling_float_int(cdFrac * 150.0f), 8);
					}
				} else if (isUnpledging) {
					String desc5 = StatCollector.translateToLocalFormatted("got.gui.factions.unpledgeDesc1", currentFaction.factionName());
					displayLines.addAll(fontRendererObj.listFormattedStringToWidth(desc5, stringWidth2));
					displayLines.add("");
					desc5 = StatCollector.translateToLocalFormatted("got.gui.factions.unpledgeDesc2");
					displayLines.addAll(fontRendererObj.listFormattedStringToWidth(desc5, stringWidth2));
				}
				for (String line : displayLines) {
					fontRendererObj.drawString(line, x, y, 8019267);
					y += mc.fontRenderer.FONT_HEIGHT;
				}
			}
		}
		if (hasScrollBar()) {
			mc.getTextureManager().bindTexture(factionsTexture);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			this.drawTexturedModalRect(guiLeft + scrollBarX, guiTop + scrollBarY, 0, 128, scrollBarWidth, scrollBarHeight);
			int factions = currentFactionList.size();
			for (int index = 0; index < factions; ++index) {
				GOTFaction faction = currentFactionList.get(index);
				float[] factionColors = faction.getFactionRGB();
				float shade = 0.6f;
				GL11.glColor4f(factionColors[0] * shade, factionColors[1] * shade, factionColors[2] * shade, 1.0f);
				float xMin = (float) index / (float) factions;
				float xMax = (float) (index + 1) / (float) factions;
				xMin = guiLeft + scrollBarX + scrollBarBorder + xMin * (scrollBarWidth - scrollBarBorder * 2);
				xMax = guiLeft + scrollBarX + scrollBarBorder + xMax * (scrollBarWidth - scrollBarBorder * 2);
				float yMin = guiTop + scrollBarY + scrollBarBorder;
				float yMax = guiTop + scrollBarY + scrollBarHeight - scrollBarBorder;
				float minU = (0 + scrollBarBorder) / 256.0f;
				float maxU = (0 + scrollBarWidth - scrollBarBorder) / 256.0f;
				float minV = (128 + scrollBarBorder) / 256.0f;
				float maxV = (128 + scrollBarHeight - scrollBarBorder) / 256.0f;
				Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(xMin, yMax, zLevel, minU, maxV);
				tessellator.addVertexWithUV(xMax, yMax, zLevel, maxU, maxV);
				tessellator.addVertexWithUV(xMax, yMin, zLevel, maxU, minV);
				tessellator.addVertexWithUV(xMin, yMin, zLevel, minU, minV);
				tessellator.draw();
			}
			mc.getTextureManager().bindTexture(factionsTexture);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			if (canScroll()) {
				int scroll = (int) (currentScroll * (scrollBarWidth - scrollBarBorder * 2 - scrollWidgetWidth));
				this.drawTexturedModalRect(guiLeft + scrollBarX + scrollBarBorder + scroll, guiTop + scrollBarY + scrollBarBorder, 0, 142, scrollWidgetWidth, scrollWidgetHeight);
			}
		}
		super.drawScreen(i, j, f);
		if (buttonFactionMap.enabled && buttonFactionMap.func_146115_a()) {
			float z = zLevel;
			String s = StatCollector.translateToLocal("got.gui.factions.viewMap");
			stringWidth = 200;
			desc = fontRendererObj.listFormattedStringToWidth(s, stringWidth);
			func_146283_a(desc, i, j);
			GL11.glDisable(2896);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			zLevel = z;
		}
		if (mouseOverWarCrimes) {
			float z = zLevel;
			String warCrimes = currentFaction.approvesWarCrimes ? "got.gui.factions.warCrimesYes" : "got.gui.factions.warCrimesNo";
			warCrimes = StatCollector.translateToLocal(warCrimes);
			stringWidth = 200;
			desc = fontRendererObj.listFormattedStringToWidth(warCrimes, stringWidth);
			func_146283_a(desc, i, j);
			GL11.glDisable(2896);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			zLevel = z;
		}
	}

	public boolean isHover(float xx, float yy, float xx1, float yy1) {
		int mouseX = MX;
		int mouseY = MY;
		return mouseX >= xx && mouseX < xx1 + xx && mouseY >= yy && mouseY < yy1 + yy;
	}

	public boolean isClicked(float xx, float yy, float xx1, float yy1) {
		final int mouseX = MX;
		final int mouseY = MY;
		return mouseX >= xx && mouseX < xx1 + xx && mouseY >= yy && mouseY < yy1 + yy && this.isClicked;
	} 

	public static void setClicked(boolean isClicked) {
		GOTGuiFactions.isClicked = isClicked;
	}

	public static String prefixEditName ="";
	public void renderPrefixSet() {
		if(!prefixField.getVisible() ) {
			return;
		}
		float x = width/2-40;
		float y=  height/2-20;
		float width = 80;
		float height = 40;
		GuiApi.drawRect(x,y, width, height, new Color(0, 0, 0, 180).getRGB());
		GuiApi.drawScaleText("Установить префикс", x + 11, y +3, 0.8f, true, 0xFFffffff);
		prefixField.drawTextBox();
		
		GuiApi.drawRect(x + 9, y + height- 12, 25, 10, (isHover(x + 9, y + height- 12, 25, 10) ?new Color(0, 0, 0, 180).getRGB()  : new Color(0, 0, 0, 120).getRGB() ));
		GuiApi.drawScaleText("Ок", x + 19, y + height- 11, 0.8f, true, 0xFFffffff);
		if(isClicked(x + 9, y + height- 12, 25, 10)) {
			CoreFaction.brainChannel.sendToServer(new PacketMessage("setPrefix#" + prefixEditName.replace(" ", "") + "#" + prefixField.getText().replace("#", "")));
			prefixField.setVisible(false);
			setClicked(false);
		}
		
		GuiApi.drawRect(x + 46, y + height- 12, 25, 10, (isHover(x + 46, y + height- 12, 25, 10) ?new Color(0, 0, 0, 180).getRGB()  : new Color(0, 0, 0, 120).getRGB() ));
		GuiApi.drawScaleText("Отмена", x + 20+30, y + height- 11, 0.8f, true, 0xFFffffff);
		if(isClicked(x + 46, y + height- 12, 25, 10)) {
			prefixField.setVisible(false);
			setClicked(false);
		}
		
	}
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int k = Mouse.getEventDWheel();
		playerList.handleMouseInput(k / 120);
		applicationList.handleMouseInput(k / 120);
		if(applicationList.isHover() || playerList.isHover()) {
			return;
		}
		
		if (k != 0 ) {
			k = Integer.signum(k);
			if (scrollPaneAlliesEnemies.hasScrollBar && scrollPaneAlliesEnemies.mouseOver) {
				int l = currentAlliesEnemies.size() - numDisplayedAlliesEnemies;
				scrollPaneAlliesEnemies.mouseWheelScroll(k, l);
			} else {
				if (k < 0) {
					currentFactionIndex = Math.min(currentFactionIndex + 1, Math.max(0, currentFactionList.size() - 1));
				}
				if (k > 0) {
					currentFactionIndex = Math.max(currentFactionIndex - 1, 0);
				}
				setCurrentScrollFromFaction();
				scrollPaneAlliesEnemies.resetScroll();
				isPledging = false;
				isUnpledging = false;
			}
		}
	}

	public boolean hasScrollBar() {
		return currentFactionList.size() > 1;
	}

	@Override
	public void initGui() {
		super.initGui();
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2 + 20;
		buttonRegions = new GOTGuiButton(0, guiLeft + xSize / 2 + 5, guiTop + 200, 120, 20, "");
		buttonList.add(buttonRegions);
		goBack = new GOTGuiButton(0, guiLeft + xSize / 2 - 125, guiTop + 200, 120, 20, StatCollector.translateToLocal("got.gui.menuButton"));
		buttonList.add(goBack);
		buttonPagePrev = new GOTGuiButtonFactionsPage(1, guiLeft + 8, guiTop + pageY + 104, false);
		buttonList.add(buttonPagePrev);
		buttonPageNext = new GOTGuiButtonFactionsPage(2, guiLeft + 232, guiTop + pageY + 104, true);
		buttonList.add(buttonPageNext);
		buttonFactionMap = new GOTGuiButtonFactionsMap(3, guiLeft + pageMapX + pageMapSize - 3 - 8, guiTop + pageY + pageMapY + 3);
		buttonList.add(buttonFactionMap);
		buttonPledge = new GOTGuiButtonPledge(this, 4, guiLeft + 14, guiTop + pageY + pageHeight - 42, "");
		buttonList.add(buttonPledge);
		buttonPledgeConfirm = new GOTGuiButtonPledge(this, 5, guiLeft + pageWidth / 2 - 16, guiTop + pageY + pageHeight - 50, "");
		buttonList.add(buttonPledgeConfirm);
		buttonPledgeRevoke = new GOTGuiButtonPledge(this, 6, guiLeft + pageWidth / 2 - 16, guiTop + pageY + pageHeight - 50, "");
		buttonList.add(buttonPledgeRevoke);
		buttonPledgeRevoke.isBroken = true;
		prevDimension = currentDimension = GOTDimension.getCurrentDimension(mc.theWorld);
		currentFaction = GOTLevelData.getData(mc.thePlayer).getViewingFaction();
		prevRegion = currentRegion = currentFaction.factionRegion;
		currentFactionList = GOTGuiFactions.currentRegion.factionList;
		prevFactionIndex = currentFactionIndex = currentFactionList.indexOf(currentFaction);
		setCurrentScrollFromFaction();
		if (mc.currentScreen == this) {
			GOTPacketClientMQEvent packet = new GOTPacketClientMQEvent(GOTPacketClientMQEvent.ClientMQEvent.FACTIONS);
			GOTPacketHandler.networkWrapper.sendToServer(packet);
		}
		
		playerList = new GuiScrollingList<>(guiLeft - 5 , height/2-38, 142 , 90, 10);
		applicationList  = new GuiScrollingList<>(guiLeft + pageWidth/2+25 , height/2-38, 85 , 90, 10);   	
		setFactionInfo(currentFaction.codeName());
		prefixField = new GuiTextField(fontRendererObj, width/2-30, height/2-5, 60, 10);
		prefixField.setVisible(false);
	}

	public static void setFactionInfo(String id) {
		GOTGuiFactions.playerList.getElements().clear();
		applicationList.getElements().clear();
		if(!PacketInfoFactions.getFactions().containsKey(id)) {
			return;
		}
		Faction faction = PacketInfoFactions.getFactions().get(id);
		String playerName = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
		faction.getPlayers().keySet().forEach((x) -> playerList.addElement(new PlayerLayer(getPrefix(faction, x), x,(faction.getLeaderName().equals(x) ? "Лидер" : (faction.getAssistantName().equals(x) ? "Совладелец" : "Игрок")))));
		Collections.sort(playerList.getList(), new Comparator<PlayerLayer>() {
			@Override
			public int compare(PlayerLayer p1, PlayerLayer p2) {
				// Сравниваем по типу: владельцы и совладельцы выше игроков
				if (p1.getGroup().equals("Лидер"))
					return -1;
				if (p2.getGroup().equals("Лидер"))
					return 1;
				if (p1.getGroup().equals("Совладелец"))
					return -1;
				if (p2.getGroup().equals("Совладелец"))
					return 1;
				return 0; // Если оба игрока одного типа, порядок не меняется
			}
		});
		faction.getApplications().keySet().forEach(x -> applicationList.addElement(new PlayerLayer(x)));
//		System.out.println(playerName);
		if(faction.getLeaderName().equals(playerName)) {
			status = GroupStatus.Owner;
		} else if(faction.getAssistantName().equals(playerName)) {
			status = GroupStatus.CoOwner;
		} else {
			status = GroupStatus.Player;
		}
		
	}
	
	
	@Override
	public void keyTyped(char c, int i) {
		prefixField.textboxKeyTyped(c, i);
		if(prefixField.isFocused()) {
			return;
		}
		if (i == 1 || i == mc.gameSettings.keyBindInventory.getKeyCode()) {
			if (isPledging) {
				isPledging = false;
				return;
			}
			if (isUnpledging) {
				isUnpledging = false;
				return;
			}
			if (isOtherPlayer) {
				mc.thePlayer.closeScreen();
				return;
			}
		}
		super.keyTyped(c, i);
		
	}

	public void setCurrentScrollFromFaction() {
		currentScroll = (float) currentFactionIndex / (float) (currentFactionList.size() - 1);
	}

	public void setOtherPlayer(String name, Map<GOTFaction, Float> alignments) {
		isOtherPlayer = true;
		otherPlayerName = name;
		playerAlignmentMap = alignments;
	}

	public void setupScrollBar(int i, int j) {
		boolean isMouseDown = Mouse.isButtonDown(0);
		int i1 = guiLeft + scrollBarX;
		int j1 = guiTop + scrollBarY;
		int i2 = i1 + scrollBarWidth;
		int j2 = j1 + scrollBarHeight;
		if (!wasMouseDown && isMouseDown && i >= i1 && j >= j1 && i < i2 && j < j2) {
			isScrolling = canScroll();
		}
		if (!isMouseDown) {
			isScrolling = false;
		}
		wasMouseDown = isMouseDown;
		if (isScrolling) {
			currentScroll = (i - i1 - scrollWidgetWidth / 2.0f) / ((float) (i2 - i1) - (float) scrollWidgetWidth);
			currentScroll = MathHelper.clamp_float(currentScroll, 0.0f, 1.0f);
			currentFactionIndex = Math.round(currentScroll * (currentFactionList.size() - 1));
			scrollPaneAlliesEnemies.resetScroll();
		}
		if (currentPage == Page.ALLIES || currentPage == Page.ENEMIES || currentPage == Page.RANKS) {
			switch (currentPage) {
			case ALLIES:
				List<GOTFaction> friends;
				currentAlliesEnemies = new ArrayList<>();
				List<GOTFaction> allies = currentFaction.getOthersOfRelation(GOTFactionRelations.Relation.ALLY);
				if (!allies.isEmpty()) {
					currentAlliesEnemies.add(GOTFactionRelations.Relation.ALLY);
					currentAlliesEnemies.addAll(allies);
				}
				if (!(friends = currentFaction.getOthersOfRelation(GOTFactionRelations.Relation.FRIEND)).isEmpty()) {
					if (!currentAlliesEnemies.isEmpty()) {
						currentAlliesEnemies.add(null);
					}
					currentAlliesEnemies.add(GOTFactionRelations.Relation.FRIEND);
					currentAlliesEnemies.addAll(friends);
				}
				break;
			case ENEMIES:
				List<GOTFaction> enemies;
				currentAlliesEnemies = new ArrayList<>();
				List<GOTFaction> mortals = currentFaction.getOthersOfRelation(GOTFactionRelations.Relation.MORTAL_ENEMY);
				if (!mortals.isEmpty()) {
					currentAlliesEnemies.add(GOTFactionRelations.Relation.MORTAL_ENEMY);
					currentAlliesEnemies.addAll(mortals);
				}
				if (!(enemies = currentFaction.getOthersOfRelation(GOTFactionRelations.Relation.ENEMY)).isEmpty()) {
					if (!currentAlliesEnemies.isEmpty()) {
						currentAlliesEnemies.add(null);
					}
					currentAlliesEnemies.add(GOTFactionRelations.Relation.ENEMY);
					currentAlliesEnemies.addAll(enemies);
				}
				break;
			case RANKS:
				currentAlliesEnemies = new ArrayList<>();
				currentAlliesEnemies.add(StatCollector.translateToLocal("got.gui.factions.rankHeader"));
				if (GOTLevelData.getData(mc.thePlayer).getAlignment(currentFaction) <= 0.0f) {
					currentAlliesEnemies.add(GOTFactionRank.RANK_ENEMY);
				}
				GOTFactionRank rank = GOTFactionRank.RANK_NEUTRAL;
				do {
					currentAlliesEnemies.add(rank);
					GOTFactionRank nextRank = currentFaction.getRankAbove(rank);
					if (nextRank == null || nextRank.isDummyRank() || currentAlliesEnemies.contains(nextRank)) {
						break;
					}
					rank = nextRank;
				} while (true);
				break;
			default:
				break;
			}
			scrollPaneAlliesEnemies.hasScrollBar = false;
			numDisplayedAlliesEnemies = currentAlliesEnemies.size();
			if (numDisplayedAlliesEnemies > 10) {
				numDisplayedAlliesEnemies = 10;
				scrollPaneAlliesEnemies.hasScrollBar = true;
			}
			scrollPaneAlliesEnemies.paneX0 = guiLeft;
			scrollPaneAlliesEnemies.scrollBarX0 = guiLeft + scrollAlliesEnemiesX;
			if (currentPage == Page.RANKS) {
				scrollPaneAlliesEnemies.scrollBarX0 += 50;
			}
			scrollPaneAlliesEnemies.paneY0 = guiTop + pageY + pageBorderTop;
			scrollPaneAlliesEnemies.paneY1 = scrollPaneAlliesEnemies.paneY0 + fontRendererObj.FONT_HEIGHT * numDisplayedAlliesEnemies;
		} else {
			scrollPaneAlliesEnemies.hasScrollBar = false;
		}
		scrollPaneAlliesEnemies.mouseDragScroll(i, j);
	
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int i, int j) {
		super.setWorldAndResolution(mc, i, j);
		mapDrawGui.setWorldAndResolution(mc, i, j);
	}

	public void updateCurrentDimensionAndFaction() {
		boolean changes;
		GOTPlayerData pd = GOTLevelData.getData(mc.thePlayer);
		HashMap<GOTDimension.DimensionRegion, GOTFaction> lastViewedRegions = new HashMap<>();
		if (currentFactionIndex != prevFactionIndex) {
			currentFaction = currentFactionList.get(currentFactionIndex);
		}
		prevFactionIndex = currentFactionIndex;
		currentDimension = GOTDimension.getCurrentDimension(mc.theWorld);
		if (currentDimension != prevDimension) {
			currentRegion = GOTGuiFactions.currentDimension.dimensionRegions.get(0);
		}
		if (currentRegion != prevRegion) {
			pd.setRegionLastViewedFaction(prevRegion, currentFaction);
			lastViewedRegions.put(prevRegion, currentFaction);
			currentFactionList = GOTGuiFactions.currentRegion.factionList;
			currentFaction = pd.getRegionLastViewedFaction(currentRegion);
			prevFactionIndex = currentFactionIndex = currentFactionList.indexOf(currentFaction);
		}
		prevDimension = currentDimension;
		prevRegion = currentRegion;
		GOTFaction prevFaction = pd.getViewingFaction();
		changes = currentFaction != prevFaction;
		if (changes) {
			pd.setViewingFaction(currentFaction);
			GOTClientProxy.sendClientInfoPacket(currentFaction, lastViewedRegions);
			isPledging = false;
			isUnpledging = false;
		}
		setFactionInfo(currentFaction.codeName());
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		playerList.updateScreen();
		applicationList.updateScreen();
		updateCurrentDimensionAndFaction();
		GOTPlayerData playerData = GOTLevelData.getData(mc.thePlayer);
		if (isPledging && !playerData.hasPledgeAlignment(currentFaction)) {
			isPledging = false;
		}
		if (isUnpledging && !playerData.isPledgedTo(currentFaction)) {
			isUnpledging = false;
		}

	}
	@Override
	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_) {
		playerList.mouseClickMove(p_146273_1_, p_146273_2_, p_146273_3_);
		applicationList.mouseClickMove(p_146273_1_, p_146273_2_, p_146273_3_);
		super.mouseClickMove(p_146273_1_, p_146273_2_, p_146273_3_, p_146273_4_);
	}
	
	public static String getPrefix(Faction faction, String player) {
		for(Map.Entry<String, String> fac: faction.getPlayers().entrySet()) {
			if(fac.getKey().equals(player)) {
				return fac.getValue();
			}
		}
		return "";
	}
	

	protected void mouseClicked(int x, int y, int b) {
		super.mouseClicked(x, y, b);
		this.isClicked = true;
		Timing timing = new Timing(100);
		timing.start();
		prefixField.mouseClicked(x, y, b);
		playerList.mouseClicked(x, y, b);
		applicationList.mouseClicked(x, y, b);
	}
	
	public static GroupStatus getStatus() {
		return status;
	}
	
	public boolean useFullPageTexture() {
		return isPledging || isUnpledging || currentPage == Page.RANKS;
	}

	public enum GroupStatus{
		Player, CoOwner, Owner;
	}
	public enum Page {
		FRONT, RANKS, ALLIES, ENEMIES, Playerlist;

		public Page next() {
			int i = ordinal();
			if (i == Page.values().length - 1) {
				return null;
			}
			i++;
			return Page.values()[i];
		}

		public Page prev() {
			int i = ordinal();
			if (i == 0) {
				return null;
			}
			i--;
			return Page.values()[i];
		}
	}
	
	class Timing extends Thread
    {
        private int timer;
        
        public Timing(final int timer) {
            this.timer = timer;
        }
        
        @Override
        public void run() {
            try {
                Thread.sleep(this.timer);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            GOTGuiFactions.isClicked = false;
            this.interrupt();
        }
    }
	

}