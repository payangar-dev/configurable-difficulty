package com.example.biomediff.config;

import com.example.biomediff.BiomeDifficulty;
import de.marhali.json5.Json5;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BiomeConfig {
    public boolean enabled = true;
    public ModifierMode playerMode = ModifierMode.DYNAMIC;
    public ModifierMode mobMode = ModifierMode.SPAWN_ONLY;
    public int checkInterval = 20;
    
    public boolean applyToHostileMobs = true;
    public boolean applyToPassiveMobs = false;
    public boolean applyToNeutralMobs = false;
    
    public boolean debugEnabled = false;
    public boolean debugLogBiomeChanges = true;
    public boolean debugLogAttributeChanges = true;
    
    public AttributeEnabledConfig enabledAttributes = new AttributeEnabledConfig();
    public BiomeMultipliers defaultMultipliers = new BiomeMultipliers();
    public Map<String, BiomeMultipliers> dimensionMultipliers = new HashMap<>();
    public Map<String, BiomeMultipliers> biomeMultipliers = new HashMap<>();
    public DepthScalingConfig depthScaling = new DepthScalingConfig();

    public static BiomeConfig load() {
        Path configPath = getConfigPath();
        
        if (!Files.exists(configPath)) {
            BiomeDifficulty.LOGGER.info("Config file not found, creating default config at {}", configPath);
            BiomeConfig defaultConfig = createDefault();
            defaultConfig.save();
            return defaultConfig;
        }
        
        try {
            String json5Content = Files.readString(configPath);
            return fromJson5(json5Content);
        } catch (Exception e) {
            BiomeDifficulty.LOGGER.error("Failed to load config, using defaults", e);
            return createDefault();
        }
    }
    
    public void save() {
        try {
            Path configPath = getConfigPath();
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, toJson5());
            BiomeDifficulty.LOGGER.info("Config saved to {}", configPath);
        } catch (IOException e) {
            BiomeDifficulty.LOGGER.error("Failed to save config", e);
        }
    }
    
    private static Path getConfigPath() {
        return Paths.get("config", "configurable-difficulty.json5");
    }
    
    private static BiomeConfig createDefault() {
        BiomeConfig config = new BiomeConfig();
        
        // Add default dimension multipliers
        BiomeMultipliers overworldMult = new BiomeMultipliers(); // All 1.0
        config.dimensionMultipliers.put("minecraft:overworld", overworldMult);
        
        BiomeMultipliers netherMult = new BiomeMultipliers();
        netherMult.maxHealth = 1.5;
        netherMult.armor = 1.3;
        netherMult.attackDamage = 1.5;
        netherMult.luck = 1.2;
        config.dimensionMultipliers.put("minecraft:the_nether", netherMult);
        
        BiomeMultipliers endMult = new BiomeMultipliers();
        endMult.maxHealth = 2.0;
        endMult.armor = 1.5;
        endMult.attackDamage = 2.0;
        endMult.luck = 1.5;
        config.dimensionMultipliers.put("minecraft:the_end", endMult);
        
        // Add some example biome configurations
        BiomeMultipliers desert = new BiomeMultipliers();
        desert.maxHealth = 1.5;
        desert.armor = 0.8;
        desert.attackDamage = 1.3;
        desert.knockbackResistance = 0.1;
        desert.luck = 1.1;
        config.biomeMultipliers.put("minecraft:desert", desert);
        
        BiomeMultipliers frozenOcean = new BiomeMultipliers();
        frozenOcean.maxHealth = 2.0;
        frozenOcean.armor = 1.5;
        frozenOcean.armorToughness = 1.3;
        frozenOcean.attackDamage = 1.5;
        frozenOcean.knockbackResistance = 0.3;
        frozenOcean.luck = 1.3;
        config.biomeMultipliers.put("minecraft:frozen_ocean", frozenOcean);
        
        BiomeMultipliers deepDark = new BiomeMultipliers();
        deepDark.maxHealth = 2.5;
        deepDark.armor = 2.0;
        deepDark.armorToughness = 1.5;
        deepDark.attackDamage = 2.0;
        deepDark.knockbackResistance = 0.4;
        deepDark.luck = 1.5;
        config.biomeMultipliers.put("minecraft:deep_dark", deepDark);
        
        BiomeMultipliers mushroomFields = new BiomeMultipliers();
        mushroomFields.maxHealth = 0.5;
        mushroomFields.armor = 0.5;
        mushroomFields.attackDamage = 0.5;
        mushroomFields.luck = 1.2;
        config.biomeMultipliers.put("minecraft:mushroom_fields", mushroomFields);
        
        return config;
    }
    
    private static BiomeConfig fromJson5(String json5Str) {
        BiomeConfig config = new BiomeConfig();
        // Parse JSON5 using the JSON5 parser
        Json5 json5Parser = new Json5();
        String jsonStr = json5Parser.parse(json5Str).toString();
        JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
        
        if (json.has("enabled")) config.enabled = json.get("enabled").getAsBoolean();
        if (json.has("playerMode")) config.playerMode = ModifierMode.fromString(json.get("playerMode").getAsString());
        if (json.has("mobMode")) config.mobMode = ModifierMode.fromString(json.get("mobMode").getAsString());
        if (json.has("checkInterval")) config.checkInterval = json.get("checkInterval").getAsInt();
        
        if (json.has("applyToHostileMobs")) config.applyToHostileMobs = json.get("applyToHostileMobs").getAsBoolean();
        if (json.has("applyToPassiveMobs")) config.applyToPassiveMobs = json.get("applyToPassiveMobs").getAsBoolean();
        if (json.has("applyToNeutralMobs")) config.applyToNeutralMobs = json.get("applyToNeutralMobs").getAsBoolean();
        
        if (json.has("debug")) {
            JsonObject debug = json.getAsJsonObject("debug");
            if (debug.has("enabled")) config.debugEnabled = debug.get("enabled").getAsBoolean();
            if (debug.has("logBiomeChanges")) config.debugLogBiomeChanges = debug.get("logBiomeChanges").getAsBoolean();
            if (debug.has("logAttributeChanges")) config.debugLogAttributeChanges = debug.get("logAttributeChanges").getAsBoolean();
        }
        
        if (json.has("enabledAttributes")) {
            config.enabledAttributes = AttributeEnabledConfig.fromJson(json.getAsJsonObject("enabledAttributes"));
        }
        
        if (json.has("defaultMultipliers")) {
            config.defaultMultipliers = BiomeMultipliers.fromJson(json.getAsJsonObject("defaultMultipliers"));
        }
        
        if (json.has("dimensionMultipliers")) {
            JsonObject dimensions = json.getAsJsonObject("dimensionMultipliers");
            for (String dimensionId : dimensions.keySet()) {
                config.dimensionMultipliers.put(dimensionId, BiomeMultipliers.fromJson(dimensions.getAsJsonObject(dimensionId)));
            }
        }
        
        if (json.has("biomeMultipliers")) {
            JsonObject biomes = json.getAsJsonObject("biomeMultipliers");
            for (String biomeId : biomes.keySet()) {
                config.biomeMultipliers.put(biomeId, BiomeMultipliers.fromJson(biomes.getAsJsonObject(biomeId)));
            }
        }
        
        if (json.has("depthScaling")) {
            config.depthScaling = DepthScalingConfig.fromJson(json.getAsJsonObject("depthScaling"));
        }
        
        return config;
    }
    
    private String toJson5() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  // Enable/disable the entire mod\n");
        sb.append("  \"enabled\": ").append(enabled).append(",\n\n");
        
        sb.append("  // How players get difficulty modifiers\n");
        sb.append("  // Options: \"dynamic\" (changes with biome), \"spawn_only\" (set at spawn)\n");
        sb.append("  \"playerMode\": \"").append(playerMode.name().toLowerCase()).append("\",\n\n");
        
        sb.append("  // How mobs get difficulty modifiers\n");
        sb.append("  // Options: \"dynamic\" (changes with biome), \"spawn_only\" (set at spawn)\n");
        sb.append("  \"mobMode\": \"").append(mobMode.name().toLowerCase()).append("\",\n\n");
        
        sb.append("  // Check interval in ticks (20 = 1 second)\n");
        sb.append("  // Lower = more responsive, higher = better performance\n");
        sb.append("  \"checkInterval\": ").append(checkInterval).append(",\n\n");
        
        sb.append("  // Players: Always affected (luck attribute only)\n");
        sb.append("  // Mobs: Which entity types should be affected (all attributes except luck)\n");
        sb.append("  \"applyToHostileMobs\": ").append(applyToHostileMobs).append(",\n");
        sb.append("  \"applyToPassiveMobs\": ").append(applyToPassiveMobs).append(",\n");
        sb.append("  \"applyToNeutralMobs\": ").append(applyToNeutralMobs).append(",\n\n");
        
        sb.append("  // Debug logging (check server logs)\n");
        sb.append("  \"debug\": {\n");
        sb.append("    \"enabled\": ").append(debugEnabled).append(",\n");
        sb.append("    \"logBiomeChanges\": ").append(debugLogBiomeChanges).append(",\n");
        sb.append("    \"logAttributeChanges\": ").append(debugLogAttributeChanges).append("\n");
        sb.append("  },\n\n");
        
        sb.append("  // Which attributes to modify\n");
        sb.append("  // Mob attributes: maxHealth, armor, armorToughness, attackDamage, attackSpeed, attackKnockback, knockbackResistance\n");
        sb.append("  // Player attribute: luck (loot quality)\n");
        sb.append("  \"enabledAttributes\": {\n");
        sb.append("    \"maxHealth\": ").append(enabledAttributes.maxHealth).append(",\n");
        sb.append("    \"armor\": ").append(enabledAttributes.armor).append(",\n");
        sb.append("    \"armorToughness\": ").append(enabledAttributes.armorToughness).append(",\n");
        sb.append("    \"attackDamage\": ").append(enabledAttributes.attackDamage).append(",\n");
        sb.append("    \"attackSpeed\": ").append(enabledAttributes.attackSpeed).append(",\n");
        sb.append("    \"attackKnockback\": ").append(enabledAttributes.attackKnockback).append(",\n");
        sb.append("    \"knockbackResistance\": ").append(enabledAttributes.knockbackResistance).append(",\n");
        sb.append("    \"luck\": ").append(enabledAttributes.luck).append("\n");
        sb.append("  },\n\n");
        
        sb.append("  // Default multipliers for biomes not explicitly configured\n");
        sb.append("  // Multiplier of 1.0 = no change, 2.0 = 2x harder, 0.5 = half difficulty\n");
        sb.append("  \"defaultMultipliers\": {\n");
        sb.append("    \"maxHealth\": ").append(defaultMultipliers.maxHealth).append(",\n");
        sb.append("    \"armor\": ").append(defaultMultipliers.armor).append(",\n");
        sb.append("    \"armorToughness\": ").append(defaultMultipliers.armorToughness).append(",\n");
        sb.append("    \"attackDamage\": ").append(defaultMultipliers.attackDamage).append(",\n");
        sb.append("    \"attackSpeed\": ").append(defaultMultipliers.attackSpeed).append(",\n");
        sb.append("    \"attackKnockback\": ").append(defaultMultipliers.attackKnockback).append(",\n");
        sb.append("    \"knockbackResistance\": ").append(defaultMultipliers.knockbackResistance).append(",\n");
        sb.append("    \"luck\": ").append(defaultMultipliers.luck).append("\n");
        sb.append("  },\n\n");
        
        sb.append("  // Dimension multipliers: Base difficulty per dimension\n");
        sb.append("  // Format: \"modid:dimension_name\" (e.g., \"minecraft:the_nether\", \"twilightforest:twilight_forest\")\n");
        sb.append("  // Overworld is the default - unconfigured dimensions use overworld values\n");
        sb.append("  // Final calculation: dimension × biome × depth\n");
        sb.append("  \"dimensionMultipliers\": {\n");
        
        int dimCount = 0;
        for (Map.Entry<String, BiomeMultipliers> entry : dimensionMultipliers.entrySet()) {
            dimCount++;
            sb.append("    \"").append(entry.getKey()).append("\": {\n");
            BiomeMultipliers mult = entry.getValue();
            sb.append("      \"maxHealth\": ").append(mult.maxHealth).append(",\n");
            sb.append("      \"armor\": ").append(mult.armor).append(",\n");
            sb.append("      \"armorToughness\": ").append(mult.armorToughness).append(",\n");
            sb.append("      \"attackDamage\": ").append(mult.attackDamage).append(",\n");
            sb.append("      \"attackSpeed\": ").append(mult.attackSpeed).append(",\n");
            sb.append("      \"attackKnockback\": ").append(mult.attackKnockback).append(",\n");
            sb.append("      \"knockbackResistance\": ").append(mult.knockbackResistance).append(",\n");
            sb.append("      \"luck\": ").append(mult.luck).append("\n");
            sb.append("    }");
            if (dimCount < dimensionMultipliers.size()) sb.append(",");
            sb.append("\n");
        }
        
        sb.append("  },\n\n");
        
        sb.append("  // Biome-specific multipliers\n");
        sb.append("  // Format: \"modid:biomename\" (e.g., \"minecraft:desert\", \"biomesoplenty:mystic_grove\")\n");
        sb.append("  // Only include attributes you want to change (others use defaults)\n");
        sb.append("  \"biomeMultipliers\": {\n");
        
        int count = 0;
        for (Map.Entry<String, BiomeMultipliers> entry : biomeMultipliers.entrySet()) {
            count++;
            sb.append("    \"").append(entry.getKey()).append("\": {\n");
            BiomeMultipliers mult = entry.getValue();
            sb.append("      \"maxHealth\": ").append(mult.maxHealth).append(",\n");
            sb.append("      \"armor\": ").append(mult.armor).append(",\n");
            sb.append("      \"armorToughness\": ").append(mult.armorToughness).append(",\n");
            sb.append("      \"attackDamage\": ").append(mult.attackDamage).append(",\n");
            sb.append("      \"attackSpeed\": ").append(mult.attackSpeed).append(",\n");
            sb.append("      \"attackKnockback\": ").append(mult.attackKnockback).append(",\n");
            sb.append("      \"knockbackResistance\": ").append(mult.knockbackResistance).append(",\n");
            sb.append("      \"luck\": ").append(mult.luck).append("\n");
            sb.append("    }").append(count < biomeMultipliers.size() ? "," : "").append("\n");
        }
        
        sb.append("  },\n\n");
        
        sb.append("  // Depth scaling: Progressive difficulty based on Y-coordinate (Overworld only)\n");
        sb.append("  // Multiplies with biome multipliers: Final = biome × depth\n");
        sb.append("  \"depthScaling\": {\n");
        sb.append("    // Enable/disable depth scaling\n");
        sb.append("    \"enabled\": ").append(depthScaling.enabled).append(",\n\n");
        sb.append("    // Y-level where scaling starts (above = no bonus)\n");
        sb.append("    \"yThreshold\": ").append(depthScaling.yThreshold).append(",\n\n");
        sb.append("    // Y-level where max multiplier is reached\n");
        sb.append("    \"maxDepth\": ").append(depthScaling.maxDepth).append(",\n\n");
        sb.append("    // Scaling mode\n");
        sb.append("    // Options: \"linear\" (even progression)\n");
        sb.append("    \"scalingMode\": \"").append(depthScaling.scalingMode).append("\",\n\n");
        sb.append("    // Maximum multipliers at maxDepth\n");
        sb.append("    \"maxMultipliers\": {\n");
        sb.append("      \"maxHealth\": ").append(depthScaling.maxMultipliers.maxHealth).append(",\n");
        sb.append("      \"armor\": ").append(depthScaling.maxMultipliers.armor).append(",\n");
        sb.append("      \"armorToughness\": ").append(depthScaling.maxMultipliers.armorToughness).append(",\n");
        sb.append("      \"attackDamage\": ").append(depthScaling.maxMultipliers.attackDamage).append(",\n");
        sb.append("      \"attackSpeed\": ").append(depthScaling.maxMultipliers.attackSpeed).append(",\n");
        sb.append("      \"attackKnockback\": ").append(depthScaling.maxMultipliers.attackKnockback).append(",\n");
        sb.append("      \"knockbackResistance\": ").append(depthScaling.maxMultipliers.knockbackResistance).append(",\n");
        sb.append("      \"luck\": ").append(depthScaling.maxMultipliers.luck).append("\n");
        sb.append("    }\n");
        sb.append("  }\n");
        sb.append("}\n");
        
        return sb.toString();
    }
    
    public BiomeMultipliers getMultipliersForBiome(String biomeId) {
        return biomeMultipliers.getOrDefault(biomeId, defaultMultipliers);
    }
}
