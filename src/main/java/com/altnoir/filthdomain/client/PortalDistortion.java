package com.altnoir.filthdomain.client;

import com.altnoir.filthdomain.FilthDomain;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

final class PortalDistortion {
    private static final ResourceLocation SHADER = FilthDomain.loc("shaders/post/portal_distortion.json");

    private volatile boolean invalidated;
    private boolean unavailable;
    private PostChain chain;
    private RenderTarget input;
    private int width;
    private int height;

    void invalidate() {
        invalidated = true;
    }

    void render(Minecraft minecraft, float progress, float phase, float strength) {
        RenderTarget target = minecraft.getMainRenderTarget();
        if (invalidated || input != target) {
            invalidated = false;
            if (chain != null) {
                chain.close();
                chain = null;
            }
            input = target;
            unavailable = false;
            width = 0;
            height = 0;
        }
        if (unavailable || target.width <= 0 || target.height <= 0) {
            return;
        }

        try {
            if (chain == null) {
                chain = new PostChain(minecraft.getTextureManager(), minecraft.getResourceManager(), target, SHADER);
            }
            if (width != target.width || height != target.height) {
                chain.resize(target.width, target.height);
                width = target.width;
                height = target.height;
            }

            chain.setUniform("Progress", progress);
            chain.setUniform("Phase", phase);
            chain.setUniform("Strength", strength);
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            chain.process(0.0F);
        } catch (IOException | JsonSyntaxException exception) {
            unavailable = true;
            FilthDomain.LOGGER.warn("Could not load the Filth Domain portal shader", exception);
        } finally {
            target.bindWrite(true);
            RenderSystem.enableDepthTest();
        }
    }
}
