package com.altnoir.filthdomain.init;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.block.entity.FilthDomainPortalBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FDBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FilthDomain.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FilthDomainPortalBlockEntity>> PORTAL =
            TYPES.register("filthdomain_portal", () -> BlockEntityType.Builder
                    .of(FilthDomainPortalBlockEntity::new, FDBlocks.FILTHDOMAIN_PORTAL.get())
                    .build(null));

    private FDBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
    }
}
