package got.common.network.clientToServer;

import cpw.mods.fml.relauncher.Side;
import got.common.handlers.StaminaServerHandler;
import got.common.network.base.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class PacketBounceRequest extends AbstractPacket.AbstractServerMessage<PacketBounceRequest> {

    private String direction;

    public PacketBounceRequest() {}

    public PacketBounceRequest(String direction) {
        this.direction = direction;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.direction = buffer.readStringFromBuffer(255);
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeStringToBuffer(direction);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if (player != null) {
            //System.out.println("Bouncing in packet " + direction);
            StaminaServerHandler.INSTANCE.handleBounceRequest(player, direction);
        }
    }
}