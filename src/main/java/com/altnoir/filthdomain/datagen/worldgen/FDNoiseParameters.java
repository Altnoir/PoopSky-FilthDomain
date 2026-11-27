package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

final class FDNoiseParameters {
    static final ResourceKey<NormalNoise.NoiseParameters> TEMPERATURE = key("biome_temperature");
    static final ResourceKey<NormalNoise.NoiseParameters> HUMIDITY = key("biome_humidity");
    static final ResourceKey<NormalNoise.NoiseParameters> HALL_FLOOR = key("hall_floor");
    static final ResourceKey<NormalNoise.NoiseParameters> HALL_ROOF = key("hall_roof");
    static final ResourceKey<NormalNoise.NoiseParameters> CALCIUM_FLOOR = key("calcium_floor");
    static final ResourceKey<NormalNoise.NoiseParameters> CALCIUM_ROOF = key("calcium_roof");

    private FDNoiseParameters() {
    }

    static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
        context.register(TEMPERATURE, new NormalNoise.NoiseParameters(-7, 1.0, 1.0));
        context.register(HUMIDITY, new NormalNoise.NoiseParameters(-7, 1.0, 1.0));
        context.register(HALL_FLOOR, new NormalNoise.NoiseParameters(-8, 1.0, 0.35));
        context.register(HALL_ROOF, new NormalNoise.NoiseParameters(-8, 1.0, 0.35));
        context.register(CALCIUM_FLOOR, new NormalNoise.NoiseParameters(-7, 1.0, 0.35));
        context.register(CALCIUM_ROOF, new NormalNoise.NoiseParameters(-7, 1.0, 0.55));
    }

    private static ResourceKey<NormalNoise.NoiseParameters> key(String path) {
        return ResourceKey.create(Registries.NOISE, FilthDomain.loc(path));
    }
}
