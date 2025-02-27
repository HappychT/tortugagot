package got.client.gui.playerlist;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import brain.factions.network.PacketMessage;
import cpw.mods.fml.client.config.GuiUtils;
import got.GOT;
import got.client.GOTTickHandlerClient;
import got.client.gui.GOTGuiFactions;
import got.client.gui.GOTGuiFactions.GroupStatus;
import got.client.gui.utils.GuiApi;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class PlayerLayer {
	private String prefix;
	private String playerName;
	private String group;
	
	public PlayerLayer(String prefix, String playerName, String group) {
		this.prefix = prefix;
		this.playerName = playerName;
		this.group = group;
	}

	public PlayerLayer(String playerName) {
		this.playerName = playerName;
	}
	
	public void draw(GOTGuiFactions gui, float x, float y, float w, float h) {
		gui.drawString(Minecraft.getMinecraft().fontRenderer,prefix + (prefix.equals("")? "":" ") + playerName + " [" + group + "]", (int) x, (int) y,8019267);

		if (gui.getStatus() == GroupStatus.Owner && gui.isEdit) {
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, 1);
			GuiApi.drawTexturedQuadFitBLEND(new ResourceLocation("got", "textures/icons/up.png"), x + w - 10 * 2, y + 2, 5, 5);
			if (gui.isClicked(x + w - 10 * 2, y + 2, 5, 5)) {
				GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("editPlayer#" + playerName + "#1"));
				gui.setClicked(false);
			}
			GuiApi.drawTexturedQuadFitBLEND(new ResourceLocation("got", "textures/icons/down.png"), x + w - 10, y + 2,5, 5);
			if (gui.isClicked(x + w - 10, y + 2, 5, 5)) {
				GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("editPlayer#" + playerName + "#-1"));
				gui.setClicked(false);
			}
			GuiApi.drawTexturedQuadFitBLEND(new ResourceLocation("got", "textures/icons/kick.png"), x + w, y + 4, 5, 1);
			if (gui.isClicked(x + w, y + 2, 5, 5)) {
				GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("editPlayer#" + playerName + "#0"));
				gui.setClicked(false);
			}
			GL11.glPopMatrix();
		}
	}
	
	public void drawHover(GOTGuiFactions gui, float x, float y, float w, float h) {
		if(gui.prefixField.getVisible()) {
			return;
		}
		if( gui.getStatus() == GroupStatus.Owner && gui.isEdit && gui.isHover(gui.guiLeft - 5 , gui.height/2-38, 142 , 90)) {
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glTranslated(0, 0, 300);
			if(gui.isHover(x + w - 10*2, y +2, 5, 5)) {		
				GOTTickHandlerClient.drawAlignmentText(Minecraft.getMinecraft().fontRenderer, (int)gui.MX,(int) gui.MY, "Повысить", 1);
			}
			if(gui.isHover(x + w - 10, y +2, 5, 5)) {
				GOTTickHandlerClient.drawAlignmentText(Minecraft.getMinecraft().fontRenderer, (int)gui.MX,(int) gui.MY, "Понизить", 1);
			}
			if(gui.isHover(x + w , y +2, 5, 5)) {
				GOTTickHandlerClient.drawAlignmentText(Minecraft.getMinecraft().fontRenderer, (int)gui.MX,(int) gui.MY, "Кикнуть", 1);
			}
			
			GL11.glPopMatrix();
		}
	}
	
	public void drawApplicationPlayer(GOTGuiFactions gui, float x, float y, float w, float h) {
		gui.drawString(Minecraft.getMinecraft().fontRenderer, playerName, (int) x, (int) y, 8019267);
		if (gui.isEdit) {
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, 1);
			GuiApi.drawTexturedQuadFitBLEND(new ResourceLocation("got", "textures/icons/plus.png"), x + w - 10 * 5.25,y + 2, 5, 5);
			if (gui.isClicked(x + w - 10 * (int)5.25,y + 2, 5, 5) && !gui.prefixField.getVisible()) {
				GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("applicat#" + playerName + "#1"));
				gui.setClicked(false);
			}
			
			GuiApi.drawTexturedQuadFitBLEND(new ResourceLocation("got", "textures/icons/kick.png"), x + w - 10 * 4.25,y + 3.5, 5, 1);
			if (gui.isClicked( x + w - 10 * (int) 4.25,y + (int)3.5, 5, 5) && !gui.prefixField.getVisible()) {
				GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("applicat#" + playerName + "#-1"));
				gui.setClicked(false);
			}
			GL11.glPopMatrix();
		}
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public String getGroup() {
		return group;
	}

}
