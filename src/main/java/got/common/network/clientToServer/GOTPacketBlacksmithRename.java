package got.common.network.clientToServer;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.common.faction.GOTContainerFactionBlacksmith;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class GOTPacketBlacksmithRename implements IMessage {
    private String name;

    public GOTPacketBlacksmithRename() {}

    public GOTPacketBlacksmithRename(String name) {
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf data) {
        name = ByteBufUtils.readUTF8String(data);
    }

    @Override
    public void toBytes(ByteBuf data) {
        ByteBufUtils.writeUTF8String(data, name);
    }

    public static class Handler implements IMessageHandler<GOTPacketBlacksmithRename, IMessage> {
        @Override
        public IMessage onMessage(GOTPacketBlacksmithRename packet, MessageContext context) {
            EntityPlayer entityplayer = context.getServerHandler().playerEntity;
            Container container = entityplayer.openContainer;
            if (container instanceof GOTContainerFactionBlacksmith) {
                GOTContainerFactionBlacksmith containerBlacksmith = (GOTContainerFactionBlacksmith) container;
                containerBlacksmith.renameItem(packet.name);
            }
            return null;
        }
    }
}

