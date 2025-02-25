package brain.factions;

public class Location {
	private int worldID;
	private double x;
	private double y;
	private double z;
	
	public Location(int worldID, double x, double y, double z) {
		this.worldID = worldID;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getWorldID() {
		return worldID;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	
}
