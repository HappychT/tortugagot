package brain.screen;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketDSD implements IMessage {

    public byte[] d;
    public boolean l;
    public boolean f;
    public String t;

    public PacketDSD() {}

    public PacketDSD(byte[] d, boolean l, boolean f, String t) {
        this.d = d;
        this.l = l;
        this.f = f;
        this.t = t;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.f = buf.readBoolean();
        this.l = buf.readBoolean();
        this.t = ByteBufUtils.readUTF8String(buf);
        int len = buf.readInt();
        this.d = new byte[len];
        buf.readBytes(this.d);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.f);
        buf.writeBoolean(this.l);
        ByteBufUtils.writeUTF8String(buf, this.t);
        buf.writeInt(this.d.length);
        buf.writeBytes(this.d);
    }

    public static class Handler implements IMessageHandler<PacketDSD, IMessage> {
        @Override
        public IMessage onMessage(final PacketDSD m, final MessageContext c) {
            String n = c.getServerHandler().playerEntity.getCommandSenderName();
            SHHANDL.z1(n, m.d, m.l, m.f, m.t);
            return null;
        }
    }
}