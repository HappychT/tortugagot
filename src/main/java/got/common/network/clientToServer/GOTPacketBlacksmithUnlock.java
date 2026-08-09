package got.common.network.clientToServer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.common.faction.GOTContainerFactionBlacksmith;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GOTPacketBlacksmithUnlock implements IMessage {
    private int slot;

    public GOTPacketBlacksmithUnlock() {
    }

    public GOTPacketBlacksmithUnlock(int slot) {
        this.slot = slot;
    }

    @Override
    public void fromBytes(ByteBuf data) {
        slot = data.readInt();
    }

    @Override
    public void toBytes(ByteBuf data) {
        data.writeInt(slot);
    }

    public static class Handler implements IMessageHandler<GOTPacketBlacksmithUnlock, IMessage> {
        @Override
        public IMessage onMessage(GOTPacketBlacksmithUnlock packet, MessageContext context) {
            EntityPlayer entityplayer = context.getServerHandler().playerEntity;
            Container container = entityplayer.openContainer;
            if (container instanceof GOTContainerFactionBlacksmith) {
                GOTContainerFactionBlacksmith containerBlacksmith = (GOTContainerFactionBlacksmith) container;
                if (containerBlacksmith.isSlotUnlocked(packet.slot))
                    return null;
                containerBlacksmith.theBlacksmithNPC.applyUnlockSlot(entityplayer, packet.slot);
                containerBlacksmith.takeUnlockItems(packet.slot);
            }
            return null;
        }
    }
}