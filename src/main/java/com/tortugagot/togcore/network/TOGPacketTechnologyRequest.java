package com.tortugagot.togcore.network;

import com.tortugagot.togcore.technology.TOGTechnologyPlayerData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class TOGPacketTechnologyRequest implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<TOGPacketTechnologyRequest, IMessage> {
        @Override
        public IMessage onMessage(TOGPacketTechnologyRequest message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            TOGTechnologyPlayerData data = TOGTechnologyPlayerData.get(player);
            if (data != null) {
                data.sync();
            }
            return null;
        }
    }
}
