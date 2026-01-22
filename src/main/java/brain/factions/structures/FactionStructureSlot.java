package brain.factions.structures;

public class FactionStructureSlot {
    public String id;
    public String name;
    public StructureType type;
    public String resource;
    public int mapX;
    public int mapY;
    public String initial_faction;
    public String for_fraction;
    public int price;
    public String structureFile;

    public int xCoord;
    public int yCoord;
    public int zCoord;

    public String ownerFactionID;
    public int level;
    public long raidCooldown;

    public StructureCategory category;
    public long lastCollectionTime;
    public int health;
    public int destructionCount;

    public int provisions;
    public int barracksCapacity;

    public String raidTime = "";

    public static enum StructureType {
        FORTRESS, RESOURCE_POINT, BARN, ENGINEERING_WORKSHOP;
    }

    public static enum StructureCategory {
        NONE, FARMS, INDUSTRY, BARN, ENGINEERING_WORKSHOP, FORTRESS
    }
}