package brain.tutorial;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class TutorialConfig {
    public static Configuration config;
    
    // Original generation coords
    public static int stage1_x, stage1_y, stage1_z;
    public static int stage6_x, stage6_y, stage6_z;
    public static int stage7_x, stage7_y, stage7_z;
    public static int stage8_x, stage8_y, stage8_z;
    public static int stage9_x, stage9_y, stage9_z;
    public static int stage10_x, stage10_y, stage10_z;
    
    // Banner generation coords
    public static boolean generateBannerPlatforms = true;
    public static int banner_start_x, banner_start_y, banner_start_z;
    public static int banner_interval;
    
    public static int end_x, end_y, end_z;
    public static String[] reward_items;
    
    public static boolean generatePlatforms = true;
    
    // Queue configuration
    public static int queue_interval;
    public static int limbo_dimension;
    public static int limbo_x, limbo_y, limbo_z;

    
    // Entity specific coords
    public static int captain_stage1_x, captain_stage1_y, captain_stage1_z;
    public static int drunkard1_stage1_x, drunkard1_stage1_y, drunkard1_stage1_z;
    public static int drunkard2_stage1_x, drunkard2_stage1_y, drunkard2_stage1_z;
    
    public static int dummy1_stage7_x, dummy1_stage7_y, dummy1_stage7_z;
    public static int dummy2_stage7_x, dummy2_stage7_y, dummy2_stage7_z;
    public static int dummy3_stage7_x, dummy3_stage7_y, dummy3_stage7_z;
    
    public static int boatswain_stage7_x, boatswain_stage7_y, boatswain_stage7_z;
    public static int sailor_stage7_x, sailor_stage7_y, sailor_stage7_z;
    
    public static int mutineer1_stage8_x, mutineer1_stage8_y, mutineer1_stage8_z;
    public static int mutineer2_stage8_x, mutineer2_stage8_y, mutineer2_stage8_z;
    public static int mutineer3_stage8_x, mutineer3_stage8_y, mutineer3_stage8_z;
    public static int captain_stage8_x, captain_stage8_y, captain_stage8_z;
    
    public static int captain_stage9_x, captain_stage9_y, captain_stage9_z;
    public static int jaqen_stage10_x, jaqen_stage10_y, jaqen_stage10_z;
    public static int armorsmith_stage6_x, armorsmith_stage6_y, armorsmith_stage6_z;
    public static int mwsmith_stage6_x, mwsmith_stage6_y, mwsmith_stage6_z;
    
    public static void setupAndLoad() {
        config = new Configuration(new File("config", "tutorial.cfg"));
        load();
    }
    
    public static void load() {
        config.load();
        
        String category = "coordinates";
        config.addCustomCategoryComment(category, "Coordinates for tutorial stages");
        
        stage1_x = config.get(category, "stage1_x", 0).getInt();
        stage1_y = config.get(category, "stage1_y", 201).getInt();
        stage1_z = config.get(category, "stage1_z", 0).getInt();
        
        banner_start_x = config.get(category, "banner_start_x", 50).getInt();
        banner_start_y = config.get(category, "banner_start_y", 201).getInt();
        banner_start_z = config.get(category, "banner_start_z", 50).getInt();
        banner_interval = config.get(category, "banner_interval", 100).getInt();
        generateBannerPlatforms = config.get(category, "generate_banner_platforms", true).getBoolean();
        
        stage6_x = config.get(category, "stage6_x", 0).getInt();
        stage6_y = config.get(category, "stage6_y", 201).getInt();
        stage6_z = config.get(category, "stage6_z", 0).getInt();
        
        stage7_x = config.get(category, "stage7_x", 100).getInt();
        stage7_y = config.get(category, "stage7_y", 201).getInt();
        stage7_z = config.get(category, "stage7_z", 100).getInt();
        
        stage8_x = config.get(category, "stage8_x", 150).getInt();
        stage8_y = config.get(category, "stage8_y", 201).getInt();
        stage8_z = config.get(category, "stage8_z", 150).getInt();
        
        stage9_x = config.get(category, "stage9_x", 200).getInt();
        stage9_y = config.get(category, "stage9_y", 201).getInt();
        stage9_z = config.get(category, "stage9_z", 200).getInt();
        
        stage10_x = config.get(category, "stage10_x", 250).getInt();
        stage10_y = config.get(category, "stage10_y", 201).getInt();
        stage10_z = config.get(category, "stage10_z", 250).getInt();
        
        end_x = config.get(category, "end_x", 0).getInt();
        end_y = config.get(category, "end_y", 64).getInt();
        end_z = config.get(category, "end_z", 0).getInt();
        reward_items = config.get(category, "reward_items", new String[]{"minecraft:apple:16", "minecraft:iron_sword:1"}).getStringList();
        
        generatePlatforms = config.get(category, "generate_platforms", true).getBoolean();
        
        queue_interval = config.get(category, "queue_interval", 10).getInt();
        limbo_dimension = config.get(category, "limbo_dimension", 0).getInt();
        limbo_x = config.get(category, "limbo_x", 0).getInt();
        limbo_y = config.get(category, "limbo_y", 200).getInt();
        limbo_z = config.get(category, "limbo_z", 0).getInt();
        
        String entCat = "entity_coordinates";
        config.addCustomCategoryComment(entCat, "Specific coordinates for tutorial entities");
        
        captain_stage1_x = config.get(entCat, "captain_stage1_x", stage1_x - 5).getInt();
        captain_stage1_y = config.get(entCat, "captain_stage1_y", stage1_y).getInt();
        captain_stage1_z = config.get(entCat, "captain_stage1_z", stage1_z + 5).getInt();
        
        drunkard1_stage1_x = config.get(entCat, "drunkard1_stage1_x", stage1_x + 2).getInt();
        drunkard1_stage1_y = config.get(entCat, "drunkard1_stage1_y", stage1_y).getInt();
        drunkard1_stage1_z = config.get(entCat, "drunkard1_stage1_z", stage1_z + 2).getInt();
        
        drunkard2_stage1_x = config.get(entCat, "drunkard2_stage1_x", stage1_x - 2).getInt();
        drunkard2_stage1_y = config.get(entCat, "drunkard2_stage1_y", stage1_y).getInt();
        drunkard2_stage1_z = config.get(entCat, "drunkard2_stage1_z", stage1_z - 2).getInt();
        
        dummy1_stage7_x = config.get(entCat, "dummy1_stage7_x", stage7_x - 2).getInt();
        dummy1_stage7_y = config.get(entCat, "dummy1_stage7_y", stage7_y).getInt();
        dummy1_stage7_z = config.get(entCat, "dummy1_stage7_z", stage7_z - 2).getInt();
        
        dummy2_stage7_x = config.get(entCat, "dummy2_stage7_x", stage7_x - 4).getInt();
        dummy2_stage7_y = config.get(entCat, "dummy2_stage7_y", stage7_y).getInt();
        dummy2_stage7_z = config.get(entCat, "dummy2_stage7_z", stage7_z).getInt();
        
        dummy3_stage7_x = config.get(entCat, "dummy3_stage7_x", stage7_x - 6).getInt();
        dummy3_stage7_y = config.get(entCat, "dummy3_stage7_y", stage7_y).getInt();
        dummy3_stage7_z = config.get(entCat, "dummy3_stage7_z", stage7_z + 2).getInt();
        
        boatswain_stage7_x = config.get(entCat, "boatswain_stage7_x", stage7_x + 2).getInt();
        boatswain_stage7_y = config.get(entCat, "boatswain_stage7_y", stage7_y).getInt();
        boatswain_stage7_z = config.get(entCat, "boatswain_stage7_z", stage7_z + 2).getInt();
        
        sailor_stage7_x = config.get(entCat, "sailor_stage7_x", stage7_x).getInt();
        sailor_stage7_y = config.get(entCat, "sailor_stage7_y", stage7_y).getInt();
        sailor_stage7_z = config.get(entCat, "sailor_stage7_z", stage7_z + 2).getInt();
        
        mutineer1_stage8_x = config.get(entCat, "mutineer1_stage8_x", stage8_x - 2).getInt();
        mutineer1_stage8_y = config.get(entCat, "mutineer1_stage8_y", stage8_y).getInt();
        mutineer1_stage8_z = config.get(entCat, "mutineer1_stage8_z", stage8_z - 2).getInt();
        
        mutineer2_stage8_x = config.get(entCat, "mutineer2_stage8_x", stage8_x + 2).getInt();
        mutineer2_stage8_y = config.get(entCat, "mutineer2_stage8_y", stage8_y).getInt();
        mutineer2_stage8_z = config.get(entCat, "mutineer2_stage8_z", stage8_z - 2).getInt();
        
        mutineer3_stage8_x = config.get(entCat, "mutineer3_stage8_x", stage8_x - 2).getInt();
        mutineer3_stage8_y = config.get(entCat, "mutineer3_stage8_y", stage8_y).getInt();
        mutineer3_stage8_z = config.get(entCat, "mutineer3_stage8_z", stage8_z + 2).getInt();
        
        captain_stage8_x = config.get(entCat, "captain_stage8_x", stage8_x).getInt();
        captain_stage8_y = config.get(entCat, "captain_stage8_y", stage8_y).getInt();
        captain_stage8_z = config.get(entCat, "captain_stage8_z", stage8_z).getInt();
        
        captain_stage9_x = config.get(entCat, "captain_stage9_x", stage9_x + 5).getInt();
        captain_stage9_y = config.get(entCat, "captain_stage9_y", stage9_y).getInt();
        captain_stage9_z = config.get(entCat, "captain_stage9_z", stage9_z).getInt();
        
        jaqen_stage10_x = config.get(entCat, "jaqen_stage10_x", stage10_x).getInt();
        jaqen_stage10_y = config.get(entCat, "jaqen_stage10_y", stage10_y).getInt();
        jaqen_stage10_z = config.get(entCat, "jaqen_stage10_z", stage10_z + 2).getInt();
        
        armorsmith_stage6_x = config.get(entCat, "armorsmith_stage6_x", stage6_x - 3).getInt();
        armorsmith_stage6_y = config.get(entCat, "armorsmith_stage6_y", stage6_y).getInt();
        armorsmith_stage6_z = config.get(entCat, "armorsmith_stage6_z", stage6_z + 2).getInt();
        
        mwsmith_stage6_x = config.get(entCat, "mwsmith_stage6_x", stage6_x + 4).getInt();
        mwsmith_stage6_y = config.get(entCat, "mwsmith_stage6_y", stage6_y).getInt();
        mwsmith_stage6_z = config.get(entCat, "mwsmith_stage6_z", stage6_z + 2).getInt();
        
        if (config.hasChanged()) {
            config.save();
        }
    }
    
    public static void setPos(String entity, int x, int y, int z) {
        String category = "entity_coordinates";
        config.get(category, entity + "_x", x).set(x);
        config.get(category, entity + "_y", y).set(y);
        config.get(category, entity + "_z", z).set(z);
        config.save();
        load();
    }
}
