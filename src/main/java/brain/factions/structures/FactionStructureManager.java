package brain.factions.structures;

import brain.factions.servers.CoreFaction;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class FactionStructureManager {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(FactionStructureSlot.class, new SlotAdapter())
            .create();
    private static File structureSlotsFile;
    private static File structureTypesFile;

    public static List<FactionStructureSlot> structureSlots = new ArrayList<>();
    public static Map<String, Map<String, String>> structureTypes = new HashMap<>();

    private static final int STRUCTURE_SEARCH_RADIUS_XZ = 10;
    private static final int STRUCTURE_SEARCH_RADIUS_Y = 5;

    public static void init(File configFolder) {
        structureSlotsFile = new File(configFolder, "faction_structures.json");
        structureTypesFile = new File(configFolder, "structure_types.json");
        loadStructureSlots();
        loadStructureTypes();

        File oldOwnershipFile = new File(configFolder, "structure_ownership.json");
        if (oldOwnershipFile.exists()) {
            try {
                String content = Files.toString(oldOwnershipFile, Charsets.UTF_8);
                Map<String, String> oldOwners = GSON.fromJson(content, new TypeToken<Map<String, String>>() {}.getType());
                if (oldOwners != null && structureSlots != null) {
                    for (FactionStructureSlot slot : structureSlots) {
                        if (oldOwners.containsKey(slot.id)) {
                            slot.ownerFactionID = oldOwners.get(slot.id);
                        }
                    }
                }
                saveStructureOwnership();
                oldOwnershipFile.delete();
                CoreFaction.logger().info("Старая база владельцев успешно перенесена в faction_structures.json и удалена!");
            } catch (Exception e) {
                CoreFaction.logger().log(Level.SEVERE, "Ошибка переноса старых владельцев", e);
            }
        }
    }

    private static void loadStructureTypes() {
        if (!structureTypesFile.exists()) {
            try {
                structureTypesFile.createNewFile();
                Map<String, Map<String, String>> defaultTypes = new HashMap<>();
                Map<String, String> fortress = new HashMap<>();
                fortress.put("Крепость", "hz_fort.json");
                defaultTypes.put("FORTRESS", fortress);
                Map<String, String> farms = new HashMap<>();
                farms.put("Плантации", "farm_plantation.json");
                farms.put("Животноводство", "farm_livestock.json");
                defaultTypes.put("FARMS", farms);

                Map<String, String> industry = new HashMap<>();
                industry.put("Лесопилка", "industry_sawmill.json");
                industry.put("Шахта", "industry_mine.json");
                defaultTypes.put("INDUSTRY", industry);

                Map<String, String> barn = new HashMap<>();
                barn.put("Амбар", "barn.json");
                defaultTypes.put("BARN", barn);

                Map<String, String> workshop = new HashMap<>();
                workshop.put("Инженерная мастерская", "engineering_workshop.json");
                defaultTypes.put("ENGINEERING_WORKSHOP", workshop);

                Map<String, String> stable = new HashMap<>();
                stable.put("Конюшня", "stable.json");
                defaultTypes.put("STABLE", stable);

                Files.write(GSON.toJson(defaultTypes).getBytes(Charsets.UTF_8), structureTypesFile);
                structureTypes = defaultTypes;
            } catch (IOException e) {
                CoreFaction.logger().log(Level.SEVERE, "Could not create structure_types.json", e);
            }
            return;
        }

        try {
            String content = Files.toString(structureTypesFile, Charsets.UTF_8);
            structureTypes = GSON.fromJson(content, new TypeToken<Map<String, Map<String, String>>>() {}.getType());
            if (structureTypes == null) {
                structureTypes = new HashMap<>();
            }
        } catch (Exception e) {
            CoreFaction.logger().log(Level.SEVERE, "Error loading structure types", e);
            structureTypes = new HashMap<>();
        }
    }

    private static void loadStructureSlots() {
        if (!structureSlotsFile.exists()) {
            try {
                structureSlotsFile.createNewFile();
                Files.write("{\"structure_slots\": []}".getBytes(Charsets.UTF_8), structureSlotsFile);
            } catch (IOException e) {
                CoreFaction.logger().log(Level.SEVERE, "Could not create faction_structures.json", e);
            }
        }

        try {
            String content = Files.toString(structureSlotsFile, Charsets.UTF_8);
            Map<String, List<FactionStructureSlot>> loaded = GSON.fromJson(content, new TypeToken<Map<String, List<FactionStructureSlot>>>() {}.getType());
            structureSlots = loaded.getOrDefault("structure_slots", new ArrayList<>());
        } catch (Exception e) {
            CoreFaction.logger().log(Level.SEVERE, "Error loading faction structures", e);
            structureSlots = new ArrayList<>();
        }
    }

    public static void saveStructureOwnership() {
        try {
            Map<String, List<FactionStructureSlot>> toSave = new HashMap<>();
            toSave.put("structure_slots", structureSlots);
            Files.write(GSON.toJson(toSave).getBytes(Charsets.UTF_8), structureSlotsFile);
        } catch (IOException e) {
            CoreFaction.logger().log(Level.SEVERE, "Error saving structure slots", e);
        }
    }

    public static void setStructureOwner(String structureId, String factionId) {
        FactionStructureSlot slot = getStructureById(structureId);
        if (slot != null) {
            slot.ownerFactionID = factionId;
        }
        saveStructureOwnership();
    }

    public static FactionStructureSlot getStructureById(String id) {
        if (id == null || structureSlots == null) return null;
        for (FactionStructureSlot slot : structureSlots) {
            if (id.equals(slot.id)) {
                return slot;
            }
        }
        return null;
    }

    public static FactionStructureSlot getStructureByName(String name) {
        if (name == null || structureSlots == null) return null;
        String lower = name.toLowerCase();
        for (FactionStructureSlot slot : structureSlots) {
            if (slot.name != null && slot.name.toLowerCase().equals(lower)) return slot;
            if (slot.id != null && slot.id.toLowerCase().equals(lower)) return slot;
        }
        return null;
    }

    public static FactionStructureSlot getFortressByName(String name) {
        FactionStructureSlot slot = getStructureByName(name);
        return (slot != null && slot.category == FactionStructureSlot.StructureCategory.FORTRESS) ? slot : null;
    }

    public static FactionStructureSlot getStructureNearby(int x, int y, int z) {
        if (structureSlots == null) return null;

        for (FactionStructureSlot slot : structureSlots) {
            double distSqXZ = Math.pow(slot.xCoord - x, 2) + Math.pow(slot.zCoord - z, 2);
            int distY = Math.abs(slot.yCoord - y);

            if (distSqXZ <= (STRUCTURE_SEARCH_RADIUS_XZ * STRUCTURE_SEARCH_RADIUS_XZ) && distY <= STRUCTURE_SEARCH_RADIUS_Y) {
                return slot;
            }
        }
        return null;
    }

    public static class SlotAdapter implements com.google.gson.JsonSerializer<FactionStructureSlot>, com.google.gson.JsonDeserializer<FactionStructureSlot> {
        @Override
        public com.google.gson.JsonElement serialize(FactionStructureSlot src, java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
            com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
            obj.addProperty("id", src.id);
            obj.addProperty("name", src.name);
            obj.addProperty("category", src.category != null ? src.category.name() : "NONE");
            if (src.resource != null && !src.resource.isEmpty()) obj.addProperty("resource", src.resource);
            obj.addProperty("mapX", src.mapX);
            obj.addProperty("mapY", src.mapY);
            obj.addProperty("price", src.price);
            if (src.structureFile != null && !src.structureFile.isEmpty()) obj.addProperty("structureFile", src.structureFile);
            obj.addProperty("xCoord", src.xCoord);
            obj.addProperty("yCoord", src.yCoord);
            obj.addProperty("zCoord", src.zCoord);
            if (src.ownerFactionID != null) obj.addProperty("ownerFactionID", src.ownerFactionID);
            obj.addProperty("level", src.level);
            obj.addProperty("destructionCount", src.destructionCount);
            obj.addProperty("canBuild", src.canBuild);

            if (src.initial_faction != null && !src.initial_faction.isEmpty()) obj.addProperty("initial_faction", src.initial_faction);
            if (src.for_fraction != null && !src.for_fraction.isEmpty()) obj.addProperty("for_fraction", src.for_fraction);

            if (src.category == FactionStructureSlot.StructureCategory.FORTRESS) {
                obj.addProperty("barnLevel", src.barnLevel > 0 ? src.barnLevel : 1);
                obj.addProperty("workshopLevel", src.workshopLevel > 0 ? src.workshopLevel : 1);
                obj.addProperty("stableLevel", src.stableLevel > 0 ? src.stableLevel : 1);
                obj.addProperty("barracksLevel", src.barracksLevel > 0 ? src.barracksLevel : 1);
                obj.addProperty("barracksCapacity", src.barracksCapacity);
                obj.addProperty("provisions", src.provisions);
                obj.addProperty("storedFoodItems", src.storedFoodItems);
                obj.addProperty("lastSiegePurchaseTime", src.lastSiegePurchaseTime);
            } else {
                obj.addProperty("securityLevel", src.securityLevel);
                if (src.raidTime != null && !src.raidTime.isEmpty()) obj.addProperty("raidTime", src.raidTime);

                if (src.category == FactionStructureSlot.StructureCategory.BARN) {
                    obj.addProperty("provisions", src.provisions);
                    obj.addProperty("storedFoodItems", src.storedFoodItems);
                }
                if (src.category == FactionStructureSlot.StructureCategory.ENGINEERING_WORKSHOP) {
                    obj.addProperty("lastSiegePurchaseTime", src.lastSiegePurchaseTime);
                }
            }
            return obj;
        }

        @Override
        public FactionStructureSlot deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) {
            FactionStructureSlot slot = new FactionStructureSlot();
            com.google.gson.JsonObject obj = json.getAsJsonObject();
            slot.id = obj.has("id") ? obj.get("id").getAsString() : "";
            slot.name = obj.has("name") ? obj.get("name").getAsString() : "";

            if (obj.has("category")) {
                slot.category = FactionStructureSlot.StructureCategory.valueOf(obj.get("category").getAsString());
            } else if (obj.has("type")) {
                String oldType = obj.get("type").getAsString();
                if (oldType.equals("FORTRESS")) slot.category = FactionStructureSlot.StructureCategory.FORTRESS;
                else slot.category = FactionStructureSlot.StructureCategory.NONE;
            } else {
                slot.category = FactionStructureSlot.StructureCategory.NONE;
            }

            slot.resource = obj.has("resource") ? obj.get("resource").getAsString() : "";
            slot.mapX = obj.has("mapX") ? obj.get("mapX").getAsInt() : 0;
            slot.mapY = obj.has("mapY") ? obj.get("mapY").getAsInt() : 0;
            slot.price = obj.has("price") ? obj.get("price").getAsInt() : 0;
            slot.structureFile = obj.has("structureFile") ? obj.get("structureFile").getAsString() : "";
            slot.xCoord = obj.has("xCoord") ? obj.get("xCoord").getAsInt() : 0;
            slot.yCoord = obj.has("yCoord") ? obj.get("yCoord").getAsInt() : 0;
            slot.zCoord = obj.has("zCoord") ? obj.get("zCoord").getAsInt() : 0;
            slot.ownerFactionID = obj.has("ownerFactionID") ? obj.get("ownerFactionID").getAsString() : null;
            slot.level = obj.has("level") ? obj.get("level").getAsInt() : 0;
            slot.destructionCount = obj.has("destructionCount") ? obj.get("destructionCount").getAsInt() : 0;
            slot.canBuild = obj.has("canBuild") ? obj.get("canBuild").getAsBoolean() : true;

            slot.initial_faction = obj.has("initial_faction") ? obj.get("initial_faction").getAsString() : null;
            slot.for_fraction = obj.has("for_fraction") ? obj.get("for_fraction").getAsString() : null;
            slot.raidTime = obj.has("raidTime") ? obj.get("raidTime").getAsString() : "";

            slot.barnLevel = obj.has("barnLevel") ? obj.get("barnLevel").getAsInt() : 0;
            slot.workshopLevel = obj.has("workshopLevel") ? obj.get("workshopLevel").getAsInt() : 0;
            slot.stableLevel = obj.has("stableLevel") ? obj.get("stableLevel").getAsInt() : 0;
            slot.barracksLevel = obj.has("barracksLevel") ? obj.get("barracksLevel").getAsInt() : 0;
            slot.barracksCapacity = obj.has("barracksCapacity") ? obj.get("barracksCapacity").getAsInt() : 0;
            slot.provisions = obj.has("provisions") ? obj.get("provisions").getAsFloat() : 0f;
            slot.storedFoodItems = obj.has("storedFoodItems") ? obj.get("storedFoodItems").getAsInt() : 0;
            slot.lastSiegePurchaseTime = obj.has("lastSiegePurchaseTime") ? obj.get("lastSiegePurchaseTime").getAsLong() : 0L;
            slot.securityLevel = obj.has("securityLevel") ? obj.get("securityLevel").getAsInt() : 0;

            return slot;
        }
    }
}