#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform float Progress;
uniform float Phase;
uniform float Strength;

in vec2 texCoord;
out vec4 fragColor;

vec2 frameUv(vec2 position, vec2 halfTexel) {
    // Mirror samples beyond the frame to avoid stretching the outermost pixels.
    vec2 reflected = 1.0 - abs(1.0 - mod(position, 2.0));
    return clamp(reflected, halfTexel, 1.0 - halfTexel);
}

float stretchDistance(float radius, float start, float width) {
    float distance = max(0.0, radius - start);
    float t = min(distance / width, 1.0);

    // Integrate the smooth wavefront so the sample radius stays monotonic through the stretch.
    return width * t * t * t * (1.0 - 0.5 * t) + max(0.0, distance - width);
}

void main() {
    vec2 centered = texCoord * 2.0 - 1.0;
    vec2 aspect = vec2(InSize.x / InSize.y, 1.0);
    vec2 screen = centered * aspect;

    // Measure in screen space so the wavefront stays circular on wide framebuffers.
    float radius = length(screen);
    float cornerRadius = length(aspect);
    float normalizedRadius = radius / cornerRadius;
    float inward = 1.0 - normalizedRadius;
    float front = 1.16 * Progress;
    float envelope = 1.0 - smoothstep(front - 0.16, front + 0.035, inward);
    float strength = Strength * smoothstep(0.0, 0.28, Progress);
    float effectStrength = strength * envelope;
    float amplitude = 0.016 * effectStrength;
    amplitude *= mix(1.0, 0.48, inward);

    if (amplitude <= 0.00001) {
        fragColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1.0);
        return;
    }

    vec2 radial = screen / max(radius, 0.001);
    vec2 tangent = vec2(-radial.y, radial.x);
    float ripple = sin(15.0 * inward - Phase * 68.0 + 1.2 * sin(3.5 * screen.y + Phase * 30.0));
    vec2 waves = vec2(sin(4.5 * screen.y + Phase * 48.0), cos(4.0 * screen.x - Phase * 42.0));
    // Advance the stretch for the full charge while leaving a small central area stable at full strength.
    float stretchStart = mix(0.965, 0.14, Progress);
    float stretch = 0.29 * strength * cornerRadius * stretchDistance(normalizedRadius, stretchStart, 0.195);
    vec2 stretchedUv = texCoord - 0.5 * radial * stretch / aspect;
    vec2 displaced = stretchedUv + (0.65 * waves + 0.55 * tangent * ripple) * amplitude / aspect;

    vec2 halfTexel = 0.5 / InSize;
    vec2 warpedUv = frameUv(displaced, halfTexel);
    vec2 maxUv = 1.0 - halfTexel;

    // Weight the color offset by distance to the frame edge without changing the circular wavefront.
    float edgeProximity = max(abs(centered.x), abs(centered.y));
    float edgeWeight = smoothstep(0.2, 1.0, edgeProximity);
    vec2 split = radial * (0.018 * effectStrength * edgeWeight) / aspect;

    // Clamp the individual color taps so mirroring does not fold red and blue onto the same edge pixels.
    float red = texture(DiffuseSampler, clamp(warpedUv + split, halfTexel, maxUv)).r;
    float green = texture(DiffuseSampler, warpedUv).g;
    float blue = texture(DiffuseSampler, clamp(warpedUv - split, halfTexel, maxUv)).b;
    fragColor = vec4(red, green, blue, 1.0);
}
