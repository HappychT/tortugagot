package brain.tutorial.network;

import brain.tutorial.TutorialManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class GOTPacketTutorialAdvance implements IMessage, IMessageHandler<GOTPacketTutorialAdvance, IMessage> {

    public GOTPacketTutorialAdvance() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public IMessage onMessage(GOTPacketTutorialAdvance message, MessageContext ctx) {
        if (ctx.side == cpw.mods.fml.relauncher.Side.SERVER) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            TutorialManager.INSTANCE.advanceStage3(player);
        }
        return null;
    }
}
