package brain.tutorial.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class GOTPacketTutorialWelcomeOpen implements IMessage {
    
    public GOTPacketTutorialWelcomeOpen() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<GOTPacketTutorialWelcomeOpen, IMessage> {
        @Override
        public IMessage onMessage(GOTPacketTutorialWelcomeOpen packet, MessageContext ctx) {
            got.GOT.proxy.openTutorialWelcomeGui();
            return null;
        }
    }
}
