package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.tag.FDTags;
import com.altnoir.filthdomain.worldgen.FilthCaveCarver;
import com.altnoir.filthdomain.worldgen.FilthTerrainSettings;
import com.altnoir.poopsky.init.PoBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

import java.util.List;

final class FDTerrain {
    static final int UPPER_NOISE_TOP_Y = 240;
    static final ResourceKey<ConfiguredWorldCarver<?>> FILTH_CAVE = ResourceKey.create(
            Registries.CONFIGURED_CARVER, FilthDomain.loc("filth_cave"));

    private FDTerrain() {
    }

    static void bootstrapCarvers(BootstrapContext<ConfiguredWorldCarver<?>> context) {
        var airMarker = Blocks.OAK_BUTTON.defaultBlockState()
                .setValue(BlockStateProperties.ATTACH_FACE, AttachFace.WALL)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        var debug = CarverDebugSettings.of(false, airMarker, Blocks.CANDLE.defaultBlockState(),
                Blocks.ORANGE_STAINED_GLASS.defaultBlockState(), Blocks.GLASS.defaultBlockState());
        context.register(FILTH_CAVE, FilthCaveCarver.configure(new CaveCarverConfiguration(
                0.07F, UniformHeight.of(VerticalAnchor.absolute(8), VerticalAnchor.absolute(47)),
                UniformFloat.of(0.1F, 0.9F), VerticalAnchor.absolute(8), debug,
                context.lookup(Registries.BLOCK).getOrThrow(FDTags.Blocks.FILTH_CAVE_REPLACEABLES),
                UniformFloat.of(0.7F, 1.4F), UniformFloat.of(0.8F, 1.3F), UniformFloat.of(-1.0F, -0.4F))));
    }

    static void bootstrapNoise(BootstrapContext<NoiseGeneratorSettings> context) {
        context.register(FilthTerrainSettings.NOISE_SETTINGS, new NoiseGeneratorSettings(
                NoiseSettings.create(FilthTerrainSettings.MIN_Y, FilthTerrainSettings.HEIGHT, 2, 2),
                PoBlocks.DRIED_POOP_BLOCK.get().defaultBlockState(), PoBlocks.URINE_LIQUID.get().defaultBlockState(),
                FDNoiseRouter.create(context), FDSurfaceRules.create(), List.of(), 32,
                false, false, false, false));
    }
}
