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

    public ServerConfig(File file) {
        this.config = new Configuration(file);
        syncConfig();
    }

    public void syncConfig() {
        config.load();

        resourceGenerationInterval = config.getInt("resourceGenerationInterval", "general", 30, 1, 3600, "Интервал генерации ресурсов в секундах.");
        structureBaseHealth = config.getInt("structureBaseHealth", "general", 1000, 100, 10000, "Базовое здоровье структуры (прочность).");
        maxStructureLevel = config.getInt("maxStructureLevel", "general", 5, 1, 10, "Максимальный уровень улучшения для структур.");
        maxStructuresPerFaction = config.getInt("maxStructuresPerFaction", "general", 10, 1, 100, "Максимальное количество ресурсных точек на фракцию.");
        maxFortressesPerFaction = config.getInt("maxFortressesPerFaction", "general", 5, 1, 20, "Максимальное количество крепостей на фракцию.");

        warStateRecruitmentCost = config.getInt("warStateRecruitmentCost", "costs", 25000, 0, 1000000, "Стоимость принятия нового участника во время войны.");
        warTaxPerMember = config.getInt("warTaxPerMember", "costs", 250, 0, 10000, "Ежедневный военный налог за каждого участника фракции.");
        provisionCostPerBarracksSlot = config.getInt("provisionCostPerBarracksSlot", "costs", 500, 1, 100000, "Стоимость одного слота в казарме (в единицах продовольствия).");


        for (int i = 1; i <= maxStructureLevel; i++) {
            upgradeCost.put(i, config.getInt("upgradeCostLevel" + i, "costs", 5000 * i, 100, 1000000, "Стоимость улучшения до уровня " + i));
        }

        for (int i = 1; i <= maxStructureLevel; i++) {
            provisionsPerCollection.put(i, config.getInt("provisionsLevel" + i, "production", 10 * i, 1, 1000, "Кол-во продовольствия для амбара уровня " + i));
            resourcesPerCollection.put(i, config.getInt("resourcesLevel" + i, "production", 5 * i, 1, 1000, "Кол-во ресурсов для ресурсной точки уровня " + i));
            resourceItems.put(i, config.getStringList("resourceItemsLevel" + i, "production", new String[]{"minecraft:coal", "minecraft:iron_ingot"}, "Предметы, которые генерирует ресурсная точка уровня " + i + " в формате 'modid:item_name'"));
            fortressBarracksCapacity.put(i, config.getInt("fortressBarracksCapacity" + i, "fortress", 10 * i, 1, 100, "Вместимость казарм в крепости уровня " + i));
        }

        for (int i = 1; i <= maxStructureLevel; i++) {
            structureBreakCounts.put(i, config.getInt("structureBreakCountLevel" + i, "raiding", i, 1, 20, "Количество разрушений, необходимых для полного уничтожения структуры уровня " + i));
        }


        if (config.hasChanged()) {
            config.save();
        }
    }
}