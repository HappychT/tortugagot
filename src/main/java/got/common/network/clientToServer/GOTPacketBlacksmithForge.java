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
            
            got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
            if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 6) {
                int prog = ext.getTutorialProgress();
                if (prog < 4) {
                    // Бронник: перековка нагрудника → переход к оружейнику
                    brain.tutorial.TutorialManager.getInstance().advanceStage6(player, 4);
                } else if (prog >= 5 && prog < 8) {
                    // Оружейник: перековка меча → готово (сервер не знает о промежуточных шагах 6 и 7)
                    brain.tutorial.TutorialManager.getInstance().advanceStage6(player, 8);
                }
            }
        }
    }
}
