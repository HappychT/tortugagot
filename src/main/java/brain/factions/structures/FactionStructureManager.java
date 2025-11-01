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

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File structureSlotsFile;
    private static File structureOwnershipFile;
    private static File structureTypesFile;

    public static List<FactionStructureSlot> structureSlots = new ArrayList<>();
    private static Map<String, String> structureOwners = new HashMap<>();
    public static Map<String, Map<String, String>> structureTypes = new HashMap<>();

    private static final int STRUCTURE_SEARCH_RADIUS_XZ = 10;
    private static final int STRUCTURE_SEARCH_RADIUS_Y = 5;


    public static void init(File configFolder) {
        structureSlotsFile = new File(configFolder, "faction_structures.json");
        structureOwnershipFile = new File(configFolder, "structure_ownership.json");
        structureTypesFile = new File(configFolder, "structure_types.json");
        loadStructureSlots();
        loadStructureOwnership();
        loadStructureTypes();
        syncOwnershipToSlots();
    }

    private static void loadStructureTypes() {
        if (!structureTypesFile.exists()) {
            try {
                structureTypesFile.createNewFile();
                Map<String, Map<String, String>> defaultTypes = new HashMap<>();
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

    private static void loadStructureOwnership() {
        if (!structureOwnershipFile.exists()) {
            structureOwners = new HashMap<>();
            for (FactionStructureSlot slot : structureSlots) {
                if (slot.initial_faction != null && !slot.initial_faction.isEmpty()) {
                    structureOwners.put(slot.id, slot.initial_faction);
                }
            }
            saveStructureOwnership();
            return;
        }

        try {
            String content = Files.toString(structureOwnershipFile, Charsets.UTF_8);
            structureOwners = GSON.fromJson(content, new TypeToken<Map<String, String>>() {}.getType());
            if (structureOwners == null) {
                structureOwners = new HashMap<>();
            }
        } catch (Exception e) {
            CoreFaction.logger().log(Level.SEVERE, "Error loading structure ownership", e);
            structureOwners = new HashMap<>();
        }
    }

    public static void saveStructureOwnership() {
        try {
            Map<String, List<FactionStructureSlot>> toSave = new HashMap<>();
            toSave.put("structure_slots", structureSlots);
            Files.write(GSON.toJson(toSave).getBytes(Charsets.UTF_8), structureSlotsFile);
            Files.write(GSON.toJson(structureOwners).getBytes(Charsets.UTF_8), structureOwnershipFile);
        } catch (IOException e) {
            CoreFaction.logger().log(Level.SEVERE, "Error saving structure ownership or slots", e);
        }
    }

    private static void syncOwnershipToSlots() {
        if (structureSlots == null || structureOwners == null) return;
        for (FactionStructureSlot slot : structureSlots) {
            slot.ownerFactionID = structureOwners.get(slot.id);
        }
    }

    public static void setStructureOwner(String structureId, String factionId) {
        if (factionId == null) {
            structureOwners.remove(structureId);
        } else {
            structureOwners.put(structureId, factionId);
        }
        syncOwnershipToSlots();
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
}