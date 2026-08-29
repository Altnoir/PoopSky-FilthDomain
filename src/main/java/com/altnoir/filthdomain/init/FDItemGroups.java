package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.poopsky.impl.creative.PoCreativeTabSection;
import com.altnoir.poopsky.impl.creative.PoSectionedCreativeModeTab;
import com.altnoir.poopsky.impl.registrate.PoRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * 附属自己的创造栏（复用前置的分区式创造栏：PoSectionedCreativeModeTab）。
 */
public final class FDItemGroups {
    private static final PoRegistrate REGISTRATE = FilthDomain.registrate();

    public static final String TAB_KEY = "itemGroup.poopsky_filthdomain";

    public static final PoCreativeTabSection TS_ITEMS = section("itemGroup.poopsky_filthdomain.section.items");
    public static final PoCreativeTabSection TS_BLOCKS = section("itemGroup.poopsky_filthdomain.section.blocks");

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> TAB = REGISTRATE.generic("filth_domain",
            Registries.CREATIVE_MODE_TAB, () ->
                    PoSectionedCreativeModeTab.configure(
                            CreativeModeTab.builder()
                                    .title(Component.translatable(TAB_KEY))
                                    .icon(FDItems.FILTH_DOMAIN_SHARD::asStack),
                            FDItemGroups::populate,
                            TS_ITEMS,
                            TS_BLOCKS
                    ).build()
    ).register();

    private static void populate(CreativeModeTab.ItemDisplayParameters parameters) {
        for (Item item : FDItems.getAllItems()) {
            TS_ITEMS.add(item);
        }
        for (Item item : FDBlocks.getAllItems()) {
            TS_BLOCKS.add(item);
        }
    }

    private static PoCreativeTabSection section(String translationKey) {
        return new PoCreativeTabSection(translationKey);
    }

    public static void register() {
    }
}
