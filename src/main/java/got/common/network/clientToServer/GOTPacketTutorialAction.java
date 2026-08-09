package got.common.network.clientToServer;

import cpw.mods.fml.relauncher.Side;
import got.common.network.base.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.common.network.ByteBufUtils;

import java.io.IOException;

public class GOTPacketTutorialAction extends AbstractPacket.AbstractServerMessage<GOTPacketTutorialAction> {

    private String action;

    public GOTPacketTutorialAction() {}

    public GOTPacketTutorialAction(String action) {
        this.action = action;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.action = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        ByteBufUtils.writeUTF8String(buffer, this.action);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if (player != null && action != null) {
            got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
            if (ext != null) {
                if (action.equals("join_queue") && ext.getTutorialStage() == 0) {
                    brain.tutorial.TutorialQueue.getInstance().addToQueue(player);
                } else if (ext.isTutorialActive()) {
                    if (action.equals("start_combat") && ext.getTutorialStage() <= 1) {
                        brain.tutorial.TutorialManager.getInstance().startCombat(player);
                    } else if (action.equals("stage9_resources") && ext.getTutorialStage() == 9) {
                        player.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(net.minecraft.init.Blocks.planks, 64, 0));
                        player.inventoryContainer.detectAndSendChanges();
                        brain.tutorial.TutorialManager.getInstance().advanceStage9(player, 1);
                    } else if (action.equals("dodge") && ext.getTutorialStage() == 7 && ext.getTutorialProgress() == 18) {
                        // Require 3 dodges before advancing
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
    }
}
