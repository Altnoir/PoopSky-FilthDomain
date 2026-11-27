package com.altnoir.filthdomain.client;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.item.FlightHarnessFlight;
import com.altnoir.filthdomain.content.item.FlightOrientation;
import com.altnoir.filthdomain.network.FlightHarnessInputPayload;
import com.altnoir.filthdomain.network.FlightHarnessPosePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Objects;
import java.util.Optional;

@EventBusSubscriber(modid = FilthDomain.MOD_ID, value = Dist.CLIENT)
public final class FlightHarnessInput {
    private static final float MOUSE_DEGREES = 0.15F;
    private static final float ROLL_DEGREES_PER_TICK = 7.5F;
    private static final float ROLL_ACCELERATION = 2.5F;
    private static final int ROLL_RETURN_TICKS = 6;
    private static LocalPlayer pilot;
    private static ClientLevel pilotLevel;
    private static FlightOrientation orientation;
    private static FlightOrientation sentOrientation;
    private static int flightStartedAt;
    private static float rollStep;
    private static boolean jumpSent;
    private static boolean reverseSent;
    private static int refreshTicks;
    private static float levelingRoll;
    private static int levelingTicks;

    private FlightHarnessInput() {
    }

    @SubscribeEvent
    public static void onInput(MovementInputUpdateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || event.getEntity() != player) {
            return;
        }

        setPilot(player);
        var input = event.getInput();
        boolean equipped = FlightHarnessFlight.canThrust(player);
        boolean controlling = equipped && acceptsInput(minecraft);
        boolean thrusting = controlling && input.jumping;
        boolean reversing = thrusting && !player.onGround() && input.down;
        if (equipped && !player.onGround() && (thrusting || orientation != null)) {
            // A tick-driven takeoff starts where the preceding frame's leveling interval ended.
            ensureOrientation(player, 1.0F);
            int roll = controlling ? (input.right ? 1 : 0) - (input.left ? 1 : 0) : 0;
            rollStep = Mth.approach(rollStep, roll * ROLL_DEGREES_PER_TICK, ROLL_ACCELERATION);
            if (rollStep != 0.0F) {
                orientation = orientation.turn(0, 0, rollStep);
            }
            applyVanillaLook(player);
        } else if (orientation != null) {
            levelingRoll = orientation.viewAngles(player.getYRot()).roll();
            levelingTicks = ROLL_RETURN_TICKS;
            orientation = null;
            rollStep = 0.0F;
        } else if (levelingTicks > 0) {
            levelingTicks--;
        }
        if (thrusting || orientation != null) {
            input.forwardImpulse = 0.0F;
            input.leftImpulse = 0.0F;
        }
        if (thrusting) {
            input.jumping = false;
            FlightHarnessFlight.applyThrust(player, orientation, reversing);
        }

        if (thrusting != jumpSent || reversing != reverseSent || !Objects.equals(orientation, sentOrientation)
                || ((thrusting || orientation != null) && --refreshTicks <= 0)) {
            PacketDistributor.sendToServer(new FlightHarnessInputPayload(thrusting, reversing, Optional.ofNullable(orientation)));
            sentOrientation = orientation;
            jumpSent = thrusting;
            reverseSent = reversing;
            refreshTicks = FlightHarnessFlight.INPUT_REFRESH_TICKS;
        }
    }

    public static boolean turn(LocalPlayer player, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        setPilot(player);
        if (player.onGround() || (orientation == null && !minecraft.options.keyJump.isDown())
                || !FlightHarnessFlight.canThrust(player) || !acceptsInput(minecraft)) {
            return false;
        }
        ensureOrientation(player, minecraft.getTimer().getGameTimeDeltaPartialTick(false));
        if (mouseX != 0.0 || mouseY != 0.0) {
            orientation = orientation.turn((float) mouseX * MOUSE_DEGREES, (float) mouseY * MOUSE_DEGREES, 0);
            applyVanillaLook(player);
        }
        return true;
    }

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (pilot == null || event.getCamera().getEntity() != pilot) {
            return;
        }
        if (orientation != null) {
            var view = frameOrientation((float) event.getPartialTick()).viewAngles(pilot.getYRot());
            event.setYaw(view.yaw());
            event.setPitch(view.pitch());
            event.setRoll(view.roll());
        } else if (levelingTicks > 0) {
            event.setRoll(levelingRollAt((float) event.getPartialTick()));
        }
    }

    public static FlightOrientation renderOrientation(Player player, float partialTick) {
        if (!FlightHarnessFlight.canThrust(player)) {
            return null;
        }
        return player == pilot ? frameOrientation(partialTick) : FlightHarnessPosePayload.remoteOrientation(player, partialTick);
    }

    public static int renderFlightTicks(Player player) {
        if (!FlightHarnessFlight.canThrust(player)) {
            return 0;
        }
        if (player == pilot) {
            return orientation == null ? 0 : player.tickCount - flightStartedAt;
        }
        return FlightHarnessPosePayload.remoteFlightTicks(player);
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        setPilot(null);
    }

    private static boolean acceptsInput(Minecraft minecraft) {
        return minecraft.screen == null && minecraft.isWindowActive();
    }

    private static FlightOrientation frameOrientation(float partialTick) {
        if (orientation == null || rollStep == 0.0F) {
            return orientation;
        }
        // Blend the tick's keyboard roll without delaying the mouse's pitch and yaw.
        return orientation.turn(0, 0, (Mth.clamp(partialTick, 0, 1) - 1.0F) * rollStep);
    }

    private static float levelingRollAt(float partialTick) {
        float remaining = (levelingTicks - partialTick) / ROLL_RETURN_TICKS;
        return levelingRoll * Mth.clamp(remaining, 0, 1);
    }

    private static void ensureOrientation(LocalPlayer player, float levelingPartialTick) {
        if (orientation == null) {
            orientation = FlightOrientation.fromView(player.getYRot(), player.getXRot())
                    .turn(0.0F, 0.0F, levelingRollAt(levelingPartialTick));
            flightStartedAt = player.tickCount;
            levelingTicks = 0;
        }
    }

    private static void applyVanillaLook(LocalPlayer player) {
        // Vanilla interaction and movement packets still need a pitch and yaw representing the actual aim.
        var view = orientation.viewAngles(player.getYRot());
        float yawChange = view.yaw() - player.getYRot();
        float pitchChange = view.pitch() - player.getXRot();
        // Hand sway compares the view with these lagging angles. Rebase both samples when the quaternion changes.
        player.yBob += yawChange;
        player.yBobO += yawChange;
        player.xBob += pitchChange;
        player.xBobO += pitchChange;
        player.setYRot(view.yaw());
        player.setXRot(view.pitch());
        player.yRotO = view.yaw();
        player.xRotO = view.pitch();
    }

    private static void setPilot(LocalPlayer player) {
        ClientLevel level = Minecraft.getInstance().level;
        if (pilot == player && pilotLevel == level) {
            return;
        }
        pilot = player;
        pilotLevel = level;
        orientation = null;
        sentOrientation = null;
        rollStep = 0.0F;
        jumpSent = false;
        reverseSent = false;
        refreshTicks = 0;
        levelingRoll = 0.0F;
        levelingTicks = 0;
    }
}
