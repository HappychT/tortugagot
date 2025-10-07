package brain.factions.network;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import brain.factions.Faction;
import brain.factions.servers.CollectionGoal;
import brain.factions.servers.CoreFaction;
import brain.factions.servers.Location;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import got.client.gui.faction.GOTGuiFactions;
import got.common.faction.GOTFactionRelations;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class PacketInfoFactions implements IMessage {
	private static final HashMap<String, Faction> factions = new HashMap<>();

	public PacketInfoFactions() {}

	@Override
	public void fromBytes(ByteBuf buf) {
		factions.clear();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			String id = ByteBufUtils.readUTF8String(buf);
			String leadername = ByteBufUtils.readUTF8String(buf);
			String assistantName = ByteBufUtils.readUTF8String(buf);

			HashMap<String, Faction.PlayerData> players = new HashMap<>();
			int sizePlayer = buf.readInt();
			for (int j = 0; j < sizePlayer; j++) {
				String playerName = ByteBufUtils.readUTF8String(buf);
				String prefix = ByteBufUtils.readUTF8String(buf);
				long joinDate = buf.readLong();
				String title = ByteBufUtils.readUTF8String(buf);
				players.put(playerName, new Faction.PlayerData(prefix, joinDate, title));
			}

			HashMap<String, Long> applications = new HashMap<>();
			int sizeApplic = buf.readInt();
			for (int j = 0; j < sizeApplic; j++) {
				applications.put(ByteBufUtils.readUTF8String(buf), 0L);
			}

			HashMap<String, Faction.Title> titles = new HashMap<>();
			int titlesSize = buf.readInt();
			for(int j = 0; j < titlesSize; j++) {
				String titleName = ByteBufUtils.readUTF8String(buf);
				int hierarchy = buf.readInt();
				long permissions = buf.readLong();
				titles.put(titleName, new Faction.Title(titleName, hierarchy, permissions));
			}

			String colorTag = ByteBufUtils.readUTF8String(buf);
			String capitalName = ByteBufUtils.readUTF8String(buf);
			long treasury = buf.readLong();

			Location home = null;
			if (buf.readBoolean()) {
				home = new Location(buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble());
			}
			long lastSetHomeTime = buf.readLong();

			int goalsSize = buf.readInt();
			List<CollectionGoal> goals = new ArrayList<>();
			for(int j = 0; j < goalsSize; j++){
				String goalName = ByteBufUtils.readUTF8String(buf);
				long target = buf.readLong();
				long current = buf.readLong();
				goals.add(new CollectionGoal(goalName, target, current));
			}

			Faction faction = new Faction(id, leadername, assistantName, players, applications, new HashMap<>(), colorTag, home, lastSetHomeTime, capitalName, treasury, titles);

			faction.getCollectionGoals().addAll(goals);

			int proposalsSize = buf.readInt();
			for (int j = 0; j < proposalsSize; j++) {
				String fromFactionID = ByteBufUtils.readUTF8String(buf);
				GOTFactionRelations.Relation relationType = GOTFactionRelations.Relation.forName(ByteBufUtils.readUTF8String(buf));
				long cost = buf.readLong();
				faction.getProposals().put(fromFactionID, new Faction.Proposal(fromFactionID, relationType, cost));
			}

			faction.setMainFortressId(ByteBufUtils.readUTF8String(buf));

			factions.put(id, faction);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (CoreFaction.factions == null) {
			buf.writeInt(0);
			return;
		}

		buf.writeInt(CoreFaction.factions.size());
		for (Faction faction : CoreFaction.factions.values()) {
			ByteBufUtils.writeUTF8String(buf, faction.getID() != null ? faction.getID() : "");
			ByteBufUtils.writeUTF8String(buf, faction.getLeaderName() != null ? faction.getLeaderName() : "");
			ByteBufUtils.writeUTF8String(buf, faction.getAssistantName() != null ? faction.getAssistantName() : "");

			HashMap<String, Faction.PlayerData> players = faction.getPlayers();
			buf.writeInt(players != null ? players.size() : 0);
			if (players != null) {
				for (Map.Entry<String, Faction.PlayerData> map : players.entrySet()) {
					ByteBufUtils.writeUTF8String(buf, map.getKey() != null ? map.getKey() : "");
					Faction.PlayerData pData = map.getValue();
					ByteBufUtils.writeUTF8String(buf, pData.getPrefix() != null ? pData.getPrefix() : "");
					buf.writeLong(pData.getJoinDate());
					ByteBufUtils.writeUTF8String(buf, pData.getTitle() != null ? pData.getTitle() : "Игрок");
				}
			}

			HashMap<String, Long> applications = faction.getApplications();
			buf.writeInt(applications != null ? applications.size() : 0);
			if (applications != null) {
				applications.keySet().forEach((x) -> ByteBufUtils.writeUTF8String(buf, x != null ? x : ""));
			}

			HashMap<String, Faction.Title> titles = faction.getTitles();
			buf.writeInt(titles != null ? titles.size() : 0);
			if (titles != null) {
				List<Faction.Title> sortedTitles = titles.values().stream()
						.sorted(Comparator.comparingInt(Faction.Title::getHierarchy))
						.collect(Collectors.toList());
				for (Faction.Title title : sortedTitles) {
					ByteBufUtils.writeUTF8String(buf, title.getName() != null ? title.getName() : "");
					buf.writeInt(title.getHierarchy());
					buf.writeLong(title.getPermissions());
				}
			}

			ByteBufUtils.writeUTF8String(buf, faction.getColorTag() != null ? faction.getColorTag() : "§f");
			ByteBufUtils.writeUTF8String(buf, faction.getCapitalName() != null ? faction.getCapitalName() : "Не указана");
			buf.writeLong(faction.getTreasury());

			buf.writeBoolean(faction.getHome() != null);
			if (faction.getHome() != null) {
				buf.writeInt(faction.getHome().getWorldID());
				buf.writeDouble(faction.getHome().getX());
				buf.writeDouble(faction.getHome().getY());
				buf.writeDouble(faction.getHome().getZ());
			}
			buf.writeLong(faction.getLastSetHomeTime());

			List<CollectionGoal> goals = faction.getCollectionGoals();
			buf.writeInt(goals != null ? goals.size() : 0);
			if (goals != null) {
				for (CollectionGoal goal : goals) {
					ByteBufUtils.writeUTF8String(buf, goal.getName() != null ? goal.getName() : "");
					buf.writeLong(goal.getTargetAmount());
					buf.writeLong(goal.getCurrentAmount());
				}
			}

			Map<String, Faction.Proposal> proposals = faction.getProposals();
			buf.writeInt(proposals != null ? proposals.size() : 0);
			if (proposals != null) {
				for (Faction.Proposal proposal : proposals.values()) {
					ByteBufUtils.writeUTF8String(buf, proposal.getFromFactionID());
					ByteBufUtils.writeUTF8String(buf, proposal.getRelationType().codeName());
					buf.writeLong(proposal.getCost());
				}
			}

			ByteBufUtils.writeUTF8String(buf, faction.getMainFortressId() != null ? faction.getMainFortressId() : "");
		}
	}

	public static HashMap<String, Faction> getFactions() {
		return factions;
	}

	public static class Handler implements IMessageHandler<PacketInfoFactions, IMessage> {
		public IMessage onMessage(PacketInfoFactions packet, MessageContext ctx) {
			if (Minecraft.getMinecraft().currentScreen instanceof GOTGuiFactions) {
				((GOTGuiFactions) Minecraft.getMinecraft().currentScreen).refresh();
			}
			return null;
		}
	}
}