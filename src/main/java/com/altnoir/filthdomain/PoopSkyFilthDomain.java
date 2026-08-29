package com.altnoir.filthdomain;

import com.altnoir.filthdomain.datagen.FDBlockTagGen;
import com.altnoir.filthdomain.datagen.FDDataGenerators;
import com.altnoir.filthdomain.init.FDItems;
import com.altnoir.filthdomain.registrate.FDRegistrate;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(PoopSkyFilthDomain.MOD_ID)
public class PoopSkyFilthDomain {
    public static final String MOD_ID = "poopsky_filthdomain";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final FDRegistrate REGISTRATE = FDRegistrate.create(MOD_ID);

    public PoopSkyFilthDomain(IEventBus modEventBus) {
        FDItems.register(modEventBus);
        FDBlockTagGen.register();
        modEventBus.addListener(FDDataGenerators::gatherData);
    }

    public static FDRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
