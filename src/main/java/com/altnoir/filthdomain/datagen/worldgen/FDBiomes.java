package com.altnoir.filthdomain.datagen.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

final class FDBiomes {
    static final ResourceKey<Biome> FILTH_PLAINS = key("filth_plains");
    static final ResourceKey<Biome> POOP_FOREST = key("poop_forest");
    static final ResourceKey<Biome> SOUL_ASH_VALLEY = key("soul_ash_valley");
    static final ResourceKey<Biome> BLOODCAP_FOREST = key("bloodcap_forest");
    static final ResourceKey<Biome> SPOREWOOD = key("sporewood");
    static final ResourceKey<Biome> BASALT_BADLANDS = key("basalt_badlands");

    private FDBiomes() {
    }

    private static ResourceKey<Biome> key(String path) {
        return ResourceKey.create(Registries.BIOME, FilthDomain.loc(path));
    }

    static void bootstrap(BootstrapContext<Biome> context) {
        FDWorldgen.register(context, Registries.BIOME, Biome.DIRECT_CODEC, """
                {
                  "basalt_badlands": {
                    "carvers": {
                      "air": "poopsky_filthdomain:filth_cave"
                    },
                    "downfall": 0.0,
                    "effects": {
                      "additions_sound": {
                        "sound": "minecraft:ambient.basalt_deltas.additions",
                        "tick_chance": 0.0111
                      },
                      "ambient_sound": "minecraft:ambient.basalt_deltas.loop",
                      "fog_color": 13148183,
                      "mood_sound": {
                        "block_search_extent": 8,
                        "offset": 2.0,
                        "sound": "minecraft:ambient.basalt_deltas.mood",
                        "tick_delay": 6000
                      },
                      "music": {
                        "max_delay": 24000,
                        "min_delay": 12000,
                        "replace_current_music": false,
                        "sound": "minecraft:music.nether.basalt_deltas"
                      },
                      "particle": {
                        "options": {
                          "type": "poopsky_filthdomain:filth_ash"
                        },
                        "probability": 0.118093334
                      },
                      "sky_color": 13148183,
                      "water_color": 5783080,
                      "water_fog_color": 2628626
                    },
                    "features": [
                      [],
                      [],
                      [],
                      [],
                      [
                        "poopsky_filthdomain:delta",
                        "poopsky_filthdomain:small_basalt_columns",
                        "poopsky_filthdomain:large_basalt_columns"
                      ],
                      [],
                      [],
                      [
                        "poopsky_filthdomain:basalt_blobs",
                        "poopsky_filthdomain:blackstone_blobs",
                        "poopsky_filthdomain:basalt_urine_spring",
                        "poopsky_filthdomain:patch_fire",
                        "poopsky_filthdomain:patch_soul_fire",
                        "poopsky_filthdomain:glowstone_extra",
                        "poopsky_filthdomain:glowstone",
                        "poopsky_filthdomain:brown_mushroom_cavern",
                        "poopsky_filthdomain:red_mushroom_cavern",
                        "poopsky_filthdomain:ore_magma",
                        "poopsky_filthdomain:dense_sealed_urine_springs",
                        "poopsky_filthdomain:ore_gold_in_basalt",
                        "poopsky_filthdomain:ore_quartz_in_basalt",
                        "poopsky_filthdomain:ore_ancient_debris_large",
                        "poopsky_filthdomain:ore_ancient_debris_small"
                      ]
                    ],
                    "has_precipitation": false,
                    "spawn_costs": {},
                    "spawners": {
                      "ambient": [],
                      "axolotls": [],
                      "creature": [
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 3,
                          "minCount": 1,
                          "weight": 60
                        }
                      ],
                      "misc": [],
                      "monster": [
                        {
                          "type": "poopsky_filthdomain:basilisk",
                          "maxCount": 2,
                          "minCount": 1,
                          "weight": 40
                        },
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 5,
                          "minCount": 2,
                          "weight": 100
                        }
                      ],
                      "underground_water_creature": [],
                      "water_ambient": [],
                      "water_creature": []
                    },
                    "temperature": 2.0
                  },
                  "bloodcap_forest": {
                    "carvers": {
                      "air": "poopsky_filthdomain:filth_cave"
                    },
                    "downfall": 0.0,
                    "effects": {
                      "additions_sound": {
                        "sound": "minecraft:ambient.crimson_forest.additions",
                        "tick_chance": 0.0111
                      },
                      "ambient_sound": "minecraft:ambient.crimson_forest.loop",
                      "fog_color": 8466721,
                      "mood_sound": {
                        "block_search_extent": 8,
                        "offset": 2.0,
                        "sound": "minecraft:ambient.crimson_forest.mood",
                        "tick_delay": 6000
                      },
                      "music": {
                        "max_delay": 24000,
                        "min_delay": 12000,
                        "replace_current_music": false,
                        "sound": "minecraft:music.nether.crimson_forest"
                      },
                      "particle": {
                        "options": {
                          "type": "poopsky_filthdomain:filth_ash"
                        },
                        "probability": 0.025
                      },
                      "sky_color": 8466721,
                      "water_color": 6831138,
                      "water_fog_color": 3020814
                    },
                    "features": [
                      [],
                      [],
                      [],
                      [],
                      [],
                      [],
                      [],
                      [
                        "poopsky_filthdomain:exposed_urine_spring",
                        "poopsky_filthdomain:patch_fire",
                        "poopsky_filthdomain:glowstone_extra",
                        "poopsky_filthdomain:glowstone",
                        "poopsky_filthdomain:ore_magma",
                        "poopsky_filthdomain:sealed_urine_spring",
                        "poopsky_filthdomain:ore_gravel",
                        "poopsky_filthdomain:ore_blackstone",
                        "poopsky_filthdomain:ore_gold_in_filth",
                        "poopsky_filthdomain:ore_quartz_in_filth",
                        "poopsky_filthdomain:ore_ancient_debris_large",
                        "poopsky_filthdomain:ore_ancient_debris_small"
                      ],
                      [],
                      [
                        "poopsky_filthdomain:lowland_urine_spring",
                        "poopsky_filthdomain:wild_primo_fungus"
                      ]
                    ],
                    "has_precipitation": false,
                    "spawn_costs": {},
                    "spawners": {
                      "ambient": [],
                      "axolotls": [],
                      "creature": [
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 3,
                          "minCount": 1,
                          "weight": 60
                        }
                      ],
                      "misc": [],
                      "monster": [
                        {
                          "type": "poopsky_filthdomain:basilisk",
                          "maxCount": 2,
                          "minCount": 1,
                          "weight": 40
                        },
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 5,
                          "minCount": 2,
                          "weight": 100
                        }
                      ],
                      "underground_water_creature": [],
                      "water_ambient": [],
                      "water_creature": []
                    },
                    "temperature": 2.0
                  },
                  "soul_ash_valley": {
                    "carvers": {
                      "air": "poopsky_filthdomain:filth_cave"
                    },
                    "downfall": 0.0,
                    "effects": {
                      "additions_sound": {
                        "sound": "minecraft:ambient.soul_sand_valley.additions",
                        "tick_chance": 0.0111
                      },
                      "ambient_sound": "minecraft:ambient.soul_sand_valley.loop",
                      "fog_color": 10145074,
                      "mood_sound": {
                        "block_search_extent": 8,
                        "offset": 2.0,
                        "sound": "minecraft:ambient.soul_sand_valley.mood",
                        "tick_delay": 6000
                      },
                      "music": {
                        "max_delay": 24000,
                        "min_delay": 12000,
                        "replace_current_music": false,
                        "sound": "minecraft:music.nether.soul_sand_valley"
                      },
                      "particle": {
                        "options": {
                          "type": "poopsky_filthdomain:filth_ash"
                        },
                        "probability": 0.00625
                      },
                      "sky_color": 10145074,
                      "water_color": 6311976,
                      "water_fog_color": 2761236
                    },
                    "features": [
                      [],
                      [],
                      [
                        "poopsky_filthdomain:basalt_pillar"
                      ],
                      [],
                      [],
                      [],
                      [],
                      [
                        "poopsky_filthdomain:exposed_urine_spring",
                        "poopsky_filthdomain:patch_fire",
                        "poopsky_filthdomain:patch_soul_fire",
                        "poopsky_filthdomain:glowstone_extra",
                        "poopsky_filthdomain:glowstone",
                        "poopsky_filthdomain:patch_bloodcap_roots",
                        "poopsky_filthdomain:ore_magma",
                        "poopsky_filthdomain:sealed_urine_spring",
                        "poopsky_filthdomain:ore_soul_sand",
                        "poopsky_filthdomain:ore_gravel",
                        "poopsky_filthdomain:ore_blackstone",
                        "poopsky_filthdomain:ore_gold_in_filth",
                        "poopsky_filthdomain:ore_quartz_in_filth",
                        "poopsky_filthdomain:ore_ancient_debris_large",
                        "poopsky_filthdomain:ore_ancient_debris_small"
                      ],
                      [],
                      [
                        "poopsky_filthdomain:lowland_urine_spring"
                      ]
                    ],
                    "has_precipitation": false,
                    "spawn_costs": {
                      "minecraft:enderman": {
                        "charge": 0.7,
                        "energy_budget": 0.15
                      },
                      "minecraft:ghast": {
                        "charge": 0.7,
                        "energy_budget": 0.15
                      },
                      "minecraft:skeleton": {
                        "charge": 0.7,
                        "energy_budget": 0.15
                      },
                      "minecraft:strider": {
                        "charge": 0.7,
                        "energy_budget": 0.15
                      }
                    },
                    "spawners": {
                      "ambient": [],
                      "axolotls": [],
                      "creature": [
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 3,
                          "minCount": 1,
                          "weight": 60
                        }
                      ],
                      "misc": [],
                      "monster": [
                        {
                          "type": "poopsky_filthdomain:basilisk",
                          "maxCount": 2,
                          "minCount": 1,
                          "weight": 40
                        },
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 5,
                          "minCount": 2,
                          "weight": 100
                        }
                      ],
                      "underground_water_creature": [],
                      "water_ambient": [],
                      "water_creature": []
                    },
                    "temperature": 2.0
                  },
                  "sporewood": {
                    "carvers": {
                      "air": "poopsky_filthdomain:filth_cave"
                    },
                    "downfall": 0.0,
                    "effects": {
                      "additions_sound": {
                        "sound": "minecraft:ambient.warped_forest.additions",
                        "tick_chance": 0.0111
                      },
                      "ambient_sound": "minecraft:ambient.warped_forest.loop",
                      "fog_color": 2180183,
                      "mood_sound": {
                        "block_search_extent": 8,
                        "offset": 2.0,
                        "sound": "minecraft:ambient.warped_forest.mood",
                        "tick_delay": 6000
                      },
                      "music": {
                        "max_delay": 24000,
                        "min_delay": 12000,
                        "replace_current_music": false,
                        "sound": "minecraft:music.nether.warped_forest"
                      },
                      "particle": {
                        "options": {
                          "type": "poopsky_filthdomain:filth_ash"
                        },
                        "probability": 0.01428
                      },
                      "sky_color": 2180183,
                      "water_color": 5918766,
                      "water_fog_color": 2631192
                    },
                    "features": [
                      [],
                      [],
                      [],
                      [],
                      [],
                      [],
                      [],
                      [
                        "poopsky_filthdomain:exposed_urine_spring",
                        "poopsky_filthdomain:patch_fire",
                        "poopsky_filthdomain:patch_soul_fire",
                        "poopsky_filthdomain:glowstone_extra",
                        "poopsky_filthdomain:glowstone",
                        "poopsky_filthdomain:ore_magma",
                        "poopsky_filthdomain:sealed_urine_spring",
                        "poopsky_filthdomain:ore_gravel",
                        "poopsky_filthdomain:ore_blackstone",
                        "poopsky_filthdomain:ore_gold_in_filth",
                        "poopsky_filthdomain:ore_quartz_in_filth",
                        "poopsky_filthdomain:ore_ancient_debris_large",
                        "poopsky_filthdomain:ore_ancient_debris_small"
                      ],
                      [],
                      [
                        "poopsky_filthdomain:lowland_urine_spring",
                        "poopsky_filthdomain:wild_glow_primo_fungus"
                      ]
                    ],
                    "has_precipitation": false,
                    "spawn_costs": {
                      "minecraft:enderman": {
                        "charge": 1.0,
                        "energy_budget": 0.12
                      }
                    },
                    "spawners": {
                      "ambient": [],
                      "axolotls": [],
                      "creature": [
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 3,
                          "minCount": 1,
                          "weight": 60
                        }
                      ],
                      "misc": [],
                      "monster": [
                        {
                          "type": "poopsky_filthdomain:basilisk",
                          "maxCount": 2,
                          "minCount": 1,
                          "weight": 40
                        },
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 4,
                          "minCount": 2,
                          "weight": 100
                        }
                      ],
                      "underground_water_creature": [],
                      "water_ambient": [],
                      "water_creature": []
                    },
                    "temperature": 2.0
                  },
                  "poop_forest": {
                    "carvers": {
                      "air": "poopsky_filthdomain:filth_cave"
                    },
                    "downfall": 0.0,
                    "effects": {
                      "additions_sound": {
                        "sound": "minecraft:ambient.nether_wastes.additions",
                        "tick_chance": 0.0111
                      },
                      "ambient_sound": "minecraft:ambient.nether_wastes.loop",
                      "fog_color": 7622961,
                      "mood_sound": {
                        "block_search_extent": 8,
                        "offset": 2.0,
                        "sound": "minecraft:ambient.nether_wastes.mood",
                        "tick_delay": 6000
                      },
                      "music": {
                        "max_delay": 24000,
                        "min_delay": 12000,
                        "replace_current_music": false,
                        "sound": "minecraft:music.nether.nether_wastes"
                      },
                      "particle": {
                        "options": {
                          "type": "poopsky_filthdomain:filth_ash"
                        },
                        "probability": 0.0125
                      },
                      "sky_color": 7622961,
                      "water_color": 6504996,
                      "water_fog_color": 2890255
                    },
                    "features": [
                      [],
                      [],
                      [],
                      [],
                      [],
                      [],
                      [],
                      [
                        "poopsky_filthdomain:exposed_urine_spring",
                        "poopsky_filthdomain:patch_fire",
                        "poopsky_filthdomain:patch_soul_fire",
                        "poopsky_filthdomain:glowstone_extra",
                        "poopsky_filthdomain:glowstone",
                        "poopsky_filthdomain:brown_mushroom_cavern",
                        "poopsky_filthdomain:red_mushroom_cavern",
                        "poopsky_filthdomain:ore_magma",
                        "poopsky_filthdomain:sealed_urine_spring",
                        "poopsky_filthdomain:ore_gravel",
                        "poopsky_filthdomain:ore_blackstone",
                        "poopsky_filthdomain:ore_gold_in_filth",
                        "poopsky_filthdomain:ore_quartz_in_filth",
                        "poopsky_filthdomain:ore_ancient_debris_large",
                        "poopsky_filthdomain:ore_ancient_debris_small"
                      ],
                      [],
                      [
                        "poopsky_filthdomain:lowland_urine_spring",
                        "poopsky_filthdomain:wild_poop_tree"
                      ]
                    ],
                    "has_precipitation": false,
                    "spawn_costs": {},
                    "spawners": {
                      "ambient": [],
                      "axolotls": [],
                      "creature": [
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 3,
                          "minCount": 1,
                          "weight": 60
                        }
                      ],
                      "misc": [],
                      "monster": [
                        {
                          "type": "poopsky_filthdomain:basilisk",
                          "maxCount": 2,
                          "minCount": 1,
                          "weight": 40
                        },
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 5,
                          "minCount": 2,
                          "weight": 100
                        }
                      ],
                      "underground_water_creature": [],
                      "water_ambient": [],
                      "water_creature": []
                    },
                    "temperature": 2.0
                  },
                  "filth_plains": {
                    "carvers": {
                      "air": "poopsky_filthdomain:filth_cave"
                    },
                    "downfall": 0.0,
                    "effects": {
                      "additions_sound": {
                        "sound": "minecraft:ambient.nether_wastes.additions",
                        "tick_chance": 0.0111
                      },
                      "ambient_sound": "minecraft:ambient.nether_wastes.loop",
                      "fog_color": 13934615,
                      "mood_sound": {
                        "block_search_extent": 8,
                        "offset": 2.0,
                        "sound": "minecraft:ambient.nether_wastes.mood",
                        "tick_delay": 6000
                      },
                      "music": {
                        "max_delay": 24000,
                        "min_delay": 12000,
                        "replace_current_music": false,
                        "sound": "minecraft:music.nether.nether_wastes"
                      },
                      "particle": {
                        "options": {
                          "type": "poopsky_filthdomain:filth_ash"
                        },
                        "probability": 0.00625
                      },
                      "sky_color": 13934615,
                      "water_color": 6504996,
                      "water_fog_color": 2890255
                    },
                    "features": [
                      [],
                      [],
                      [],
                      [],
                      [],
                      [],
                      [],
                      [
                        "poopsky_filthdomain:exposed_urine_spring",
                        "poopsky_filthdomain:patch_fire",
                        "poopsky_filthdomain:patch_soul_fire",
                        "poopsky_filthdomain:glowstone_extra",
                        "poopsky_filthdomain:glowstone",
                        "poopsky_filthdomain:brown_mushroom_cavern",
                        "poopsky_filthdomain:red_mushroom_cavern",
                        "poopsky_filthdomain:ore_magma",
                        "poopsky_filthdomain:sealed_urine_spring",
                        "poopsky_filthdomain:ore_gravel",
                        "poopsky_filthdomain:ore_blackstone",
                        "poopsky_filthdomain:ore_gold_in_filth",
                        "poopsky_filthdomain:ore_quartz_in_filth",
                        "poopsky_filthdomain:ore_ancient_debris_large",
                        "poopsky_filthdomain:ore_ancient_debris_small"
                      ],
                      [],
                      [
                        "poopsky_filthdomain:lowland_urine_spring",
                        "poopsky_filthdomain:brown_mushroom_normal",
                        "poopsky_filthdomain:red_mushroom_normal"
                      ]
                    ],
                    "has_precipitation": false,
                    "spawn_costs": {},
                    "spawners": {
                      "ambient": [],
                      "axolotls": [],
                      "creature": [
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 3,
                          "minCount": 1,
                          "weight": 60
                        }
                      ],
                      "misc": [],
                      "monster": [
                        {
                          "type": "poopsky_filthdomain:basilisk",
                          "maxCount": 2,
                          "minCount": 1,
                          "weight": 40
                        },
                        {
                          "type": "poopsky:poolime",
                          "maxCount": 5,
                          "minCount": 2,
                          "weight": 100
                        }
                      ],
                      "underground_water_creature": [],
                      "water_ambient": [],
                      "water_creature": []
                    },
                    "temperature": 2.0
                  }
                }
                """);
    }
}
