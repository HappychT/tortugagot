package got.common.network.clientToServer;

import cpw.mods.fml.relauncher.Side;
import got.common.handlers.StaminaServerHandler;
import got.common.network.base.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class PacketBounceRequest extends AbstractPacket.AbstractServerMessage<PacketBounceRequest> {

    private int direction;

    public PacketBounceRequest() {}

    public PacketBounceRequest(int direction) {
        this.direction = direction;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.direction = buffer.readVarIntFromBuffer();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeVarIntToBuffer(direction);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if (player != null) {
            //System.out.println("Bouncing in packet " + direction);
            StaminaServerHandler.INSTANCE.handleBounceRequest(player, direction);
        }
    }
}