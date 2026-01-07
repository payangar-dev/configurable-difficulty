package com.example.biomediff.config;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class AttributeEnabledConfig {
    @SerialEntry
    public boolean maxHealth = true;

    @SerialEntry
    public boolean armor = true;

    @SerialEntry
    public boolean armorToughness = true;

    @SerialEntry
    public boolean attackDamage = true;

    @SerialEntry
    public boolean attackSpeed = false;

    @SerialEntry
    public boolean attackKnockback = false;

    @SerialEntry
    public boolean knockbackResistance = true;

    @SerialEntry
    public boolean luck = false;
}
