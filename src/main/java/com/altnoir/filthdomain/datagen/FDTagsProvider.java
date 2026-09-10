package com.altnoir.filthdomain.datagen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.abysslib.reginth.providers.ProviderType;
import com.altnoir.abysslib.reginth.providers.ReginthTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

/**
 * 单独挂载的方块标签生成器（世界生成标签）。
 * <p>
 * 为什么不注册到 Registrate 默认的 RegistrateDataProvider：其构造过程会通过
 * getFilledProvider() 急切触发 createContentsProvider()（内部 clear + addTags），
 * 当同一 datagen 进程里还有前置 PoopSky 的 RegistrateDataProvider 并发运行时，
 * 会与 TagsProvider.run 的迭代撞车（ConcurrentModificationException，稳定复现）。
 * 这里自行构建 BLOCK_TAGS provider 并重写 addTags，整条链路单线程、无竞态。
 */
public class FDTagsProvider implements DataProvider {

    private final GatherDataEvent event;

    public FDTagsProvider(GatherDataEvent event) {
        this.event = event;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        ReginthTagsProvider.IntrinsicImpl<Block> tags = new ReginthTagsProvider.IntrinsicImpl<>(
                FilthDomain.registrate(),
                ProviderType.BLOCK_TAGS,
                "blocks",
                event.getGenerator().getPackOutput(),
                Registries.BLOCK,
                event.getLookupProvider(),
                block -> block.builtInRegistryHolder().key(),
                event.getExistingFileHelper()) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                FDBlockTagGen.addTags(this);
            }
        };
        return tags.run(output);
    }

    @Override
    public String getName() {
        return "FilthDomain Block Tags";
    }
}
