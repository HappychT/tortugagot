package got.common.network;

import java.util.HashMap;
import java.util.Map;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.GOT;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import io.netty.buffer.ByteBuf;

public class GOTPacketAlignmentSee implements IMessage {
	public String username;
	public String factionName;
	public Map<GOTFaction, Float> alignmentMap = new HashMap<>();

	public GOTPacketAlignmentSee() {
	}

	public GOTPacketAlignmentSee(String name, GOTPlayerData pd) {
		this.username = name;
		GOTFaction pledgedFaction = pd.getPledgeFaction();
		this.factionName = pledgedFaction != null ? pledgedFaction.codeName() : "";
		for (GOTFaction f : GOTFaction.getPlayableAlignmentFactions()) {
			float al = pd.getAlignment(f);
			alignmentMap.put(f, al);
		}
	}

	@Override
	public void fromBytes(ByteBuf data) {
		username = ByteBufUtils.readUTF8String(data);
		factionName = ByteBufUtils.readUTF8String(data);
		int mapSize = data.readInt();
		alignmentMap = new HashMap<>();
		for (int i = 0; i < mapSize; i++) {
			String facName = ByteBufUtils.readUTF8String(data);
			float alignment = data.readFloat();
			GOTFaction f = GOTFaction.forName(facName);
			if (f != null) {
				alignmentMap.put(f, alignment);
			}
		}
	}

	@Override
	public void toBytes(ByteBuf data) {
		ByteBufUtils.writeUTF8String(data, username);
		ByteBufUtils.writeUTF8String(data, factionName);
		data.writeInt(alignmentMap.size());
		for (Map.Entry<GOTFaction, Float> entry : alignmentMap.entrySet()) {
			GOTFaction f = entry.getKey();
			float alignment = entry.getValue();
			ByteBufUtils.writeUTF8String(data, f.codeName());
			data.writeFloat(alignment);
		}
	}

	public static class Handler implements IMessageHandler<GOTPacketAlignmentSee, IMessage> {
		@Override
		public IMessage onMessage(GOTPacketAlignmentSee packet, MessageContext context) {
			GOT.proxy.displayAlignmentSee(packet.username, packet.factionName, packet.alignmentMap);
			return null;
		}
	}
}