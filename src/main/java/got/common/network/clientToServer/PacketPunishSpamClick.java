package got.common.network.clientToServer;

import cpw.mods.fml.relauncher.Side;
import got.common.network.base.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;

public class PacketPunishSpamClick extends AbstractPacket.AbstractServerMessage<PacketPunishSpamClick> {

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if (player != null) {
            int duration = 45 * 20;

            player.addPotionEffect(new PotionEffect(Potion.weakness.id, duration, 9));
            
            player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, duration, 9));
            
            player.addPotionEffect(new PotionEffect(Potion.blindness.id, duration, 0));
        }
    }
}