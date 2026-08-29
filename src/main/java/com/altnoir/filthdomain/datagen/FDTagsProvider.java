package com.altnoir.filthdomain.datagen;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 单独挂载的方块标签生成器，绕过 RegistrateDataProvider（见 {@code FDRegistrate} 注释）。
 * 通过公开的 {@link ProviderType#create} 构建 BLOCK_TAGS provider 再运行：
 * MC 的 {@code TagsProvider.run} 会在同一条 CompletableFuture 链上执行
 * createContentsProvider → addTags（触发 FDBlockTagGen.generate）→ 写文件，
 * 不存在并发 createContentsProvider 竞态。
 */
public class FDTagsProvider implements DataProvider {

    private final AbstractRegistrate<?> registrate;
    private final GatherDataEvent event;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public FDTagsProvider(AbstractRegistrate<?> registrate, GatherDataEvent event) {
        this.registrate = registrate;
        this.event = event;
        this.lookupProvider = event.getLookupProvider();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        RegistrateTagsProvider.IntrinsicImpl<Block> tags = ProviderType.create(
                ProviderType.BLOCK_TAGS, registrate, event, Map.of(), lookupProvider);
        return tags.run(output);
    }

    @Override
    public String getName() {
        return "FilthDomain Block Tags";
    }
}
