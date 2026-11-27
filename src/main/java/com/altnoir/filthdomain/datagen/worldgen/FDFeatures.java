package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.tag.FDTags;
import com.altnoir.filthdomain.worldgen.UpperTerrainPlacement;
import com.altnoir.poopsky.init.PoBlocks;
import com.altnoir.poopsky.init.PoFluids;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.VeryBiasedToBottomHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.ArrayList;
import java.util.List;

final class FDFeatures {
    private FDFeatures() {
    }

    static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        Block driedPoop = PoBlocks.DRIED_POOP_BLOCK.get();
        var air = BlockPredicate.ONLY_IN_AIR_PREDICATE;
        var onDriedPoop = BlockPredicate.allOf(air, BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), driedPoop));

        configure(context, "basalt_blobs", Feature.REPLACE_BLOBS, blobs(driedPoop, Blocks.BASALT));
        configure(context, "blackstone_blobs", Feature.REPLACE_BLOBS, blobs(driedPoop, Blocks.BLACKSTONE));
        configure(context, "basalt_pillar", Feature.BLOCK_COLUMN, basaltColumn(1, 4));
        configure(context, "small_basalt_columns", Feature.BLOCK_COLUMN, basaltColumn(1, 4));
        configure(context, "large_basalt_columns", Feature.BLOCK_COLUMN, basaltColumn(5, 10));
        configure(context, "delta", Feature.DELTA_FEATURE, new DeltaFeatureConfiguration(
                PoBlocks.URINE_LIQUID.get().defaultBlockState(), PoBlocks.CHILI_POOP_BLOCK.get().defaultBlockState(),
                UniformInt.of(3, 7), UniformInt.of(0, 2)));

        configure(context, "basalt_urine_spring", Feature.SPRING, urineSpring(true, 4, 1,
                driedPoop, PoBlocks.POOP_SAND.get(), PoBlocks.RAW_POOP_BLOCK.get(), PoBlocks.CHILI_POOP_BLOCK.get(),
                PoBlocks.POOP_BRICKS.get()));
        configure(context, "lowland_urine_spring", Feature.SPRING, urineSpring(true, 4, 1,
                PoBlocks.CUT_POOP_BLOCK.get(), PoBlocks.POOP_EMPTY_LOG.get(), PoBlocks.SMOOTH_POOP_BLOCK.get(),
                PoBlocks.RAW_SEA_POOP_BLOCK.get(), PoBlocks.RAW_SAPLING_POOP_BLOCK.get(), driedPoop,
                PoBlocks.CHILI_POOP_BLOCK.get()));
        configure(context, "sealed_urine_spring", Feature.SPRING, urineSpring(false, 5, 0, driedPoop));
        configure(context, "exposed_urine_spring", Feature.SPRING, urineSpring(false, 4, 1, driedPoop));

        configure(context, "glowstone_extra", Feature.RANDOM_PATCH,
                patch(PoBlocks.RAW_WITHER_POOP_BLOCK.get(), 64, BlockPredicate.matchesBlocks(driedPoop)));
        configure(context, "patch_bloodcap_roots", Feature.RANDOM_PATCH, patch(PoBlocks.GINKGO_SAPLING.get(), 96, air));
        configure(context, "patch_brown_mushroom", Feature.RANDOM_PATCH, patch(PoBlocks.SALTPETER_CLUSTER.get(), 96, air));
        configure(context, "patch_red_mushroom", Feature.RANDOM_PATCH, patch(PoBlocks.MEDIUM_SALTPETER_BUD.get(), 96, air));
        configure(context, "patch_fire", Feature.RANDOM_PATCH, patch(PoBlocks.POOP_CAKE.get(), 96, onDriedPoop));
        configure(context, "patch_soul_fire", Feature.RANDOM_PATCH, patch(PoBlocks.POOP_LOG.get(), 96, onDriedPoop));

        configure(context, "ore_ancient_debris_large", Feature.SCATTERED_ORE, buriedTnt(3));
        configure(context, "ore_ancient_debris_small", Feature.SCATTERED_ORE, buriedTnt(2));
        configure(context, "ore_blackstone", Feature.ORE, ore(driedPoop, PoBlocks.POOP_BRICKS.get(), 33));
        configure(context, "ore_gold", Feature.ORE, ore(driedPoop, PoBlocks.GOLDEN_POOP_BLOCK.get(), 10));
        configure(context, "ore_gravel", Feature.ORE, ore(driedPoop, PoBlocks.RAW_POOP_BLOCK.get(), 33));
        configure(context, "ore_magma", Feature.ORE, ore(driedPoop, PoBlocks.CHILI_POOP_BLOCK.get(), 33));
        configure(context, "ore_quartz", Feature.ORE, ore(driedPoop, PoBlocks.POOLIME_BLOCK.get(), 14));
        configure(context, "ore_soul_sand", Feature.ORE, ore(driedPoop, PoBlocks.POOP_SAND.get(), 12));
    }

    static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        var square = InSquarePlacement.spread();
        var air = BlockPredicate.ONLY_IN_AIR_PREDICATE;
        var fullHeight = inset(0);
        var slightlyInset = inset(4);
        var oreHeight = inset(10);
        var surface = HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING);

        place(context, "basalt_blobs", CountPlacement.of(75), square, fullHeight);
        place(context, "blackstone_blobs", CountPlacement.of(25), square, fullHeight);
        place(context, "basalt_pillar", CountPlacement.of(10), square, fullHeight,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), air, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)));
        place(context, "basalt_urine_spring", CountPlacement.of(16), square, slightlyInset);
        place(context, "exposed_urine_spring", CountPlacement.of(8), square, slightlyInset);
        place(context, "sealed_urine_spring", CountPlacement.of(16), square, oreHeight);
        place(context, "dense_sealed_urine_springs", "sealed_urine_spring", CountPlacement.of(32), square, oreHeight);
        place(context, "lowland_urine_spring", CountPlacement.of(20), square,
                HeightRangePlacement.of(VeryBiasedToBottomHeight.of(
                        VerticalAnchor.absolute(0), VerticalAnchor.absolute(FDTerrain.UPPER_NOISE_TOP_Y - 9), 8)));

        place(context, "delta", CountOnEveryLayerPlacement.of(40));
        place(context, "small_basalt_columns", CountOnEveryLayerPlacement.of(4));
        place(context, "large_basalt_columns", CountOnEveryLayerPlacement.of(2));
        place(context, "glowstone", "glowstone_extra", CountPlacement.of(10), square, fullHeight);
        place(context, "glowstone_extra", CountPlacement.of(BiasedToBottomInt.of(0, 9)), square, slightlyInset);
        place(context, "patch_bloodcap_roots", fullHeight);
        place(context, "patch_fire", CountPlacement.of(UniformInt.of(0, 5)), square, slightlyInset);
        place(context, "patch_soul_fire", CountPlacement.of(UniformInt.of(0, 5)), square, slightlyInset);
        place(context, "brown_mushroom_cavern", "patch_brown_mushroom", RarityFilter.onAverageOnceEvery(2), square, fullHeight);
        place(context, "red_mushroom_cavern", "patch_red_mushroom", RarityFilter.onAverageOnceEvery(2), square, fullHeight);
        place(context, "brown_mushroom_normal", "patch_brown_mushroom", RarityFilter.onAverageOnceEvery(256), square, surface);
        place(context, "red_mushroom_normal", "patch_red_mushroom", RarityFilter.onAverageOnceEvery(512), square, surface);

        place(context, "ore_ancient_debris_large", square, HeightRangePlacement.triangle(VerticalAnchor.absolute(8), VerticalAnchor.absolute(24)));
        place(context, "ore_ancient_debris_small", square, inset(8));
        place(context, "ore_blackstone", CountPlacement.of(2), square, absoluteRange(5, 31));
        place(context, "ore_gravel", CountPlacement.of(2), square, absoluteRange(5, 41));
        place(context, "ore_magma", CountPlacement.of(4), square, absoluteRange(27, 36));
        place(context, "ore_soul_sand", CountPlacement.of(12), square, absoluteRange(0, 31));
        place(context, "ore_gold_in_basalt", "ore_gold", CountPlacement.of(20), square, oreHeight);
        place(context, "ore_gold_in_filth", "ore_gold", CountPlacement.of(10), square, oreHeight);
        place(context, "ore_quartz_in_basalt", "ore_quartz", CountPlacement.of(32), square, oreHeight);
        place(context, "ore_quartz_in_filth", "ore_quartz", CountPlacement.of(16), square, oreHeight);
    }

    static ResourceKey<PlacedFeature> placedKey(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, FilthDomain.loc(path));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredKey(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, FilthDomain.loc(path));
    }

    private static <C extends FeatureConfiguration> void configure(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                    String path, Feature<C> feature, C configuration) {
        FeatureUtils.register(context, configuredKey(path), feature, configuration);
    }

    private static void place(BootstrapContext<PlacedFeature> context, String path, PlacementModifier... modifiers) {
        place(context, path, path, modifiers);
    }

    private static void place(BootstrapContext<PlacedFeature> context, String path, String feature, PlacementModifier... modifiers) {
        var placement = new ArrayList<>(List.of(modifiers));
        // Heightmap and vertical scan placements can end up lower than their starting position.
        placement.add(UpperTerrainPlacement.INSTANCE);
        placement.add(BiomeFilter.biome());
        PlacementUtils.register(context, placedKey(path),
                context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredKey(feature)), placement);
    }

    private static HeightRangePlacement inset(int inset) {
        return absoluteRange(inset, FDTerrain.UPPER_NOISE_TOP_Y - 1 - inset);
    }

    private static HeightRangePlacement absoluteRange(int min, int max) {
        return HeightRangePlacement.uniform(VerticalAnchor.absolute(min), VerticalAnchor.absolute(max));
    }

    private static ReplaceSphereConfiguration blobs(Block target, Block replacement) {
        return new ReplaceSphereConfiguration(target.defaultBlockState(), replacement.defaultBlockState(), UniformInt.of(3, 7));
    }

    private static BlockColumnConfiguration basaltColumn(int min, int max) {
        return BlockColumnConfiguration.simple(UniformInt.of(min, max), BlockStateProvider.simple(Blocks.BASALT));
    }

    private static SpringConfiguration urineSpring(boolean needsFloor, int rocks, int holes, Block... validBlocks) {
        return new SpringConfiguration(PoFluids.URINE.get().getSource(true), needsFloor, rocks, holes,
                HolderSet.direct(Block::builtInRegistryHolder, validBlocks));
    }

    private static RandomPatchConfiguration patch(Block block, int attempts, BlockPredicate placement) {
        return new RandomPatchConfiguration(attempts, 7, 3, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(block)), UpperTerrainPlacement.INSTANCE,
                BlockPredicateFilter.forPredicate(placement)));
    }

    private static OreConfiguration ore(Block target, Block result, int size) {
        return new OreConfiguration(new BlockMatchTest(target), result.defaultBlockState(), size);
    }

    private static OreConfiguration buriedTnt(int size) {
        return new OreConfiguration(new TagMatchTest(FDTags.Blocks.BASE_POOP), PoBlocks.POP.get().defaultBlockState(), size, 1.0F);
    }
}
