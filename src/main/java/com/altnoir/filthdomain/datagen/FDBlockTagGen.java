package com.altnoir.filthdomain.datagen;

import com.altnoir.filthdomain.tag.FDTags;
import com.altnoir.poopsky.init.PoBlocks;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * 绝望世界维度使用的方块标签（与前置 {@code BlockTagGen} 同样的 Registrate 写法）。
 * 引用的都是前置 PoopSky 的方块，与数据包中的 worldgen 数据配合使用。
 * <p>
 * 注意：不注册 unassociated BLOCK_TAGS 生成器（否则 RegistrateDataProvider 的急切
 * 调用会与并发 run 产生 ConcurrentModificationException 竞态），而是由
 * {@code FDTagsProvider} 在 addTags 阶段直接调用本方法。
 */
public final class FDBlockTagGen {
    private static RegistrateTagsProvider.IntrinsicImpl<Block> provider;

    private FDBlockTagGen() {
    }

    public static void addTags(RegistrateTagsProvider.IntrinsicImpl<Block> provider) {
        FDBlockTagGen.provider = provider;

        Block[] basePoopBlocks = {
                PoBlocks.POOP_BLOCK.get(),
                PoBlocks.RAW_POOP_BLOCK.get(),
                PoBlocks.POOP_BRICKS.get(),
                PoBlocks.POOP_SAND.get(),
                PoBlocks.GOLDEN_POOP_BLOCK.get(),
                PoBlocks.SALTPETER_BLOCK.get(),
                PoBlocks.CHILI_POOP_BLOCK.get(),
                PoBlocks.CUT_POOP_BLOCK.get(),
                PoBlocks.SMOOTH_POOP_BLOCK.get(),
                PoBlocks.DRIED_POOP_BLOCK.get(),
                PoBlocks.CRACKED_POOP_BRICKS.get(),
                PoBlocks.MOSSY_POOP_BRICKS.get(),
                PoBlocks.RAW_SAPLING_POOP_BLOCK.get(),
                PoBlocks.RAW_SEA_POOP_BLOCK.get(),
                PoBlocks.RAW_WITHER_POOP_BLOCK.get(),
        };
        tag(FDTags.Blocks.BASE_POOP_POOP).add(basePoopBlocks);
        tag(FDTags.Blocks.DESPERATE_CARVER_REPLACEABLES).add(basePoopBlocks);
    }

    private static IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block> tag(TagKey<Block> tag) {
        return provider.addTag(tag);
    }
}
