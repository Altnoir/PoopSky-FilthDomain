package com.altnoir.filthdomain.worldgen;

import com.altnoir.filthdomain.FilthDomain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = FilthDomain.MOD_ID)
public final class FilthDomainGravity {
    private static final ResourceLocation MODIFIER_ID = FilthDomain.loc("layer_gravity");
    private static final AttributeModifier THREE_QUARTERS = multiplier(0.75);
    private static final AttributeModifier HALF = multiplier(0.5);
    private static final AttributeModifier ONE_QUARTER = multiplier(0.25);
    private static final AttributeModifier ZERO = multiplier(0.0);

    private FilthDomainGravity() {
    }

    @SubscribeEvent
    public static void onJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity living) {
            update(living, event.getLevel());
        }
    }

    @SubscribeEvent
    public static void beforeEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity living) {
            update(living, living.level());
        }
    }

    private static void update(LivingEntity entity, Level level) {
        if (level.isClientSide()) {
            return;
        }
        var modifier = level.dimension().equals(FilthTerrainSettings.DIMENSION) ? gravityAt(entity.getY()) : null;
        if (modifier == null && !entity.getAttributes().hasModifier(Attributes.GRAVITY, MODIFIER_ID)) {
            return;
        }
        var gravity = entity.getAttribute(Attributes.GRAVITY);
        if (gravity == null) {
            return;
        }
        var current = gravity.getModifier(MODIFIER_ID);
        if (modifier != null && (current == null || current.amount() != modifier.amount())) {
            gravity.addOrUpdateTransientModifier(modifier);
        } else if (modifier == null && current != null) {
            gravity.removeModifier(MODIFIER_ID);
        }
    }

    @Nullable
    private static AttributeModifier gravityAt(double y) {
        if (y < -192) {
            return ZERO;
        }
        if (y < -128) {
            return ONE_QUARTER;
        }
        if (y < 0) {
            return HALF;
        }
        if (y < 128) {
            return THREE_QUARTERS;
        }
        return null;
    }

    private static AttributeModifier multiplier(double multiplier) {
        return new AttributeModifier(MODIFIER_ID, multiplier - 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
