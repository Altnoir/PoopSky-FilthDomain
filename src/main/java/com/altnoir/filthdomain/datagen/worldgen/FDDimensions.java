package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

import java.util.List;
import java.util.OptionalLong;

final class FDDimensions {
    private static final ResourceKey<LevelStem> FILTH_DOMAIN = ResourceKey.create(
            Registries.LEVEL_STEM, FilthDomain.loc("filthdomain"));
    private static final ResourceKey<DimensionType> FILTH_DOMAIN_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE, FILTH_DOMAIN.location());

    private static final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-2.0F, 2.0F);

    private FDDimensions() {
    }

    static void bootstrapTypes(BootstrapContext<DimensionType> context) {
        context.register(FILTH_DOMAIN_TYPE, new DimensionType(
                OptionalLong.of(18000), false, true, false, true, 8.0,
                false, false, 0, 256, 128, BlockTags.INFINIBURN_NETHER,
                BuiltinDimensionTypes.NETHER_EFFECTS, 0.6F,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(7), 15)));
    }

    static void bootstrap(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        Climate.Parameter cold = Climate.Parameter.span(-2.0F, -0.2F);
        Climate.Parameter mild = Climate.Parameter.span(-0.2F, 0.2F);
        Climate.Parameter warm = Climate.Parameter.span(0.2F, 2.0F);
        Climate.Parameter dry = Climate.Parameter.span(-2.0F, 0.0F);
        Climate.Parameter humid = Climate.Parameter.span(0.0F, 2.0F);
        MultiNoiseBiomeSource biomeSource = MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(List.of(
                biome(biomes, FDBiomes.FILTH_PLAINS, mild, Climate.Parameter.span(-2.0F, 0.22F)),
                biome(biomes, FDBiomes.POOP_FOREST, mild, Climate.Parameter.span(0.22F, 2.0F)),
                biome(biomes, FDBiomes.SOUL_ASH_VALLEY, cold, humid),
                biome(biomes, FDBiomes.BLOODCAP_FOREST, warm, dry),
                biome(biomes, FDBiomes.SPOREWOOD, warm, humid),
                biome(biomes, FDBiomes.BASALT_BADLANDS, cold, dry)
        )));
        context.register(FILTH_DOMAIN, new LevelStem(context.lookup(Registries.DIMENSION_TYPE).getOrThrow(FILTH_DOMAIN_TYPE),
                new NoiseBasedChunkGenerator(biomeSource, context.lookup(Registries.NOISE_SETTINGS)
                        .getOrThrow(FDTerrain.FILTH_NOISE))));
    }

    private static Pair<Climate.ParameterPoint, Holder<Biome>> biome(
            HolderGetter<Biome> biomes, ResourceKey<Biome> key,
            Climate.Parameter temperature, Climate.Parameter humidity) {
        // The other router fields describe terrain and should not bias biome selection.
        return Pair.of(Climate.parameters(temperature, humidity, FULL_RANGE, FULL_RANGE, FULL_RANGE, FULL_RANGE, 0),
                biomes.getOrThrow(key));
    }
}
