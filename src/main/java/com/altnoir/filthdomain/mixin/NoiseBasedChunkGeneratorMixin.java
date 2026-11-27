package com.altnoir.filthdomain.mixin;

import com.altnoir.filthdomain.worldgen.FilthTerrainSettings;
import com.google.common.base.Suppliers;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(NoiseBasedChunkGenerator.class)
abstract class NoiseBasedChunkGeneratorMixin {
    @Shadow @Final @Mutable
    private Supplier<Aquifer.FluidPicker> globalFluidPicker;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void filthdomain$keepDeepCavesDry(BiomeSource biomes, Holder<NoiseGeneratorSettings> settings, CallbackInfo ci) {
        if (settings.is(FilthTerrainSettings.NOISE_SETTINGS)) {
            // Wrap the existing picker so PoopSky's own fluid rules still apply above the separator.
            var original = globalFluidPicker;
            var dry = new Aquifer.FluidStatus(FilthTerrainSettings.MIN_Y, Blocks.AIR.defaultBlockState());
            globalFluidPicker = Suppliers.memoize(() -> {
                var upper = original.get();
                return (x, y, z) -> y < 0 ? dry : upper.computeFluid(x, y, z);
            });
        }
    }
}
