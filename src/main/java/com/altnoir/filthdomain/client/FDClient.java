package com.altnoir.filthdomain.client;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.init.FDBlockEntities;
import com.altnoir.filthdomain.init.FDEntityType;
import com.altnoir.filthdomain.init.FDParticles;
import net.minecraft.client.particle.WhiteAshParticle;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@Mod(value = FilthDomain.MOD_ID, dist = Dist.CLIENT)
public final class FDClient {
    public FDClient(IEventBus modEventBus) {
        modEventBus.addListener(FDBedrockModelResources::onRegisterV2BedrockResources);
        modEventBus.addListener(FDClient::registerRenderers);
        modEventBus.addListener(FDClient::registerParticles);
        modEventBus.addListener(FDClient::registerReloadListeners);
    }

    private static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) resources -> PortalViewEffect.invalidateShader());
    }

    private static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(FDParticles.FILTH_ASH.get(), WhiteAshParticle.Provider::new);
        event.registerSpriteSet(FDParticles.PORTAL_MOTE.get(), PortalMoteParticle::provider);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FDEntityType.BASILISK.get(), BasiliskRenderer::new);
        event.registerBlockEntityRenderer(FDBlockEntities.PORTAL.get(), FilthDomainPortalRenderer::new);
    }
}
