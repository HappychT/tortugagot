package got.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import noname.weapons.entity.EntityBatteringRam;

public class GOTPacketActivateRam implements IMessage {

    private int entityId;
    private boolean status;

    public GOTPacketActivateRam() {
    }

    public GOTPacketActivateRam(int entityId, boolean status) {
        this.entityId = entityId;
        this.status = status;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.status = buf.readBoolean();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeBoolean(this.status);

    }

    public static class Handler implements IMessageHandler<GOTPacketActivateRam, IMessage> {

        @Override
        public IMessage onMessage(GOTPacketActivateRam message, MessageContext ctx) {
            int entityId = message.entityId;
            boolean status = message.status;

            World world = ctx.getServerHandler().playerEntity.worldObj;

            Entity entity = world.getEntityByID(entityId);
            if (entity instanceof EntityBatteringRam) {
                EntityBatteringRam batteringRam = (EntityBatteringRam) entity;
                EntityPlayer driver = batteringRam.getDriver();
                if (driver != null && driver.getEntityId() == ctx.getServerHandler().playerEntity.getEntityId())
                    ((EntityBatteringRam) entity).setStatus(status);
            }

            return null;
        }
    }
}