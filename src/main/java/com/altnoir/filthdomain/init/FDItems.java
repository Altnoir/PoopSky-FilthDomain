package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.PoopSkyFilthDomain;
import com.altnoir.poopsky.init.PoItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 附属模组的物品注册。
 * 注意：使用本模组自己的 DeferredRegister（mod id 是自己的），
 * 不要复用前置的 {@code PoopSky.registrate()}。
 */
public final class FDItems {

    private FDItems() {
    }

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PoopSkyFilthDomain.MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PoopSkyFilthDomain.MOD_ID);

    // 示例物品：秽之域碎片
    public static final DeferredItem<Item> FILTH_DOMAIN_SHARD = ITEMS.register("filth_domain_shard",
            () -> new Item(new Item.Properties()));

    // 附属自己的创造栏；同时演示直接调用前置模组的代码库（PoItems.POOP）
    public static final Supplier<CreativeModeTab> FILTH_DOMAIN_TAB = CREATIVE_TABS.register("filth_domain",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.poopsky_filthdomain"))
                    .icon(() -> new ItemStack(FILTH_DOMAIN_SHARD.get()))
                    .displayItems((params, output) -> {
                        output.accept(FILTH_DOMAIN_SHARD.get());
                        output.accept(new ItemStack(PoItems.POOP.get()));
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }
}
