package com.altnoir.filthdomain.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.function.Function;

public final class FilthCaveCarver extends CaveWorldCarver {
    private static final DeferredRegister<WorldCarver<?>> CARVERS =
            DeferredRegister.create(Registries.CARVER, FilthDomain.MOD_ID);
    private static final DeferredHolder<WorldCarver<?>, FilthCaveCarver> FILTH_CAVE =
            CARVERS.register("filth_cave", FilthCaveCarver::new);

    private FilthCaveCarver() {
        super(CaveCarverConfiguration.CODEC);
    }

    public static void register(IEventBus bus) {
        CARVERS.register(bus);
    }

    public static ConfiguredWorldCarver<CaveCarverConfiguration> configure(CaveCarverConfiguration config) {
        return FILTH_CAVE.get().configured(config);
    }

    @Override
    protected boolean carveBlock(CarvingContext context, CaveCarverConfiguration config, ChunkAccess chunk,
                                 Function<BlockPos, Holder<Biome>> biomes, CarvingMask mask,
                                 BlockPos.MutableBlockPos pos, BlockPos.MutableBlockPos scratch,
                                 Aquifer aquifer, MutableBoolean foundSurface) {
        // Dried poop in the mixed divider would otherwise be carvable.
        return pos.getY() > 0 && super.carveBlock(context, config, chunk, biomes, mask, pos, scratch, aquifer, foundSurface);
    }
}
