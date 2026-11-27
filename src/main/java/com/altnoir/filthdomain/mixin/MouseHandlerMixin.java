package com.altnoir.filthdomain.mixin;

import com.altnoir.filthdomain.client.FlightHarnessInput;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
abstract class MouseHandlerMixin {
    @WrapWithCondition(method = "turnPlayer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private boolean filthdomain$useVanillaTurn(LocalPlayer player, double mouseX, double mouseY) {
        return !FlightHarnessInput.turn(player, mouseX, mouseY);
    }
}
