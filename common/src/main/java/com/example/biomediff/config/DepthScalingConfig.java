package com.example.biomediff.config;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class DepthScalingConfig {
    @SerialEntry(comment = "Enable/disable depth scaling")
    public boolean enabled = false;

    @SerialEntry(comment = "Y-level where scaling starts (above = no bonus)")
    public int yThreshold = 0;

    @SerialEntry(comment = "Y-level where max multiplier is reached")
    public int maxDepth = -64;

    @SerialEntry(comment = "Scaling mode: linear")
    public String scalingMode = "linear";

    @SerialEntry(comment = "Maximum multipliers at maxDepth")
    public BiomeMultipliers maxMultipliers = new BiomeMultipliers();
}
