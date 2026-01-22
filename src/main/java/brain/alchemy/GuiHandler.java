package brain.alchemy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_ALCHEMY_BAG = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_ALCHEMY_BAG) {
            return new ContainerAlchemyBag(player.inventory, player.getCurrentEquippedItem());
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_ALCHEMY_BAG) {
            return new GuiAlchemyBag(player.inventory, player.getCurrentEquippedItem());
        }
        return null;
    }
}
