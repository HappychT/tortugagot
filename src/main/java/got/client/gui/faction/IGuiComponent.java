package got.client.gui.faction;

import net.minecraft.client.gui.GuiButton;
import java.util.List;


public interface IGuiComponent {
    void initGui(List<GuiButton> buttonList);
    void drawScreen(int mouseX, int mouseY, float partialTicks);
    void actionPerformed(GuiButton button);
    void mouseClicked(int mouseX, int mouseY, int button);
    void keyTyped(char c, int key);
    void handleMouseInput();
    void update();
    boolean isTextFieldFocused();

}