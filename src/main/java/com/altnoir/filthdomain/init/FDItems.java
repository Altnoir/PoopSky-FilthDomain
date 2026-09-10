package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.abysslib.reginth.Reginth;
import com.altnoir.abysslib.reginth.util.entry.ItemEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
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
            .register();

    public static List<Item> getAllItems() {
        return REGISTRATE.getAll(Registries.ITEM).stream()
                .map(DeferredHolder::get)
                .toList();
    }

    public static void register() {
    }
}
