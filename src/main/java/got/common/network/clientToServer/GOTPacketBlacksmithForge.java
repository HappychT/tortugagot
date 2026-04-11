package got.common.network.clientToServer;

import cpw.mods.fml.relauncher.Side;
import got.common.faction.GOTContainerFactionBlacksmith;
import got.common.network.base.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;



public class GOTPacketBlacksmithForge extends AbstractPacket.AbstractServerMessage<GOTPacketBlacksmithForge> {


    public GOTPacketBlacksmithForge() {}

    @Override
    protected void read(PacketBuffer buffer) throws IOException {

    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {

    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if(player != null) {
            ((GOTContainerFactionBlacksmith) player.openContainer).applyWeaponEnchantments();
            ((GOTContainerFactionBlacksmith) player.openContainer).takeEnchantItems(player);
        }
    }
}
