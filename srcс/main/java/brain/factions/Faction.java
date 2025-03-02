package brain.factions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Faction {
	private String ID;
	private String leaderName;
	private String assistantName;
	private HashMap<String, String> players;
	private HashMap<String, Long> applications;
	private HashMap<String, Long> mutePlayer;
	private String colorTag;
	private Location home;
	private long lastSetHomeTime;
	
	public Faction(String ID, String leaderName, String assestantName, HashMap<String, String> players, HashMap<String, Long> applications, HashMap<String, Long> mutePlayer, String colorTag, Location home, long lastSetHomeTime) {
		this.ID = ID;
		this.leaderName = leaderName;
		this.assistantName = assestantName;
		this.players = players;
		this.applications = applications;
		this.mutePlayer = mutePlayer;
		this.colorTag = colorTag;
		this.home = home;
		this.lastSetHomeTime = lastSetHomeTime;
	}
	public String getAssistantName() {
		return assistantName;
	}
	
	public String getID() {
		return ID;
	}
	
	public String getLeaderName() {
		return leaderName;
	}
	
	public HashMap<String, String> getPlayers() {
		return players;
	}
	
	public void setAssistantName(String assistantName) {
		this.assistantName = assistantName;
	}
	
	public void setID(String iD) {
		ID = iD;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	
	public void setPlayers(HashMap<String, String> players) {
		this.players = players;
	}
	
	public HashMap<String, Long> getApplications() {
		return applications;
	}
	
	public HashMap<String, Long> getMutePlayer() {
		return mutePlayer;
	}
	
	public void setLastSetHomeTime(long lastSetHomeTime) {
		this.lastSetHomeTime = lastSetHomeTime;
	}
	
	public long getLastSetHomeTime() {
		return lastSetHomeTime;
	}
	public String getColorTag() {
		return colorTag;
	}
	
	public Location getHome() {
		return home;
	}
	
	public void setHome(Location home) {
		this.home = home;
	}
}
