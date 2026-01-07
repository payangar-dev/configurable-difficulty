package com.example.biomediff.config;

public enum ModifierMode {
    SPAWN_ONLY,
    DYNAMIC;
    
    public static ModifierMode fromString(String str) {
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SPAWN_ONLY;
        }
    }
}
