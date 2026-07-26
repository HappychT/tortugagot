package brain.tutorial.network;

import cpw.mods.fml.relauncher.Side;
import got.common.network.base.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import brain.tutorial.client.TutorialClientState;

import java.io.IOException;

public class GOTPacketTutorialQueue extends AbstractPacket.AbstractClientMessage<GOTPacketTutorialQueue> {

    private boolean inQueue;
    private int position;
    private int totalInQueue;

    public GOTPacketTutorialQueue() {}

    public GOTPacketTutorialQueue(boolean inQueue, int position, int totalInQueue) {
        this.inQueue = inQueue;
        this.position = position;
        this.totalInQueue = totalInQueue;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.inQueue = buffer.readBoolean();
        this.position = buffer.readInt();
        this.totalInQueue = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeBoolean(this.inQueue);
        buffer.writeInt(this.position);
        buffer.writeInt(this.totalInQueue);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if (side == Side.CLIENT) {
            TutorialClientState.inQueue = this.inQueue;
            TutorialClientState.queuePosition = this.position;
            TutorialClientState.queueTotal = this.totalInQueue;
        }
    }
}
