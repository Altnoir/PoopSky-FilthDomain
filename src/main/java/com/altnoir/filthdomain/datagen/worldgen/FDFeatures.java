package com.altnoir.filthdomain.datagen.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

final class FDFeatures {
    private FDFeatures() {
    }

    static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        FDWorldgen.register(context, Registries.CONFIGURED_FEATURE, ConfiguredFeature.DIRECT_CODEC, """
                {
                  "basalt_blobs": {
                    "type": "minecraft:netherrack_replace_blobs",
                    "config": {
                      "radius": {
                        "type": "minecraft:uniform",
                        "max_inclusive": 7,
                        "min_inclusive": 3
                      },
                      "state": {
                        "Name": "minecraft:basalt"
                      },
                      "target": {
                        "Name": "poopsky:poop_block"
                      }
                    }
                  },
                  "basalt_pillar": {
                    "type": "minecraft:block_column",
                    "config": {
                      "allowed_placement": {
                        "type": "minecraft:matching_blocks",
                        "blocks": "minecraft:air"
                      },
                      "direction": "up",
                      "layers": [
                        {
                          "height": {
                            "type": "minecraft:uniform",
                            "max_inclusive": 4,
                            "min_inclusive": 1
                          },
                          "provider": {
                            "type": "minecraft:simple_state_provider",
                            "state": {
                              "Name": "minecraft:basalt"
                            }
                          }
                        }
                      ],
                      "prioritize_tip": false
                    }
                  },
                  "blackstone_blobs": {
                    "type": "minecraft:netherrack_replace_blobs",
                    "config": {
                      "radius": {
                        "type": "minecraft:uniform",
                        "max_inclusive": 7,
                        "min_inclusive": 3
                      },
                      "state": {
                        "Name": "minecraft:blackstone"
                      },
                      "target": {
                        "Name": "poopsky:poop_block"
                      }
                    }
                  },
                  "delta": {
                    "type": "minecraft:delta_feature",
                    "config": {
                      "contents": {
                        "Name": "poopsky:urine_liquid",
                        "Properties": {
                          "level": "0"
                        }
                      },
                      "rim": {
                        "Name": "poopsky:chili_poop_block"
                      },
                      "rim_size": {
                        "type": "minecraft:uniform",
                        "max_inclusive": 2,
                        "min_inclusive": 0
                      },
                      "size": {
                        "type": "minecraft:uniform",
                        "max_inclusive": 7,
                        "min_inclusive": 3
                      }
                    }
                  },
                  "glowstone_extra": {
                    "type": "minecraft:random_patch",
                    "config": {
                      "feature": {
                        "feature": {
                          "type": "minecraft:simple_block",
                          "config": {
                            "to_place": {
                              "type": "minecraft:simple_state_provider",
                              "state": {
                                "Name": "poopsky:raw_wither_poop_block"
                              }
                            }
                          }
                        },
                        "placement": [
                          {
                            "type": "minecraft:block_predicate_filter",
                            "predicate": {
                              "type": "minecraft:matching_blocks",
                              "blocks": "poopsky:poop_block"
                            }
                          }
                        ]
                      },
                      "tries": 64,
                      "xz_spread": 7,
                      "y_spread": 3
                    }
                  },
                  "large_basalt_columns": {
                    "type": "minecraft:block_column",
                    "config": {
                      "allowed_placement": {
                        "type": "minecraft:matching_blocks",
                        "blocks": "minecraft:air"
                      },
                      "direction": "up",
                      "layers": [
                        {
                          "height": {
                            "type": "minecraft:uniform",
                            "max_inclusive": 10,
                            "min_inclusive": 5
                          },
                          "provider": {
                            "type": "minecraft:simple_state_provider",
                            "state": {
                              "Name": "minecraft:basalt"
                            }
                          }
                        }
                      ],
                      "prioritize_tip": false
                    }
                  },
                  "ore_ancient_debris_large": {
                    "type": "minecraft:scattered_ore",
                    "config": {
                      "discard_chance_on_air_exposure": 1.0,
                      "size": 3,
                      "targets": [
                        {
                          "state": {
                            "Name": "poopsky:poop_tnt"
                          },
                          "target": {
                            "predicate_type": "minecraft:tag_match",
                            "tag": "poopsky_filthdomain:base_poop"
                          }
                        }
                      ]
                    }
                  },
                  "ore_ancient_debris_small": {
                    "type": "minecraft:scattered_ore",
                    "config": {
                      "discard_chance_on_air_exposure": 1.0,
                      "size": 2,
                      "targets": [
                        {
                          "state": {
                            "Name": "poopsky:poop_tnt"
                          },
                          "target": {
                            "predicate_type": "minecraft:tag_match",
                            "tag": "poopsky_filthdomain:base_poop"
                          }
                        }
                      ]
                    }
                  },
                  "ore_blackstone": {
                    "type": "minecraft:ore",
                    "config": {
                      "discard_chance_on_air_exposure": 0.0,
                      "size": 33,
                      "targets": [
                        {
                          "state": {
                            "Name": "poopsky:poop_bricks"
                          },
                          "target": {
                            "block": "poopsky:poop_block",
                            "predicate_type": "minecraft:block_match"
                          }
                        }
                      ]
                    }
                  },
                  "ore_gravel": {
                    "type": "minecraft:ore",
                    "config": {
                      "discard_chance_on_air_exposure": 0.0,
                      "size": 33,
                      "targets": [
                        {
                          "state": {
                            "Name": "poopsky:raw_poop_block"
                          },
                          "target": {
                            "block": "poopsky:poop_block",
                            "predicate_type": "minecraft:block_match"
                          }
                        }
                      ]
                    }
                  },
                  "ore_magma": {
                    "type": "minecraft:ore",
                    "config": {
                      "discard_chance_on_air_exposure": 0.0,
                      "size": 33,
                      "targets": [
                        {
                          "state": {
                            "Name": "poopsky:chili_poop_block"
                          },
                          "target": {
                            "block": "poopsky:poop_block",
                            "predicate_type": "minecraft:block_match"
                          }
                        }
                      ]
                    }
                  },
                  "ore_gold": {
                    "type": "minecraft:ore",
                    "config": {
                      "discard_chance_on_air_exposure": 0.0,
                      "size": 10,
                      "targets": [
                        {
                          "state": {
                            "Name": "poopsky:golden_poop_block"
                          },
                          "target": {
                            "block": "poopsky:poop_block",
                            "predicate_type": "minecraft:block_match"
                          }
                        }
                      ]
                    }
                  },
                  "ore_quartz": {
                    "type": "minecraft:ore",
                    "config": {
                      "discard_chance_on_air_exposure": 0.0,
                      "size": 14,
                      "targets": [
                        {
                          "state": {
                            "Name": "poopsky:poolime_block"
                          },
                          "target": {
                            "block": "poopsky:poop_block",
                            "predicate_type": "minecraft:block_match"
                          }
                        }
                      ]
                    }
                  },
                  "ore_soul_sand": {
                    "type": "minecraft:ore",
                    "config": {
                      "discard_chance_on_air_exposure": 0.0,
                      "size": 12,
                      "targets": [
                        {
                          "state": {
                            "Name": "poopsky:poop_sand"
                          },
                          "target": {
                            "block": "poopsky:poop_block",
                            "predicate_type": "minecraft:block_match"
                          }
                        }
                      ]
                    }
                  },
                  "patch_brown_mushroom": {
                    "type": "minecraft:random_patch",
                    "config": {
                      "feature": {
                        "feature": {
                          "type": "minecraft:simple_block",
                          "config": {
                            "to_place": {
                              "type": "minecraft:simple_state_provider",
                              "state": {
                                "Name": "poopsky:saltpeter_cluster",
                                "Properties": {
                                  "facing": "up"
                                }
                              }
                            }
                          }
                        },
                        "placement": [
                          {
                            "type": "minecraft:block_predicate_filter",
                            "predicate": {
                              "type": "minecraft:matching_blocks",
                              "blocks": "minecraft:air"
                            }
                          }
                        ]
                      },
                      "tries": 96,
                      "xz_spread": 7,
                      "y_spread": 3
                    }
                  },
                  "patch_bloodcap_roots": {
                    "type": "minecraft:random_patch",
                    "config": {
                      "feature": {
                        "feature": {
                          "type": "minecraft:simple_block",
                          "config": {
                            "to_place": {
                              "type": "minecraft:simple_state_provider",
                              "state": {
                                "Name": "poopsky:ginkgo_sapling"
                              }
                            }
                          }
                        },
                        "placement": [
                          {
                            "type": "minecraft:block_predicate_filter",
                            "predicate": {
                              "type": "minecraft:matching_blocks",
                              "blocks": "minecraft:air"
                            }
                          }
                        ]
                      },
                      "tries": 96,
                      "xz_spread": 7,
                      "y_spread": 3
                    }
                  },
                  "patch_fire": {
                    "type": "minecraft:random_patch",
                    "config": {
                      "feature": {
                        "feature": {
                          "type": "minecraft:simple_block",
                          "config": {
                            "to_place": {
                              "type": "minecraft:simple_state_provider",
                              "state": {
                                "Name": "poopsky:poop_cake"
                              }
                            }
                          }
                        },
                        "placement": [
                          {
                            "type": "minecraft:block_predicate_filter",
                            "predicate": {
                              "type": "minecraft:all_of",
                              "predicates": [
                                {
                                  "type": "minecraft:matching_blocks",
                                  "blocks": "minecraft:air"
                                },
                                {
                                  "type": "minecraft:matching_blocks",
                                  "blocks": "poopsky:poop_block",
                                  "offset": [
                                    0,
                                    -1,
                                    0
                                  ]
                                }
                              ]
                            }
                          }
                        ]
                      },
                      "tries": 96,
                      "xz_spread": 7,
                      "y_spread": 3
                    }
                  },
                  "patch_red_mushroom": {
                    "type": "minecraft:random_patch",
                    "config": {
                      "feature": {
                        "feature": {
                          "type": "minecraft:simple_block",
                          "config": {
                            "to_place": {
                              "type": "minecraft:simple_state_provider",
                              "state": {
                                "Name": "poopsky:medium_saltpeter_bud",
                                "Properties": {
                                  "facing": "up"
                                }
                              }
                            }
                          }
                        },
                        "placement": [
                          {
                            "type": "minecraft:block_predicate_filter",
                            "predicate": {
                              "type": "minecraft:matching_blocks",
                              "blocks": "minecraft:air"
                            }
                          }
                        ]
                      },
                      "tries": 96,
                      "xz_spread": 7,
                      "y_spread": 3
                    }
                  },
                  "patch_soul_fire": {
                    "type": "minecraft:random_patch",
                    "config": {
                      "feature": {
                        "feature": {
                          "type": "minecraft:simple_block",
                          "config": {
                            "to_place": {
                              "type": "minecraft:simple_state_provider",
                              "state": {
                                "Name": "poopsky:poop_log"
                              }
                            }
                          }
                        },
                        "placement": [
                          {
                            "type": "minecraft:block_predicate_filter",
                            "predicate": {
                              "type": "minecraft:all_of",
                              "predicates": [
                                {
                                  "type": "minecraft:matching_blocks",
                                  "blocks": "minecraft:air"
                                },
                                {
                                  "type": "minecraft:matching_blocks",
                                  "blocks": "poopsky:dried_poop_block",
                                  "offset": [
                                    0,
                                    -1,
                                    0
                                  ]
                                }
                              ]
                            }
                          }
                        ]
                      },
                      "tries": 96,
                      "xz_spread": 7,
                      "y_spread": 3
                    }
                  },
                  "small_basalt_columns": {
                    "type": "minecraft:block_column",
                    "config": {
                      "allowed_placement": {
                        "type": "minecraft:matching_blocks",
                        "blocks": "minecraft:air"
                      },
                      "direction": "up",
                      "layers": [
                        {
                          "height": {
                            "type": "minecraft:uniform",
                            "max_inclusive": 4,
                            "min_inclusive": 1
                          },
                          "provider": {
                            "type": "minecraft:simple_state_provider",
                            "state": {
                              "Name": "minecraft:basalt"
                            }
                          }
                        }
                      ],
                      "prioritize_tip": false
                    }
                  },
                  "basalt_urine_spring": {
                    "type": "minecraft:spring_feature",
                    "config": {
                      "hole_count": 1,
                      "requires_block_below": true,
                      "rock_count": 4,
                      "state": {
                        "Name": "poopsky:urine",
                        "Properties": {
                          "falling": "true"
                        }
                      },
                      "valid_blocks": [
                        "poopsky:poop_block",
                        "poopsky:poop_sand",
                        "poopsky:raw_poop_block",
                        "poopsky:chili_poop_block",
                        "poopsky:poop_bricks"
                      ]
                    }
                  },
                  "lowland_urine_spring": {
                    "type": "minecraft:spring_feature",
                    "config": {
                      "hole_count": 1,
                      "requires_block_below": true,
                      "rock_count": 4,
                      "state": {
                        "Name": "poopsky:urine",
                        "Properties": {
                          "falling": "true"
                        }
                      },
                      "valid_blocks": [
                        "poopsky:cut_poop_block",
                        "poopsky:poop_empty_log",
                        "poopsky:smooth_poop_block",
                        "poopsky:raw_sea_poop_block",
                        "poopsky:raw_sapling_poop_block",
                        "poopsky:dried_poop_block",
                        "poopsky:chili_poop_block",
                        "poopsky:poop_block"
                      ]
                    }
                  },
                  "sealed_urine_spring": {
                    "type": "minecraft:spring_feature",
                    "config": {
                      "hole_count": 0,
                      "requires_block_below": false,
                      "rock_count": 5,
                      "state": {
                        "Name": "poopsky:urine",
                        "Properties": {
                          "falling": "true"
                        }
                      },
                      "valid_blocks": "poopsky:poop_block"
                    }
                  },
                  "exposed_urine_spring": {
                    "type": "minecraft:spring_feature",
                    "config": {
                      "hole_count": 1,
                      "requires_block_below": false,
                      "rock_count": 4,
                      "state": {
                        "Name": "poopsky:urine",
                        "Properties": {
                          "falling": "true"
                        }
                      },
                      "valid_blocks": "poopsky:poop_block"
                    }
                  }
                }
                """);
    }

    static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        FDWorldgen.register(context, Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC, """
                {
                  "basalt_blobs": {
                    "feature": "poopsky_filthdomain:basalt_blobs",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 75
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 0
                          },
                          "min_inclusive": {
                            "above_bottom": 0
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "basalt_pillar": {
                    "feature": "poopsky_filthdomain:basalt_pillar",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 10
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 0
                          },
                          "min_inclusive": {
                            "above_bottom": 0
                          }
                        }
                      },
                      {
                        "type": "minecraft:environment_scan",
                        "direction_of_search": "down",
                        "max_steps": 12,
                        "allowed_search_condition": {
                          "type": "minecraft:matching_blocks",
                          "blocks": "minecraft:air"
                        },
                        "target_condition": {
                          "type": "minecraft:solid"
                        }
                      },
                      {
                        "type": "minecraft:random_offset",
                        "xz_spread": 0,
                        "y_spread": 1
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "blackstone_blobs": {
                    "feature": "poopsky_filthdomain:blackstone_blobs",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 25
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 0
                          },
                          "min_inclusive": {
                            "above_bottom": 0
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "brown_mushroom_cavern": {
                    "feature": "poopsky_filthdomain:patch_brown_mushroom",
                    "placement": [
                      {
                        "type": "minecraft:rarity_filter",
                        "chance": 2
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 0
                          },
                          "min_inclusive": {
                            "above_bottom": 0
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "brown_mushroom_normal": {
                    "feature": "poopsky_filthdomain:patch_brown_mushroom",
                    "placement": [
                      {
                        "type": "minecraft:rarity_filter",
                        "chance": 256
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:heightmap",
                        "heightmap": "MOTION_BLOCKING"
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "delta": {
                    "feature": "poopsky_filthdomain:delta",
                    "placement": [
                      {
                        "type": "minecraft:count_on_every_layer",
                        "count": 40
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "glowstone_extra": {
                    "feature": "poopsky_filthdomain:glowstone_extra",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": {
                          "type": "minecraft:biased_to_bottom",
                          "max_inclusive": 9,
                          "min_inclusive": 0
                        }
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 4
                          },
                          "min_inclusive": {
                            "above_bottom": 4
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "glowstone": {
                    "feature": "poopsky_filthdomain:glowstone_extra",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 10
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 0
                          },
                          "min_inclusive": {
                            "above_bottom": 0
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "large_basalt_columns": {
                    "feature": "poopsky_filthdomain:large_basalt_columns",
                    "placement": [
                      {
                        "type": "minecraft:count_on_every_layer",
                        "count": 2
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_ancient_debris_large": {
                    "feature": "poopsky_filthdomain:ore_ancient_debris_large",
                    "placement": [
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:trapezoid",
                          "max_inclusive": {
                            "absolute": 24
                          },
                          "min_inclusive": {
                            "absolute": 8
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_blackstone": {
                    "feature": "poopsky_filthdomain:ore_blackstone",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 2
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "absolute": 31
                          },
                          "min_inclusive": {
                            "absolute": 5
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_ancient_debris_small": {
                    "feature": "poopsky_filthdomain:ore_ancient_debris_small",
                    "placement": [
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 8
                          },
                          "min_inclusive": {
                            "above_bottom": 8
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_gold_in_basalt": {
                    "feature": "poopsky_filthdomain:ore_gold",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 20
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 10
                          },
                          "min_inclusive": {
                            "above_bottom": 10
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_gold_in_filth": {
                    "feature": "poopsky_filthdomain:ore_gold",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 10
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 10
                          },
                          "min_inclusive": {
                            "above_bottom": 10
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_gravel": {
                    "feature": "poopsky_filthdomain:ore_gravel",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 2
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "absolute": 41
                          },
                          "min_inclusive": {
                            "absolute": 5
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_magma": {
                    "feature": "poopsky_filthdomain:ore_magma",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 4
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "absolute": 36
                          },
                          "min_inclusive": {
                            "absolute": 27
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_quartz_in_basalt": {
                    "feature": "poopsky_filthdomain:ore_quartz",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 32
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 10
                          },
                          "min_inclusive": {
                            "above_bottom": 10
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_quartz_in_filth": {
                    "feature": "poopsky_filthdomain:ore_quartz",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 16
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 10
                          },
                          "min_inclusive": {
                            "above_bottom": 10
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "ore_soul_sand": {
                    "feature": "poopsky_filthdomain:ore_soul_sand",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 12
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "absolute": 31
                          },
                          "min_inclusive": {
                            "above_bottom": 0
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "patch_bloodcap_roots": {
                    "feature": "poopsky_filthdomain:patch_bloodcap_roots",
                    "placement": [
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 0
                          },
                          "min_inclusive": {
                            "above_bottom": 0
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "patch_fire": {
                    "feature": "poopsky_filthdomain:patch_fire",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": {
                          "type": "minecraft:uniform",
                          "max_inclusive": 5,
                          "min_inclusive": 0
                        }
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 4
                          },
                          "min_inclusive": {
                            "above_bottom": 4
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "patch_soul_fire": {
                    "feature": "poopsky_filthdomain:patch_soul_fire",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": {
                          "type": "minecraft:uniform",
                          "max_inclusive": 5,
                          "min_inclusive": 0
                        }
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 4
                          },
                          "min_inclusive": {
                            "above_bottom": 4
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "red_mushroom_cavern": {
                    "feature": "poopsky_filthdomain:patch_red_mushroom",
                    "placement": [
                      {
                        "type": "minecraft:rarity_filter",
                        "chance": 2
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 0
                          },
                          "min_inclusive": {
                            "above_bottom": 0
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "red_mushroom_normal": {
                    "feature": "poopsky_filthdomain:patch_red_mushroom",
                    "placement": [
                      {
                        "type": "minecraft:rarity_filter",
                        "chance": 512
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:heightmap",
                        "heightmap": "MOTION_BLOCKING"
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "small_basalt_columns": {
                    "feature": "poopsky_filthdomain:small_basalt_columns",
                    "placement": [
                      {
                        "type": "minecraft:count_on_every_layer",
                        "count": 4
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "dense_sealed_urine_springs": {
                    "feature": "poopsky_filthdomain:sealed_urine_spring",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 32
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 10
                          },
                          "min_inclusive": {
                            "above_bottom": 10
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "sealed_urine_spring": {
                    "feature": "poopsky_filthdomain:sealed_urine_spring",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 16
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 10
                          },
                          "min_inclusive": {
                            "above_bottom": 10
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "basalt_urine_spring": {
                    "feature": "poopsky_filthdomain:basalt_urine_spring",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 16
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 4
                          },
                          "min_inclusive": {
                            "above_bottom": 4
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "lowland_urine_spring": {
                    "feature": "poopsky_filthdomain:lowland_urine_spring",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 20
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:very_biased_to_bottom",
                          "inner": 8,
                          "max_inclusive": {
                            "below_top": 8
                          },
                          "min_inclusive": {
                            "above_bottom": 0
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  },
                  "exposed_urine_spring": {
                    "feature": "poopsky_filthdomain:exposed_urine_spring",
                    "placement": [
                      {
                        "type": "minecraft:count",
                        "count": 8
                      },
                      {
                        "type": "minecraft:in_square"
                      },
                      {
                        "type": "minecraft:height_range",
                        "height": {
                          "type": "minecraft:uniform",
                          "max_inclusive": {
                            "below_top": 4
                          },
                          "min_inclusive": {
                            "above_bottom": 4
                          }
                        }
                      },
                      {
                        "type": "minecraft:biome"
                      }
                    ]
                  }
                }
                """);
    }
}
