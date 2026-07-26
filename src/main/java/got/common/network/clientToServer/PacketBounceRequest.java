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

            got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
            if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 7 && ext.getTutorialProgress() == 10) {
                net.minecraft.item.ItemStack held = player.getHeldItem();
                boolean validWeapon = true;
                if (held != null) {
                    if (held.getItem() instanceof got.common.item.weapon.GOTItemShieldSpear || 
                        held.getItem() instanceof got.common.item.weapon.GOTItemShieldPike ||
                        held.getItem() == got.common.database.GOTRegistry.ironSpear ||
                        held.getItem() == got.common.database.GOTRegistry.ironPike) {
                        validWeapon = false;
                    }
                } else {
                    validWeapon = false; // must hold a weapon
                }
                
                if (validWeapon) {
                    brain.tutorial.TutorialManager.getInstance().advanceStage7(player, 11);
                }
            }
        }
    }
}