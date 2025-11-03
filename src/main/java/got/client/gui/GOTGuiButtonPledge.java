package got.client.gui;

import java.util.Arrays;
import java.util.List;

import got.client.gui.faction.GOTGuiFactions;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GOTGuiButtonPledge extends GuiButton {
	private final GOTGuiFactions parentGUI;
	private List<String> displayLines;
	public boolean isBroken;

	public GOTGuiButtonPledge(GOTGuiFactions gui, int id, int x, int y, String... text) {
		super(id, x, y, 32, 32, "");
		this.parentGUI = gui;
		this.displayLines = Arrays.asList(text);
	}

	public void setDisplayLines(String... text) {
		this.displayLines = Arrays.asList(text);
	}

	@Override
	public void drawButton(Minecraft mc, int i, int j) {
		if (this.visible) {
			mc.getTextureManager().bindTexture(GOTGuiFactions.factionsTexture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_146123_n = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height;
			int k = this.getHoverState(this.field_146123_n);

			int u = 0;
			int v = 192;
			if (this.isBroken) {
				u += this.width;
			}
			if (!this.enabled) {
				v += this.height;
			} else if (this.field_146123_n) {
				v += this.height * 2;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);

			if (this.field_146123_n) {
				this.parentGUI.drawButtonHoveringText(this.displayLines, i, j);
			}
		}
	}
}