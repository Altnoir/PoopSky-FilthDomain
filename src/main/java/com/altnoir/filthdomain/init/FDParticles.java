package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FDParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, FilthDomain.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FILTH_ASH =
            PARTICLES.register("filth_ash", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PORTAL_MOTE =
            PARTICLES.register("portal_mote", () -> new SimpleParticleType(false));

    private FDParticles() {
    }

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
}
