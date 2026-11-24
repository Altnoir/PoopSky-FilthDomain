package com.altnoir.filthdomain.content.block;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.block.entity.FilthDomainPortalBlockEntity;
import com.altnoir.filthdomain.tag.FDTags;
import com.altnoir.poopsky.init.PoBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

final class FilthDomainPortalDestination {
    private static final ResourceKey<Level> FILTH_DOMAIN = ResourceKey.create(
            Registries.DIMENSION, FilthDomain.loc("filthdomain"));
    private static final int SEARCH_RADIUS = 16;
    private static final int SEARCH_HEIGHT = 32;
    private static final int PLATFORM_SEARCH_RADIUS = 6;
    // Keep arrivals just beyond the activation area so the new door does not immediately charge again.
    private static final int EXIT_DISTANCE = FilthDomainPortalBlockEntity.HORIZONTAL_REACH + 1;

    private FilthDomainPortalDestination() {
    }

    @Nullable
    static DimensionTransition find(ServerLevel source, Entity entity, BlockPos sourcePos, FilthDomainPortalBlock block) {
        if (!(entity instanceof ServerPlayer player) || !player.isAlive() || player.isSpectator()
                || !block.isComplete(source, sourcePos)
                || !FilthDomainPortalBlockEntity.activationBounds(sourcePos).intersects(player.getBoundingBox())
                || !(source.getBlockEntity(sourcePos) instanceof FilthDomainPortalBlockEntity sourcePortal)) {
            return null;
        }

        GlobalPos link = sourcePortal.linkedPortal();
        ServerLevel destination = destinationLevel(source, link);
        if (destination == null || !source.getServer().isLevelEnabled(destination)
                || !player.canChangeDimensions(source, destination)) {
            return null;
        }

        GlobalPos sourceEndpoint = GlobalPos.of(source.dimension(), sourcePos);
        boolean hasDestinationLink = link != null && link.dimension().equals(destination.dimension());
        if (hasDestinationLink && destination.getWorldBorder().isWithinBounds(link.pos())
                && block.isComplete(destination, link.pos())
                && destination.getBlockEntity(link.pos()) instanceof FilthDomainPortalBlockEntity pairedPortal
                && (pairedPortal.linkedPortal() == null || pairedPortal.linkedPortal().equals(sourceEndpoint))) {
            BlockPos landing = findNaturalLanding(destination, player, link.pos());
            if (landing != null) {
                pairedPortal.linkTo(sourceEndpoint);
                return transition(destination, player, landing);
            }
        }

        BlockPos origin = hasDestinationLink ? destination.getWorldBorder().clampToBounds(link.pos())
                : scaledPosition(source, destination, sourcePos);
        PortalSite site = createDestination(destination, player, origin, block);
        if (site == null || !(destination.getBlockEntity(site.base()) instanceof FilthDomainPortalBlockEntity exit)) {
            return null;
        }

        exit.linkTo(sourceEndpoint);
        sourcePortal.linkTo(GlobalPos.of(destination.dimension(), site.base()));
        return transition(destination, player, site.landing());
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

    private static BlockPos scaledPosition(ServerLevel source, ServerLevel destination, BlockPos sourcePos) {
        double scale = DimensionType.getTeleportationScale(source.dimensionType(), destination.dimensionType());
        return destination.getWorldBorder().clampToBounds(
                (sourcePos.getX() + 0.5) * scale, sourcePos.getY(), (sourcePos.getZ() + 0.5) * scale);
    }

    private static DimensionTransition transition(ServerLevel destination, ServerPlayer player, BlockPos landing) {
        return new DimensionTransition(destination, Vec3.atBottomCenterOf(landing), Vec3.ZERO,
                player.getYRot(), player.getXRot(),
                DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)
                        .then(entity -> entity.portalProcess = null));
    }

    @Nullable
    private static PortalSite createDestination(ServerLevel level, ServerPlayer player, BlockPos origin,
                                                FilthDomainPortalBlock block) {
        int minY = level.getMinBuildHeight() + 1;
        int ceiling = level.dimensionType().hasCeiling()
                ? Math.min(level.getMaxBuildHeight(), level.getMinBuildHeight() + level.dimensionType().logicalHeight())
                : level.getMaxBuildHeight();
        int maxY = ceiling - FilthDomainPortalBlockEntity.HEIGHT;
        if (maxY < minY) {
            return null;
        }
        BlockPos center = new BlockPos(origin.getX(), Mth.clamp(origin.getY(), minY, maxY), origin.getZ());

        PortalSite natural = search(center, SEARCH_RADIUS, SEARCH_HEIGHT, minY, maxY, base -> {
            if (!level.isEmptyBlock(base) || !safeFloor(level, base.below()) || !block.hasPlacementSpace(level, base)) {
                return null;
            }
            return findNaturalLanding(level, player, base);
        });
        if (natural != null && block.placeStructure(level, natural.base())) {
            return natural;
        }

        int openSearchHeight = Math.max(center.getY() - minY, maxY - center.getY());
        PortalSite platform = search(center, PLATFORM_SEARCH_RADIUS, openSearchHeight, minY, maxY,
                base -> findPlatformLanding(level, base, false));
        if (platform == null) {
            // Like the vanilla fallback, make a small clearing when no existing cavity can hold a portal.
            platform = search(center, PLATFORM_SEARCH_RADIUS, SEARCH_HEIGHT, minY, maxY,
                    base -> findPlatformLanding(level, base, true));
        }
        if (platform == null) {
            return null;
        }

        buildPlatform(level, platform);
        return level.isEmptyBlock(platform.base()) && block.hasPlacementSpace(level, platform.base())
                && canStand(level, player, platform.landing())
                && block.placeStructure(level, platform.base()) ? platform : null;
    }

