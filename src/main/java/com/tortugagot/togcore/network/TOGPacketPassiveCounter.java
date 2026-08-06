package com.tortugagot.togcore.network;

import com.tortugagot.togcore.TogCore;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class TOGPacketPassiveCounter implements IMessage {
    private String label;
    private int count;
    private int threshold;
    private boolean ready;

    public TOGPacketPassiveCounter() {
    }

    public TOGPacketPassiveCounter(String label, int count, int threshold, boolean ready) {
        this.label = label;
        this.count = count;
        this.threshold = threshold;
        this.ready = ready;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        label = ByteBufUtils.readUTF8String(buf);
        count = buf.readInt();
        threshold = buf.readInt();
        ready = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, label != null ? label : "");
        buf.writeInt(count);
        buf.writeInt(threshold);
        buf.writeBoolean(ready);
    }

    public static class Handler implements IMessageHandler<TOGPacketPassiveCounter, IMessage> {
        @Override
        public IMessage onMessage(TOGPacketPassiveCounter message, MessageContext ctx) {
            TogCore.proxy.receivePassiveCounterUpdate(message.label, message.count, message.threshold, message.ready);
            return null;
        }
    }
}
