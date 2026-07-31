package com.tortugagot.togcore.network;

import com.tortugagot.togcore.TogCore;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TOGPacketTechnologySync implements IMessage {
    public int masteryPoints;
    public List<String> unlockedTechnologies = new ArrayList<String>();

    public TOGPacketTechnologySync() {
    }

    public TOGPacketTechnologySync(int masteryPoints, Collection<String> unlockedTechnologies) {
        this.masteryPoints = masteryPoints;
        this.unlockedTechnologies.addAll(unlockedTechnologies);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        masteryPoints = buf.readInt();
        unlockedTechnologies.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            unlockedTechnologies.add(ByteBufUtils.readUTF8String(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(masteryPoints);
        buf.writeInt(unlockedTechnologies.size());
        for (String id : unlockedTechnologies) {
            ByteBufUtils.writeUTF8String(buf, id);
        }
    }

    public static class Handler implements IMessageHandler<TOGPacketTechnologySync, IMessage> {
        @Override
        public IMessage onMessage(TOGPacketTechnologySync message, MessageContext ctx) {
            TogCore.proxy.receiveTechnologySync(message.masteryPoints, message.unlockedTechnologies);
            return null;
        }
    }
}