    @Nullable
    private static PortalSite search(BlockPos center, int radius, int height, int minY, int maxY,
                                     Function<BlockPos, BlockPos> landingFinder) {
        for (BlockPos column : BlockPos.spiralAround(center, radius, Direction.EAST, Direction.SOUTH)) {
            for (int step = 0; step <= height * 2; step++) {
                int offset = ((step + 1) / 2) * (step % 2 == 0 ? -1 : 1);
                int y = center.getY() + offset;
                if (y < minY || y > maxY) {
                    continue;
                }
                BlockPos base = new BlockPos(column.getX(), y, column.getZ());
                BlockPos landing = landingFinder.apply(base);
                if (landing != null) {
                    return new PortalSite(base, landing);
                }
            }
        }
        return null;
    }

    @Nullable
    private static BlockPos findNaturalLanding(ServerLevel level, ServerPlayer player, BlockPos base) {
        return findExit(base, feet -> canStand(level, player, feet));
    }

    @Nullable
    private static BlockPos findExit(BlockPos base, Predicate<BlockPos> passable) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (passable.test(base.relative(direction)) && passable.test(base.relative(direction, 2))) {
                BlockPos exit = base.relative(direction, EXIT_DISTANCE);
                if (passable.test(exit)) {
                    return exit;
                }
            }
        }
        return null;
    }

    private static boolean canStand(ServerLevel level, ServerPlayer player, BlockPos feet) {
        return level.getWorldBorder().isWithinBounds(feet) && level.isEmptyBlock(feet)
                && level.isEmptyBlock(feet.above()) && safeFloor(level, feet.below())
                && level.noCollision(player, EntityType.PLAYER.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(feet)));
    }

    private static boolean safeFloor(ServerLevel level, BlockPos floor) {
        BlockState state = level.getBlockState(floor);
        return state.getFluidState().isEmpty() && state.isFaceSturdy(level, floor, Direction.UP)
                && !EntityType.PLAYER.isBlockDangerous(state) && !state.is(Blocks.MAGMA_BLOCK) && !state.is(BlockTags.CAMPFIRES);
    }

    @Nullable
    private static BlockPos findPlatformLanding(ServerLevel level, BlockPos base, boolean carve) {
        if (!level.getWorldBorder().isWithinBounds(base.offset(-EXIT_DISTANCE, 0, -EXIT_DISTANCE))
                || !level.getWorldBorder().isWithinBounds(base.offset(EXIT_DISTANCE, 0, EXIT_DISTANCE))
                || !canClear(level, base, carve) || !canClear(level, base.above(), carve)
                || !canClear(level, base.above(2), carve)) {
            return null;
        }
        BlockPos landing = findExit(base, feet -> canClear(level, feet, carve) && canClear(level, feet.above(), carve));
        if (landing == null) {
            return null;
        }

        BlockPos floorMin = base.offset(-EXIT_DISTANCE, -1, -EXIT_DISTANCE);
        BlockPos floorMax = base.offset(EXIT_DISTANCE, -1, EXIT_DISTANCE);
        for (BlockPos floor : BlockPos.betweenClosed(floorMin, floorMax)) {
            BlockState state = level.getBlockState(floor);
            if (!safeFloor(level, floor) && (state.hasBlockEntity()
                    || !state.canBeReplaced() && !canClear(level, floor, carve))) {
                return null;
            }
        }
        AABB workArea = new AABB(floorMin.getX(), floorMin.getY(), floorMin.getZ(),
                floorMax.getX() + 1, base.getY() + FilthDomainPortalBlockEntity.HEIGHT, floorMax.getZ() + 1);
        return level.getEntitiesOfClass(LivingEntity.class, workArea,
                entity -> entity.isAlive() && !entity.isSpectator()).isEmpty() ? landing : null;
    }

    private static boolean canClear(ServerLevel level, BlockPos pos, boolean carve) {
        BlockState state = level.getBlockState(pos);
        return !state.hasBlockEntity() && state.getFluidState().isEmpty()
                && (state.canBeReplaced() || carve && (state.is(FDTags.Blocks.BASE_POOP)
                        || state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.BASE_STONE_NETHER)
                        || state.is(BlockTags.DIRT)));
    }

    private static void buildPlatform(ServerLevel level, PortalSite site) {
        BlockPos base = site.base();
        BlockState floorState = PoBlocks.POOP_BRICKS.get().defaultBlockState();
        for (BlockPos floor : BlockPos.betweenClosed(base.offset(-EXIT_DISTANCE, -1, -EXIT_DISTANCE),
                base.offset(EXIT_DISTANCE, -1, EXIT_DISTANCE))) {
            if (!safeFloor(level, floor)) {
                level.setBlock(floor, floorState, Block.UPDATE_ALL);
            }
        }
        for (BlockPos feet : BlockPos.betweenClosed(base, site.landing())) {
            clear(level, feet);
            clear(level, feet.above());
        }
        clear(level, base.above(2));
    }

    private static void clear(ServerLevel level, BlockPos pos) {
        if (!level.isEmptyBlock(pos)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    private record PortalSite(BlockPos base, BlockPos landing) {
    }
}
