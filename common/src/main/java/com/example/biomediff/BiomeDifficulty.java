package com.example.biomediff;

import com.example.biomediff.config.BiomeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiomeDifficulty {
    public static final String MOD_ID = "biomediff";
    public static final String MOD_NAME = "Biome Difficulty";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    
    private static BiomeConfig config;
    
    public static void init() {
        LOGGER.info("Initializing {} mod", MOD_NAME);
        loadConfig();
    }
    
    public static void loadConfig() {
        config = BiomeConfig.load();
        LOGGER.info("Configuration loaded successfully");
    }
    
    public static BiomeConfig getConfig() {
        return config;
    }
}
