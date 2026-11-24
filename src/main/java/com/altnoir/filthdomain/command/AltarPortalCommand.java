package com.altnoir.filthdomain.command;

import com.altnoir.filthdomain.FilthDomain;
import com.altnoir.filthdomain.content.block.FilthDomainPortalBlock;
import com.altnoir.filthdomain.init.FDBlocks;
import com.altnoir.poopsky.content.block.entity.OminousAltarBlockEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = FilthDomain.MOD_ID)
public final class AltarPortalCommand {
    public static final String NAME = "poopsky_filthdomain:replace_altar";

    private AltarPortalCommand() {
    }

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(NAME)
                .requires(source -> source.hasPermission(2))
                .executes(context -> replaceAltar(context.getSource())));
    }

    private static int replaceAltar(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        BlockPos pos = BlockPos.containing(source.getPosition());
        if (!(level.getBlockEntity(pos) instanceof OminousAltarBlockEntity altar)) {
            return 0;
        }

        FilthDomainPortalBlock portal = FDBlocks.FILTHDOMAIN_PORTAL.get();
        if (!altar.getOutput().is(portal.asItem()) || !portal.hasPlacementSpace(level, pos)) {
            return 0;
        }

        // Consume the recipe output before replacing the altar, which drops its remaining inventory.
        ItemStack output = altar.getHandler(Direction.DOWN).extractItem(0, 1, false);
        if (output.isEmpty()) {
            return 0;
        }
        if (!portal.placeStructure(level, pos)) {
            Block.popResource(level, pos, output);
        }
        return 1;
    }
}
