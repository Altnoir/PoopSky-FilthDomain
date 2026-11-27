package com.altnoir.filthdomain.content.item;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.init.FDItems;
import com.altnoir.filthdomain.network.FlightHarnessPosePayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = FilthDomain.MOD_ID)
public final class FlightHarnessFlight {
    public static final int INPUT_REFRESH_TICKS = 5;
    private static final int INPUT_TIMEOUT_TICKS = 20;
    private static final double LAUNCH_SPEED = 0.62;
    private static final double THRUST_PER_TICK = 0.045;
    private static final double MOMENTUM_RETENTION = 0.96;
    private static final double MAX_THRUST_SPEED = 2.55;
    private static final double FORWARD_RETENTION = 0.985;
    private static final double VANILLA_HORIZONTAL_RETENTION = 0.91F;
    private static final double VANILLA_VERTICAL_RETENTION = 0.98F;
    private static final Map<ServerPlayer, HeldInput> HELD_INPUTS = new WeakHashMap<>();

    private FlightHarnessFlight() {
    }

    public static boolean canThrust(Player player) {
        return player.isAlive() && !player.isSpectator() && !player.isPassenger()
                && !player.isSleeping() && !player.getAbilities().flying
                && player.getItemBySlot(EquipmentSlot.CHEST).is(FDItems.MIASMA_HARNESS.get());
    }

    public static void setInput(ServerPlayer player, boolean held, boolean reversing, FlightOrientation orientation) {
        if ((held || orientation != null) && canThrust(player)) {
            HeldInput input = HELD_INPUTS.get(player);
            if (input == null || !input.dimension.equals(player.level().dimension())) {
                input = new HeldInput(player.level().dimension());
                HELD_INPUTS.put(player, input);
            }
            input.expiresAt = player.getServer().getTickCount() + INPUT_TIMEOUT_TICKS;
            input.held = held;
            input.reversing = reversing;
            input.orientation = orientation;
        } else {
            clearInput(player);
        }
    }

    public static boolean isThrusting(ServerPlayer player) {
        HeldInput input = activeInput(player);
        return input != null && input.held;
    }

    private static HeldInput activeInput(ServerPlayer player) {
        HeldInput input = HELD_INPUTS.get(player);
        if (input == null) {
            return null;
        }
        if (!canThrust(player) || !input.dimension.equals(player.level().dimension())
                || (!input.held && player.onGround()) || player.getServer().getTickCount() >= input.expiresAt) {
            clearInput(player);
            return null;
        }
        return input;
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer player && activeInput(player) instanceof HeldInput input) {
            if (input.held) {
                applyThrust(player, input.orientation, input.reversing);
            }
            int tick = player.getServer().getTickCount();
            if (!Objects.equals(input.orientation, input.sentOrientation)
                    || (input.orientation != null && tick >= input.nextPoseTick)) {
                syncPose(player, input.orientation);
                input.sentOrientation = input.orientation;
                input.nextPoseTick = tick + INPUT_REFRESH_TICKS;
            }
        }
    }

    public static void applyThrust(Player player, FlightOrientation orientation, boolean reversing) {
        if (player.onGround()) {
            player.setDeltaMovement(0.0, LAUNCH_SPEED, 0.0);
        } else {
            Vec3 direction = orientation == null ? player.getLookAngle() : orientation.forward();
            if (reversing) {
                direction = direction.scale(-1.0);
            }
            Vec3 momentum = flightMomentum(player, direction);
            Vec3 propelled = momentum.add(direction.scale(THRUST_PER_TICK));
            double speedLimitSquared = Math.max(MAX_THRUST_SPEED * MAX_THRUST_SPEED, momentum.lengthSqr());
            if (propelled.lengthSqr() > speedLimitSquared) {
                propelled = propelled.scale(Math.sqrt(speedLimitSquared / propelled.lengthSqr()));
            }
            player.setDeltaMovement(propelled);
        }
        player.resetFallDistance();
    }

    private static Vec3 flightMomentum(Player player, Vec3 direction) {
        Vec3 motion = player.getDeltaMovement();
        if (player.isInFluidType() || player.isFallFlying() || player.hasEffect(MobEffects.LEVITATION)) {
            return motion.scale(MOMENTUM_RETENTION);
        }
        boolean discardFriction = player.shouldDiscardFriction();
        if (player.isControlledByLocalInstance()) {
            double gravity = player.getGravity();
            if (motion.y <= 0.0 && player.hasEffect(MobEffects.SLOW_FALLING)) {
                gravity = Math.min(gravity, 0.01);
            }
            // Local travel stores its drag and gravity in the velocity after moving.
            if (!discardFriction) {
                motion = motion.multiply(1.0 / VANILLA_HORIZONTAL_RETENTION, 1.0 / VANILLA_VERTICAL_RETENTION,
                        1.0 / VANILLA_HORIZONTAL_RETENTION);
            }
            motion = motion.add(0.0, gravity, 0.0);
        }
        // Favor momentum along the thrust axis so a sustained run can reach the flight speed limit.
        Vec3 forward = direction.scale(Math.max(0.0, motion.dot(direction)));
        Vec3 drift = motion.subtract(forward).scale(MOMENTUM_RETENTION);
        if (!discardFriction) {
            drift = drift.multiply(VANILLA_HORIZONTAL_RETENTION, VANILLA_VERTICAL_RETENTION, VANILLA_HORIZONTAL_RETENTION);
        }
        return forward.scale(FORWARD_RETENTION).add(drift);
    }

    private static void clearInput(ServerPlayer player) {
        HeldInput previous = HELD_INPUTS.remove(player);
        if (previous != null && previous.sentOrientation != null && previous.dimension.equals(player.level().dimension())) {
            syncPose(player, null);
        }
    }

    private static void syncPose(ServerPlayer player, FlightOrientation orientation) {
        PacketDistributor.sendToPlayersTrackingEntity(player,
                new FlightHarnessPosePayload(player.getId(), Optional.ofNullable(orientation)));
    }

    private static final class HeldInput {
        private final ResourceKey<Level> dimension;
        private int expiresAt;
        private int nextPoseTick;
        private boolean held;
        private boolean reversing;
        private FlightOrientation orientation;
        private FlightOrientation sentOrientation;

        private HeldInput(ResourceKey<Level> dimension) {
            this.dimension = dimension;
        }
    }
}
