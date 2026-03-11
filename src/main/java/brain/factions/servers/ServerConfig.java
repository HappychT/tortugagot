package brain.factions.servers;

import net.minecraftforge.common.config.Configuration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ServerConfig {

    private final Configuration config;

    public int resourceGenerationInterval;
    public int structureBaseHealth;
    public int maxStructureLevel;
    public int maxStructuresPerFaction;
    public int maxFortressesPerFaction;

    public Map<Integer, Integer> upgradeCost = new HashMap<>();
    public int warStateRecruitmentCost;
    public int warTaxPerMember;

    public int provisionCostPerBarracksSlot;

    public Map<Integer, Integer> provisionsPerCollection = new HashMap<>();
    public Map<Integer, Integer> resourcesPerCollection = new HashMap<>();
    public Map<Integer, String[]> resourceItems = new HashMap<>();

    public Map<Integer, Integer> structureBreakCounts = new HashMap<>();
    public Map<Integer, Integer> fortressBarracksCapacity = new HashMap<>();

    public String raidTimeResourcePoints;
    public Map<String, String> specificRaidTimes = new HashMap<>();

    public ServerConfig(File file) {
        this.config = new Configuration(file);
        syncConfig();
    }

    public void syncConfig() {
        config.load();

        resourceGenerationInterval = config.getInt("resourceGenerationInterval", "general", 30, 1, Integer.MAX_VALUE, "Интервал генерации ресурсов в секундах.");
        structureBaseHealth = config.getInt("structureBaseHealth", "general", 1000, 100, Integer.MAX_VALUE, "Базовое здоровье структуры (прочность).");
        maxStructureLevel = config.getInt("maxStructureLevel", "general", 5, 1, Integer.MAX_VALUE, "Максимальный уровень улучшения для структур.");
        maxStructuresPerFaction = config.getInt("maxStructuresPerFaction", "general", 10, 1, Integer.MAX_VALUE, "Максимальное количество ресурсных точек на фракцию.");
        maxFortressesPerFaction = config.getInt("maxFortressesPerFaction", "general", 5, 1, Integer.MAX_VALUE, "Максимальное количество крепостей на фракцию.");

        warStateRecruitmentCost = config.getInt("warStateRecruitmentCost", "costs", 25000, 0, Integer.MAX_VALUE, "Стоимость принятия нового участника во время войны.");
        warTaxPerMember = config.getInt("warTaxPerMember", "costs", 250, 0, Integer.MAX_VALUE, "Ежедневный военный налог за каждого участника фракции.");
        provisionCostPerBarracksSlot = config.getInt("provisionCostPerBarracksSlot", "costs", 500, 1, Integer.MAX_VALUE, "Стоимость одного слота в казарме (в единицах продовольствия).");

        raidTimeResourcePoints = config.getString("raidTimeResourcePoints", "general", "18:00-20:00", "Общее время рейда для ресурсных точек (HH:mm-HH:mm).");

        String[] categories = {"FARMS", "INDUSTRY", "BARN", "ENGINEERING_WORKSHOP", "FORTRESS"};
        for(String cat : categories) {
            String time = config.getString("raidTime_" + cat, "raid_times", raidTimeResourcePoints, "Время рейда для категории " + cat);
            specificRaidTimes.put(cat, time);
        }

        for (int i = 1; i <= maxStructureLevel; i++) {
            upgradeCost.put(i, config.getInt("upgradeCostLevel" + i, "costs", 5000 * i, 100, Integer.MAX_VALUE, "Стоимость улучшения до уровня " + i));
        }

        for (int i = 1; i <= maxStructureLevel; i++) {
            provisionsPerCollection.put(i, config.getInt("provisionsLevel" + i, "production", 10 * i, 1, Integer.MAX_VALUE, "Кол-во продовольствия для амбара уровня " + i));
            resourcesPerCollection.put(i, config.getInt("resourcesLevel" + i, "production", 5 * i, 1, Integer.MAX_VALUE, "Кол-во ресурсов для ресурсной точки уровня " + i));
            resourceItems.put(i, config.getStringList("resourceItemsLevel" + i, "production", new String[]{"minecraft:coal", "minecraft:iron_ingot"}, "Предметы, которые генерирует ресурсная точка уровня " + i + " в формате 'modid:item_name'"));
            fortressBarracksCapacity.put(i, config.getInt("fortressBarracksCapacity" + i, "fortress", 10 * i, 1, Integer.MAX_VALUE, "Вместимость казарм в крепости уровня " + i));
        }

        for (int i = 1; i <= maxStructureLevel; i++) {
            structureBreakCounts.put(i, config.getInt("structureBreakCountLevel" + i, "raiding", i, 1, Integer.MAX_VALUE, "Количество разрушений, необходимых для полного уничтожения структуры уровня " + i));
        }


        if (config.hasChanged()) {
            config.save();
        }
    }
}