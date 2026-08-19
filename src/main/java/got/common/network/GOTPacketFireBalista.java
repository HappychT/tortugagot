package got.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import noname.weapons.entity.EntityBalista;

public class GOTPacketFireBalista implements IMessage {

    private boolean swing;

    public GOTPacketFireBalista() {
    }

    public GOTPacketFireBalista(boolean swing) {
        this.swing = swing;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.swing = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.swing);
    }

    public static class Handler implements IMessageHandler<GOTPacketFireBalista, IMessage> {

        @Override
        public IMessage onMessage(GOTPacketFireBalista message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if (player.ridingEntity instanceof EntityBalista) {
                EntityBalista balista = (EntityBalista) player.ridingEntity;
                if (message.swing) {
                    balista.tryFire();
                }
            }
            return null;
        }
    }
}