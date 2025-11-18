package brain.factions.arenas;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ArenaManager {

    public static final ArenaManager instance = new ArenaManager();
    private File configFile;
    private Configuration config;

    private final Map<String, ArenaRegion> regions = new HashMap<>();

    private static final String CATEGORY_REGIONS = "regions";

    private ArenaManager() {
    }


    public void initConfig(File modConfigDir) {
        configFile = new File(modConfigDir, "savearenas.cfg");
        config = new Configuration(configFile);
        loadRegions();
    }


    public void loadRegions() {
        regions.clear();
        config.load();

        if (!config.hasCategory(CATEGORY_REGIONS)) {
            config.save();
            return;
        }


        Set<ConfigCategory> regionCategories = config.getCategory(CATEGORY_REGIONS).getChildren();

        for (ConfigCategory regionCat : regionCategories) {
            String regionName = regionCat.getName();

            String categoryPath = regionCat.getQualifiedName();

            try {
                int dim = config.get(categoryPath, "dimensionId", 0).getInt();
                int minX = config.get(categoryPath, "minX", 0).getInt();
                int minY = config.get(categoryPath, "minY", 0).getInt();
                int minZ = config.get(categoryPath, "minZ", 0).getInt();
                int maxX = config.get(categoryPath, "maxX", 0).getInt();
                int maxY = config.get(categoryPath, "maxY", 0).getInt();
                int maxZ = config.get(categoryPath, "maxZ", 0).getInt();

                ArenaRegion region = new ArenaRegion(regionName, dim, minX, minY, minZ, maxX, maxY, maxZ);
                regions.put(regionName, region);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void addAndSaveRegion(ArenaRegion region) {
        regions.put(region.getName(), region);

        String category = CATEGORY_REGIONS + "." + region.getName();

        config.get(category, "dimensionId", region.getDimensionId()).set(region.getDimensionId());
        config.get(category, "minX", region.getMinX()).set(region.getMinX());
        config.get(category, "minY", region.getMinY()).set(region.getMinY());
        config.get(category, "minZ", region.getMinZ()).set(region.getMinZ());
        config.get(category, "maxX", region.getMaxX()).set(region.getMaxX());
        config.get(category, "maxY", region.getMaxY()).set(region.getMaxY());
        config.get(category, "maxZ", region.getMaxZ()).set(region.getMaxZ());

        config.save();
    }

    public boolean isPlayerInAnyRegion(EntityPlayer player) {
        for (ArenaRegion region : regions.values()) {
            if (region.isPlayerInside(player)) {
                return true;
            }
        }
        return false;
    }
}