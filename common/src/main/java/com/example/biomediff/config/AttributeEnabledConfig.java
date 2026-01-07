package com.example.biomediff.config;

import com.google.gson.JsonObject;

public class AttributeEnabledConfig {
    public boolean maxHealth = true;
    public boolean armor = true;
    public boolean armorToughness = true;
    public boolean attackDamage = true;
    public boolean attackSpeed = false;
    public boolean attackKnockback = false;
    public boolean knockbackResistance = true;
    public boolean luck = false;
    
    public static AttributeEnabledConfig fromJson(JsonObject json) {
        AttributeEnabledConfig config = new AttributeEnabledConfig();
        
        if (json.has("maxHealth")) config.maxHealth = json.get("maxHealth").getAsBoolean();
        if (json.has("armor")) config.armor = json.get("armor").getAsBoolean();
        if (json.has("armorToughness")) config.armorToughness = json.get("armorToughness").getAsBoolean();
        if (json.has("attackDamage")) config.attackDamage = json.get("attackDamage").getAsBoolean();
        if (json.has("attackSpeed")) config.attackSpeed = json.get("attackSpeed").getAsBoolean();
        if (json.has("attackKnockback")) config.attackKnockback = json.get("attackKnockback").getAsBoolean();
        if (json.has("knockbackResistance")) config.knockbackResistance = json.get("knockbackResistance").getAsBoolean();
        if (json.has("luck")) config.luck = json.get("luck").getAsBoolean();
        
        return config;
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
}
