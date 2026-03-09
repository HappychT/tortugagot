package brain.factions;

import brain.factions.servers.CollectionGoal;
import brain.factions.servers.Location;
import got.common.faction.GOTFactionRelations.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Faction {
	private String ID;
	private String leaderName;
	private String assistantName;
	private HashMap<String, PlayerData> players;
	private HashMap<String, Long> applications;
	private HashMap<String, Long> mutePlayer;
	private String colorTag;
	private String capitalName;
	private long treasury;
	private List<CollectionGoal.Transaction> treasuryHistory;
	private List<CollectionGoal> collectionGoals;
	private HashMap<String, Title> titles;
	private Map<String, Proposal> proposals;

	private boolean inWarState;
	private long warTaxOwed;
	private int unpaidWarTaxDays;
	private String mainFortressId;

	private Location home;
	private long lastSetHomeTime;

	public Faction(String ID, String leaderName, String assistantName, HashMap<String, PlayerData> players, HashMap<String, Long> applications, HashMap<String, Long> mutePlayer, String colorTag, Location home, long lastSetHomeTime, String capitalName, long treasury, HashMap<String, Title> titles) {
		this.ID = ID;
		this.leaderName = leaderName;
		this.assistantName = assistantName;
		this.players = players;
		this.applications = applications;
		this.mutePlayer = mutePlayer;
		this.colorTag = colorTag;
		this.home = home;
		this.lastSetHomeTime = lastSetHomeTime;
		this.capitalName = capitalName;
		this.treasury = treasury;
		this.treasuryHistory = new ArrayList<>();
		this.collectionGoals = new ArrayList<>();
		this.titles = titles;
		this.proposals = new HashMap<>();
		this.inWarState = false;
		this.warTaxOwed = 0;
		this.unpaidWarTaxDays = 0;
		this.mainFortressId = "";
	}

	public void logTreasuryTransaction(String playerName, long amount) {
		if (this.treasuryHistory == null) {
			this.treasuryHistory = new ArrayList<>();
		}
		this.treasuryHistory.add(new CollectionGoal.Transaction(playerName, amount, System.currentTimeMillis()));
	}

	public List<CollectionGoal.Transaction> getTreasuryHistory() {
		return treasuryHistory;
	}

	public boolean playerHasPermission(String playerName, Permission perm) {
		if (playerName.equals(leaderName) || playerName.equals(assistantName)) return true;
		PlayerData pData = players.get(playerName);
		if (pData == null) return false;
		Title title = titles.get(pData.getTitle());
		if (title == null) return false;
		return title.hasPermission(perm);
	}

	public String getAssistantName() { return assistantName; }
	public String getID() { return ID; }
	public String getLeaderName() { return leaderName; }
	public HashMap<String, PlayerData> getPlayers() { return players; }
	public void setAssistantName(String assistantName) { this.assistantName = assistantName; }
	public void setID(String iD) { ID = iD; }
	public void setLeaderName(String leaderName) { this.leaderName = leaderName; }
	public void setPlayers(HashMap<String, PlayerData> players) { this.players = players; }
	public HashMap<String, Long> getApplications() { return applications; }
	public HashMap<String, Long> getMutePlayer() { return mutePlayer; }
	public String getColorTag() { return colorTag; }
	public String getCapitalName() { return capitalName; }
	public long getTreasury() { return treasury; }
	public void setTreasury(long treasury) { this.treasury = treasury; }
	public List<CollectionGoal> getCollectionGoals() { return collectionGoals; }
	public HashMap<String, Title> getTitles() { return titles; }
	public Location getHome() { return home; }
	public void setHome(Location home) { this.home = home; }
	public long getLastSetHomeTime() { return lastSetHomeTime; }
	public void setLastSetHomeTime(long lastSetHomeTime) { this.lastSetHomeTime = lastSetHomeTime; }
	public Map<String, Proposal> getProposals() { return proposals; }

	public boolean isInWarState() { return inWarState; }
	public void setInWarState(boolean inWarState) { this.inWarState = inWarState; }
	public long getWarTaxOwed() { return warTaxOwed; }
	public void setWarTaxOwed(long warTaxOwed) { this.warTaxOwed = warTaxOwed; }
	public int getUnpaidWarTaxDays() { return unpaidWarTaxDays; }
	public void setUnpaidWarTaxDays(int unpaidWarTaxDays) { this.unpaidWarTaxDays = unpaidWarTaxDays; }
	public String getMainFortressId() { return mainFortressId; }
	public void setMainFortressId(String mainFortressId) { this.mainFortressId = mainFortressId; }


	public static class PlayerData {
		private String prefix;
		private long joinDate;
		private String title;

		public PlayerData(String prefix, long joinDate, String title) {
			this.prefix = prefix;
			this.joinDate = joinDate;
			this.title = title;
		}

		public String getPrefix() { return prefix; }
		public void setPrefix(String prefix) { this.prefix = prefix; }
		public long getJoinDate() { return joinDate; }
		public String getTitle() { return title; }
		public void setTitle(String title) { this.title = title; }

		@Override
		public String toString() {
			return this.prefix;
		}
	}

	public static class Title {
		private final String name;
		private int hierarchy;
		private final long permissions;

		public Title(String name, int hierarchy, long permissions) {
			this.name = name;
			this.hierarchy = hierarchy;
			this.permissions = permissions;
		}

		public String getName() { return name; }
		public int getHierarchy() { return hierarchy; }
		public void setHierarchy(int hierarchy) { this.hierarchy = hierarchy; }
		public long getPermissions() { return permissions; }

		public boolean hasPermission(Permission perm) {
			return (permissions & perm.getBit()) != 0;
		}
	}

	public static class Proposal {
		private String fromFactionID;
		private Relation relationType;
		private long cost;

		public Proposal(String fromFactionID, Relation relationType, long cost) {
			this.fromFactionID = fromFactionID;
			this.relationType = relationType;
			this.cost = cost;
		}

		public String getFromFactionID() {
			return fromFactionID;
		}

		public Relation getRelationType() {
			return relationType;
		}

		public long getCost() {
			return cost;
		}
	}

	public enum Permission {
		CAN_ACCEPT_APPLICATIONS("Принимать/отклонять заявки", 1L << 0),
		CAN_KICK_MEMBERS("Выгонять игроков", 1L << 1),
		CAN_CREATE_TITLES("Создавать/назначать титулы", 1L << 2),
		CAN_MANAGE_HIERARCHY("Менять иерархию титулов", 1L << 3),
		CAN_MANAGE_TREASURY("Управлять казной", 1L << 4),
		CAN_USE_TREASURY("Использовать казну", 1L << 5),
		CAN_MANAGE_BARRACKS("Управлять казармой", 1L << 6),
		CAN_MANAGE_STRUCTURES("Захватывать и улучшать точки", 1L << 7),
		CAN_INTERACT_RESOURCE_POINTS("Взаимодействие с ресурсными точками", 1L << 8),
		CAN_BUY_SIEGE_WEAPONS("Приобретение осадных орудий", 1L << 9),
		CAN_UPGRADE_STRUCTURES("Улучшения зданий", 1L << 10);
		private final String description;
		private final long bit;

		Permission(String desc, long bit) {
			this.description = desc;
			this.bit = bit;
		}

		public String getDescription() { return description; }
		public long getBit() { return bit; }
	}
}