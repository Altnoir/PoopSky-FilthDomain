package com.altnoir.filthdomain.mixin;

import com.altnoir.filthdomain.client.FlightHarnessInput;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidModel.class)
abstract class HumanoidModelMixin {
    @ModifyExpressionValue(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getFallFlyingTicks()I"))
    private int filthdomain$useElytraAnimation(int vanillaTicks, @Local(argsOnly = true) LivingEntity entity) {
        // Present the harness flight duration to the model only. Entity pose and glide physics stay independent.
        if (entity instanceof Player player) {
            return Math.max(vanillaTicks, FlightHarnessInput.renderFlightTicks(player));
        }
        return vanillaTicks;
    }
}
