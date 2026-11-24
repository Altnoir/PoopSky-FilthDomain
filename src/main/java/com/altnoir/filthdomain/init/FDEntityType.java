package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.entity.BasiliskEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FDEntityType {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, FilthDomain.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<BasiliskEntity>> BASILISK =
            ENTITY_TYPES.register("basilisk", () -> EntityType.Builder
                    .of(BasiliskEntity::new, MobCategory.MONSTER)
                    .sized(0.9F, 0.7F)
                    .eyeHeight(0.5F)
                    .clientTrackingRange(8)
                    .build(FilthDomain.loc("basilisk").toString()));

    private FDEntityType() {
    }

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
