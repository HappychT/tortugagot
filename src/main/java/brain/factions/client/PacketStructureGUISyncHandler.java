package brain.factions.client;

import brain.factions.Annot;
import brain.factions.network.PacketStructureGUISync;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.client.gui.faction.GuiStructureBlock;
import net.minecraft.client.Minecraft;

public class PacketStructureGUISyncHandler implements IMessageHandler<PacketStructureGUISync, IMessage> {
    @Override
    public IMessage onMessage(final PacketStructureGUISync message, MessageContext ctx) {
        if (!Annot.SERVER) {
            Minecraft.getMinecraft().func_152344_a(new Runnable() {
                @Override
                public void run() {
                    if (Minecraft.getMinecraft().currentScreen instanceof GuiStructureBlock) {
                        ((GuiStructureBlock) Minecraft.getMinecraft().currentScreen).updateData(message.getSlot(), message.getAvailableStructures());
                    }
                }
            });
        }
        return null;
    }
}