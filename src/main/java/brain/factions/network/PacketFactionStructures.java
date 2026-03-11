package brain.factions.network;

import brain.factions.servers.CoreFaction;
import brain.factions.servers.StructureManager;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.client.gui.faction.GOTGuiFactions;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PacketFactionStructures implements IMessage {

    private List<FactionStructureSlot> structures;
    public static String raidTimeString;
    public boolean isRaidTimeFortress;
    public int serverTimeMinutes;

    public PacketFactionStructures() {
        this.structures = new ArrayList<>();
    }

    public PacketFactionStructures(List<FactionStructureSlot> structures) {
        this.structures = structures;
        this.isRaidTimeFortress = StructureManager.isRaidTimeFortress;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        raidTimeString = ByteBufUtils.readUTF8String(buf);
        isRaidTimeFortress = buf.readBoolean();
        serverTimeMinutes = buf.readInt();
        StructureManager.isRaidTimeFortress = isRaidTimeFortress;

        int size = buf.readInt();
        this.structures = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            FactionStructureSlot slot = new FactionStructureSlot();
            slot.id = ByteBufUtils.readUTF8String(buf);
            slot.name = ByteBufUtils.readUTF8String(buf);
            slot.mapX = buf.readInt();
            slot.mapY = buf.readInt();
            slot.xCoord = buf.readInt();
            slot.yCoord = buf.readInt();
            slot.zCoord = buf.readInt();
            slot.level = buf.readInt();

            boolean hasOwner = buf.readBoolean();
            if (hasOwner) {
                slot.ownerFactionID = ByteBufUtils.readUTF8String(buf);
            } else {
                slot.ownerFactionID = null;
            }
            slot.raidTime = ByteBufUtils.readUTF8String(buf);
            this.structures.add(slot);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, StructureManager.config.raidTimeResourcePoints != null ? StructureManager.config.raidTimeResourcePoints : "00:00-00:00");
        buf.writeBoolean(StructureManager.isRaidTimeFortress);

        LocalTime now = LocalTime.now();
        buf.writeInt(now.getHour() * 60 + now.getMinute());

        buf.writeInt(this.structures.size());
        for (FactionStructureSlot slot : this.structures) {
            ByteBufUtils.writeUTF8String(buf, slot.id != null ? slot.id : "");
            ByteBufUtils.writeUTF8String(buf, slot.name != null ? slot.name : "Точка интереса");
            buf.writeInt(slot.mapX);
            buf.writeInt(slot.mapY);
            buf.writeInt(slot.xCoord);
            buf.writeInt(slot.yCoord);
            buf.writeInt(slot.zCoord);
            buf.writeInt(slot.level);

            buf.writeBoolean(slot.ownerFactionID != null);
            if (slot.ownerFactionID != null) {
                ByteBufUtils.writeUTF8String(buf, slot.ownerFactionID);
            }
            ByteBufUtils.writeUTF8String(buf, slot.raidTime != null ? slot.raidTime : "");
        }
    }

    public static class Handler implements IMessageHandler<PacketFactionStructures, IMessage> {
        @Override
        public IMessage onMessage(final PacketFactionStructures message, MessageContext ctx) {
            Minecraft.getMinecraft().func_152344_a(new Runnable() {
                @Override
                public void run() {
                    StructureManager.isRaidTimeFortress = message.isRaidTimeFortress;

                    if (StructureManager.config != null) {
                        StructureManager.config.raidTimeResourcePoints = PacketFactionStructures.raidTimeString;
                    }

                    LocalTime now = LocalTime.now();
                    int clientMinutes = now.getHour() * 60 + now.getMinute();
                    StructureManager.serverTimeOffsetMinutes = message.serverTimeMinutes - clientMinutes;

                    FactionStructureManager.structureSlots = message.structures;

                    if (Minecraft.getMinecraft().currentScreen instanceof GOTGuiFactions) {
                        ((GOTGuiFactions) Minecraft.getMinecraft().currentScreen).mapView.receiveStructureData(message.structures);
                    }
                }
            });
            return null;
        }
    }
}