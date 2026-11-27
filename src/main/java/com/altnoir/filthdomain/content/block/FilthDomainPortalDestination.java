package com.altnoir.filthdomain.content.block;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.block.entity.FilthDomainPortalBlockEntity;
import com.altnoir.poopsky.init.PoBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

final class FilthDomainPortalDestination {
    private static final ResourceKey<Level> FILTH_DOMAIN = ResourceKey.create(
            Registries.DIMENSION, FilthDomain.loc("filthdomain"));
    private static final int SEARCH_RADIUS = 16;
    private static final int PLATFORM_SEARCH_RADIUS = 6;
    private static final TicketType<BlockPos> PRELOAD_TICKET = TicketType.create(
            "filthdomain_portal_preload", Comparator.comparingLong(BlockPos::asLong), FilthDomainPortalBlock.CHARGE_TICKS + 40);
    // Keep arrivals just beyond the activation area so the new door does not immediately charge again.
    private static final int MIN_EXIT_DISTANCE = FilthDomainPortalBlockEntity.HORIZONTAL_REACH + 1;

    private FilthDomainPortalDestination() {
    }

    static void preload(ServerLevel source, BlockPos sourcePos, @Nullable GlobalPos link, LivingEntity traveler) {
        ServerLevel destination = destinationLevel(source, link);
        if (destination == null || !source.getServer().isLevelEnabled(destination)
                || !traveler.canChangeDimensions(source, destination)) {
            return;
        }

        boolean paired = link != null && link.dimension().equals(destination.dimension());
        BlockPos center = targetPosition(source, destination, sourcePos, link);
        int radius = LandingSpace.forEntity(traveler).platformReach() + (paired ? 0 : SEARCH_RADIUS);
        var chunks = destination.getChunkSource();
        // Full-status tickets let the chunk scheduler do the work during the charge, without waiting here.
        ChunkPos.rangeClosed(new ChunkPos(center.offset(-radius, 0, -radius)),
                new ChunkPos(center.offset(radius, 0, radius)))
                .forEach(chunk -> chunks.addRegionTicket(PRELOAD_TICKET, chunk, 0, sourcePos));
    }

    @Nullable
    static DimensionTransition find(ServerLevel source, Entity entity, BlockPos sourcePos, FilthDomainPortalBlock block) {
        if (!(entity instanceof LivingEntity traveler) || !traveler.isAlive() || traveler.isSpectator()
                || !block.isComplete(source, sourcePos)
                || !FilthDomainPortalBlockEntity.activationBounds(sourcePos).intersects(traveler.getBoundingBox())
                || !(source.getBlockEntity(sourcePos) instanceof FilthDomainPortalBlockEntity sourcePortal)) {
            return null;
        }

        GlobalPos link = sourcePortal.linkedPortal();
        ServerLevel destination = destinationLevel(source, link);
        if (destination == null || !source.getServer().isLevelEnabled(destination)
                || !traveler.canChangeDimensions(source, destination)) {
            return null;
        }

        LandingSpace space = LandingSpace.forEntity(traveler);
        GlobalPos sourceEndpoint = GlobalPos.of(source.dimension(), sourcePos);
        boolean hasDestinationLink = link != null && link.dimension().equals(destination.dimension());
        if (hasDestinationLink && destination.getWorldBorder().isWithinBounds(link.pos())
                && block.isComplete(destination, link.pos())
                && destination.getBlockEntity(link.pos()) instanceof FilthDomainPortalBlockEntity pairedPortal
                && (pairedPortal.linkedPortal() == null || pairedPortal.linkedPortal().equals(sourceEndpoint))) {
            BlockPos landing = findNaturalLanding(destination, traveler, link.pos(), space);
            if (landing != null) {
                pairedPortal.linkTo(sourceEndpoint);
                return transition(destination, traveler, landing);
            }
        }

        BlockPos origin = targetPosition(source, destination, sourcePos, link);
        PortalSite site = createDestination(destination, traveler, origin, block, space);
        if (site == null || !(destination.getBlockEntity(site.base()) instanceof FilthDomainPortalBlockEntity exit)) {
            return null;
        }

        exit.linkTo(sourceEndpoint);
        sourcePortal.linkTo(GlobalPos.of(destination.dimension(), site.base()));
        return transition(destination, traveler, site.landing());
    }

