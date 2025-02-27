package got.common.network.serverToClient;

import cpw.mods.fml.relauncher.Side;
import got.common.network.base.AbstractPacket;
import got.rome.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class PacketSendStamina extends AbstractPacket.AbstractClientMessage<PacketSendStamina> {

    private int stamina;

    public PacketSendStamina() {

    }

    public PacketSendStamina(int stamina) {
        this.stamina = stamina;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.stamina = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(stamina);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        ExtendedPlayer.get(player).setStamina(stamina);
    }
}
