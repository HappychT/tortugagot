package noname.weapons.war;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.io.*;

public class WarModeManager {

    private static WarModeManager instance;
    private boolean warModeActive = false;
    private static final String SAVE_FILE_NAME = "war_mode.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private WarModeManager() {
    }

    public static WarModeManager getInstance() {
        if (instance == null) {
            instance = new WarModeManager();
        }
        return instance;
    }

    public boolean isWarModeActive() {
        return warModeActive;
    }

    public void setWarMode(boolean active) {
        this.warModeActive = active;
        saveState();
    }

    public void loadState() {
        File saveFile = getSaveFile();
        if (saveFile != null && saveFile.exists()) {
            try (FileReader reader = new FileReader(saveFile)) {
                WarModeState state = gson.fromJson(reader, WarModeState.class);
                if (state != null) {
                    this.warModeActive = state.warModeActive;
                }
            } catch (IOException e) {
                System.err.println("[WarMode] Ошибка загрузки состояния: " + e.getMessage());
            }
        }
    }

    private void saveState() {
        File saveFile = getSaveFile();
        if (saveFile != null) {
            try {
                saveFile.getParentFile().mkdirs();
                try (FileWriter writer = new FileWriter(saveFile)) {
                    WarModeState state = new WarModeState();
                    state.warModeActive = this.warModeActive;
                    gson.toJson(state, writer);
                }
            } catch (IOException e) {
                System.err.println("[WarMode] Ошибка сохранения состояния: " + e.getMessage());
            }
        }
    }

    private File getSaveFile() {
        MinecraftServer server = MinecraftServer.getServer();
        if (server != null) {
            WorldServer world = server.worldServerForDimension(0);
            if (world != null) {
                File worldDir = world.getSaveHandler().getWorldDirectory();
                return new File(worldDir, SAVE_FILE_NAME);
            }
        }
        return null;
    }

    public void reset() {
        this.warModeActive = false;
    }

    private static class WarModeState {
        boolean warModeActive;
    }
}
