package got.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.GOT;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public class LOTRPacketParticleFX implements IMessage {
    private String particleName;
    private int count;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private double posX;
    private double posY;
    private double posZ;

    public LOTRPacketParticleFX() {
    }

    public LOTRPacketParticleFX(String particle, int count, double x, double y, double z, double velX, double velY, double velZ) {
        this.particleName = particle;
        this.count = count;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.velocityX = velX;
        this.velocityY = velY;
        this.velocityZ = velZ;
    }

    @Override
    public void toBytes(ByteBuf data) {
        ByteBufUtils.writeUTF8String(data, particleName);
        data.writeInt(count);
        data.writeDouble(posX);
        data.writeDouble(posY);
        data.writeDouble(posZ);
        data.writeDouble(velocityX);
        data.writeDouble(velocityY);
        data.writeDouble(velocityZ);
    }

    @Override
    public void fromBytes(ByteBuf data) {
        particleName = ByteBufUtils.readUTF8String(data);
        count = data.readInt();
        posX = data.readDouble();
        posY = data.readDouble();
        posZ = data.readDouble();
        velocityX = data.readDouble();
        velocityY = data.readDouble();
        velocityZ = data.readDouble();
    }

    public static class Handler implements IMessageHandler<LOTRPacketParticleFX, IMessage> {
        @Override
        public IMessage onMessage(LOTRPacketParticleFX packet, MessageContext context) {
            World world = GOT.proxy.getClientWorld();
            for (int i = 0; i < packet.count; i++) {
                world.spawnParticle(packet.particleName, packet.posX, packet.posY, packet.posZ, packet.velocityX, packet.velocityY, packet.velocityZ);
            }
            return null;
        }
    }
}
