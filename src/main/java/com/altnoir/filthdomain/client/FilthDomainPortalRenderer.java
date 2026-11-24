package com.altnoir.filthdomain.client;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.block.entity.FilthDomainPortalBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.AABB;

public final class FilthDomainPortalRenderer implements BlockEntityRenderer<FilthDomainPortalBlockEntity> {
    private static final RenderType CIRCLE = RenderType.entityTranslucentEmissive(
            FilthDomain.loc("textures/effect/portal_circle.png"));
    private static final float Y = 0.012F;

    public FilthDomainPortalRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FilthDomainPortalBlockEntity portal, float partialTick, PoseStack poses,
                       MultiBufferSource buffers, int light, int overlay) {
        int alpha = Math.round(portal.opacity(partialTick) * 220.0F);
        if (alpha == 0) {
            return;
        }

        VertexConsumer vertices = buffers.getBuffer(CIRCLE);
        PoseStack.Pose pose = poses.last();
        float min = -FilthDomainPortalBlockEntity.HORIZONTAL_REACH;
        float max = FilthDomainPortalBlockEntity.HORIZONTAL_REACH + 1;
        vertex(vertices, pose, min, min, 0.0F, 0.0F, alpha);
        vertex(vertices, pose, min, max, 0.0F, 1.0F, alpha);
        vertex(vertices, pose, max, max, 1.0F, 1.0F, alpha);
        vertex(vertices, pose, max, min, 1.0F, 0.0F, alpha);
    }

    private static void vertex(VertexConsumer vertices, PoseStack.Pose pose, float x, float z,
                               float u, float v, int alpha) {
        vertices.addVertex(pose, x, Y, z)
                .setColor(255, 255, 255, alpha)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public AABB getRenderBoundingBox(FilthDomainPortalBlockEntity portal) {
        return FilthDomainPortalBlockEntity.activationBounds(portal.getBlockPos());
    }
}
