package com.altnoir.filthdomain.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public final class FilthTerrainSettings {
    public static final int MIN_Y = -256;
    public static final int TOP_Y = 256;
    public static final int HEIGHT = TOP_Y - MIN_Y;
    public static final ResourceKey<NoiseGeneratorSettings> NOISE_SETTINGS =
            ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(FilthDomain.MOD_ID, "filthdomain"));
    public static final ResourceKey<Level> DIMENSION = ResourceKey.create(Registries.DIMENSION, NOISE_SETTINGS.location());

    private FilthTerrainSettings() {
    }
}
