package com.altnoir.filthdomain.datagen;

import com.altnoir.filthdomain.datagen.worldgen.FDWorldgen;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

public class FDDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(), new FDTagsProvider(event));
        event.getGenerator().addProvider(event.includeServer(),
                new FDWorldgen(event.getGenerator().getPackOutput(), event.getLookupProvider()));
        event.getGenerator().addProvider(event.includeServer(),
                new LootTableProvider(event.getGenerator().getPackOutput(), Set.of(),
                        List.of(new LootTableProvider.SubProviderEntry(FDEntityLootGen::new, LootContextParamSets.ENTITY)),
                        event.getLookupProvider()));
        event.getGenerator().addProvider(event.includeServer(),
                new FDRecipeProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
        event.getGenerator().addProvider(event.includeClient(),
                new FDSoundProvider(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(),
                new FDParticleProvider(event.getGenerator().getPackOutput()));
    }
}
