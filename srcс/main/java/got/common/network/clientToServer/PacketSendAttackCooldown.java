package got.common.network.clientToServer;

import cpw.mods.fml.relauncher.Side;
import got.common.network.base.AbstractPacket;
import got.rome.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

import static got.common.libs.GOTLib.getAttackCooldown;


public class PacketSendAttackCooldown extends AbstractPacket.AbstractServerMessage<PacketSendAttackCooldown> {

    @Override
    protected void read(PacketBuffer buffer) throws IOException {

    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {

    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if(player != null) {
            if(ExtendedPlayer.get(player).getAttackCooldown() <= 0) {
                ExtendedPlayer.get(player).setAttackCooldown(getAttackCooldown(player));
            }
        }
    }
}
