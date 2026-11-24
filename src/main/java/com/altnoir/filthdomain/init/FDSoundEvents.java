package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FDSoundEvents {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, FilthDomain.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BASILISK_AMBIENT =
            register("entity.basilisk.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BASILISK_HURT =
            register("entity.basilisk.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BASILISK_DEATH =
            register("entity.basilisk.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BASILISK_STEP =
            register("entity.basilisk.step");

    private FDSoundEvents() {
    }

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(FilthDomain.loc(name)));
    }

    public static void register(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }
}
