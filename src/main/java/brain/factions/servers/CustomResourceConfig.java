package brain.factions.servers;

import brain.factions.Annot;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomResourceConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    public static Map<String, Map<String, Map<String, List<ResourceEntry>>>> factionResources = new HashMap<>();

    public static class ResourceEntry {
        public String item;
        public double amount;
    }

    public static void init(File configFolder) {
        if (Annot.SERVER) {
            configFile = new File(configFolder, "faction_resources.json");
            load();
        }
    }

    public static void load() {
        if (Annot.SERVER) {

            if (!configFile.exists()) {
                try {
                    configFile.createNewFile();
                    Files.write(GSON.toJson(new HashMap<>()).getBytes(Charsets.UTF_8), configFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            try {
                String content = Files.toString(configFile, Charsets.UTF_8);
                factionResources = GSON.fromJson(content, new TypeToken<Map<String, Map<String, Map<String, List<ResourceEntry>>>>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}