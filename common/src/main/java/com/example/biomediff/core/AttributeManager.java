package com.example.biomediff.core;

import com.example.biomediff.BiomeDifficulty;
import com.example.biomediff.config.AttributeEnabledConfig;
import com.example.biomediff.config.BiomeMultipliers;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public class AttributeManager {
    private static final String MODIFIER_PREFIX = "biomediff_";
    
    private static final UUID MAX_HEALTH_UUID = UUID.nameUUIDFromBytes((MODIFIER_PREFIX + "max_health").getBytes());
    private static final UUID ARMOR_UUID = UUID.nameUUIDFromBytes((MODIFIER_PREFIX + "armor").getBytes());
    private static final UUID ARMOR_TOUGHNESS_UUID = UUID.nameUUIDFromBytes((MODIFIER_PREFIX + "armor_toughness").getBytes());
    private static final UUID ATTACK_DAMAGE_UUID = UUID.nameUUIDFromBytes((MODIFIER_PREFIX + "attack_damage").getBytes());
    private static final UUID ATTACK_SPEED_UUID = UUID.nameUUIDFromBytes((MODIFIER_PREFIX + "attack_speed").getBytes());
    private static final UUID ATTACK_KNOCKBACK_UUID = UUID.nameUUIDFromBytes((MODIFIER_PREFIX + "attack_knockback").getBytes());
    private static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.nameUUIDFromBytes((MODIFIER_PREFIX + "knockback_resistance").getBytes());
    private static final UUID LUCK_UUID = UUID.nameUUIDFromBytes((MODIFIER_PREFIX + "luck").getBytes());
    
    public static void applyModifiers(LivingEntity entity, String biomeId, BiomeMultipliers baseMultipliers) {
        AttributeEnabledConfig enabled = BiomeDifficulty.getConfig().enabledAttributes;
        
        // Get dimension multiplier (with overworld as fallback)
        String dimensionId = getDimensionId(entity);
        BiomeMultipliers dimensionMultipliers = getDimensionMultipliers(dimensionId);
        
        // Calculate depth-based multipliers
        BiomeMultipliers depthMultipliers = calculateDepthMultipliers(entity);
        
        // Combine all multipliers: dimension × biome × depth
        BiomeMultipliers combined = BiomeMultipliers.combine(dimensionMultipliers, baseMultipliers);
        BiomeMultipliers multipliers = BiomeMultipliers.combine(combined, depthMultipliers);
        
        // Check if entity is a player
        boolean isPlayer = entity instanceof net.minecraft.world.entity.player.Player;
        
        // Store current health percentage before modifying max health (mobs only)
        float healthPercentage = isPlayer ? 0 : entity.getHealth() / entity.getMaxHealth();
        
        // === MOB ATTRIBUTES (not for players) ===
        if (!isPlayer) {
            if (enabled.maxHealth && multipliers.maxHealth != 1.0) {
                applyModifier(entity, Attributes.MAX_HEALTH, MAX_HEALTH_UUID, 
                    "Biome Max Health", multipliers.maxHealth - 1.0);
            }
            
            if (enabled.armor && multipliers.armor != 1.0) {
                applyModifier(entity, Attributes.ARMOR, ARMOR_UUID,
                    "Biome Armor", multipliers.armor - 1.0);
            }
            
            if (enabled.armorToughness && multipliers.armorToughness != 1.0) {
                applyModifier(entity, Attributes.ARMOR_TOUGHNESS, ARMOR_TOUGHNESS_UUID,
                    "Biome Armor Toughness", multipliers.armorToughness - 1.0);
            }
            
            if (enabled.attackDamage && multipliers.attackDamage != 1.0) {
                applyModifier(entity, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_UUID,
                    "Biome Attack Damage", multipliers.attackDamage - 1.0);
            }
            
            if (enabled.attackSpeed && multipliers.attackSpeed != 1.0) {
                applyModifier(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED_UUID,
                    "Biome Attack Speed", multipliers.attackSpeed - 1.0);
            }
            
            if (enabled.attackKnockback && multipliers.attackKnockback != 1.0) {
                applyModifier(entity, Attributes.ATTACK_KNOCKBACK, ATTACK_KNOCKBACK_UUID,
                    "Biome Attack Knockback", multipliers.attackKnockback - 1.0);
            }
            
            if (enabled.knockbackResistance && multipliers.knockbackResistance != 0.0) {
                applyModifier(entity, Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_UUID,
                    "Biome Knockback Resistance", multipliers.knockbackResistance);
            }
            
            // Restore health percentage after max health change
            if (enabled.maxHealth && multipliers.maxHealth != 1.0) {
                float newHealth = healthPercentage * entity.getMaxHealth();
                entity.setHealth(Math.min(newHealth, entity.getMaxHealth()));
            }
        }
        
        // === PLAYER ATTRIBUTE (luck only) ===
        if (isPlayer) {
            if (enabled.luck && multipliers.luck != 1.0) {
                applyModifier(entity, Attributes.LUCK, LUCK_UUID,
                    "Biome Luck", multipliers.luck - 1.0);
            }
        }
        
        if (BiomeDifficulty.getConfig().debugEnabled && BiomeDifficulty.getConfig().debugLogAttributeChanges) {
            BiomeDifficulty.LOGGER.info("Applied modifiers to {} - Dimension: {}, Biome: {}", 
                entity.getName().getString(), dimensionId, biomeId);
        }
    }
    
    public static void removeModifiers(LivingEntity entity) {
        AttributeEnabledConfig enabled = BiomeDifficulty.getConfig().enabledAttributes;
        boolean isPlayer = entity instanceof net.minecraft.world.entity.player.Player;
        
        // Store current health percentage before removing max health modifier (mobs only)
        float healthPercentage = isPlayer ? 0 : entity.getHealth() / entity.getMaxHealth();
        
        // === MOB ATTRIBUTES ===
        if (!isPlayer) {
            if (enabled.maxHealth) removeModifier(entity, Attributes.MAX_HEALTH, MAX_HEALTH_UUID);
            if (enabled.armor) removeModifier(entity, Attributes.ARMOR, ARMOR_UUID);
            if (enabled.armorToughness) removeModifier(entity, Attributes.ARMOR_TOUGHNESS, ARMOR_TOUGHNESS_UUID);
            if (enabled.attackDamage) removeModifier(entity, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_UUID);
            if (enabled.attackSpeed) removeModifier(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED_UUID);
            if (enabled.attackKnockback) removeModifier(entity, Attributes.ATTACK_KNOCKBACK, ATTACK_KNOCKBACK_UUID);
            if (enabled.knockbackResistance) removeModifier(entity, Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_UUID);
            
            // Restore health percentage after max health change
            if (enabled.maxHealth) {
                float newHealth = healthPercentage * entity.getMaxHealth();
                entity.setHealth(Math.min(newHealth, entity.getMaxHealth()));
            }
        }
        
        // === PLAYER ATTRIBUTE ===
        if (isPlayer) {
            if (enabled.luck) removeModifier(entity, Attributes.LUCK, LUCK_UUID);
        }
    }
    
    private static void applyModifier(LivingEntity entity, Holder<Attribute> attribute, UUID uuid, String name, double value) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return;
        
        // Remove existing modifier if present
        ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath("biomediff", uuid.toString());
        if (instance.getModifier(modifierId) != null) {
            instance.removeModifier(modifierId);
        }
        
        // Add new modifier using ADD_MULTIPLIED_BASE operation (equivalent to old MULTIPLY_BASE)
        AttributeModifier modifier = new AttributeModifier(
            modifierId,
            value,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
        instance.addPermanentModifier(modifier);
    }
    
    private static void removeModifier(LivingEntity entity, Holder<Attribute> attribute, UUID uuid) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath("biomediff", uuid.toString());
            instance.removeModifier(modifierId);
        }
    }
    
    public static String getBiomeId(LivingEntity entity) {
        ResourceLocation biomeId = entity.level()
            .getBiome(entity.blockPosition())
            .unwrapKey()
            .map(key -> key.location())
            .orElse(null);
        
        return biomeId != null ? biomeId.toString() : "minecraft:plains";
    }
    
    private static BiomeMultipliers calculateDepthMultipliers(LivingEntity entity) {
        com.example.biomediff.config.DepthScalingConfig config = BiomeDifficulty.getConfig().depthScaling;
        
        // Check if enabled
        if (!config.enabled) {
            return new BiomeMultipliers(); // Returns 1.0x for all attributes
        }
        
        // Check if in overworld
        if (!isOverworld(entity)) {
            return new BiomeMultipliers();
        }
        
        // Get entity Y position
        int entityY = entity.blockPosition().getY();
        
        // Above threshold = no bonus
        if (entityY >= config.yThreshold) {
            return new BiomeMultipliers();
        }
        
        // Calculate depth factor (0.0 to 1.0)
        float depthFactor = calculateDepthFactor(entityY, config);
        
        // Apply depth factor to max multipliers
        BiomeMultipliers result = new BiomeMultipliers();
        result.maxHealth = lerp(1.0, config.maxMultipliers.maxHealth, depthFactor);
        result.armor = lerp(1.0, config.maxMultipliers.armor, depthFactor);
        result.armorToughness = lerp(1.0, config.maxMultipliers.armorToughness, depthFactor);
        result.attackDamage = lerp(1.0, config.maxMultipliers.attackDamage, depthFactor);
        result.attackSpeed = lerp(1.0, config.maxMultipliers.attackSpeed, depthFactor);
        result.attackKnockback = lerp(1.0, config.maxMultipliers.attackKnockback, depthFactor);
        result.knockbackResistance = lerp(0.0, config.maxMultipliers.knockbackResistance, depthFactor);
        result.luck = lerp(1.0, config.maxMultipliers.luck, depthFactor);
        
        return result;
    }
    
    private static float calculateDepthFactor(int entityY, com.example.biomediff.config.DepthScalingConfig config) {
        int range = config.yThreshold - config.maxDepth;
        if (range <= 0) return 1.0f; // Avoid division by zero
        
        int depth = config.yThreshold - entityY;
        float factor = Math.min(1.0f, (float) depth / range);
        
        if ("linear".equals(config.scalingMode)) {
            return factor;
        }
        
        // Default to linear
        return factor;
    }
    
    private static boolean isOverworld(LivingEntity entity) {
        return entity.level().dimension() == net.minecraft.world.level.Level.OVERWORLD;
    }
    
    private static double lerp(double start, double end, float factor) {
        return start + (end - start) * factor;
    }
    
    public static String getDimensionId(LivingEntity entity) {
        ResourceLocation dimensionId = entity.level().dimension().location();
        return dimensionId != null ? dimensionId.toString() : "minecraft:overworld";
    }
    
    private static BiomeMultipliers getDimensionMultipliers(String dimensionId) {
        Map<String, BiomeMultipliers> dimMultipliers = BiomeDifficulty.getConfig().dimensionMultipliers;
        
        // Check if dimension has custom multipliers
        if (dimMultipliers.containsKey(dimensionId)) {
            return dimMultipliers.get(dimensionId);
        }
        
        // Fallback to overworld as default
        if (dimMultipliers.containsKey("minecraft:overworld")) {
            return dimMultipliers.get("minecraft:overworld");
        }
        
        // Ultimate fallback: 1.0x for all attributes
        return new BiomeMultipliers();
    }
}
