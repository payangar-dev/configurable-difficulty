package com.example.biomediff.config;

import com.google.gson.JsonObject;

public class BiomeMultipliers {
    public double maxHealth = 1.0;
    public double armor = 1.0;
    public double armorToughness = 1.0;
    public double attackDamage = 1.0;
    public double attackSpeed = 1.0;
    public double attackKnockback = 1.0;
    public double knockbackResistance = 0.0;
    public double luck = 1.0;
    
    public static BiomeMultipliers fromJson(JsonObject json) {
        BiomeMultipliers multipliers = new BiomeMultipliers();
        
        if (json.has("maxHealth")) multipliers.maxHealth = json.get("maxHealth").getAsDouble();
        if (json.has("armor")) multipliers.armor = json.get("armor").getAsDouble();
        if (json.has("armorToughness")) multipliers.armorToughness = json.get("armorToughness").getAsDouble();
        if (json.has("attackDamage")) multipliers.attackDamage = json.get("attackDamage").getAsDouble();
        if (json.has("attackSpeed")) multipliers.attackSpeed = json.get("attackSpeed").getAsDouble();
        if (json.has("attackKnockback")) multipliers.attackKnockback = json.get("attackKnockback").getAsDouble();
        if (json.has("knockbackResistance")) multipliers.knockbackResistance = json.get("knockbackResistance").getAsDouble();
        if (json.has("luck")) multipliers.luck = json.get("luck").getAsDouble();
        
        return multipliers;
    }
    
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("maxHealth", maxHealth);
        json.addProperty("armor", armor);
        json.addProperty("armorToughness", armorToughness);
        json.addProperty("attackDamage", attackDamage);
        json.addProperty("attackSpeed", attackSpeed);
        json.addProperty("attackKnockback", attackKnockback);
        json.addProperty("knockbackResistance", knockbackResistance);
        json.addProperty("luck", luck);
        return json;
    }
    
    public static BiomeMultipliers combine(BiomeMultipliers a, BiomeMultipliers b) {
        BiomeMultipliers result = new BiomeMultipliers();
        result.maxHealth = a.maxHealth * b.maxHealth;
        result.armor = a.armor * b.armor;
        result.armorToughness = a.armorToughness * b.armorToughness;
        result.attackDamage = a.attackDamage * b.attackDamage;
        result.attackSpeed = a.attackSpeed * b.attackSpeed;
        result.attackKnockback = a.attackKnockback * b.attackKnockback;
        result.knockbackResistance = a.knockbackResistance + b.knockbackResistance;
        result.luck = a.luck * b.luck;
        return result;
    }
}
