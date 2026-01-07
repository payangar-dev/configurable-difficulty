package com.example.biomediff.fabric;

import com.example.biomediff.BiomeDifficulty;
import com.example.biomediff.core.DynamicModifierHandler;
import com.example.biomediff.core.SpawnModifierHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.entity.LivingEntity;

public class BiomeDifficultyFabric implements ModInitializer {
    private int tickCounter = 0;
    
    @Override
    public void onInitialize() {
        BiomeDifficulty.LOGGER.info("Initializing Biome Difficulty for Fabric");
        BiomeDifficulty.init();
        
        // Register entity spawn event
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof LivingEntity livingEntity) {
                SpawnModifierHandler.onEntitySpawn(livingEntity);
            }
        });
        
        // Register server tick event for dynamic updates
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!BiomeDifficulty.getConfig().enabled) return;
            
            tickCounter++;
            if (tickCounter < BiomeDifficulty.getConfig().checkInterval) return;
            tickCounter = 0;
            
            // Check all loaded entities for biome changes
            server.getAllLevels().forEach(level -> {
                level.getAllEntities().forEach(entity -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        DynamicModifierHandler.checkEntity(livingEntity);
                    }
                });
            });
        });
        
        // Register entity unload event for cleanup
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof LivingEntity livingEntity) {
                DynamicModifierHandler.onEntityRemoved(livingEntity);
            }
        });
    }
}
