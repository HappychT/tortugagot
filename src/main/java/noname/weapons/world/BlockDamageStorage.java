package noname.weapons.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockDamageStorage {
    
    private static final Map<String, Float> blockDamageMap = new HashMap<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static boolean needsSave = false;
    private static int saveTimer = 0;
    private static int clearDamageTimer = 0;
    private static final int CLEAR_INTERVAL = 30 * 60 * 20;
    private static final Map<Integer, Integer> immediateSaveTimers = new HashMap<>();

    private static File getStorageFile(World world) {
        try {
            if (world.getSaveHandler() == null) {
                return null;
            }
            File worldDir = world.getSaveHandler().getWorldDirectory();
            if (worldDir == null) {
                return null;
            }
            File siegeWeaponsDir = new File(worldDir, "got");
            if (!siegeWeaponsDir.exists()) {
                boolean created = siegeWeaponsDir.mkdirs();
                if (!created) {
                    return null;
                }
            }
            if (!siegeWeaponsDir.isDirectory()) {
                return null;
            }
            return new File(siegeWeaponsDir, "block_damage_dim" + world.provider.dimensionId + ".json");
        } catch (Exception ignored) {
            return null;
        }
    }
    
    public static void loadDimensionData(World world) {
        File file = getStorageFile(world);
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        
        try {
            FileReader reader = new FileReader(file);
            JsonObject json = (JsonObject) new JsonParser().parse(reader);
            reader.close();
            
            int dimId = world.provider.dimensionId;
            String prefix = dimId + ":";
            int count = 0;
            
            for (Map.Entry<String, com.google.gson.JsonElement> entry : json.entrySet()) {
                String key = prefix + entry.getKey();
                float damage = entry.getValue().getAsFloat();
                blockDamageMap.put(key, damage);
                count++;
            }
            
        } catch (Exception ignored) {
        }
    }
    
    public static void saveDimensionData(World world) {
        File file = getStorageFile(world);
        if (file == null) {
            return;
        }
        
        try {
            int dimId = world.provider.dimensionId;
            String prefix = dimId + ":";
            
            JsonObject json = new JsonObject();
            int count = 0;
            
            for (Map.Entry<String, Float> entry : blockDamageMap.entrySet()) {
                if (entry.getKey().startsWith(prefix)) {
                    String coordKey = entry.getKey().substring(prefix.length());
                    json.addProperty(coordKey, entry.getValue());
                    count++;
                }
            }
            
            FileWriter writer = new FileWriter(file);
            gson.toJson(json, writer);
            writer.close();
            
            if (count == 0 && file.exists() && file.length() == 0) {
                file.delete();
            }
            needsSave = false;
        } catch (Exception ignored) {
        }
    }

    public static void clearAllDamage(World world) {
        if (world.isRemote) return;
        int damageCount = blockDamageMap.size();
        if (damageCount > 0) {
            blockDamageMap.clear();
            needsSave = true;
            saveTimer = 0;
            immediateSaveTimers.clear();
        }
    }

    public static void tick(World world) {
        if (world.isRemote) return;

        clearDamageTimer++;

        if (clearDamageTimer >= CLEAR_INTERVAL) {
            clearAllDamage(world);
            clearDamageTimer = 0;
        }

        if (needsSave) {
            saveTimer++;
            if (saveTimer >= 200) {
                saveDimensionData(world);
                saveTimer = 0;
            }
        }

        int dimId = world.provider.dimensionId;
        Integer timer = immediateSaveTimers.get(dimId);
        if (timer != null) {
            if (timer >= 60) {
                saveDimensionData(world);
                immediateSaveTimers.remove(dimId);
            } else {
                immediateSaveTimers.put(dimId, timer + 1);
            }
        }
    }


    public static float getBlockDamage(World world, int x, int y, int z) {
        String key = getKey(world, x, y, z);
        Float damage = blockDamageMap.get(key);
        if (damage != null) {
            return damage;
        }

        String coordPrefix = world.provider.dimensionId + ":" + x + "," + y + "," + z + ":";
        String removedKey = null;
        for (String existingKey : blockDamageMap.keySet()) {
            if (existingKey.startsWith(coordPrefix)) {
                removedKey = existingKey;
                break;
            }
        }

        if (removedKey != null) {
            blockDamageMap.remove(removedKey);
            needsSave = true;
            saveTimer = 0;

            if (!world.isRemote) {
                long hash = ((long)x << 20) ^ ((long)y << 10) ^ (long)z;
                int entityId = -(int)(hash & 0x7FFFFFFF);
                if (entityId == 0 || entityId == -1) {
                    entityId = -(Math.abs(x) % 10000 * 10000 + Math.abs(y) % 256 * 256 + Math.abs(z) % 10000) - 2;
                }
                ((WorldServer)world).getPlayerManager().markBlockForUpdate(x, y, z);
            }
        }

        return 0.0F;
    }



    public static void addBlockDamage(World world, int x, int y, int z, float damage) {
        float currentDamage = getBlockDamage(world, x, y, z);

        String key = getKey(world, x, y, z);
        float newDamage = currentDamage + damage;
        blockDamageMap.put(key, newDamage);
        needsSave = true;
        saveTimer = 0;

        int dimId = world.provider.dimensionId;
        immediateSaveTimers.put(dimId, 1);
    }


    public static void setBlockDamage(World world, int x, int y, int z, float damage) {
        getBlockDamage(world, x, y, z);

        String key = getKey(world, x, y, z);
        blockDamageMap.put(key, damage);
        needsSave = true;
        saveTimer = 0;
    }

    public static void removeBlockDamage(World world, int x, int y, int z) {
        String key = getKey(world, x, y, z);
        if (blockDamageMap.remove(key) != null) {
            needsSave = true;
            saveTimer = 0;
        }

    }

    private static String getKey(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        String blockId = Block.blockRegistry.getNameForObject(block);

        return world.provider.dimensionId + ":" + x + "," + y + "," + z + ":" + blockId + ":" + metadata;
    }


    public static Map<String, Float> getAllDamagedBlocks(World world) {
        int dimId = world.provider.dimensionId;
        String prefix = dimId + ":";
        Map<String, Float> result = new HashMap<>();

        for (Map.Entry<String, Float> entry : blockDamageMap.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                String fullKey = entry.getKey().substring(prefix.length());
                int secondColonIndex = fullKey.indexOf(":", 0);
                if (secondColonIndex > 0) {
                    String coordKey = fullKey.substring(0, secondColonIndex);
                    result.put(coordKey, entry.getValue());
                }
            }
        }

        return result;
    }
}

