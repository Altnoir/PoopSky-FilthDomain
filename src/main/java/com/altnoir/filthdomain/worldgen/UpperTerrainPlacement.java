package com.altnoir.filthdomain.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class UpperTerrainPlacement extends PlacementFilter {
    public static final UpperTerrainPlacement INSTANCE = new UpperTerrainPlacement();
    private static final MapCodec<UpperTerrainPlacement> CODEC = MapCodec.unit(INSTANCE);
    private static final DeferredRegister<PlacementModifierType<?>> TYPES =
            DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, FilthDomain.MOD_ID);
    private static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<UpperTerrainPlacement>> TYPE =
            TYPES.register("upper_terrain", () -> () -> CODEC);

    private UpperTerrainPlacement() {
    }

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        return pos.getY() >= 0;
    }

    @Override
    public PlacementModifierType<?> type() {
        return TYPE.get();
    }
}
