package got.common.network;

import brain.factions.network.PacketMessage;
import cpw.mods.fml.common.network.simpleimpl.*;
import got.GOT;
import got.common.*;
import got.common.faction.GOTFaction;
import got.rome.ExtendedPlayer;
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
				brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("quet"));
			} else if (pd.canPledgeTo(fac) && pd.canMakeNewPledge()) {
				brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("sendApplication#" + fac.codeName()));
			} else {
				ExtendedPlayer ext = ExtendedPlayer.get(entityplayer);
				if (ext != null && ext.getTutorialStage() == 3) {
					// FORCE JOIN FOR TUTORIAL
					pd.setPledgeFaction(fac);
					brain.factions.Faction factionData = brain.factions.servers.CoreFaction.factions.get(fac.codeName());
					if (factionData != null) {
						long perms = brain.factions.Faction.Permission.CAN_CREATE_TITLES.getBit() | 
									 brain.factions.Faction.Permission.CAN_MANAGE_HIERARCHY.getBit() | 
									 brain.factions.Faction.Permission.CAN_MANAGE_TREASURY.getBit();
						brain.factions.Faction.Title tutTitle = new brain.factions.Faction.Title("\u041e\u0431\u0443\u0447\u0435\u043d\u0438\u0435", 0, perms);
						factionData.getTitles().put("\u041e\u0431\u0443\u0447\u0435\u043d\u0438\u0435", tutTitle);
						factionData.getPlayers().put(entityplayer.getCommandSenderName(), new brain.factions.Faction.PlayerData("", System.currentTimeMillis(), "\u041e\u0431\u0443\u0447\u0435\u043d\u0438\u0435"));
						brain.factions.servers.CoreFaction.brainChannel.sendToAll(new brain.factions.network.PacketInfoFactions());
					}
				}
			}
			return null;
		}
	}
}
