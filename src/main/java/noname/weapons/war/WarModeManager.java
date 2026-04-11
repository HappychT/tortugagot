package noname.weapons.war;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.io.*;

public class WarModeManager {

    private static WarModeManager instance;
    private boolean warModeActive = false;
    private String activeStructureId = null;
    private int activeDimension = -1;
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
        setWarMode(active, this.activeStructureId, this.activeDimension);
    }

    public void setWarMode(boolean active, String structureId) {
        setWarMode(active, structureId, this.activeDimension);
    }

    public void setWarMode(boolean active, String structureId, int dimension) {
        this.warModeActive = active;
        this.activeStructureId = active ? structureId : null;
        this.activeDimension = active ? dimension : -1;
        saveState();
    }

    public String getActiveStructureId() {
        return activeStructureId;
    }

    public int getActiveDimension() {
        return activeDimension;
    }

    public void loadState() {
        File saveFile = getSaveFile();
        if (saveFile != null && saveFile.exists()) {
            try (FileReader reader = new FileReader(saveFile)) {
                WarModeState state = gson.fromJson(reader, WarModeState.class);
                if (state != null) {
                    this.warModeActive = state.warModeActive;
                    this.activeStructureId = state.activeStructureId;
                    this.activeDimension = state.activeDimension;
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
                    state.activeStructureId = this.activeStructureId;
                    state.activeDimension = this.activeDimension;
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
        this.activeStructureId = null;
        this.activeDimension = -1;
    }

    private static class WarModeState {
        boolean warModeActive;
        String activeStructureId;
        int activeDimension;
    }
}
