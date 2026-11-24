package com.altnoir.filthdomain.init;

import com.altnoir.abysslib.creative.BannerStyle;
import com.altnoir.abysslib.creative.CreativeTabSection;
import com.altnoir.abysslib.creative.SectionedCreativeModeTab;
import com.altnoir.abysslib.reginth.Reginth;
import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.abysslib.reginth.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * 附属自己的创造栏（复用 AbyssLib 的分区式创造栏：SectionedCreativeModeTab）。
 */
public final class FDItemGroups {
    private static final Reginth REGISTRATE = FilthDomain.registrate();

    public static final String TAB_KEY = "itemGroup.poopsky_filthdomain";

    public static final CreativeTabSection TS_ITEMS = section("itemGroup.poopsky_filthdomain.section.items");
    public static final CreativeTabSection TS_BLOCKS = section("itemGroup.poopsky_filthdomain.section.blocks");

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> TAB = REGISTRATE.generic("filth_domain",
            Registries.CREATIVE_MODE_TAB, () ->
                    SectionedCreativeModeTab.configure(
                            CreativeModeTab.builder()
                                    .title(Component.translatable(TAB_KEY))
                                    .icon(FDItems.FILTH_DOMAIN_SHARD::asStack),
                            BannerStyle.colors(3, 0xFF0C2238, 0xFF48617C, 0xFF7090B5, 0xFFDCE9F8),
                            FDItemGroups::populate,
                            TS_ITEMS,
                            TS_BLOCKS
                    ).build()
    ).register();

    private static void populate(CreativeModeTab.ItemDisplayParameters parameters) {
        for (Item item : FDItems.getAllItems()) {
            if (!(item instanceof BlockItem)) {
                TS_ITEMS.add(item);
            }
        }
        for (Item item : FDBlocks.getAllItems()) {
            TS_BLOCKS.add(item);
        }
    }

    private static CreativeTabSection section(String translationKey) {
        return new CreativeTabSection(translationKey);
    }

    public static void register() {
    }
}
