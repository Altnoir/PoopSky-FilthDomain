package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.worldgen.FilthTerrainSettings;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;

import static net.minecraft.world.level.levelgen.DensityFunctions.add;
import static net.minecraft.world.level.levelgen.DensityFunctions.constant;
import static net.minecraft.world.level.levelgen.DensityFunctions.max;
import static net.minecraft.world.level.levelgen.DensityFunctions.min;
import static net.minecraft.world.level.levelgen.DensityFunctions.mul;
import static net.minecraft.world.level.levelgen.DensityFunctions.rangeChoice;
import static net.minecraft.world.level.levelgen.DensityFunctions.shiftedNoise2d;
import static net.minecraft.world.level.levelgen.DensityFunctions.yClampedGradient;
import static net.minecraft.world.level.levelgen.DensityFunctions.zero;

final class FDNoiseRouter {
    private FDNoiseRouter() {
    }

    static NoiseRouter create(BootstrapContext<NoiseGeneratorSettings> context) {
        var density = context.lookup(Registries.DENSITY_FUNCTION);
        var noise = context.lookup(Registries.NOISE);
        var y = reference(density, FDDensityFunctions.Y);
        var shiftX = vanilla(density, "shift_x");
        var shiftZ = vanilla(density, "shift_z");
        var temperature = shiftedNoise2d(shiftX, shiftZ, 0.5, noise.getOrThrow(FDNoiseParameters.TEMPERATURE));
        var humidity = shiftedNoise2d(shiftX, shiftZ, 0.5, noise.getOrThrow(FDNoiseParameters.HUMIDITY));

        var baseNoise = DensityFunctions.interpolated(reference(density, FDDensityFunctions.BASE_3D_NOISE));
        var shapeNoise = add(baseNoise, constant(-0.38));
        var deep = deepTerrain(density, y, shapeNoise);
        var upperFade = min(yClampedGradient(1, 16, 0, 1), yClampedGradient(224, 240, 1, 0));
        var upper = add(upperTerrain(y, vanilla(density, "overworld/sloped_cheese")),
                mul(upperFade, mul(constant(0.32), shapeNoise.clamp(-0.28, 0.28))));
        // Branch after interpolating the original terrain so its transition cannot open a hole through the divider.
        var finalDensity = rangeChoice(y, 1, FDTerrain.UPPER_NOISE_TOP_Y, upper,
                rangeChoice(y, FilthTerrainSettings.MIN_Y, 1, deep, constant(-1)));
        return new NoiseRouter(zero(), zero(), zero(), zero(), temperature, humidity,
                vanilla(density, "overworld/continents"), vanilla(density, "overworld/erosion"),
                rangeChoice(y, 0, 128, zero(), constant(1)), vanilla(density, "overworld/ridges"),
                zero(), finalDensity, zero(), zero(), zero());
    }

    private static DensityFunction deepTerrain(HolderGetter<DensityFunction> density, DensityFunction y, DensityFunction shapeNoise) {
        var hallFade = min(yClampedGradient(-120, -102, 0, 1), yClampedGradient(-38, -8, 1, 0));
        var hallCave = add(reference(density, FDDensityFunctions.DEEP_HALL),
                mul(hallFade, mul(constant(17), shapeNoise.clamp(-0.32, 0.09))));
        var pillarField = DensityFunctions.interpolated(reference(density, FDDensityFunctions.DEEP_HALL_PILLARS));
        var pillars = add(pillarField, mul(constant(0.24), shapeNoise.clamp(-0.12, 0.12)));
        // Merge per block so a positive pillar sample cannot pull the surrounding floor or roof into its cell.
        var hall = max(hallCave, pillars);
        var calcium = add(reference(density, FDDensityFunctions.CALCIUM_CAVE),
                mul(constant(2), shapeNoise.clamp(-0.28, 0.28)));
        return rangeChoice(y, -120, -8, hall, rangeChoice(y, -184, -136, calcium, constant(1)));
    }

    private static DensityFunction upperTerrain(DensityFunction y, DensityFunction slopes) {
        var caveNoise = BlendedNoise.createUnseeded(0.1, 0.3, 160, 128, 1);
        var fadeAtCaveRoof = mul(yClampedGradient(112, 136, 1, 0), add(constant(-0.4), caveNoise));
        var caveDensity = mul(constant(1.125), mul(yClampedGradient(-8, 24, 0, 1),
                add(constant(0.4), add(constant(-0.4), fadeAtCaveRoof))));
        var surfaceDensity = mul(constant(2.5), mul(yClampedGradient(144, 384, 0, 1), slopes));
        var combined = add(constant(0.4), rangeChoice(y, 0, 128, caveDensity, surfaceDensity));
        return interpolateTerrain(combined);
    }

    private static DensityFunction interpolateTerrain(DensityFunction density) {
        return mul(constant(0.64), DensityFunctions.interpolated(DensityFunctions.blendDensity(density))).squeeze();
    }

    private static DensityFunction vanilla(HolderGetter<DensityFunction> density, String path) {
        var key = ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.withDefaultNamespace(path));
        return reference(density, key);
    }

    private static DensityFunction reference(HolderGetter<DensityFunction> density, ResourceKey<DensityFunction> key) {
        return new DensityFunctions.HolderHolder(density.getOrThrow(key));
    }
}
