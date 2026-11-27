package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.abysslib.reginth.Reginth;
import com.altnoir.abysslib.reginth.util.entry.ItemEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;

/**
 * 附属模组的物品注册（Reginth 写法，与前置一致）。
 * ReginthItemBuilder 会自动生成默认模型与语言键。
 */
public final class FDItems {
    private static final Reginth REGISTRATE = FilthDomain.registrate();
    private static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, FilthDomain.MOD_ID);

    private FDItems() {
    }

    // 示例物品：秽之域碎片
    public static final ItemEntry<Item> FILTH_DOMAIN_SHARD = REGISTRATE.item("filth_domain_shard", Item::new)
            .model((context, provider) -> provider.generated(context, provider.mcLoc("item/flint")))
            .register();

    private static final String MIASMA_HARNESS_ID = "miasma_harness";
    private static final DeferredHolder<ArmorMaterial, ArmorMaterial> MIASMA_HARNESS_MATERIAL =
            ARMOR_MATERIALS.register(MIASMA_HARNESS_ID, id -> new ArmorMaterial(
                    Map.of(ArmorItem.Type.CHESTPLATE, 3, ArmorItem.Type.HELMET, 0,
                            ArmorItem.Type.LEGGINGS, 0, ArmorItem.Type.BOOTS, 0, ArmorItem.Type.BODY, 0),
                    10, SoundEvents.ARMOR_EQUIP_ELYTRA, () -> Ingredient.of(FILTH_DOMAIN_SHARD.get()),
                    List.of(new ArmorMaterial.Layer(id)), 0.0F, 0.0F));

    public static final ItemEntry<ArmorItem> MIASMA_HARNESS = REGISTRATE
            .item(MIASMA_HARNESS_ID, props -> new ArmorItem(MIASMA_HARNESS_MATERIAL,
                    ArmorItem.Type.CHESTPLATE, props.durability(432)))
            .lang("Miasma Flight Harness")
            .model((context, provider) -> provider.generated(context))
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

    public static void register(IEventBus bus) {
        ARMOR_MATERIALS.register(bus);
    }
}
