package com.altnoir.filthdomain.content.block;

import com.altnoir.filthdomain.content.block.entity.FilthDomainPortalBlockEntity;
import com.altnoir.filthdomain.init.FDBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public final class FilthDomainPortalBlock extends BaseEntityBlock implements Portal {
    public static final MapCodec<FilthDomainPortalBlock> CODEC = simpleCodec(FilthDomainPortalBlock::new);
    public static final int CHARGE_TICKS = 100;
    public static final EnumProperty<Section> SECTION = EnumProperty.create("section", Section.class);

    private static final VoxelShape BOTTOM_SHAPE = Shapes.or(
            box(0, 0, 0, 16, 4, 16),
            box(2, 4, 2, 14, 12, 14),
            box(0, 12, 0, 16, 16, 16));
    private static final VoxelShape CENTER_SHAPE = box(3, 0, 3, 13, 16, 13);
    private static final VoxelShape TOP_SHAPE = Shapes.or(
            box(3, 0, 3, 13, 8, 13),
            box(1, 8, 1, 15, 12, 15));

    public FilthDomainPortalBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(SECTION, Section.BOTTOM));
    }

    @Override
    protected MapCodec<FilthDomainPortalBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(SECTION) == Section.BOTTOM ? new FilthDomainPortalBlockEntity(pos, state) : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(SECTION) != Section.BOTTOM) {
            return null;
        }
        return createTickerHelper(type, FDBlockEntities.PORTAL.get(),
                level.isClientSide ? FilthDomainPortalBlockEntity::clientTick : FilthDomainPortalBlockEntity::serverTick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SECTION);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos base = context.getClickedPos();
        BlockState state = defaultBlockState();
        if (base.getY() + 2 >= level.getMaxBuildHeight()
                || !canPlaceSection(context, base.above(), state.setValue(SECTION, Section.CENTER))
                || !canPlaceSection(context, base.above(2), state.setValue(SECTION, Section.TOP))) {
            return null;
        }
        return state;
    }

    private static boolean canPlaceSection(BlockPlaceContext context, BlockPos pos, BlockState state) {
        Level level = context.getLevel();
        return level.getWorldBorder().isWithinBounds(pos)
                && level.getBlockState(pos).canBeReplaced(context)
                && level.isUnobstructed(state, pos, CollisionContext.empty());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        placeUpperSections(level, pos, state);
    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return CHARGE_TICKS;
    }

    @Nullable
    @Override
    public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        return FilthDomainPortalDestination.find(level, entity, pos, this);
    }

    public boolean isComplete(Level level, BlockPos pos) {
        return hasSection(level, pos, Section.BOTTOM)
                && hasSection(level, pos.above(), Section.CENTER)
                && hasSection(level, pos.above(2), Section.TOP);
    }

    private boolean hasSection(Level level, BlockPos pos, Section section) {
        BlockState state = level.getBlockState(pos);
        return state.is(this) && state.getValue(SECTION) == section;
    }

    public boolean hasPlacementSpace(ServerLevel level, BlockPos pos) {
        BlockState base = defaultBlockState();
        BlockPos center = pos.above();
        BlockPos top = pos.above(2);
        return top.getY() < level.getMaxBuildHeight() && level.getWorldBorder().isWithinBounds(top)
                && level.isEmptyBlock(center) && level.isEmptyBlock(top)
                && level.isUnobstructed(base, pos, CollisionContext.empty())
                && level.isUnobstructed(base.setValue(SECTION, Section.CENTER), center, CollisionContext.empty())
                && level.isUnobstructed(base.setValue(SECTION, Section.TOP), top, CollisionContext.empty());
    }

    public boolean placeStructure(ServerLevel level, BlockPos pos) {
        BlockState base = defaultBlockState();
        if (!level.setBlock(pos, base, UPDATE_ALL)) {
            return false;
        }
        placeUpperSections(level, pos, base);
        return isComplete(level, pos);
    }

    private static void placeUpperSections(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos.above(), state.setValue(SECTION, Section.CENTER), UPDATE_ALL);
        level.setBlock(pos.above(2), state.setValue(SECTION, Section.TOP), UPDATE_ALL);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Section section = state.getValue(SECTION);
        Section expected = switch (direction) {
            case UP -> switch (section) {
                case BOTTOM -> Section.CENTER;
                case CENTER -> Section.TOP;
                case TOP -> null;
            };
            case DOWN -> switch (section) {
                case BOTTOM -> null;
                case CENTER -> Section.BOTTOM;
                case TOP -> Section.CENTER;
            };
            default -> null;
        };
        if (expected != null && (!neighbor.is(this) || neighbor.getValue(SECTION) != expected)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(SECTION)) {
            case BOTTOM -> BOTTOM_SHAPE;
            case CENTER -> CENTER_SHAPE;
            case TOP -> TOP_SHAPE;
        };
    }

    public enum Section implements StringRepresentable {
        BOTTOM("bottom"),
        CENTER("center"),
        TOP("top");

        private final String name;

        Section(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
