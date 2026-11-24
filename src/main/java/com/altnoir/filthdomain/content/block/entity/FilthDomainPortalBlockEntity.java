package com.altnoir.filthdomain.content.block.entity;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.block.FilthDomainPortalBlock;
import com.altnoir.filthdomain.init.FDBlockEntities;
import com.altnoir.filthdomain.init.FDBlocks;
import com.altnoir.filthdomain.init.FDParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = FilthDomain.MOD_ID)
public final class FilthDomainPortalBlockEntity extends BlockEntity {
    public static final int HORIZONTAL_REACH = 2;
    public static final int HEIGHT = 3;
    public static final float CIRCLE_OUTER_RADIUS = 2.30F;
    public static final float CIRCLE_INNER_RADIUS = 1.87F;

    private static final int SCAN_INTERVAL = 30;
    private static final float FADE_STEP = 1.0F / 20.0F;

    @Nullable
    private GlobalPos linkedPortal;
    private boolean active;
    private float previousOpacity;
    private float opacity;

    public FilthDomainPortalBlockEntity(BlockPos pos, BlockState state) {
        super(FDBlockEntities.PORTAL.get(), pos, state);
    }

    @SubscribeEvent
    public static void initializeExistingPortals(ChunkEvent.Load event) {
        if (event.getLevel().isClientSide() || !(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }
        Block block = FDBlocks.FILTHDOMAIN_PORTAL.get();
        // Portals placed before the effect was added have no saved block entity.
        chunk.findBlocks(state -> state.is(block)
                && state.getValue(FilthDomainPortalBlock.SECTION) == FilthDomainPortalBlock.Section.BOTTOM, (pos, state) -> {
            BlockPos base = pos.immutable();
            if (chunk.getBlockEntity(base) == null
                    && chunk.getBlockEntity(base, LevelChunk.EntityCreationType.IMMEDIATE) != null) {
                chunk.setUnsaved(true);
            }
        });
    }

    public static AABB activationBounds(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return new AABB(x - HORIZONTAL_REACH, y, z - HORIZONTAL_REACH,
                x + HORIZONTAL_REACH + 1, y + HEIGHT, z + HORIZONTAL_REACH + 1);
    }

    public boolean isActive() {
        return active;
    }

    public float opacity(float partialTick) {
        float progress = Mth.lerp(partialTick, previousOpacity, opacity);
        return progress * progress * (3.0F - 2.0F * progress);
    }

    @Nullable
    public GlobalPos linkedPortal() {
        return linkedPortal;
    }

    public void linkTo(GlobalPos destination) {
        if (!destination.equals(linkedPortal)) {
            linkedPortal = destination;
            setChanged();
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FilthDomainPortalBlockEntity portal) {
        ServerLevel server = (ServerLevel) level;
        var block = (FilthDomainPortalBlock) state.getBlock();
        AABB bounds = activationBounds(pos);
        if (Math.floorMod(level.getGameTime(), SCAN_INTERVAL) == Math.floorMod(pos.asLong(), SCAN_INTERVAL)) {
            boolean occupied = block.isComplete(level, pos) && !level.getEntitiesOfClass(LivingEntity.class, bounds,
                    entity -> entity.isAlive() && !entity.isSpectator()).isEmpty();
            if (portal.active != occupied) {
                portal.active = occupied;
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
            }
        }

        if (portal.active) {
            for (ServerPlayer player : server.players()) {
                if (player.isAlive() && !player.isSpectator() && player.canUsePortal(false)
                        && bounds.intersects(player.getBoundingBox())) {
                    markPlayerInside(block, server, player, pos);
                }
            }
        }
    }

    private static void markPlayerInside(FilthDomainPortalBlock block, ServerLevel level, ServerPlayer player, BlockPos pos) {
        if (player.portalProcess != null && player.portalProcess.isSamePortal(block)) {
            BlockPos previous = player.portalProcess.getEntryPosition();
            if (!previous.equals(pos)) {
                if (activationBounds(previous).intersects(player.getBoundingBox()) && block.isComplete(level, previous)) {
                    return;
                }
                player.portalProcess = null;
            }
        }
        player.setAsInsidePortal(block, pos);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, FilthDomainPortalBlockEntity portal) {
        portal.previousOpacity = portal.opacity;
        portal.opacity = Mth.clamp(portal.opacity + (portal.active ? FADE_STEP : -FADE_STEP), 0.0F, 1.0F);
        if (portal.opacity <= 0.0F || (level.getGameTime() & 1L) != 0L) {
            return;
        }

        RandomSource random = level.getRandom();
        int count = portal.opacity >= 0.65F ? 2 : 1;
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Mth.TWO_PI;
            double radius = random.nextBoolean() ? CIRCLE_OUTER_RADIUS : CIRCLE_INNER_RADIUS;
            level.addParticle(FDParticles.PORTAL_MOTE.get(),
                    pos.getX() + 0.5 + Math.cos(angle) * radius,
                    pos.getY() + 0.04,
                    pos.getZ() + 0.5 + Math.sin(angle) * radius,
                    (random.nextDouble() - 0.5) * 0.016, 0.025 + random.nextDouble() * 0.025,
                    (random.nextDouble() - 0.5) * 0.016);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("active", active);
        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (linkedPortal != null) {
            GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, linkedPortal).ifSuccess(value -> tag.put("linked_portal", value));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        active = tag.getBoolean("active");
        var link = tag.get("linked_portal");
        linkedPortal = link == null ? null : GlobalPos.CODEC.parse(NbtOps.INSTANCE, link).result().orElse(null);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
