package com.altnoir.filthdomain.client;

import com.altnoir.filthdomain.FilthDomain;
import com.github.mcmodderanchor.simplebedrockmodel.v2.event.RegisterV2BedrockResourcesEvent;
import net.minecraft.resources.ResourceLocation;

public final class FDBedrockModelResources {
    public static final ResourceLocation BASILISK_MODEL = FilthDomain.loc("basilisk.geo");
    public static final ResourceLocation BASILISK_ANIMATION = FilthDomain.loc("basilisk.animation");

    private FDBedrockModelResources() {
    }

    public static void onRegisterV2BedrockResources(RegisterV2BedrockResourcesEvent event) {
        event.bakedModel(BASILISK_MODEL)
                .animation(BASILISK_ANIMATION)
                .register();
    }
}
