package com.altnoir.filthdomain.network;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.item.FlightHarnessFlight;
import com.altnoir.filthdomain.content.item.FlightOrientation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.Optional;

public record FlightHarnessInputPayload(boolean jumpHeld, boolean reversing, Optional<FlightOrientation> orientation) implements CustomPacketPayload {
    private static final Type<FlightHarnessInputPayload> TYPE =
            new Type<>(FilthDomain.loc("flight_harness_input"));
    private static final StreamCodec<RegistryFriendlyByteBuf, FlightHarnessInputPayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.BOOL, FlightHarnessInputPayload::jumpHeld,
                    ByteBufCodecs.BOOL, FlightHarnessInputPayload::reversing,
                    ByteBufCodecs.optional(FlightOrientation.STREAM_CODEC), FlightHarnessInputPayload::orientation,
                    FlightHarnessInputPayload::new);

    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("3");
        registrar.playToServer(TYPE, CODEC, (payload, context) -> {
            if (context.player() instanceof ServerPlayer player) {
                var orientation = payload.orientation().map(FlightOrientation::validated);
                if (payload.orientation().isEmpty() || orientation.isPresent()) {
                    FlightHarnessFlight.setInput(player, payload.jumpHeld(), payload.reversing(), orientation.orElse(null));
                }
            }
        });
        FlightHarnessPosePayload.register(registrar);
    }

    @Override
    public Type<FlightHarnessInputPayload> type() {
        return TYPE;
    }
}
