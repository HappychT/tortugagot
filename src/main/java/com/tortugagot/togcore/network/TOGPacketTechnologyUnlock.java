package com.tortugagot.togcore.network;

import com.tortugagot.togcore.technology.TOGTechnology;
import com.tortugagot.togcore.technology.TOGTechnologyPlayerData;
import com.tortugagot.togcore.technology.TOGTechnologyRegistry;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class TOGPacketTechnologyUnlock implements IMessage {
    private String technologyId;

    public TOGPacketTechnologyUnlock() {
    }

    public TOGPacketTechnologyUnlock(String technologyId) {
        this.technologyId = technologyId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        technologyId = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, technologyId != null ? technologyId : "");
    }

    public static class Handler implements IMessageHandler<TOGPacketTechnologyUnlock, IMessage> {
        @Override
        public IMessage onMessage(TOGPacketTechnologyUnlock message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            TOGTechnologyPlayerData data = TOGTechnologyPlayerData.get(player);
            if (data == null) {
                return null;
            }
            String reason = data.getBlockedReason(message.technologyId);
            if (reason == null && data.unlock(message.technologyId)) {
                TOGTechnology technology = TOGTechnologyRegistry.get(message.technologyId);
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Открыта технология: " + technology.getName()));
            } else if (reason != null) {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + reason));
            }
            data.sync();
            return null;
        }
    }
}
