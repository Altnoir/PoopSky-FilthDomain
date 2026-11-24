package com.altnoir.filthdomain.datagen;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.command.AltarPortalCommand;
import com.altnoir.filthdomain.init.FDBlocks;
import com.altnoir.filthdomain.init.FDItems;
import com.altnoir.poopsky.content.recipe.AltarRecipeBuilder;
import com.altnoir.poopsky.init.PoBlocks;
import com.altnoir.poopsky.init.PoFluids;
import com.altnoir.poopsky.init.PoItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

import java.util.concurrent.CompletableFuture;

public final class FDRecipeProvider extends RecipeProvider {
    public FDRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FDItems.FILTH_DOMAIN_SHARD)
                .pattern("pp")
                .pattern("pp")
                .define('p', PoItems.POOP)
                .unlockedBy("has_poop", has(PoItems.POOP))
                .save(output, FDItems.FILTH_DOMAIN_SHARD.getId());

        AltarRecipeBuilder.altar(FDItems.FILTH_DOMAIN_SHARD, FDBlocks.FILTHDOMAIN_PORTAL)
                .pedestals(PoBlocks.POOP_BRICKS, PoItems.UREA, PoBlocks.POOP_BRICKS, PoItems.UREA,
                        PoBlocks.POOP_BRICKS, PoItems.UREA, PoBlocks.POOP_BRICKS, PoItems.UREA)
                .fluid(PoFluids.URINE.get(), 4000)
                .duration(400)
                .command(AltarPortalCommand.NAME)
                .description("jei.poopsky_filthdomain.altar.description.portal")
                .save(output, FilthDomain.loc("ominous_altar/filthdomain_portal"));
    }
}
