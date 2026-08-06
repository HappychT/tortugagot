package com.tortugagot.togcore;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
    private static File currentConfigFile;

    public static void synchronizeConfiguration(File configFile) {
        currentConfigFile = configFile;
        Configuration configuration = new Configuration(configFile);
        configuration.load();
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static boolean reload() {
        if (currentConfigFile == null) {
            return false;
        }
        synchronizeConfiguration(currentConfigFile);
        return true;
    }
}
