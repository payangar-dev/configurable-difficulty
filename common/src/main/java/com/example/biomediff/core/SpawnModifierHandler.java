package com.example.biomediff.core;

import com.example.biomediff.BiomeDifficulty;
import com.example.biomediff.config.BiomeConfig;
import com.example.biomediff.config.BiomeMultipliers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;

public class SpawnModifierHandler {
    private static final String MODIFIER_APPLIED_TAG = "BiomeDiffApplied";
    
    public static void onEntitySpawn(LivingEntity entity) {
        if (!BiomeDifficulty.getConfig().enabled) return;
        if (entity.level().isClientSide) return;
        if (hasModifierApplied(entity)) return;
        
        BiomeConfig config = BiomeDifficulty.getConfig();
        
        // Check if we should apply to this entity type
        if (!shouldApplyToEntity(entity, config)) return;
        
        // Determine if we should apply spawn-time modifiers to this entity
        boolean shouldApply = false;
        if (entity instanceof Player && config.playerMode == com.example.biomediff.config.ModifierMode.SPAWN_ONLY) {
            shouldApply = true;
        } else if (!(entity instanceof Player) && config.mobMode == com.example.biomediff.config.ModifierMode.SPAWN_ONLY) {
            shouldApply = true;
        }
        
        if (!shouldApply) return;
        
        String biomeId = AttributeManager.getBiomeId(entity);
        BiomeMultipliers multipliers = config.getMultipliersForBiome(biomeId);
        
        AttributeManager.applyModifiers(entity, biomeId, multipliers);
        setModifierApplied(entity);
        
        if (config.debugEnabled && config.debugLogBiomeChanges) {
            BiomeDifficulty.LOGGER.info("Entity {} spawned in biome {}, applied spawn-time modifiers", 
                entity.getName().getString(), biomeId);
        }
    }
    
    private static boolean shouldApplyToEntity(LivingEntity entity, BiomeConfig config) {
        // Players always affected (luck only)
        if (entity instanceof Player) return true;
        
        // Mobs: check configuration
        if (entity instanceof Monster) return config.applyToHostileMobs;
        if (entity instanceof NeutralMob) return config.applyToNeutralMobs;
        if (entity instanceof Animal || entity instanceof WaterAnimal) return config.applyToPassiveMobs;
        
        return false;
    }
    
    private static boolean hasModifierApplied(LivingEntity entity) {
        return entity.getTags().contains(MODIFIER_APPLIED_TAG);
    }
    
    private static void setModifierApplied(LivingEntity entity) {
        entity.addTag(MODIFIER_APPLIED_TAG);
    }
}
