package got.client.gui.faction.pages;

import net.minecraft.client.gui.GuiButton;
import java.util.List;

public class ManagementPage implements IPageRenderer {
    @Override public void onOpened() {}
    @Override public void initGui(List<GuiButton> buttonList) {}
    @Override public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {}
    @Override public void actionPerformed(GuiButton button) {}
    @Override public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {}
    @Override public void handleMouseInput() {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override public boolean isTextFieldFocused() { return false; }
}