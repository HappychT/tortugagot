package brain.factions.network;

import brain.factions.structures.FactionStructureSlot;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketStructureGUISync implements IMessage {

    private int x, y, z;
    private FactionStructureSlot slot;
    private Map<String, List<String>> availableStructures;

    public PacketStructureGUISync() {}

    public PacketStructureGUISync(int x, int y, int z, FactionStructureSlot slot, Map<String, List<String>> availableStructures) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.slot = slot;
        this.availableStructures = availableStructures;
    }

    public PacketStructureGUISync(int x, int y, int z, FactionStructureSlot slot) {
        this(x, y, z, slot, new HashMap<>());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.availableStructures = new HashMap<>();

        boolean hasSlot = buf.readBoolean();
        if (hasSlot) {
            this.slot = new FactionStructureSlot();
            this.slot.id = ByteBufUtils.readUTF8String(buf);
            this.slot.name = ByteBufUtils.readUTF8String(buf);
            this.slot.price = buf.readInt();
            this.slot.category = FactionStructureSlot.StructureCategory.values()[buf.readInt()];

            boolean hasOwner = buf.readBoolean();
            if (hasOwner) {
                this.slot.ownerFactionID = ByteBufUtils.readUTF8String(buf);
                this.slot.level = buf.readInt();
                this.slot.resource = ByteBufUtils.readUTF8String(buf);
                this.slot.provisions = buf.readFloat();
                this.slot.barracksCapacity = buf.readInt();
                this.slot.storedFoodItems = buf.readInt();
                this.slot.lastSiegePurchaseTime = buf.readLong();
                this.slot.securityLevel = buf.readInt();
                this.slot.barnLevel = buf.readInt();
                this.slot.workshopLevel = buf.readInt();
                this.slot.stableLevel = buf.readInt();
                this.slot.barracksLevel = buf.readInt();
                this.slot.destructionCount = buf.readInt();
            } else {
                int mapSize = buf.readInt();
                for (int i = 0; i < mapSize; i++) {
                    String category = ByteBufUtils.readUTF8String(buf);
                    int listSize = buf.readInt();
                    List<String> subTypes = new ArrayList<>(listSize);
                    for (int j = 0; j < listSize; j++) {
                        subTypes.add(ByteBufUtils.readUTF8String(buf));
                    }
                    this.availableStructures.put(category, subTypes);
                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeBoolean(this.slot != null);
        if (this.slot == null) return;

        ByteBufUtils.writeUTF8String(buf, this.slot.id != null ? this.slot.id : "");
        ByteBufUtils.writeUTF8String(buf, this.slot.name != null ? this.slot.name : "Неизвестная точка");
        buf.writeInt(this.slot.price);
        buf.writeInt(this.slot.category != null ? this.slot.category.ordinal() : FactionStructureSlot.StructureCategory.NONE.ordinal());

        boolean hasOwner = this.slot.ownerFactionID != null;
        buf.writeBoolean(hasOwner);
        if (hasOwner) {
            ByteBufUtils.writeUTF8String(buf, this.slot.ownerFactionID);
            buf.writeInt(this.slot.level);
            ByteBufUtils.writeUTF8String(buf, this.slot.resource != null ? this.slot.resource : "");
            buf.writeFloat(this.slot.provisions);
            buf.writeInt(this.slot.barracksCapacity);
            buf.writeInt(this.slot.storedFoodItems);
            buf.writeLong(this.slot.lastSiegePurchaseTime);
            buf.writeInt(this.slot.securityLevel);
            buf.writeInt(this.slot.barnLevel);
            buf.writeInt(this.slot.workshopLevel);
            buf.writeInt(this.slot.stableLevel);
            buf.writeInt(this.slot.barracksLevel);
            buf.writeInt(this.slot.destructionCount);
        } else {
            buf.writeInt(this.availableStructures.size());
            for (Map.Entry<String, List<String>> entry : this.availableStructures.entrySet()) {
                ByteBufUtils.writeUTF8String(buf, entry.getKey());
                buf.writeInt(entry.getValue().size());
                for (String subType : entry.getValue()) {
                    ByteBufUtils.writeUTF8String(buf, subType);
                }
            }
        }
    }

    public FactionStructureSlot getSlot() { return slot; }
    public Map<String, List<String>> getAvailableStructures() { return availableStructures; }
}