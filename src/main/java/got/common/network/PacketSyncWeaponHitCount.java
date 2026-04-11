package got.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.GOT;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;

public class PacketSyncWeaponHitCount implements IMessage {

    private int itemID;
    private int hitCount;

    public PacketSyncWeaponHitCount() {}

    public PacketSyncWeaponHitCount(Item item, int count) {
        this.itemID = Item.getIdFromItem(item);
        this.hitCount = count;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.itemID = buf.readInt();
        this.hitCount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.itemID);
        buf.writeInt(this.hitCount);
    }

    public static class Handler implements IMessageHandler<PacketSyncWeaponHitCount, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncWeaponHitCount message, MessageContext ctx) {
            Item item = Item.getItemById(message.itemID);
            GOT.proxy.handleSyncWeaponHitCount(item, message.hitCount);
            return null;
        }
    }
}