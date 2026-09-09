package com.altnoir.filthdomain.init;

import com.altnoir.abysslib.creative.ALBannerStyle;
import com.altnoir.abysslib.creative.ALCreativeTabSection;
import com.altnoir.abysslib.creative.ALSectionedCreativeModeTab;
import com.altnoir.abysslib.registrate.ALRegistrate;
import com.altnoir.filthdomain.FilthDomain;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * 附属自己的创造栏（复用 AbyssLib 的分区式创造栏：ALSectionedCreativeModeTab）。
 */
public final class FDItemGroups {
    private static final ALRegistrate REGISTRATE = FilthDomain.registrate();

    public static final String TAB_KEY = "itemGroup.poopsky_filthdomain";

    /** Filth Domain 创造栏横幅配色：与 PoopSky 同蓝系但更深。 */
    private static final ALBannerStyle BANNER = ALBannerStyle.colors(2,
            0xFF0C2238,
            0xFF48617C,  // 暗边框
            0xFF7090B5,  // 亮边框
            0xFFDCE9F8   // 文字
    );

    public static final ALCreativeTabSection TS_ITEMS = section("itemGroup.poopsky_filthdomain.section.items");
    public static final ALCreativeTabSection TS_BLOCKS = section("itemGroup.poopsky_filthdomain.section.blocks");

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> TAB = REGISTRATE.generic("filth_domain",
            Registries.CREATIVE_MODE_TAB, () ->
                    ALSectionedCreativeModeTab.configure(
                            CreativeModeTab.builder()
                                    .title(Component.translatable(TAB_KEY))
                                    .icon(FDItems.FILTH_DOMAIN_SHARD::asStack),
                            BANNER,
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

    private static ALCreativeTabSection section(String translationKey) {
        return new ALCreativeTabSection(translationKey);
    }

    public static void register() {
    }
}
