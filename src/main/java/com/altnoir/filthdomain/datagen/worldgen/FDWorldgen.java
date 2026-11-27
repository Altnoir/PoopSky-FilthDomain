package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.poopsky.impl.DatapackGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class FDWorldgen extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder REGISTRIES = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, FDFeatures::bootstrapConfigured)
            .add(Registries.PLACED_FEATURE, context -> {
                FDFeatures.bootstrapPlaced(context);
                FDForestFeatures.bootstrapPlaced(context);
            })
            .add(Registries.CONFIGURED_CARVER, FDTerrain::bootstrapCarvers)
            .add(Registries.BIOME, FDBiomes::bootstrap)
            .add(Registries.NOISE, FDNoiseParameters::bootstrap)
            .add(Registries.DENSITY_FUNCTION, FDDensityFunctions::bootstrap)
            .add(Registries.NOISE_SETTINGS, FDTerrain::bootstrapNoise)
            .add(Registries.DIMENSION_TYPE, FDDimensions::bootstrapTypes)
            .add(Registries.LEVEL_STEM, FDDimensions::bootstrap);

    public FDWorldgen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, new DatapackGen(output, registries).getRegistryProvider(), REGISTRIES, Set.of(FilthDomain.MOD_ID));
    }
}
