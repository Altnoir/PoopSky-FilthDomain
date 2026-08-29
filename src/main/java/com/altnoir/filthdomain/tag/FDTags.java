package com.altnoir.filthdomain.tag;

import com.altnoir.filthdomain.PoopSkyFilthDomain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class FDTags {

    private FDTags() {
    }

    public static final class Blocks {
        // 绝望世界的基础粪便方块（远古残骸矿石的生成目标）
        public static final TagKey<Block> BASE_POOP_POOP = create("base_poop_poopsky");
        // 绝望世界洞穴雕刻器可替换方块
        public static final TagKey<Block> DESPERATE_CARVER_REPLACEABLES = create("desperate_carver_replaceables");

        private static TagKey<Block> create(String path) {
            return TagKey.create(Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(PoopSkyFilthDomain.MOD_ID, path));
        }
    }
}
