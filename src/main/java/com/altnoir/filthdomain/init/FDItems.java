package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.abysslib.reginth.Reginth;
import com.altnoir.abysslib.reginth.util.entry.ItemEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

/**
 * 附属模组的物品注册（Reginth 写法，与前置一致）。
 * ReginthItemBuilder 会自动生成默认模型与语言键。
 */
public final class FDItems {
    private static final Reginth REGISTRATE = FilthDomain.registrate();

    private FDItems() {
    }

    // 示例物品：秽之域碎片
    public static final ItemEntry<Item> FILTH_DOMAIN_SHARD = REGISTRATE.item("filth_domain_shard", Item::new)
            .model((context, provider) -> provider.generated(context, provider.mcLoc("item/flint")))
            .register();

    // 咒蛙之眼：咒蛙的掉落物
    public static final ItemEntry<Item> EYE_OF_BASILISK = REGISTRATE.item("eye_of_basilisk", Item::new)
            .model((context, provider) -> provider.generated(context))
            .register();

    public static final ItemEntry<DeferredSpawnEggItem> BASILISK_SPAWN_EGG = REGISTRATE
            .item("basilisk_spawn_egg", props -> new DeferredSpawnEggItem(FDEntityType.BASILISK, 0x71877A, 0xE1CCA8, props))
            .model((context, provider) -> provider.withExistingParent(context.getName(),
                    provider.mcLoc("item/template_spawn_egg")))
            .register();

    public static List<Item> getAllItems() {
        return REGISTRATE.getAll(Registries.ITEM).stream()
                .map(DeferredHolder::get)
                .toList();
    }

    public static void register() {
    }
}
