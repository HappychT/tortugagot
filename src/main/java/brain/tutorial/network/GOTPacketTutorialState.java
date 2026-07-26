package brain.tutorial.network;

import brain.tutorial.client.TutorialClientState;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

import java.util.HashSet;

public class GOTPacketTutorialState implements IMessage {
    private boolean isTutorialActive;
    private int tutorialStage;
    private int tutorialProgress;
    private String subtitle;
    private int[] entityIds;

    public GOTPacketTutorialState() {
    }

    public GOTPacketTutorialState(boolean isTutorialActive, int tutorialStage, int tutorialProgress, String subtitle, int[] entityIds) {
        this.isTutorialActive = isTutorialActive;
        this.tutorialStage = tutorialStage;
        this.tutorialProgress = tutorialProgress;
        this.subtitle = subtitle == null ? "" : subtitle;
        this.entityIds = entityIds == null ? new int[0] : entityIds;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.isTutorialActive);
        buf.writeInt(this.tutorialStage);
        buf.writeInt(this.tutorialProgress);
        cpw.mods.fml.common.network.ByteBufUtils.writeUTF8String(buf, this.subtitle);
        buf.writeInt(this.entityIds.length);
        for (int id : this.entityIds) {
            buf.writeInt(id);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isTutorialActive = buf.readBoolean();
        this.tutorialStage = buf.readInt();
        this.tutorialProgress = buf.readInt();
        this.subtitle = cpw.mods.fml.common.network.ByteBufUtils.readUTF8String(buf);
        int length = buf.readInt();
        this.entityIds = new int[length];
        for (int i = 0; i < length; i++) {
            this.entityIds[i] = buf.readInt();
        }
    }

    public static class Handler implements IMessageHandler<GOTPacketTutorialState, IMessage> {
        @Override
        public IMessage onMessage(GOTPacketTutorialState packet, MessageContext ctx) {
            TutorialClientState.isTutorialActive = packet.isTutorialActive;
            TutorialClientState.tutorialStage = packet.tutorialStage;
            TutorialClientState.tutorialProgress = packet.tutorialProgress;
            TutorialClientState.currentSubtitle = packet.subtitle;
            TutorialClientState.tutorialEntities = new HashSet<>();
            for (int id : packet.entityIds) {
                TutorialClientState.tutorialEntities.add(id);
            }
            return null;
        }
    }
}
