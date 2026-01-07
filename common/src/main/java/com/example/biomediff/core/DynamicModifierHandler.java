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

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class DynamicModifierHandler {
    private static final Map<UUID, String> entityBiomeMap = new WeakHashMap<>();
    private static int tickCounter = 0;
    
    public static void onServerTick() {
        if (!BiomeDifficulty.getConfig().enabled) return;
        
        tickCounter++;
        if (tickCounter < BiomeDifficulty.getConfig().checkInterval) return;
        tickCounter = 0;
        
        // This method is called from platform-specific implementations
        // that iterate through entities
    }
    
    public static void checkEntity(LivingEntity entity) {
        if (!BiomeDifficulty.getConfig().enabled) return;
        if (entity.level().isClientSide) return;
        
        BiomeConfig config = BiomeDifficulty.getConfig();
        
        // Check if we should apply to this entity type
        if (!shouldApplyToEntity(entity, config)) return;
        
        // Determine if we should use dynamic updates for this entity
        boolean shouldUseDynamic = false;
        if (entity instanceof Player && config.playerMode == com.example.biomediff.config.ModifierMode.DYNAMIC) {
            shouldUseDynamic = true;
        } else if (!(entity instanceof Player) && config.mobMode == com.example.biomediff.config.ModifierMode.DYNAMIC) {
            shouldUseDynamic = true;
        }
        
        if (!shouldUseDynamic) return;
        
        String currentBiomeId = AttributeManager.getBiomeId(entity);
        String previousBiomeId = entityBiomeMap.get(entity.getUUID());
        
        // If biome hasn't changed, do nothing
        if (currentBiomeId.equals(previousBiomeId)) return;
        
        // Remove old modifiers if there were any
        if (previousBiomeId != null) {
            AttributeManager.removeModifiers(entity);
        }
        
        // Apply new modifiers
        BiomeMultipliers multipliers = config.getMultipliersForBiome(currentBiomeId);
        AttributeManager.applyModifiers(entity, currentBiomeId, multipliers);
        
        // Update tracked biome
        entityBiomeMap.put(entity.getUUID(), currentBiomeId);
        
        if (config.debugEnabled && config.debugLogBiomeChanges) {
            BiomeDifficulty.LOGGER.info("Entity {} moved from biome {} to {}, updated modifiers", 
                entity.getName().getString(), previousBiomeId != null ? previousBiomeId : "none", currentBiomeId);
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
    
    public static void onEntityRemoved(LivingEntity entity) {
        entityBiomeMap.remove(entity.getUUID());
    }
}
