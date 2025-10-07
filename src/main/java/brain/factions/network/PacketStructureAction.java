package brain.factions.network;

import brain.factions.servers.ServerTaskExecutor;
import brain.factions.servers.StructureManager;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketStructureAction implements IMessage {

    private String action;
    private String structureId;
    private String data1;
    private String data2;

    public PacketStructureAction() {}

    public PacketStructureAction(String action, String structureId, String data1, String data2) {
        this.action = action;
        this.structureId = structureId;
        this.data1 = data1;
        this.data2 = data2;
    }

    public PacketStructureAction(String action, String structureId) {
        this(action, structureId, "", "");
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        action = ByteBufUtils.readUTF8String(buf);
        structureId = ByteBufUtils.readUTF8String(buf);
        data1 = ByteBufUtils.readUTF8String(buf);
        data2 = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, action);
        ByteBufUtils.writeUTF8String(buf, structureId);
        ByteBufUtils.writeUTF8String(buf, data1);
        ByteBufUtils.writeUTF8String(buf, data2);
    }

    public static class Handler implements IMessageHandler<PacketStructureAction, IMessage> {
        @Override
        public IMessage onMessage(final PacketStructureAction message, MessageContext ctx) {
            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            ServerTaskExecutor.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    switch (message.action) {
                        case "purchase":
                            StructureManager.handlePurchase(player, message.structureId, message.data1, message.data2);
                            break;
                        case "collect":
                            StructureManager.handleCollect(player, message.structureId);
                            break;
                        case "upgrade":
                            StructureManager.handleUpgrade(player, message.structureId);
                            break;
                    }
                }
            });
            return null;
        }
    }
}