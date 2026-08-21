package brain.factions.structures;

public class FactionStructureSlot {
    public String id;
    public String name;
    public StructureCategory category;
    public String resource;
    public int mapX;
    public int mapY;
    public String initial_faction;
    public String for_fraction;
    public int price;
    public String structureFile;
    public int securityLevel = 0;
    public int barnLevel = 0;
    public int workshopLevel = 0;
    public int stableLevel = 0;
    public int barracksLevel = 0;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    public float provisions;
    public int storedFoodItems;
    public int barracksCapacity;
    public String ownerFactionID;
    public int level;
    public boolean canBuild = true;

    public long lastCollectionTime;
    public int destructionCount;
    public long lastSiegePurchaseTime;

    public String raidTime = "";
    public int raidDays = 1; // 1 = каждый день, 2 = раз в 2 дня и т.д. 0 = нет ограничения по дням

    public static enum StructureCategory {
        NONE, FARMS, INDUSTRY, BARN, ENGINEERING_WORKSHOP, FORTRESS, STABLE
    }
}