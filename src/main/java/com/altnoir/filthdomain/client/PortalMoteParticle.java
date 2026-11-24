package com.altnoir.filthdomain.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

final class PortalMoteParticle extends TextureSheetParticle {
    private PortalMoteParticle(ClientLevel level, double x, double y, double z,
                               double dx, double dy, double dz, SpriteSet sprites) {
        super(level, x, y, z);
        xd = dx;
        yd = dy;
        zd = dz;
        friction = 0.985F;
        hasPhysics = false;
        lifetime = 24 + random.nextInt(19);
        quadSize = 0.045F + random.nextFloat() * 0.025F;
        if (random.nextInt(4) == 0) {
            setColor(0.65F, 0.74F, 0.30F);
        } else {
            setColor(1.0F, 0.72F, 0.28F);
        }
        setAlpha(0.15F);
        pickSprite(sprites);
    }

    static ParticleProvider<SimpleParticleType> provider(SpriteSet sprites) {
        return (type, level, x, y, z, dx, dy, dz) -> new PortalMoteParticle(level, x, y, z, dx, dy, dz, sprites);
    }

    @Override
    public void tick() {
        super.tick();
        setAlpha(0.75F * Mth.clamp(Math.min(age / 4.0F, (lifetime - age) / 10.0F), 0.0F, 1.0F));
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
