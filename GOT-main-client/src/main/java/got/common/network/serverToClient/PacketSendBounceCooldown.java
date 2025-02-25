package got.common.network.serverToClient;

import cpw.mods.fml.relauncher.Side;
import got.common.network.base.AbstractPacket;
import got.rome.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class PacketSendBounceCooldown extends AbstractPacket.AbstractClientMessage<PacketSendBounceCooldown> {

    private int bounce;

    public PacketSendBounceCooldown() {

    }

    public PacketSendBounceCooldown(int bounce) {
        this.bounce = bounce;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.bounce = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(bounce);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        ExtendedPlayer.get(player).setBounceCooldown(bounce);
    }
}
