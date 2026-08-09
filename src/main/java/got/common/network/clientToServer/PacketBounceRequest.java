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
            boolean bounced = StaminaServerHandler.INSTANCE.handleBounceRequest(player, direction);

            got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
            if (bounced && ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 7 && ext.getTutorialProgress() == 18) {
                int dodgeCount = brain.tutorial.TutorialManager.getInstance().incrementDodgeCount(player);
                if (dodgeCount >= 3) {
                    brain.tutorial.TutorialManager.getInstance().advanceStage7(player, 19);
                } else {
                    brain.tutorial.TutorialManager.getInstance().setSubtitle(player,
                        String.format(brain.tutorial.TutorialTexts.get("subtitle.stage7.dodge_progress"), dodgeCount, 3));
                }
            }
        }
    }
}