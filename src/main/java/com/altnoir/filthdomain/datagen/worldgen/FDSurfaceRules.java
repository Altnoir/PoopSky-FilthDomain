package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.init.FDBlocks;
import com.altnoir.poopsky.init.PoBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

import static net.minecraft.world.level.levelgen.SurfaceRules.hole;
import static net.minecraft.world.level.levelgen.SurfaceRules.ifTrue;
import static net.minecraft.world.level.levelgen.SurfaceRules.isBiome;
import static net.minecraft.world.level.levelgen.SurfaceRules.noiseCondition;
import static net.minecraft.world.level.levelgen.SurfaceRules.not;
import static net.minecraft.world.level.levelgen.SurfaceRules.sequence;
import static net.minecraft.world.level.levelgen.SurfaceRules.verticalGradient;
import static net.minecraft.world.level.levelgen.SurfaceRules.yBlockCheck;
import static net.minecraft.world.level.levelgen.SurfaceRules.yStartCheck;
import static net.minecraft.world.level.levelgen.VerticalAnchor.absolute;
import static net.minecraft.world.level.levelgen.VerticalAnchor.aboveBottom;

final class FDSurfaceRules {
    private static final SurfaceRules.ConditionSource ON_FLOOR = SurfaceRules.stoneDepthCheck(0, false, CaveSurface.FLOOR);
    private static final SurfaceRules.ConditionSource UNDER_FLOOR = SurfaceRules.stoneDepthCheck(0, true, CaveSurface.FLOOR);
    private static final SurfaceRules.ConditionSource UNDER_CEILING = SurfaceRules.stoneDepthCheck(0, true, CaveSurface.CEILING);

    private FDSurfaceRules() {
    }

    static RuleSource create() {
        var driedPoop = state(PoBlocks.DRIED_POOP_BLOCK.get());
        var rawPoop = state(PoBlocks.RAW_POOP_BLOCK.get());
        var sand = state(PoBlocks.POOP_SAND.get());

        return sequence(
                ifTrue(verticalGradient("bedrock_floor", aboveBottom(0), aboveBottom(10)), state(Blocks.BEDROCK)),
                ifTrue(verticalGradient("filthbedrock_transition", absolute(-197), absolute(-187)),
                        state(FDBlocks.FILTHBEDROCK.get())),
                ifTrue(verticalGradient("calcium_transition", absolute(-133), absolute(-123)),
                        state(FDBlocks.CALCIUM_BLOCK.get())),
                ifTrue(verticalGradient("hardened_poop_transition", absolute(-5), absolute(5)),
                        state(FDBlocks.HARDENED_POOP_BLOCK.get())),
                ifTrue(not(yBlockCheck(absolute(1), 0)), driedPoop),
                ifTrue(yBlockCheck(absolute(FDTerrain.UPPER_NOISE_TOP_Y - 6), 0), driedPoop),
                ifTrue(isBiome(FDBiomes.BASALT_BADLANDS), basaltBadlands(rawPoop, sand)),
                ifTrue(isBiome(FDBiomes.SOUL_ASH_VALLEY), soulAshValley(rawPoop, sand)),
                ifTrue(ON_FLOOR, sequence(
                        ifTrue(not(yBlockCheck(absolute(32), 0)), ifTrue(hole(), state(PoBlocks.URINE_LIQUID.get()))),
                        ifTrue(isBiome(FDBiomes.SPOREWOOD), forest(
                                state(PoBlocks.MYCELIUM_BLOCK.get()), state(PoBlocks.RAW_SEA_POOP_BLOCK.get()))),
                        ifTrue(isBiome(FDBiomes.POOP_FOREST), forest(rawPoop, state(PoBlocks.RAW_SAPLING_POOP_BLOCK.get()))),
                        ifTrue(isBiome(FDBiomes.BLOODCAP_FOREST), forest(
                                state(PoBlocks.PRIMO_HYPHAE.get()), state(PoBlocks.MYCELIUM_BLOCK.get()))))),
                ifTrue(isBiome(FDBiomes.FILTH_PLAINS), filthPlains(driedPoop, sand)),
                driedPoop);
    }

    private static RuleSource basaltBadlands(RuleSource rawPoop, RuleSource sand) {
        return sequence(
                ifTrue(UNDER_CEILING, rawPoop),
                ifTrue(UNDER_FLOOR, sequence(
                        ifTrue(noiseCondition(Noises.PATCH, -0.012, Double.MAX_VALUE), shoreline(sand)),
                        ifTrue(noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.0, Double.MAX_VALUE), rawPoop),
                        state(Blocks.BASALT))));
    }

    private static RuleSource soulAshValley(RuleSource rawPoop, RuleSource sand) {
        var sandPatch = ifTrue(noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.0, Double.MAX_VALUE), sand);
        return sequence(
                ifTrue(UNDER_CEILING, sequence(sandPatch, rawPoop)),
                ifTrue(UNDER_FLOOR, sequence(
                        ifTrue(noiseCondition(Noises.PATCH, -0.012, Double.MAX_VALUE), shoreline(sand)),
                        sandPatch, rawPoop)));
    }

    private static RuleSource forest(RuleSource patch, RuleSource floor) {
        return ifTrue(not(noiseCondition(Noises.NETHERRACK, 0.54, Double.MAX_VALUE)),
                ifTrue(yBlockCheck(absolute(31), 0), sequence(
                        ifTrue(noiseCondition(Noises.NETHER_WART, 1.17, Double.MAX_VALUE), patch), floor)));
    }

    private static RuleSource filthPlains(RuleSource driedPoop, RuleSource sand) {
        return sequence(
                ifTrue(UNDER_FLOOR, ifTrue(noiseCondition(Noises.SOUL_SAND_LAYER, -0.012, Double.MAX_VALUE),
                        sequence(ifTrue(not(hole()), shoreline(sand)), driedPoop))),
                ifTrue(ON_FLOOR, ifTrue(yBlockCheck(absolute(31), 0), ifTrue(not(yStartCheck(absolute(35), 0)),
                        ifTrue(noiseCondition(Noises.GRAVEL_LAYER, -0.012, Double.MAX_VALUE), sequence(
                                ifTrue(yBlockCheck(absolute(32), 0), sand),
                                ifTrue(not(hole()), sand)))))));
    }

    private static RuleSource shoreline(RuleSource sand) {
        return ifTrue(yStartCheck(absolute(30), 0), ifTrue(not(yStartCheck(absolute(35), 0)), sand));
    }

    private static RuleSource state(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
