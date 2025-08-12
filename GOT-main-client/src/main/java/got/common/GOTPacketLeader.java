package got.common;

import cpw.mods.fml.common.network.simpleimpl.*;
import got.GOT;
import got.common.*;
import got.common.faction.GOTFaction;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class GOTPacketLeader implements IMessage {
    public GOTFaction leaderFaction;

    public GOTPacketLeader() {
    }

    public GOTPacketLeader(GOTFaction f) {
        leaderFaction = f;
    }

    @Override
    public void fromBytes(ByteBuf data) {
        byte facID = data.readByte();
        leaderFaction = facID == -1 ? null : GOTFaction.forID(facID);
    }

    @Override
    public void toBytes(ByteBuf data) {
        int facID = leaderFaction == null ? -1 : leaderFaction.ordinal();
        data.writeByte(facID);
    }

    public static class Handler implements IMessageHandler<GOTPacketLeader, IMessage> {
        @Override
        public IMessage onMessage(GOTPacketLeader packet, MessageContext context) {
            if (!GOT.proxy.isSingleplayer()) {
                EntityPlayer entityplayer = GOT.proxy.getClientPlayer();
                GOTPlayerData pd = GOTLevelData.getData(entityplayer);
                pd.setLeaderFaction(packet.leaderFaction);
            }
            return null;
        }
    }

}
