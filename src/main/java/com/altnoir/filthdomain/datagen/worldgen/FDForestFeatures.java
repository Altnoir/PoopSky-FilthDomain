package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.poopsky.init.PoBlocks;
import com.altnoir.poopsky.worldgen.PoConfigureFeatures;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

final class FDForestFeatures {
    private FDForestFeatures() {
    }

    static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        placeOnForestFloor(context, "wild_primo_fungus", PoConfigureFeatures.PRIMO_FUNGUS_FD,
                4, PoBlocks.MYCELIUM_BLOCK.get());
        placeOnForestFloor(context, "wild_glow_primo_fungus", PoConfigureFeatures.GLOW_PRIMO_FUNGUS_FD,
                3, PoBlocks.RAW_SEA_POOP_BLOCK.get(), PoBlocks.MYCELIUM_BLOCK.get());
        placeOnForestFloor(context, "wild_poop_tree", PoConfigureFeatures.POOP_TREE,
                5, PoBlocks.RAW_SAPLING_POOP_BLOCK.get(), PoBlocks.RAW_POOP_BLOCK.get());
    }

    private static void placeOnForestFloor(BootstrapContext<PlacedFeature> context, String path,
                                           ResourceKey<ConfiguredFeature<?, ?>> feature, int attempts, Block... floor) {
        context.register(ResourceKey.create(Registries.PLACED_FEATURE, FilthDomain.loc(path)),
                new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), List.of(
                        CountOnEveryLayerPlacement.of(attempts),
                        BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), List.of(floor)))),
                        BiomeFilter.biome()
                )));
    }
}
