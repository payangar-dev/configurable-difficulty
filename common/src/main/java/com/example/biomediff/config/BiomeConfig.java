package com.example.biomediff.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;

import java.util.HashMap;
import java.util.Map;

public class BiomeConfig {
    public static final ConfigClassHandler<BiomeConfig> HANDLER =
        ConfigClassHandler.createBuilder(BiomeConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                .setPath(YACLPlatform.getConfigDir().resolve("configurable-difficulty.json5"))
                .setJson5(true)
                .build())
            .build();

    @SerialEntry(comment = "Enable/disable the entire mod")
    public boolean enabled = true;

    @SerialEntry(comment = "How players get difficulty modifiers\\nOptions: DYNAMIC, SPAWN_ONLY")
    public ModifierMode playerMode = ModifierMode.DYNAMIC;

    @SerialEntry(comment = "How mobs get difficulty modifiers\\nOptions: DYNAMIC, SPAWN_ONLY")
    public ModifierMode mobMode = ModifierMode.SPAWN_ONLY;

    @SerialEntry(comment = "Check interval in ticks (20 = 1 second)\\nLower = more responsive, higher = better performance")
    public int checkInterval = 20;

    @SerialEntry(comment = "Apply to hostile mobs")
    public boolean applyToHostileMobs = true;

    @SerialEntry(comment = "Apply to passive mobs")
    public boolean applyToPassiveMobs = false;

    @SerialEntry(comment = "Apply to neutral mobs")
    public boolean applyToNeutralMobs = false;

    @SerialEntry(comment = "Enable debug logging")
    public boolean debugEnabled = false;

    @SerialEntry(comment = "Log biome changes")
    public boolean debugLogBiomeChanges = true;

    @SerialEntry(comment = "Log attribute changes")
    public boolean debugLogAttributeChanges = true;

    @SerialEntry(comment = "Which attributes to modify")
    public AttributeEnabledConfig enabledAttributes = new AttributeEnabledConfig();

    @SerialEntry(comment = "Default multipliers for biomes not explicitly configured\\nMultiplier of 1.0 = no change, 2.0 = 2x harder, 0.5 = half difficulty")
    public BiomeMultipliers defaultMultipliers = new BiomeMultipliers();

    @SerialEntry(comment = "Dimension multipliers: Base difficulty per dimension\\nFormat: modid:dimension_name")
    public Map<String, BiomeMultipliers> dimensionMultipliers = createDefaultDimensionMultipliers();

    @SerialEntry(comment = "Biome-specific multipliers\\nFormat: modid:biomename")
    public Map<String, BiomeMultipliers> biomeMultipliers = createDefaultBiomeMultipliers();

    @SerialEntry(comment = "Depth scaling: Progressive difficulty based on Y-coordinate")
    public DepthScalingConfig depthScaling = new DepthScalingConfig();

    public static BiomeConfig load() {
        return HANDLER.instance();
    }

    public void save() {
        HANDLER.save();
    }

    private static Map<String, BiomeMultipliers> createDefaultDimensionMultipliers() {
        Map<String, BiomeMultipliers> map = new HashMap<>();

        BiomeMultipliers overworldMult = new BiomeMultipliers();
        map.put("minecraft:overworld", overworldMult);

        BiomeMultipliers netherMult = new BiomeMultipliers();
        netherMult.maxHealth = 1.5;
        netherMult.armor = 1.3;
        netherMult.attackDamage = 1.5;
        netherMult.luck = 1.2;
        map.put("minecraft:the_nether", netherMult);

        BiomeMultipliers endMult = new BiomeMultipliers();
        endMult.maxHealth = 2.0;
        endMult.armor = 1.5;
        endMult.attackDamage = 2.0;
        endMult.luck = 1.5;
        map.put("minecraft:the_end", endMult);

        return map;
    }

    private static Map<String, BiomeMultipliers> createDefaultBiomeMultipliers() {
        Map<String, BiomeMultipliers> map = new HashMap<>();

        BiomeMultipliers desert = new BiomeMultipliers();
        desert.maxHealth = 1.5;
        desert.armor = 0.8;
        desert.attackDamage = 1.3;
        desert.knockbackResistance = 0.1;
        desert.luck = 1.1;
        map.put("minecraft:desert", desert);

        BiomeMultipliers frozenOcean = new BiomeMultipliers();
        frozenOcean.maxHealth = 2.0;
        frozenOcean.armor = 1.5;
        frozenOcean.armorToughness = 1.3;
        frozenOcean.attackDamage = 1.5;
        frozenOcean.knockbackResistance = 0.3;
        frozenOcean.luck = 1.3;
        map.put("minecraft:frozen_ocean", frozenOcean);

        BiomeMultipliers deepDark = new BiomeMultipliers();
        deepDark.maxHealth = 2.5;
        deepDark.armor = 2.0;
        deepDark.armorToughness = 1.5;
        deepDark.attackDamage = 2.0;
        deepDark.knockbackResistance = 0.4;
        deepDark.luck = 1.5;
        map.put("minecraft:deep_dark", deepDark);

        BiomeMultipliers mushroomFields = new BiomeMultipliers();
        mushroomFields.maxHealth = 0.5;
        mushroomFields.armor = 0.5;
        mushroomFields.attackDamage = 0.5;
        mushroomFields.luck = 1.2;
        map.put("minecraft:mushroom_fields", mushroomFields);

        return map;
    }

    public BiomeMultipliers getMultipliersForBiome(String biomeId) {
        return biomeMultipliers.getOrDefault(biomeId, defaultMultipliers);
    }
}