    @Nullable
    private static ServerLevel destinationLevel(ServerLevel source, @Nullable GlobalPos link) {
        if (!source.dimension().equals(FILTH_DOMAIN)) {
            return source.getServer().getLevel(FILTH_DOMAIN);
        }
        if (link != null && !link.dimension().equals(FILTH_DOMAIN)) {
            ServerLevel linkedLevel = source.getServer().getLevel(link.dimension());
            if (linkedLevel != null && source.getServer().isLevelEnabled(linkedLevel)) {
                return linkedLevel;
            }
        }
        return source.getServer().overworld();
    }

    private static BlockPos targetPosition(ServerLevel source, ServerLevel destination, BlockPos sourcePos,
                                           @Nullable GlobalPos link) {
        if (link != null && link.dimension().equals(destination.dimension())) {
            return destination.getWorldBorder().clampToBounds(link.pos());
        }
        double scale = DimensionType.getTeleportationScale(source.dimensionType(), destination.dimensionType());
        return destination.getWorldBorder().clampToBounds(
                (sourcePos.getX() + 0.5) * scale, sourcePos.getY(), (sourcePos.getZ() + 0.5) * scale);
    }

    private static DimensionTransition transition(ServerLevel destination, LivingEntity traveler, BlockPos landing) {
        return new DimensionTransition(destination, Vec3.atBottomCenterOf(landing), Vec3.ZERO,
                traveler.getYRot(), traveler.getXRot(),
                DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)
                        .then(entity -> {
                            entity.portalProcess = null;
                            if (entity instanceof ServerPlayer arrivedPlayer) {
                                arrivedPlayer.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 40));
                                arrivedPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60));
                            }
                        }));
    }

    @Nullable
    private static PortalSite createDestination(ServerLevel level, LivingEntity traveler, BlockPos origin,
                                                FilthDomainPortalBlock block, LandingSpace space) {
        int minY = level.getMinBuildHeight() + 1;
        int ceiling = level.dimensionType().hasCeiling()
                ? Math.min(level.getMaxBuildHeight(), level.getMinBuildHeight() + level.dimensionType().logicalHeight())
                : level.getMaxBuildHeight();
        int maxY = ceiling - Math.max(FilthDomainPortalBlockEntity.HEIGHT, Mth.ceil(space.dimensions().height()));
        if (maxY < minY) {
            return null;
        }
        for (BlockPos column : BlockPos.spiralAround(origin, SEARCH_RADIUS, Direction.EAST, Direction.SOUTH)) {
            if (!level.getWorldBorder().isWithinBounds(column)) {
                continue;
            }
            BlockPos base = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column);
            if (base.getY() < minY || base.getY() > maxY || !level.isEmptyBlock(base)
                    || !safeFloor(level, traveler, base.below()) || !block.hasPlacementSpace(level, base)) {
                continue;
            }
            BlockPos landing = findExit(base, space, feet -> canStand(level, traveler, feet, space)
                    && isSurfaceLanding(level, feet, space));
            if (landing != null && block.placeStructure(level, base)) {
                return new PortalSite(base, landing);
            }
        }

        int platformRadius = Math.min(SEARCH_RADIUS, Math.max(PLATFORM_SEARCH_RADIUS, space.platformReach()));
        for (BlockPos column : BlockPos.spiralAround(origin, platformRadius, Direction.EAST, Direction.SOUTH)) {
            PortalSite site = findSurfacePlatform(level, traveler, column, block, space, maxY);
            if (site != null) {
                buildPlatform(level, site.base(), space.platformReach());
                return block.placeStructure(level, site.base()) ? site : null;
            }
        }
        return null;
    }

    private static boolean isSurfaceLanding(ServerLevel level, BlockPos feet, LandingSpace space) {
        for (BlockPos floor : occupiedBlocks(footprintBelow(space.boundsAt(feet)))) {
            if (level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, floor.getX(), floor.getZ()) > feet.getY()) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static BlockPos findNaturalLanding(ServerLevel level, LivingEntity traveler, BlockPos base, LandingSpace space) {
        return findExit(base, space, feet -> canStand(level, traveler, feet, space));
    }

    @Nullable
    private static BlockPos findExit(BlockPos base, LandingSpace space, Predicate<BlockPos> passable) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int distance = space.approach();
            while (distance <= space.exit() && passable.test(base.relative(direction, distance))) {
                distance++;
            }
            if (distance > space.exit()) {
                return base.relative(direction, space.exit());
            }
        }
        return null;
    }

    private static boolean canStand(ServerLevel level, LivingEntity traveler, BlockPos feet, LandingSpace space) {
        AABB bounds = space.boundsAt(feet);
        if (!level.getWorldBorder().isWithinBounds(bounds) || bounds.maxY > level.getMaxBuildHeight()) {
            return false;
        }

        for (BlockPos pos : occupiedBlocks(bounds)) {
            if (!level.isEmptyBlock(pos)) {
                return false;
            }
        }
        for (BlockPos floor : occupiedBlocks(footprintBelow(bounds))) {
            if (!safeFloor(level, traveler, floor)) {
                return false;
            }
        }
        return level.noCollision(traveler, bounds);
    }

    private static AABB footprintBelow(AABB bounds) {
        return new AABB(bounds.minX, bounds.minY - 1, bounds.minZ, bounds.maxX, bounds.minY, bounds.maxZ);
    }

    private static Iterable<BlockPos> occupiedBlocks(AABB bounds) {
        return BlockPos.betweenClosed(Mth.floor(bounds.minX), Mth.floor(bounds.minY), Mth.floor(bounds.minZ),
                Mth.ceil(bounds.maxX) - 1, Mth.ceil(bounds.maxY) - 1, Mth.ceil(bounds.maxZ) - 1);
    }

    private static boolean safeFloor(ServerLevel level, LivingEntity traveler, BlockPos floor) {
        BlockState state = level.getBlockState(floor);
        return state.getFluidState().isEmpty() && state.isFaceSturdy(level, floor, Direction.UP)
                && !traveler.getType().isBlockDangerous(state) && !state.is(Blocks.MAGMA_BLOCK) && !state.is(BlockTags.CAMPFIRES);
    }

    @Nullable
    private static PortalSite findSurfacePlatform(ServerLevel level, LivingEntity traveler, BlockPos column,
                                                  FilthDomainPortalBlock block, LandingSpace space, int maxY) {
        int reach = space.platformReach();
        BlockPos min = column.offset(-reach, 0, -reach);
        BlockPos max = column.offset(reach, 0, reach);
        if (!level.getWorldBorder().isWithinBounds(min) || !level.getWorldBorder().isWithinBounds(max)) {
            return null;
        }

        // WORLD_SURFACE includes fluids and plants, so the platform never replaces the water or terrain below it.
        int floorY = level.getMinBuildHeight();
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            floorY = Math.max(floorY, level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()));
        }
        BlockPos base = column.atY(floorY + 1);
        if (base.getY() > maxY || !block.hasPlacementSpace(level, base)) {
            return null;
        }
        double height = Math.max(FilthDomainPortalBlockEntity.HEIGHT, space.dimensions().height());
        AABB workArea = new AABB(min.getX(), floorY, min.getZ(), max.getX() + 1, base.getY() + height, max.getZ() + 1);
        if (!level.getEntitiesOfClass(LivingEntity.class, workArea,
                entity -> entity.isAlive() && !entity.isSpectator()).isEmpty()) {
            return null;
        }
        BlockPos landing = findExit(base, space, feet -> level.noCollision(traveler, space.boundsAt(feet)));
        return landing == null ? null : new PortalSite(base, landing);
    }

    private static void buildPlatform(ServerLevel level, BlockPos base, int reach) {
        BlockState floorState = PoBlocks.POOP_BRICKS.get().defaultBlockState();
        for (BlockPos floor : BlockPos.betweenClosed(base.offset(-reach, -1, -reach), base.offset(reach, -1, reach))) {
            level.setBlock(floor, floorState, Block.UPDATE_ALL);
        }
    }

    private record LandingSpace(EntityDimensions dimensions, int approach, int exit, int platformReach) {
        static LandingSpace forEntity(LivingEntity traveler) {
            EntityDimensions dimensions = traveler.getDimensions(Pose.STANDING);
            int approach = Mth.ceil(0.5 + dimensions.width() / 2.0);
            int exit = Math.max(MIN_EXIT_DISTANCE, FilthDomainPortalBlockEntity.HORIZONTAL_REACH + approach);
            int extraFloor = Math.max(0, Mth.ceil(dimensions.width() / 2.0 - 0.5));
            return new LandingSpace(dimensions, approach, exit, exit + extraFloor);
        }

        AABB boundsAt(BlockPos feet) {
            return dimensions.makeBoundingBox(Vec3.atBottomCenterOf(feet));
        }
    }

    private record PortalSite(BlockPos base, BlockPos landing) {
    }
}
