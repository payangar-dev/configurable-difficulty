package com.example.biomediff.config;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class BiomeMultipliers {
    @SerialEntry
    public double maxHealth = 1.0;

    @SerialEntry
    public double armor = 1.0;

    @SerialEntry
    public double armorToughness = 1.0;

    @SerialEntry
    public double attackDamage = 1.0;

    @SerialEntry
    public double attackSpeed = 1.0;

    @SerialEntry
    public double attackKnockback = 1.0;

    @SerialEntry
    public double knockbackResistance = 0.0;

    @SerialEntry
    public double luck = 1.0;

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
