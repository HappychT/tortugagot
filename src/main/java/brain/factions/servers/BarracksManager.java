package brain.factions.servers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class BarracksManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File barracksFile;
    private static Map<String, List<PlayerProfile>> barracksPlayers = new HashMap<>();

    public static void init(File configFolder) {
        barracksFile = new File(configFolder, "barracks_players.json");
        loadBarracksPlayers();
    }

    private static void loadBarracksPlayers() {
        if (!barracksFile.exists()) {
            try {
                barracksFile.createNewFile();
                Files.write(GSON.toJson(new HashMap<String, List<PlayerProfile>>()).getBytes(Charsets.UTF_8), barracksFile);
            } catch (IOException e) {
                CoreFaction.logger().log(Level.SEVERE, "Could not create barracks_players.json", e);
            }
        }

        try {
            String content = Files.toString(barracksFile, Charsets.UTF_8);
            barracksPlayers = GSON.fromJson(content, new TypeToken<HashMap<String, List<PlayerProfile>>>() {}.getType());
            if (barracksPlayers == null) {
                barracksPlayers = new HashMap<>();
            }
        } catch (Exception e) {
            CoreFaction.logger().log(Level.SEVERE, "Error loading barracks players", e);
            barracksPlayers = new HashMap<>();
        }
    }

    public static void saveBarracksPlayers() {
        try {
            Files.write(GSON.toJson(barracksPlayers).getBytes(Charsets.UTF_8), barracksFile);
        } catch (IOException e) {
            CoreFaction.logger().log(Level.SEVERE, "Error saving barracks players", e);
        }
    }

    public static List<PlayerProfile> getBarracksPlayers(String structureId) {
        return barracksPlayers.getOrDefault(structureId, new ArrayList<>());
    }

    public static void addPlayer(String structureId, String playerName) {
        if (MinecraftServer.getServer() == null) return;
        UUID playerUUID = MinecraftServer.getServer().func_152358_ax().func_152655_a(playerName).getId();
        if (playerUUID == null) {
            playerUUID = UsernameCache.getMap().entrySet().stream()
                    .filter(entry -> entry.getValue().equalsIgnoreCase(playerName))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
        }
        if (playerUUID != null) {
            List<PlayerProfile> players = barracksPlayers.computeIfAbsent(structureId, k -> new ArrayList<>());
            UUID finalPlayerUUID = playerUUID;
            if (players.stream().noneMatch(p -> p.getUuid().equals(finalPlayerUUID.toString()))) {
                players.add(new PlayerProfile(playerUUID.toString(), playerName));
                saveBarracksPlayers();
            }
        }
    }

    public static void removePlayer(String structureId, String playerName) {
        List<PlayerProfile> players = barracksPlayers.get(structureId);
        if (players != null) {
            players.removeIf(p -> p.getName().equals(playerName));
            saveBarracksPlayers();
        }
    }

    public static class PlayerProfile {
        private String uuid;
        private String name;

        public PlayerProfile(String uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }

        public String getUuid() {
            return uuid;
        }

        public String getName() {
            return name;
        }
    }
}