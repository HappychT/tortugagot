package got.common.network.clientToServer;

import cpw.mods.fml.relauncher.Side;
import got.common.faction.GOTContainerFactionBlacksmith;
import got.common.network.base.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;



public class GOTPacketBlacksmithEnchantmentButton extends AbstractPacket.AbstractServerMessage<GOTPacketBlacksmithEnchantmentButton> {

    private int slot;

    public GOTPacketBlacksmithEnchantmentButton() {}

    public GOTPacketBlacksmithEnchantmentButton(int slot) {
        this.slot = slot;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.slot = buffer.readVarIntFromBuffer();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeVarIntToBuffer(slot);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if(player != null) {
            ((GOTContainerFactionBlacksmith) player.openContainer).switchButtonState(slot);
        }
    }
}
