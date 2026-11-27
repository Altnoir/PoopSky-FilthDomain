package com.altnoir.filthdomain.content.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record FlightOrientation(float x, float y, float z, float w) {
    public static final StreamCodec<ByteBuf, FlightOrientation> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, FlightOrientation::x, ByteBufCodecs.FLOAT, FlightOrientation::y,
            ByteBufCodecs.FLOAT, FlightOrientation::z, ByteBufCodecs.FLOAT, FlightOrientation::w,
            FlightOrientation::new);

    public static FlightOrientation fromView(float yaw, float pitch) {
        return of(new Quaternionf().rotationYXZ(Mth.PI - yaw * Mth.DEG_TO_RAD, -pitch * Mth.DEG_TO_RAD, 0));
    }

    public FlightOrientation turn(float yaw, float pitch, float roll) {
        return of(rotation().rotateY(-yaw * Mth.DEG_TO_RAD).rotateX(-pitch * Mth.DEG_TO_RAD)
                .rotateZ(-roll * Mth.DEG_TO_RAD).normalize());
    }

    public Quaternionf rotation() {
        return new Quaternionf(x, y, z, w);
    }

    public FlightOrientation validated() {
        Quaternionf rotation = rotation();
        float lengthSquared = rotation.lengthSquared();
        if (!Float.isFinite(lengthSquared) || lengthSquared < 0.99F || lengthSquared > 1.01F) {
            return null;
        }
        return of(rotation.normalize());
    }

    public Vec3 forward() {
        return new Vec3(new Vector3f(0, 0, -1).rotate(rotation()));
    }

    public ViewAngles viewAngles(float fallbackYaw) {
        Vec3 forward = forward();
        double horizontal = forward.horizontalDistance();
        float yaw = horizontal < 1.0E-6 ? fallbackYaw
                : (float) Math.toDegrees(Math.atan2(-forward.x, forward.z));
        float pitch = (float) Math.toDegrees(Math.atan2(-forward.y, horizontal));
        // Extract roll relative to an upright camera that points in the same direction.
        Quaternionf relative = fromView(yaw, pitch).rotation().conjugate().mul(rotation());
        Vector3f up = new Vector3f(0, 1, 0).rotate(relative);
        return new ViewAngles(yaw, pitch, (float) Math.toDegrees(Math.atan2(up.x, up.y)));
    }

    public FlightOrientation interpolate(FlightOrientation target, float progress) {
        return of(rotation().slerp(target.rotation(), progress).normalize());
    }

    private static FlightOrientation of(Quaternionf rotation) {
        return new FlightOrientation(rotation.x, rotation.y, rotation.z, rotation.w);
    }

    public record ViewAngles(float yaw, float pitch, float roll) {
    }
}
