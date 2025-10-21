package got.client.gui.faction.overlays;

import got.client.gui.faction.IGuiComponent;
import net.minecraft.client.gui.GuiButton;
import java.util.List;

public interface IOverlayRenderer extends IGuiComponent {
    void initGui(List<GuiButton> buttonList);
    void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks);
    void actionPerformed(GuiButton button);
    void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button);
    void keyTyped(char c, int key);
    void handleMouseInput();
    void update();
}