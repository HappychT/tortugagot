package got.common.network;

import brain.factions.network.PacketMessage;
import cpw.mods.fml.common.network.simpleimpl.*;
import got.GOT;
import got.common.*;
import got.common.faction.GOTFaction;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class GOTPacketPledgeSet implements IMessage {
	public GOTFaction pledgeFac;

	public GOTPacketPledgeSet() {
	}

	public GOTPacketPledgeSet(GOTFaction f) {
		pledgeFac = f;
	}

	@Override
	public void fromBytes(ByteBuf data) {
		byte facID = data.readByte();
		pledgeFac = facID == -1 ? null : GOTFaction.forID(facID);
	}

	@Override
	public void toBytes(ByteBuf data) {
		int facID = pledgeFac == null ? -1 : pledgeFac.ordinal();
		data.writeByte(facID);
	}

	public static class Handler implements IMessageHandler<GOTPacketPledgeSet, IMessage> {
		@Override
		public IMessage onMessage(GOTPacketPledgeSet packet, MessageContext context) {
			EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
			GOTPlayerData pd = GOTLevelData.getData(entityplayer);
			GOTFaction fac = packet.pledgeFac;
			if (fac == null) {
				// Отвергает
				GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("quet"));
				//pd.revokePledgeFaction(entityplayer, true);
			} else if (pd.canPledgeTo(fac) && pd.canMakeNewPledge()) {
				//Присягает
				GOT.coreFaction.brainChannel.sendToServer(new PacketMessage("sendApplication#" + fac.codeName()));
				//pd.setPledgeFaction(fac);
			}
			return null;
		}
	}

}
