package com.altnoir.filthdomain.mixin;

import com.altnoir.filthdomain.content.item.FlightHarnessFlight;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
abstract class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;clientIsFloating:Z"))
    private boolean filthdomain$checkFloating(boolean floating) {
        // Vanilla treats sustained ascent as floating, even when a mod supplies the propulsion.
        return floating && !FlightHarnessFlight.isThrusting(player);
    }
}
