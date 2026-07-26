package brain.tutorial.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class GOTPacketTutorialRestart implements IMessage {
    public GOTPacketTutorialRestart() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<GOTPacketTutorialRestart, IMessage> {
        @Override
        public IMessage onMessage(GOTPacketTutorialRestart packet, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (player != null) {
                got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
                if (ext != null) {
                    ext.setTutorialReplay(true);
                    brain.tutorial.TutorialManager.getInstance().startStage3(player, ext);
                }
            }
            return null;
        }
    }
}
