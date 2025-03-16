package brain.factions.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import brain.factions.servers.CoreFaction;
import brain.factions.servers.Faction;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.GOT;
import got.client.gui.GOTGuiFactions;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

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
		

	}

	// SERVER
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(CoreFaction.factions.size());
		for(Faction faction : CoreFaction.factions.values()) {
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