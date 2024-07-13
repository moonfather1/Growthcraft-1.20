package growthcraft.bamboo.block;

import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BambooPostBlock extends RotatedPillarBlock
{
    public BambooPostBlock() {
        super(BlockBehaviour.Properties.of()
                                       .mapColor((state) -> MapColor.SAND).instrument(NoteBlockInstrument.CHIME).strength(1.3F).sound(SoundType.BAMBOO_WOOD).ignitedByLava());
        this.registerDefaultState(this.stateDefinition.any().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)));
    }

    //////////////////////////////////////////////////

    private static BambooPostBlock instanceHorizontal = null, instanceVertical = null;

    public static BambooPostBlock create(boolean vertical) {
        if (vertical) {
            if (instanceVertical == null) { instanceVertical = new BambooPostBlock(); }
            return instanceVertical;
        }
        else {
            if (instanceHorizontal == null) { instanceHorizontal = new BambooPostBlock(); }
            return instanceHorizontal;
        }
    }

    /////////////////////////////////////////////////

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch (state.getValue(RotatedPillarBlock.AXIS)) {
                    case Y -> VISUAL_SHAPE_Y;
                    case X -> VISUAL_SHAPE_X;
                    case Z -> VISUAL_SHAPE_Z;
                };
    }
    private static final VoxelShape VISUAL_SHAPE_Y = Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 13/16d);
    private static final VoxelShape VISUAL_SHAPE_Z = Shapes.box(3/16d, 3/16d, 0/16d, 13/16d, 13/16d, 16/16d);
    private static final VoxelShape VISUAL_SHAPE_X = Shapes.box(0/16d, 3/16d, 3/16d, 16/16d, 13/16d, 13/16d);

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return switch (state.getValue(RotatedPillarBlock.AXIS)) {
                    case Y -> COLLI_SHAPE_Y;
                    case X -> COLLI_SHAPE_X;
                    case Z -> COLLI_SHAPE_Z;
                };
    }

    private static final VoxelShape COLLI_SHAPE_Y = Shapes.box(5/16d, 0/16d, 5/16d, 11/16d, 16/16d, 11/16d);
    private static final VoxelShape COLLI_SHAPE_Z = Shapes.box(5/16d, 5/16d, 0/16d, 11/16d, 11/16d, 16/16d);
    private static final VoxelShape COLLI_SHAPE_X = Shapes.box(0/16d, 5/16d, 5/16d, 16/16d, 11/16d, 11/16d);

    /////////////////////////////////////////////////////

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55933_) {
        p_55933_.add(AXIS, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidstate = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        boolean vertical = blockPlaceContext.getClickedFace().getAxis().equals(Direction.Axis.Y);
        Block block = vertical ? GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get() : GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get(); // block shouldn't know this but i need it
        return block.defaultBlockState().setValue(AXIS, blockPlaceContext.getClickedFace().getAxis()).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState p_54377_) {
        return p_54377_.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_54377_);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return create(true).asItem().getDefaultInstance();
    }
}
