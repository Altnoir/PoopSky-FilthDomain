package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.init.FDEntityType;
import com.altnoir.filthdomain.init.FDParticles;
import com.altnoir.poopsky.init.PoEntityType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

final class FDBiomes {
    static final ResourceKey<Biome> BASALT_BADLANDS = key("basalt_badlands");
    static final ResourceKey<Biome> BLOODCAP_FOREST = key("bloodcap_forest");
    static final ResourceKey<Biome> FILTH_PLAINS = key("filth_plains");
    static final ResourceKey<Biome> POOP_FOREST = key("poop_forest");
    static final ResourceKey<Biome> SOUL_ASH_VALLEY = key("soul_ash_valley");
    static final ResourceKey<Biome> SPOREWOOD = key("sporewood");

    private FDBiomes() {
    }

    static void bootstrap(BootstrapContext<Biome> context) {
        basaltBadlands(context);
        bloodcapForest(context);
        filthPlains(context);
        poopForest(context);
        soulAshValley(context);
        sporewood(context);
    }

    private static void basaltBadlands(BootstrapContext<Biome> context) {
        var generation = generation(context);
        addFeatures(generation, GenerationStep.Decoration.SURFACE_STRUCTURES,
                "delta", "small_basalt_columns", "large_basalt_columns");
        addFeatures(generation, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                "basalt_blobs", "blackstone_blobs", "basalt_urine_spring",
                "patch_fire", "patch_soul_fire", "glowstone_extra",
                "glowstone", "brown_mushroom_cavern", "red_mushroom_cavern",
                "ore_magma", "dense_sealed_urine_springs", "ore_gold_in_basalt",
                "ore_quartz_in_basalt", "ore_ancient_debris_large", "ore_ancient_debris_small");
        var mobs = mobs(5);
        context.register(BASALT_BADLANDS, biome(generation, mobs, 13148183, 5783080, 2628626,
                0.118093334F, SoundEvents.AMBIENT_BASALT_DELTAS_LOOP, SoundEvents.AMBIENT_BASALT_DELTAS_MOOD,
                SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, SoundEvents.MUSIC_BIOME_BASALT_DELTAS));
    }

    private static void bloodcapForest(BootstrapContext<Biome> context) {
        var generation = generation(context);
        addFeatures(generation, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                "exposed_urine_spring", "patch_fire", "glowstone_extra",
                "glowstone", "ore_magma", "sealed_urine_spring",
                "ore_gravel", "ore_blackstone", "ore_gold_in_filth",
                "ore_quartz_in_filth", "ore_ancient_debris_large", "ore_ancient_debris_small");
        addFeatures(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
                "lowland_urine_spring", "wild_primo_fungus");
        var mobs = mobs(5);
        context.register(BLOODCAP_FOREST, biome(generation, mobs, 8466721, 6831138, 3020814,
                0.025F, SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP, SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD,
                SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, SoundEvents.MUSIC_BIOME_CRIMSON_FOREST));
    }

    private static void filthPlains(BootstrapContext<Biome> context) {
        var generation = generation(context);
        addCommonUnderground(generation);
        addFeatures(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
                "lowland_urine_spring", "brown_mushroom_normal", "red_mushroom_normal");
        var mobs = mobs(5);
        context.register(FILTH_PLAINS, biome(generation, mobs, 13934615, 6504996, 2890255,
                0.00625F, SoundEvents.AMBIENT_NETHER_WASTES_LOOP, SoundEvents.AMBIENT_NETHER_WASTES_MOOD,
                SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, SoundEvents.MUSIC_BIOME_NETHER_WASTES));
    }

    private static void poopForest(BootstrapContext<Biome> context) {
        var generation = generation(context);
        addCommonUnderground(generation);
        addFeatures(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
                "lowland_urine_spring", "wild_poop_tree");
        var mobs = mobs(5);
        context.register(POOP_FOREST, biome(generation, mobs, 7622961, 6504996, 2890255,
                0.0125F, SoundEvents.AMBIENT_NETHER_WASTES_LOOP, SoundEvents.AMBIENT_NETHER_WASTES_MOOD,
                SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, SoundEvents.MUSIC_BIOME_NETHER_WASTES));
    }

