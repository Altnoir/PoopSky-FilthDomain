package com.altnoir.filthdomain.datagen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.init.FDParticles;
import com.altnoir.poopsky.PoopSky;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

import java.util.List;
import java.util.Set;

public final class FDParticleProvider extends ParticleDescriptionProvider {
    public FDParticleProvider(PackOutput output) {
        // This particle uses a texture from the required PoopSky mod's resource pack.
        super(output, new ExistingFileHelper(List.of(), Set.of(PoopSky.MOD_ID, FilthDomain.MOD_ID), true, null, null));
    }

    @Override
    protected void addDescriptions() {
        spriteSet(FDParticles.FILTH_ASH.get(), PoopSky.loc("poop_particle"));
        spriteSet(FDParticles.PORTAL_MOTE.get(), FilthDomain.loc("portal_mote"));
    }
}
