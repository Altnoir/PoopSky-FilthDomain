package com.altnoir.filthdomain.client;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.block.FilthDomainPortalBlock;
import com.altnoir.filthdomain.content.block.entity.FilthDomainPortalBlockEntity;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = FilthDomain.MOD_ID, value = Dist.CLIENT)
public final class PortalViewEffect {
    private static final float FADE_IN_STEP = 1.0F / FilthDomainPortalBlock.CHARGE_TICKS;
    private static final float FADE_OUT_STEP = 1.0F / 16.0F;
    private static final double ZOOM_RATE_PER_SECOND = 0.04;
    private static final double ZOOM_RECOVERY_SECONDS = 0.8;
    private static final int PHASE_PERIOD = 2400;
    private static final PortalDistortion DISTORTION = new PortalDistortion();

    private static LocalPlayer effectPlayer;
    private static ClientLevel effectLevel;
    private static float previousProgress;
    private static float progress;
    private static long previousFrameNanos;
    private static boolean zooming;
    private static double zoomSeconds;
    private static double zoomRecoveryPerSecond;

    private PortalViewEffect() {
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        ClientLevel level = minecraft.level;
        if (effectPlayer != player || effectLevel != level) {
            effectPlayer = player;
            effectLevel = level;
            previousProgress = 0.0F;
            progress = 0.0F;
            zooming = false;
            zoomSeconds = 0.0;
            zoomRecoveryPerSecond = 0.0;
        }
        if (player == null || level == null || minecraft.isPaused()) {
            return;
        }

        previousProgress = progress;
        boolean affected = player.isAlive() && !player.isSpectator() && isInsideActivePortal(level, player.getBoundingBox());
        progress = Mth.clamp(progress + (affected ? FADE_IN_STEP : -FADE_OUT_STEP), 0.0F, 1.0F);
        if (zooming && !affected) {
            zoomRecoveryPerSecond = zoomSeconds / ZOOM_RECOVERY_SECONDS;
        }
        zooming = affected;
    }

    @SubscribeEvent
    public static void updateZoom(RenderFrameEvent.Pre event) {
        long now = Util.getNanos();
        double frameSeconds = previousFrameNanos == 0L ? 0.0 : (now - previousFrameNanos) * 1.0E-9;
        previousFrameNanos = now;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.isPaused() || !isEffectCamera(minecraft)) {
            return;
        }

        // Keep zooming if the old world is still being rendered while a game tick is delayed.
        zoomSeconds = zooming ? zoomSeconds + frameSeconds
                : Math.max(0.0, zoomSeconds - zoomRecoveryPerSecond * frameSeconds);
    }

    static void invalidateShader() {
        DISTORTION.invalidate();
    }

    @SubscribeEvent
    public static void narrowFov(ViewportEvent.ComputeFov event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!event.usedConfiguredFov() || event.getCamera() != minecraft.gameRenderer.getMainCamera()
                || !isEffectCamera(minecraft)) {
            return;
        }

        if (zoomSeconds <= 0.0) {
            return;
        }

        // Scaling tan(FOV / 2) gives a constant ratio of screen magnification per second.
        double lensScale = Math.exp(-zoomSeconds * ZOOM_RATE_PER_SECOND * minecraft.options.fovEffectScale().get());
        double halfFov = Math.toRadians(event.getFOV()) * 0.5;
        event.setFOV(Math.toDegrees(2.0 * Math.atan(Math.tan(halfFov) * lensScale)));
    }

    @SubscribeEvent
    public static void beforeGui(RenderGuiEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        float amount = Mth.lerp(partialTick, previousProgress, progress);
        float strength = minecraft.options.screenEffectScale().get().floatValue();
        if (level == null || amount <= 0.0F || strength <= 0.0F || !isEffectCamera(minecraft)) {
            return;
        }

        float phase = (Math.floorMod(level.getGameTime(), PHASE_PERIOD) + partialTick) * (Mth.TWO_PI / PHASE_PERIOD);
        event.getGuiGraphics().flush();
        DISTORTION.render(minecraft, amount, phase, strength * strength);
    }

    private static boolean isEffectCamera(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        return player != null && minecraft.level != null && player == effectPlayer && minecraft.level == effectLevel
                && minecraft.getCameraEntity() == player;
    }

    private static boolean isInsideActivePortal(ClientLevel level, AABB bounds) {
        int reach = FilthDomainPortalBlockEntity.HORIZONTAL_REACH;
        BlockPos min = BlockPos.containing(bounds.minX - reach,
                bounds.minY - FilthDomainPortalBlockEntity.HEIGHT + 1, bounds.minZ - reach);
        BlockPos max = BlockPos.containing(bounds.maxX + reach, bounds.maxY, bounds.maxZ + reach);
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            if (level.hasChunkAt(pos) && level.getBlockEntity(pos) instanceof FilthDomainPortalBlockEntity portal
                    && portal.isActive() && FilthDomainPortalBlockEntity.activationBounds(pos).intersects(bounds)) {
                return true;
            }
        }
        return false;
    }
}
