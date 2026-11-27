package com.altnoir.filthdomain.network;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.item.FlightOrientation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public record FlightHarnessPosePayload(int entityId, Optional<FlightOrientation> orientation) implements CustomPacketPayload {
    private static final Type<FlightHarnessPosePayload> TYPE = new Type<>(FilthDomain.loc("flight_harness_pose"));
    private static final StreamCodec<RegistryFriendlyByteBuf, FlightHarnessPosePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, FlightHarnessPosePayload::entityId,
            ByteBufCodecs.optional(FlightOrientation.STREAM_CODEC), FlightHarnessPosePayload::orientation,
            FlightHarnessPosePayload::new);
    private static final Map<Player, RemotePose> CLIENT_POSES = new WeakHashMap<>();

    static void register(PayloadRegistrar registrar) {
        registrar.playToClient(TYPE, CODEC, (payload, context) -> {
            if (context.player().level().getEntity(payload.entityId()) instanceof Player player) {
                payload.orientation().map(FlightOrientation::validated).ifPresentOrElse(orientation -> {
                    FlightOrientation previous = remoteOrientation(player, 0);
                    int startedAt = previous == null ? player.tickCount : CLIENT_POSES.get(player).startedAt();
                    CLIENT_POSES.put(player, new RemotePose(previous == null ? orientation : previous,
                            orientation, player.tickCount, startedAt));
                }, () -> CLIENT_POSES.remove(player));
            }
        });
    }

    public static FlightOrientation remoteOrientation(Player player, float partialTick) {
        RemotePose pose = activePose(player);
        if (pose == null) {
            return null;
        }
        float progress = Mth.clamp((player.tickCount - pose.receivedAt() + partialTick) / 2.0F, 0, 1);
        return pose.previous().interpolate(pose.target(), progress);
    }

    public static int remoteFlightTicks(Player player) {
        RemotePose pose = activePose(player);
        return pose == null ? 0 : player.tickCount - pose.startedAt();
    }

    private static RemotePose activePose(Player player) {
        RemotePose pose = CLIENT_POSES.get(player);
        return pose != null && player.tickCount - pose.receivedAt() < 20 ? pose : null;
    }

    @Override
    public Type<FlightHarnessPosePayload> type() {
        return TYPE;
    }

    private record RemotePose(FlightOrientation previous, FlightOrientation target, int receivedAt, int startedAt) {
    }
}
