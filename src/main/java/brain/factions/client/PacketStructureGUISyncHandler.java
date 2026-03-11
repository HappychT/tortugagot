package brain.factions.client;

import brain.factions.network.PacketStructureGUISync;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.client.gui.faction.GuiStructureBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class PacketStructureGUISyncHandler implements IMessageHandler<PacketStructureGUISync, IMessage> {
    @Override
    public IMessage onMessage(final PacketStructureGUISync message, MessageContext ctx) {
        Minecraft.getMinecraft().func_152344_a(new Runnable() {
            @Override
            public void run() {
                System.out.println("[DEBUG][StructureHeart] PacketStructureGUISyncHandler on client x=" + message.getX() + " y=" + message.getY() + " z=" + message.getZ());
                if (Minecraft.getMinecraft().thePlayer != null) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[DEBUG] client received PacketStructureGUISync"));
                }
                if (Minecraft.getMinecraft().currentScreen instanceof GuiStructureBlock) {
                    System.out.println("[DEBUG][StructureHeart] updating existing GuiStructureBlock");
                    ((GuiStructureBlock) Minecraft.getMinecraft().currentScreen).updateData(message.getSlot(), message.getAvailableStructures());
                } else {
                    System.out.println("[DEBUG][StructureHeart] opening new GuiStructureBlock");
                    GuiStructureBlock gui = new GuiStructureBlock(message.getX(), message.getY(), message.getZ(), message.getSlot());
                    gui.updateData(message.getSlot(), message.getAvailableStructures());
                    Minecraft.getMinecraft().displayGuiScreen(gui);
                }
            }
        });
        return null;
    }
}
