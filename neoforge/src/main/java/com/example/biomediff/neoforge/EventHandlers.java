package com.example.biomediff.neoforge;

import com.example.biomediff.BiomeDifficulty;
import com.example.biomediff.core.DynamicModifierHandler;
import com.example.biomediff.core.SpawnModifierHandler;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class EventHandlers {
    private int tickCounter = 0;
    
    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            if (!event.getLevel().isClientSide()) {
                SpawnModifierHandler.onEntitySpawn(livingEntity);
            }
        }
    }
    
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (!BiomeDifficulty.getConfig().enabled) return;
        
        tickCounter++;
        if (tickCounter < BiomeDifficulty.getConfig().checkInterval) return;
        tickCounter = 0;
        
        // Check all loaded entities for biome changes
        event.getServer().getAllLevels().forEach(level -> {
            level.getAllEntities().forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity) {
                    DynamicModifierHandler.checkEntity(livingEntity);
                }
            });
        });
    }
}
