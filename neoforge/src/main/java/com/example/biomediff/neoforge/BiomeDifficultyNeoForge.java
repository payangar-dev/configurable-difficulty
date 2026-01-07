package com.example.biomediff.neoforge;

import com.example.biomediff.BiomeDifficulty;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(BiomeDifficulty.MOD_ID)
public class BiomeDifficultyNeoForge {
    
    public BiomeDifficultyNeoForge(IEventBus modEventBus) {
        BiomeDifficulty.LOGGER.info("Initializing Biome Difficulty for NeoForge");
        
        modEventBus.addListener(this::commonSetup);
        
        // Register event handlers
        NeoForge.EVENT_BUS.register(new EventHandlers());
    }
    
    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(BiomeDifficulty::init);
    }
}
