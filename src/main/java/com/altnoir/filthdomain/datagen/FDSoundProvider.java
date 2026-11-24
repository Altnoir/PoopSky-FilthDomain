package com.altnoir.filthdomain.datagen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.init.FDSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public final class FDSoundProvider extends SoundDefinitionsProvider {
    public FDSoundProvider(PackOutput output, ExistingFileHelper existingFiles) {
        super(output, FilthDomain.MOD_ID, existingFiles);
    }

    @Override
    public void registerSounds() {
        add(FDSoundEvents.ENTITY_BASILISK_AMBIENT,
                frogSound(SoundEvents.FROG_AMBIENT, "subtitle.poopsky_filthdomain.basilisk.ambient"));
        add(FDSoundEvents.ENTITY_BASILISK_HURT,
                frogSound(SoundEvents.FROG_HURT, "subtitle.poopsky_filthdomain.basilisk.hurt"));
        add(FDSoundEvents.ENTITY_BASILISK_DEATH,
                frogSound(SoundEvents.FROG_DEATH, "subtitle.poopsky_filthdomain.basilisk.death"));
        add(FDSoundEvents.ENTITY_BASILISK_STEP,
                frogSound(SoundEvents.FROG_STEP, "subtitles.block.generic.footsteps"));
    }

    private static SoundDefinition frogSound(SoundEvent event, String subtitle) {
        return definition().subtitle(subtitle).with(sound(event.getLocation(), SoundDefinition.SoundType.EVENT));
    }
}
