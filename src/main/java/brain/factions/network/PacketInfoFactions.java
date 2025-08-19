package brain.factions.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import brain.factions.Annot;
import brain.factions.Faction;
import brain.factions.servers.CoreFaction;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.GOT;
import got.client.gui.GOTGuiFactions;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

import static brain.factions.Annot.Side.SERVER;

public class PacketInfoFactions implements IMessage {
	private static HashMap<String, Faction> factions = new HashMap<>();


	public PacketInfoFactions() {

	}

	public PacketInfoFactions(HashMap<String, Faction> factions) {
		this.factions = factions;
	}

	// CLIENT
	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			String id = ByteBufUtils.readUTF8String(buf);
			String leadername = ByteBufUtils.readUTF8String(buf);
			String assistantName = ByteBufUtils.readUTF8String(buf);

			HashMap<String, String> players = new HashMap<>();
			int sizePlayer = buf.readInt();

			for (int j = 0; j < sizePlayer; j++) {
				players.put(ByteBufUtils.readUTF8String(buf), ByteBufUtils.readUTF8String(buf));
			}

			HashMap<String, Long> applications = new HashMap<String, Long>();
			int sizeApplic = buf.readInt();
			for (int j = 0; j < sizeApplic; j++) {
				applications.put(ByteBufUtils.readUTF8String(buf), 0L);
			}

			factions.put(id, new Faction(id, leadername, assistantName, players, applications, null, ByteBufUtils.readUTF8String(buf)));
		}

	}
	@Annot(SERVER)
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(CoreFaction.factions.size());
		for(brain.factions.servers.Faction faction : CoreFaction.factions.values()) {
			ByteBufUtils.writeUTF8String(buf, faction.getID());
			ByteBufUtils.writeUTF8String(buf, faction.getLeaderName());
			ByteBufUtils.writeUTF8String(buf, faction.getAssistantName());

			buf.writeInt(faction.getPlayers().size());
			for(Map.Entry<String, String> map : faction.getPlayers().entrySet()) {
				ByteBufUtils.writeUTF8String(buf, map.getKey());
				ByteBufUtils.writeUTF8String(buf, map.getValue());
			}
			buf.writeInt(faction.getApplications().size());
			faction.getApplications().keySet().forEach((x) -> ByteBufUtils.writeUTF8String(buf, x));
			ByteBufUtils.writeUTF8String(buf, faction.getColorTag());
		}
	}

	public static HashMap<String, Faction> getFactions() {
		return factions;
	}
	public static class Handler implements IMessageHandler<PacketInfoFactions, IMessage> {

		public IMessage onMessage(PacketInfoFactions packet, MessageContext ctx) {

			if(Minecraft.getMinecraft().currentScreen instanceof GOTGuiFactions) {
				if(GOTGuiFactions.currentRegion != null) {
					GOTGuiFactions.setFactionInfo(GOTGuiFactions.currentRegion.codeName());
				}
			}
			return null;
		}
	}
}