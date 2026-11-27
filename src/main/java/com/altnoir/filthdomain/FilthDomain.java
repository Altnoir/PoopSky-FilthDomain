package com.altnoir.filthdomain;

import com.altnoir.abysslib.AbyssLib;
import com.altnoir.abysslib.reginth.Reginth;
import com.altnoir.filthdomain.content.entity.BasiliskEntity;
import com.altnoir.filthdomain.datagen.FDDataGenerators;
import com.altnoir.filthdomain.datagen.FDLangGen;
import com.altnoir.filthdomain.init.FDBlockEntities;
import com.altnoir.filthdomain.init.FDBlocks;
import com.altnoir.filthdomain.init.FDEntityType;
import com.altnoir.filthdomain.init.FDItemGroups;
import com.altnoir.filthdomain.init.FDItems;
import com.altnoir.filthdomain.init.FDParticles;
import com.altnoir.filthdomain.init.FDSoundEvents;
import com.altnoir.filthdomain.network.FlightHarnessInputPayload;
import com.altnoir.filthdomain.worldgen.FilthCaveCarver;
import com.altnoir.filthdomain.worldgen.UpperTerrainPlacement;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.slf4j.Logger;

@Mod(FilthDomain.MOD_ID)
public class FilthDomain {
    public static final String MOD_ID = "poopsky_filthdomain";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final Reginth REGINTH = Reginth.create(MOD_ID);

    public FilthDomain(IEventBus modEventBus) {
        FDItems.register(modEventBus);
        FDBlocks.register();
        FDBlockEntities.register(modEventBus);
        FDItemGroups.register();
        FDLangGen.register();
        FDEntityType.register(modEventBus);
        FDParticles.register(modEventBus);
        FDSoundEvents.register(modEventBus);
        UpperTerrainPlacement.register(modEventBus);
        FilthCaveCarver.register(modEventBus);
        modEventBus.addListener(FDDataGenerators::gatherData);
        modEventBus.addListener(FilthDomain::registerEntityAttributes);
        modEventBus.addListener(FilthDomain::registerSpawnPlacements);
        modEventBus.addListener(FlightHarnessInputPayload::register);
    }

    public static Reginth registrate() {
        return REGINTH;
    }

    public static ResourceLocation loc(String path) {
        return AbyssLib.modloc(MOD_ID, path);
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(FDEntityType.BASILISK.get(), BasiliskEntity.createAttributes().build());
    }

    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(FDEntityType.BASILISK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                FilthDomain::checkBasiliskSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private static boolean checkBasiliskSpawnRules(EntityType<BasiliskEntity> type, ServerLevelAccessor level,
                                                   MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (spawnType != MobSpawnType.NATURAL) {
            return true;
        }
        return level.getDifficulty() != Difficulty.PEACEFUL
                && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)
                && level.getRawBrightness(pos, 0) < 8;
    }
}
