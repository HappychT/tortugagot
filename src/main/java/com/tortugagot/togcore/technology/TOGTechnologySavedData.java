package com.tortugagot.togcore.technology;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TOGTechnologySavedData extends WorldSavedData {
    private static final String DATA_NAME = "togcore_technology_players";

    private final Map<String, PlayerTechnologyState> playerStates = new LinkedHashMap<String, PlayerTechnologyState>();

    public TOGTechnologySavedData(String name) {
        super(name);
    }

    public static TOGTechnologySavedData get(World world) {
        World storageWorld = DimensionManager.getWorld(0);
        if (storageWorld == null) {
            storageWorld = world;
        }
        if (storageWorld == null) {
            return null;
        }
        MapStorage storage = storageWorld.perWorldStorage;
        TOGTechnologySavedData data = (TOGTechnologySavedData) storage.loadData(TOGTechnologySavedData.class, DATA_NAME);
        if (data == null) {
            data = new TOGTechnologySavedData(DATA_NAME);
            storage.setData(DATA_NAME, data);
        }
        return data;
    }

    public boolean loadPlayer(UUID playerUuid, TOGTechnologyPlayerData playerData) {
        if (playerUuid == null || playerData == null) {
            return false;
        }
        PlayerTechnologyState state = playerStates.get(playerUuid.toString());
        if (state == null) {
            return false;
        }
        playerData.applyPersistentState(state.masteryPoints, state.unlockedTechnologies, state.spentTechnologyPoints);
        return true;
    }

    public boolean hasTechnology(UUID playerUuid, String technologyId) {
        if (playerUuid == null || technologyId == null) {
            return false;
        }
        PlayerTechnologyState state = playerStates.get(playerUuid.toString());
        return state != null && state.unlockedTechnologies.contains(technologyId);
    }

    public void savePlayer(UUID playerUuid, int masteryPoints, Set<String> unlockedTechnologies, Map<String, Integer> spentTechnologyPoints) {
        if (playerUuid == null) {
            return;
        }
        PlayerTechnologyState state = new PlayerTechnologyState();
        state.masteryPoints = Math.max(0, masteryPoints);
        if (unlockedTechnologies != null) {
            for (String id : unlockedTechnologies) {
                if (TOGTechnologyRegistry.get(id) != null) {
                    state.unlockedTechnologies.add(id);
                }
            }
        }
        if (spentTechnologyPoints != null) {
            for (Map.Entry<String, Integer> entry : spentTechnologyPoints.entrySet()) {
                String id = entry.getKey();
                if (state.unlockedTechnologies.contains(id)) {
                    Integer cost = entry.getValue();
                    state.spentTechnologyPoints.put(id, cost != null ? Math.max(0, cost) : 0);
                }
            }
        }
        for (String id : state.unlockedTechnologies) {
            if (!state.spentTechnologyPoints.containsKey(id)) {
                state.spentTechnologyPoints.put(id, 0);
            }
        }
        playerStates.put(playerUuid.toString(), state);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        playerStates.clear();
        NBTTagList players = compound.getTagList("Players", 10);
        for (int playerIndex = 0; playerIndex < players.tagCount(); playerIndex++) {
            NBTTagCompound playerTag = players.getCompoundTagAt(playerIndex);
            String uuid = playerTag.getString("UUID");
            if (uuid.length() == 0) {
                continue;
            }
            PlayerTechnologyState state = readState(playerTag);
            playerStates.put(uuid, state);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        NBTTagList players = new NBTTagList();
        for (Map.Entry<String, PlayerTechnologyState> entry : playerStates.entrySet()) {
            NBTTagCompound playerTag = new NBTTagCompound();
            playerTag.setString("UUID", entry.getKey());
            writeState(playerTag, entry.getValue());
            players.appendTag(playerTag);
        }
        compound.setTag("Players", players);
    }

    private PlayerTechnologyState readState(NBTTagCompound playerTag) {
        PlayerTechnologyState state = new PlayerTechnologyState();
        state.masteryPoints = Math.max(0, playerTag.getInteger("MasteryPoints"));

        NBTTagList unlocked = playerTag.getTagList("Unlocked", 8);
        for (int i = 0; i < unlocked.tagCount(); i++) {
            String id = unlocked.getStringTagAt(i);
            if (TOGTechnologyRegistry.get(id) != null) {
                state.unlockedTechnologies.add(id);
            }
        }

        NBTTagList spent = playerTag.getTagList("SpentTechnologyPoints", 10);
        for (int i = 0; i < spent.tagCount(); i++) {
            NBTTagCompound spentTag = spent.getCompoundTagAt(i);
            String id = spentTag.getString("Id");
            if (state.unlockedTechnologies.contains(id)) {
                state.spentTechnologyPoints.put(id, Math.max(0, spentTag.getInteger("Cost")));
            }
        }
        for (String id : state.unlockedTechnologies) {
            if (!state.spentTechnologyPoints.containsKey(id)) {
                state.spentTechnologyPoints.put(id, 0);
            }
        }
        return state;
    }

    private void writeState(NBTTagCompound playerTag, PlayerTechnologyState state) {
        playerTag.setInteger("MasteryPoints", Math.max(0, state.masteryPoints));
        NBTTagList unlocked = new NBTTagList();
        for (String id : state.unlockedTechnologies) {
            unlocked.appendTag(new NBTTagString(id));
        }
        playerTag.setTag("Unlocked", unlocked);

        NBTTagList spent = new NBTTagList();
        for (Map.Entry<String, Integer> entry : state.spentTechnologyPoints.entrySet()) {
            if (!state.unlockedTechnologies.contains(entry.getKey())) {
                continue;
            }
            NBTTagCompound spentTag = new NBTTagCompound();
            spentTag.setString("Id", entry.getKey());
            spentTag.setInteger("Cost", entry.getValue() != null ? Math.max(0, entry.getValue()) : 0);
            spent.appendTag(spentTag);
        }
        playerTag.setTag("SpentTechnologyPoints", spent);
    }

    private static class PlayerTechnologyState {
        private int masteryPoints;
        private final Set<String> unlockedTechnologies = new LinkedHashSet<String>();
        private final Map<String, Integer> spentTechnologyPoints = new LinkedHashMap<String, Integer>();
    }
}
