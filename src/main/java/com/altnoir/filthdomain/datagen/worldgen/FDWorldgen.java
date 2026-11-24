package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.poopsky.impl.DatapackGen;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Optional;
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
            .add(Registries.NOISE, FDTerrain::bootstrapClimateNoise)
            .add(Registries.NOISE_SETTINGS, FDTerrain::bootstrapNoise)
            .add(Registries.DIMENSION_TYPE, FDDimensions::bootstrapTypes)
            .add(Registries.LEVEL_STEM, FDDimensions::bootstrap);

    public FDWorldgen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, new DatapackGen(output, registries).getRegistryProvider(), REGISTRIES, Set.of(FilthDomain.MOD_ID));
    }

    static <T> void register(BootstrapContext<T> context, ResourceKey<? extends Registry<T>> registry,
                             Codec<T> codec, String definitions) {
        // Resolve registry references through the bootstrap, including entries in the same run.
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, new RegistryOps.RegistryInfoLookup() {
            @Override
            public <S> Optional<RegistryOps.RegistryInfo<S>> lookup(
                    ResourceKey<? extends Registry<? extends S>> key) {
                return Optional.of(new RegistryOps.RegistryInfo<>(
                        new HolderOwner<>() {}, context.lookup(key), Lifecycle.stable()));
            }
        });
        JsonParser.parseString(definitions).getAsJsonObject().entrySet().forEach(entry -> {
            ResourceKey<T> key = ResourceKey.create(registry, FilthDomain.loc(entry.getKey()));
            T value = codec.parse(ops, entry.getValue())
                    .getOrThrow(message -> new IllegalStateException(key + ": " + message));
            context.register(key, value);
        });
    }
}
