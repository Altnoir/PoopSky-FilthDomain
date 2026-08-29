package com.altnoir.filthdomain;

import com.altnoir.filthdomain.datagen.FDDataGenerators;
import com.altnoir.filthdomain.datagen.FDLangGen;
import com.altnoir.filthdomain.init.FDBlocks;
import com.altnoir.filthdomain.init.FDItemGroups;
import com.altnoir.filthdomain.init.FDItems;
import com.altnoir.poopsky.impl.registrate.PoRegistrate;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(FilthDomain.MOD_ID)
public class FilthDomain {
    public static final String MOD_ID = "poopsky_filthdomain";
    public static final Logger LOGGER = LogUtils.getLogger();

    // 用前置的 PoRegistrate（继承其全部 Registrate 定制），但绑定到附属自己的 mod id
    private static final PoRegistrate REGISTRATE = PoRegistrate.create(MOD_ID);

    public FilthDomain(IEventBus modEventBus) {
        FDItems.register();
        FDBlocks.register();
        FDItemGroups.register();
        FDLangGen.register();
        modEventBus.addListener(FDDataGenerators::gatherData);
    }

    public static PoRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
