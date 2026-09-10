package com.altnoir.filthdomain.datagen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.abysslib.reginth.Reginth;
import com.altnoir.abysslib.reginth.providers.ProviderType;

public final class FDLangGen {
    private static final Reginth REGISTRATE = FilthDomain.registrate();

    private FDLangGen() {
    }

    public static void register() {
        // 物品/方块等注册表条目的英文名由 Registrate 的 defaultLang 自动生成，
        // 这里补充创造栏与分区的英文键。
        // LANG 不在标签的 parent 链上，无急切调用竞态，可安全使用 addDataGenerator。
        REGISTRATE.addDataGenerator(ProviderType.LANG, prov -> {
            prov.add("itemGroup.poopsky_filthdomain", "Sky Toilet: Filth Domain");
            prov.add("itemGroup.poopsky_filthdomain.section.items", "Items");
            prov.add("itemGroup.poopsky_filthdomain.section.blocks", "Blocks");
        });
    }
}
