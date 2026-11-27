package com.altnoir.filthdomain.mixin;

import com.altnoir.filthdomain.client.FlightHarnessInput;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin {
    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFFF)V"))
    private void filthdomain$rotateFlyingPlayer(LivingEntityRenderer<?, ?> renderer, LivingEntity entity, PoseStack poses,
                                                float bob, float bodyYaw, float partialTick, float scale, Operation<Void> original) {
        if (entity instanceof Player player) {
            var orientation = FlightHarnessInput.renderOrientation(player, partialTick);
            if (orientation != null) {
                float pivot = player.getBbHeight() * 0.5F;
                float flightTicks = FlightHarnessInput.renderFlightTicks(player) + partialTick;
                // Use the elytra's takeoff easing, with a target that also includes the harness roll.
                float blend = Mth.clamp(flightTicks * flightTicks / 100.0F, 0.0F, 1.0F);
                var upright = Axis.YP.rotationDegrees(180.0F - bodyYaw);
                var flying = orientation.rotation().rotateX(-Mth.HALF_PI);
                poses.translate(0, pivot, 0);
                poses.mulPose(upright.slerp(flying, blend));
                poses.translate(0, -pivot, 0);
                return;
            }
        }
        original.call(renderer, entity, poses, bob, bodyYaw, partialTick, scale);
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    private void filthdomain$alignFlyingPlayerModel(EntityModel<?> model, Entity entity, float limbSwing, float limbAmount,
                                                    float age, float headYaw, float headPitch, Operation<Void> original,
                                                    @Local(argsOnly = true, ordinal = 1) float partialTick) {
        if (entity instanceof Player player && FlightHarnessInput.renderOrientation(player, partialTick) != null) {
            original.call(model, entity, limbSwing, limbAmount, age, 0.0F, 0.0F);
        } else {
            original.call(model, entity, limbSwing, limbAmount, age, headYaw, headPitch);
        }
    }
}
