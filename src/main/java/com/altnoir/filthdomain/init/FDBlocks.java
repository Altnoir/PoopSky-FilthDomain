package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.block.FilthDomainPortalBlock;
import com.altnoir.abysslib.reginth.Reginth;
import com.altnoir.abysslib.reginth.util.entry.BlockEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

/**
 * 附属模组的方块注册（Reginth 写法，与前置一致）。
 * ReginthBlockBuilder 会自动生成默认 blockstate / 战利品表 / 语言键，并自动创建方块物品。
 */
public final class FDBlocks {
    private static final Reginth REGISTRATE = FilthDomain.registrate();

    private FDBlocks() {
    }

    // 示例方块：秽之域方块
    public static final BlockEntry<Block> FILTH_DOMAIN_BLOCK = REGISTRATE.block("filth_domain_block",
                    props -> new Block(props.strength(2.0F).mapColor(MapColor.COLOR_BROWN)))
            .blockstate((context, provider) -> provider.simpleBlock(context.get(),
                    provider.models().cubeAll(context.getName(), provider.mcLoc("block/brown_terracotta"))))
            .register();

    public static final BlockEntry<Block> HARDENED_POOP_BLOCK = terrainBlock(
            "hardened_poop_block", "Hardened Poop Block", MapColor.TERRACOTTA_BROWN, SoundType.TUFF, 2.5F);
    public static final BlockEntry<Block> CALCIUM_BLOCK = terrainBlock(
            "calcium_block", "Calcium Block", MapColor.TERRACOTTA_WHITE, SoundType.CALCITE, 2.0F);
    public static final BlockEntry<Block> FILTHBEDROCK = terrainBlock(
            "filthbedrock", "Filthbedrock", MapColor.COLOR_GRAY, SoundType.DEEPSLATE, 3.5F);

    public static final BlockEntry<FilthDomainPortalBlock> FILTHDOMAIN_PORTAL = REGISTRATE
            .block("filthdomain_portal", props -> new FilthDomainPortalBlock(props.strength(2.0F)
                    .mapColor(MapColor.COLOR_BROWN).lightLevel(state -> 15)
                    .noOcclusion().noLootTable().pushReaction(PushReaction.BLOCK)))
            .blockstate((context, provider) -> provider.getVariantBuilder(context.get()).forAllStates(state -> {
                String model = switch (state.getValue(FilthDomainPortalBlock.SECTION)) {
                    case BOTTOM -> "filthdomain_portal_bellow";
                    case CENTER -> "filthdomain_portal_center";
                    case TOP -> "filthdomain_portal_top";
                };
                return ConfiguredModel.builder()
                        .modelFile(provider.models().getExistingFile(FilthDomain.loc("block/" + model)))
                        .build();
            }))
            .loot((provider, block) -> {})
            .lang("Filth Domain Portal")
            .item()
            .model((context, provider) -> provider.generated(context))
            .build()
            .register();

    private static BlockEntry<Block> terrainBlock(String id, String name, MapColor color, SoundType sound, float hardness) {
        return REGISTRATE.block(id, props -> new Block(props.mapColor(color).sound(sound)
                        .strength(hardness, 6.0F).requiresCorrectToolForDrops()))
                .lang(name)
                .simpleItem()
                .register();
    }

    public static List<Item> getAllItems() {
        return REGISTRATE.getAll(Registries.BLOCK).stream()
                .map(DeferredHolder::get)
                .map(Block::asItem)
                .toList();
    }

    public static void register() {
        var oldId = FilthDomain.loc("dense_calcium_rock");
        BuiltInRegistries.BLOCK.addAlias(oldId, FILTHBEDROCK.getId());
        BuiltInRegistries.ITEM.addAlias(oldId, FILTHBEDROCK.getId());
    }
}