    private static void soulAshValley(BootstrapContext<Biome> context) {
        var generation = generation(context);
        addFeatures(generation, GenerationStep.Decoration.LOCAL_MODIFICATIONS,
                "basalt_pillar");
        addFeatures(generation, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                "exposed_urine_spring", "patch_fire", "patch_soul_fire",
                "glowstone_extra", "glowstone", "patch_bloodcap_roots",
                "ore_magma", "sealed_urine_spring", "ore_soul_sand",
                "ore_gravel", "ore_blackstone", "ore_gold_in_filth",
                "ore_quartz_in_filth", "ore_ancient_debris_large", "ore_ancient_debris_small");
        addFeatures(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
                "lowland_urine_spring");
        var mobs = mobs(5);
        mobs.addMobCharge(EntityType.ENDERMAN, 0.7, 0.15);
        mobs.addMobCharge(EntityType.GHAST, 0.7, 0.15);
        mobs.addMobCharge(EntityType.SKELETON, 0.7, 0.15);
        mobs.addMobCharge(EntityType.STRIDER, 0.7, 0.15);
        context.register(SOUL_ASH_VALLEY, biome(generation, mobs, 10145074, 6311976, 2761236,
                0.00625F, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD,
                SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY));
    }

    private static void sporewood(BootstrapContext<Biome> context) {
        var generation = generation(context);
        addFeatures(generation, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                "exposed_urine_spring", "patch_fire", "patch_soul_fire",
                "glowstone_extra", "glowstone", "ore_magma",
                "sealed_urine_spring", "ore_gravel", "ore_blackstone",
                "ore_gold_in_filth", "ore_quartz_in_filth", "ore_ancient_debris_large",
                "ore_ancient_debris_small");
        addFeatures(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
                "lowland_urine_spring", "wild_glow_primo_fungus");
        var mobs = mobs(4);
        mobs.addMobCharge(EntityType.ENDERMAN, 1, 0.12);
        context.register(SPOREWOOD, biome(generation, mobs, 2180183, 5918766, 2631192,
                0.01428F, SoundEvents.AMBIENT_WARPED_FOREST_LOOP, SoundEvents.AMBIENT_WARPED_FOREST_MOOD,
                SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, SoundEvents.MUSIC_BIOME_WARPED_FOREST));
    }

    private static BiomeGenerationSettings.Builder generation(BootstrapContext<Biome> context) {
        return new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                context.lookup(Registries.CONFIGURED_CARVER))
                .addCarver(GenerationStep.Carving.AIR, FDTerrain.FILTH_CAVE);
    }

    private static void addCommonUnderground(BiomeGenerationSettings.Builder generation) {
        addFeatures(generation, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                "exposed_urine_spring", "patch_fire", "patch_soul_fire",
                "glowstone_extra", "glowstone", "brown_mushroom_cavern",
                "red_mushroom_cavern", "ore_magma", "sealed_urine_spring",
                "ore_gravel", "ore_blackstone", "ore_gold_in_filth",
                "ore_quartz_in_filth", "ore_ancient_debris_large", "ore_ancient_debris_small");
    }

    private static void addFeatures(BiomeGenerationSettings.Builder generation, GenerationStep.Decoration step, String... features) {
        for (String feature : features) {
            generation.addFeature(step, FDFeatures.placedKey(feature));
        }
    }

    private static MobSpawnSettings.Builder mobs(int monsterPoolimeMax) {
        return new MobSpawnSettings.Builder()
                .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(PoEntityType.POOLIME.get(), 60, 1, 3))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(FDEntityType.BASILISK.get(), 40, 1, 2))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(PoEntityType.POOLIME.get(), 100, 2, monsterPoolimeMax));
    }

    private static Biome biome(BiomeGenerationSettings.Builder generation, MobSpawnSettings.Builder mobs,
                               int fogAndSky, int water, int waterFog, float ashChance, Holder<SoundEvent> loop,
                               Holder<SoundEvent> mood, Holder<SoundEvent> additions, Holder<SoundEvent> music) {
        BiomeSpecialEffects effects = new BiomeSpecialEffects.Builder()
                .fogColor(fogAndSky).skyColor(fogAndSky).waterColor(water).waterFogColor(waterFog)
                .ambientLoopSound(loop)
                .ambientMoodSound(new AmbientMoodSettings(mood, 6000, 8, 2.0))
                .ambientAdditionsSound(new AmbientAdditionsSettings(additions, 0.0111))
                .ambientParticle(new AmbientParticleSettings(FDParticles.FILTH_ASH.get(), ashChance))
                .backgroundMusic(new Music(music, 12000, 24000, false))
                .build();
        return new Biome.BiomeBuilder().hasPrecipitation(false).temperature(2.0F).downfall(0.0F)
                .specialEffects(effects).mobSpawnSettings(mobs.build()).generationSettings(generation.build()).build();
    }

    private static ResourceKey<Biome> key(String path) {
        return ResourceKey.create(Registries.BIOME, FilthDomain.loc(path));
    }
}
