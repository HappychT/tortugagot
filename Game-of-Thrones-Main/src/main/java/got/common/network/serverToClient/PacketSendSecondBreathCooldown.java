package got.common.network.serverToClient;

import cpw.mods.fml.relauncher.Side;
import got.common.network.base.AbstractPacket;
import got.rome.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class PacketSendSecondBreathCooldown extends AbstractPacket.AbstractClientMessage<PacketSendSecondBreathCooldown>{

    private int cooldown;

    public PacketSendSecondBreathCooldown() {

    }

    public PacketSendSecondBreathCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.cooldown = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(cooldown);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        ExtendedPlayer.get(player).setSecondBreathCooldown(cooldown);
    }
}
