package brain.factions.network;

import brain.factions.structures.FactionStructureSlot;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.client.gui.faction.GOTGuiFactions;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class PacketFactionStructures implements IMessage {

    private List<FactionStructureSlot> structures;

    public PacketFactionStructures() {
        this.structures = new ArrayList<>();
    }

    public PacketFactionStructures(List<FactionStructureSlot> structures) {
        this.structures = structures;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        this.structures = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            FactionStructureSlot slot = new FactionStructureSlot();
            slot.id = ByteBufUtils.readUTF8String(buf);
            slot.name = ByteBufUtils.readUTF8String(buf);
            slot.mapX = buf.readInt();
            slot.mapY = buf.readInt();
            slot.price = buf.readInt();

            boolean hasOwner = buf.readBoolean();
            if (hasOwner) {
                slot.ownerFactionID = ByteBufUtils.readUTF8String(buf);
            } else {
                slot.ownerFactionID = null;
            }
            this.structures.add(slot);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.structures.size());
        for (FactionStructureSlot slot : this.structures) {
            ByteBufUtils.writeUTF8String(buf, slot.id != null ? slot.id : "");
            ByteBufUtils.writeUTF8String(buf, slot.name != null ? slot.name : "Точка интереса");
            buf.writeInt(slot.mapX);
            buf.writeInt(slot.mapY);
            buf.writeInt(slot.price);

            buf.writeBoolean(slot.ownerFactionID != null);
            if (slot.ownerFactionID != null) {
                ByteBufUtils.writeUTF8String(buf, slot.ownerFactionID);
            }
        }
    }

    public static class Handler implements IMessageHandler<PacketFactionStructures, IMessage> {
        @Override
        public IMessage onMessage(PacketFactionStructures message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GOTGuiFactions) {
                ((GOTGuiFactions) Minecraft.getMinecraft().currentScreen).mapView.receiveStructureData(message.structures);
            }
            return null;
        }
    }
}