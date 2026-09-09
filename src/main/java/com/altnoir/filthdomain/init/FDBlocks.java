package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.abysslib.registrate.ALRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

/**
 * 附属模组的方块注册（Registrate 写法，与前置一致）。
 * ALBlockBuilder 会自动生成默认 blockstate / 战利品表 / 语言键，并自动创建方块物品。
 */
public final class FDBlocks {
    private static final ALRegistrate REGISTRATE = FilthDomain.registrate();

    private FDBlocks() {
    }

    // 示例方块：秽之域方块
    public static final BlockEntry<Block> FILTH_DOMAIN_BLOCK = REGISTRATE.block("filth_domain_block",
                    props -> new Block(props.strength(2.0F).mapColor(MapColor.COLOR_BROWN)))
            .register();

    public static List<Item> getAllItems() {
        return REGISTRATE.getAll(Registries.BLOCK).stream()
                .map(DeferredHolder::get)
                .map(Block::asItem)
                .toList();
    }

    public static void register() {
    }
}
