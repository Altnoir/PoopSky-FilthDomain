package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import static net.minecraft.world.level.levelgen.DensityFunctions.add;
import static net.minecraft.world.level.levelgen.DensityFunctions.constant;
import static net.minecraft.world.level.levelgen.DensityFunctions.max;
import static net.minecraft.world.level.levelgen.DensityFunctions.mul;

final class FDDensityFunctions {
    static final ResourceKey<DensityFunction> Y = ResourceKey.create(
            Registries.DENSITY_FUNCTION, ResourceLocation.withDefaultNamespace("y"));
    static final ResourceKey<DensityFunction> DEEP_HALL = key("deep_hall");
    static final ResourceKey<DensityFunction> DEEP_HALL_PILLARS = key("deep_hall/pillars");
    static final ResourceKey<DensityFunction> CALCIUM_CAVE = key("calcium_cave");
    static final ResourceKey<DensityFunction> BASE_3D_NOISE = key("base_3d_noise_filthdomain");

    private FDDensityFunctions() {
    }

    static void bootstrap(BootstrapContext<DensityFunction> context) {
        var densities = context.lookup(Registries.DENSITY_FUNCTION);
        var y = new DensityFunctions.HolderHolder(densities.getOrThrow(Y));
        var noise = context.lookup(Registries.NOISE);
        context.register(BASE_3D_NOISE, BlendedNoise.createUnseeded(0.3, 0.1, 160, 128, 1));
        context.register(DEEP_HALL, cavity(y,
                height(noise.getOrThrow(FDNoiseParameters.HALL_FLOOR), -107, 8),
                height(noise.getOrThrow(FDNoiseParameters.HALL_ROOF), -26, 8)));
        // Use a continuous signed field so the interpolation grid never contains an abrupt off value.
        context.register(DEEP_HALL_PILLARS, add(mul(constant(1.65), pillarNoise(noise)), constant(-0.075))
                .clamp(-0.45, 1.5));
        context.register(CALCIUM_CAVE, cavity(y,
                height(noise.getOrThrow(FDNoiseParameters.CALCIUM_FLOOR), -175, 4),
                height(noise.getOrThrow(FDNoiseParameters.CALCIUM_ROOF), -150, 6)));
    }

    private static DensityFunction pillarNoise(HolderGetter<NormalNoise.NoiseParameters> noise) {
        // Sample the pillar field less frequently across X and Z to broaden each column.
        var columns = DensityFunctions.noise(noise.getOrThrow(Noises.PILLAR), 14.0, 0.3);
        var rareness = DensityFunctions.mappedNoise(noise.getOrThrow(Noises.PILLAR_RARENESS), 0.0, -1.65);
        var thickness = DensityFunctions.mappedNoise(noise.getOrThrow(Noises.PILLAR_THICKNESS), 0.0, 1.1);
        return DensityFunctions.cacheOnce(mul(add(mul(columns, constant(3.0)), rareness), thickness.cube()));
    }

    private static DensityFunction height(Holder<NormalNoise.NoiseParameters> noise, int height, int amplitude) {
        var variation = DensityFunctions.cache2d(DensityFunctions.noise(noise, 1.0, 0.0).clamp(-1.0, 1.0));
        return add(constant(height), mul(constant(amplitude), variation));
    }

    private static DensityFunction cavity(DensityFunction y, DensityFunction floor, DensityFunction roof) {
        // Density is negative only in between the two independently varying boundaries.
        var distance = max(add(floor, mul(constant(-1), y)), add(y, mul(constant(-1), roof)));
        return mul(constant(0.25), distance);
    }

    private static ResourceKey<DensityFunction> key(String path) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, FilthDomain.loc(path));
    }
}
