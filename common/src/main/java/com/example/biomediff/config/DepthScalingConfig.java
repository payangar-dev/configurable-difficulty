package com.example.biomediff.config;

import com.google.gson.JsonObject;

public class DepthScalingConfig {
    public boolean enabled = false;
    public int yThreshold = 0;
    public int maxDepth = -64;
    public String scalingMode = "linear";
    public BiomeMultipliers maxMultipliers = new BiomeMultipliers();
    
    public static DepthScalingConfig fromJson(JsonObject json) {
        DepthScalingConfig config = new DepthScalingConfig();
        
        if (json.has("enabled")) config.enabled = json.get("enabled").getAsBoolean();
        if (json.has("yThreshold")) config.yThreshold = json.get("yThreshold").getAsInt();
        if (json.has("maxDepth")) config.maxDepth = json.get("maxDepth").getAsInt();
        if (json.has("scalingMode")) config.scalingMode = json.get("scalingMode").getAsString();
        
        if (json.has("maxMultipliers")) {
            config.maxMultipliers = BiomeMultipliers.fromJson(json.getAsJsonObject("maxMultipliers"));
        }
        
        return config;
    }
    
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("enabled", enabled);
        json.addProperty("yThreshold", yThreshold);
        json.addProperty("maxDepth", maxDepth);
        json.addProperty("scalingMode", scalingMode);
        json.add("maxMultipliers", maxMultipliers.toJson());
        return json;
    }
}
