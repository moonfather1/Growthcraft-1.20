package growthcraft.cellar.block;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.core.utils.BlockPropertiesUtils;
import growthcraft.lib.utils.BlockStateUtils;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CultureJarBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    private static final VoxelShape VOXEL_SHAPE = Block.box(
            5.0D, 0.0D, 5.0D,
            11.0D, 8.0D, 11.0D
    );

    public CultureJarBlock() {
        this(getInitProperties());
    }

    public CultureJarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(LIT, false)
        );
    }

    private static Properties getInitProperties() {
        Properties properties = Properties.copy(Blocks.GLASS);
        properties.strength(0.5F);
        properties.noOcclusion();
        properties.isValidSpawn(BlockPropertiesUtils::never);
        properties.isRedstoneConductor(BlockPropertiesUtils::never);
        properties.isSuffocating(BlockPropertiesUtils::never);
        properties.isViewBlocking(BlockPropertiesUtils::never);
        return properties;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return VOXEL_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder.add(FACING).add(LIT));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return GrowthcraftCellarBlockEntities.CULTURE_JAR_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(LIT, BlockStateUtils.isHeatedFromTwoBlockRange(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_60584_) {
        return PushReaction.DESTROY;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING))).setValue(LIT, state.getValue(LIT));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING))).setValue(LIT, state.getValue(LIT));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos blockPos)
    {
        // allow the jar to be on solid blocks or at least blocks with solid top center.
        BlockPos belowPos = blockPos.below();
        BlockState below = world.getBlockState(belowPos);
        if (below.getBlock() instanceof StairBlock && below.getValue(StairBlock.HALF).equals(Half.BOTTOM)) {
            return false; // logic below fails to get top area of stairs properly, and it's annoying.
        }
        VoxelShape top = below.getFaceOcclusionShape(world, belowPos, Direction.UP);
        boolean belowHas8x8Support = top.min(Direction.Axis.X) <= 4/16d && top.max(Direction.Axis.X) >= 12/16d && top.min(Direction.Axis.Z) <= 4/16d && top.max(Direction.Axis.Z) >= 12/16d;
        return belowHas8x8Support && super.canSurvive(state, world, blockPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos blockPos, Block block, BlockPos neighbor, boolean p_60514_)
    {
        // when a block is removed from below the jar, drop the jar too.
        if (this.canSurvive(state, level, blockPos)) {
            super.neighborChanged(state, level, blockPos, block, neighbor, p_60514_);
        }
        else {
            level.destroyBlock(blockPos, true);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!level.isClientSide && state.getBlock() != newState.getBlock())
        {
            if (level.getBlockEntity(pos) instanceof CultureJarBlockEntity jar)
            {
                jar.dropHeldItems();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(
                blockEntityType,
                GrowthcraftCellarBlockEntities.CULTURE_JAR_BLOCK_ENTITY.get(),
                (worldLevel, pos, blockState, blockEntity) -> (blockEntity).tick()
        );
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            CultureJarBlockEntity blockEntity = (CultureJarBlockEntity) level.getBlockEntity(blockPos);
            if (blockEntity == null) return InteractionResult.FAIL;

            // Try fluid handling first:
            if(player.getItemInHand(interactionHand).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()){
                boolean fluidInteractionResult = false;

                // Special case for handling Vanilla milk by converting it to GC Milk:
                if (player.getItemInHand(interactionHand).getItem() == Items.MILK_BUCKET) {
                    int capacity = blockEntity.getFluidTank(0).getCapacity();
                    int amount = blockEntity.getFluidTank(0).getFluidAmount();
                    int remainingFill = capacity - amount;

                    if (blockEntity.getFluidTank(0).isEmpty()
                            || (remainingFill >= 1000
                            && blockEntity.getFluidStackInTank(0).getFluid().getFluidType() == GrowthcraftMilkFluids.MILK.source.get().getFluidType())
                    ) {
                        try {
                            FluidStack fluidStack = new FluidStack(GrowthcraftMilkFluids.MILK.source.get().getSource(), 1000);
                            blockEntity.getFluidTank(0).fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                            player.setItemInHand(interactionHand, new ItemStack(Items.BUCKET));
                            level.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                            fluidInteractionResult = true;
                        } catch (Exception ex) {
                            GrowthcraftCellar.LOGGER.error(String.format("Exception Thrown: %s unable to place vanilla milk in CultureJarBlockEntity at %s.", player.getDisplayName().getString(), blockPos));
                            GrowthcraftCellar.LOGGER.error(ex.getMessage());
                            GrowthcraftCellar.LOGGER.error(ex.fillInStackTrace());
                        }
                    }
                } else {
                    try {
                        fluidInteractionResult =  FluidUtil.interactWithFluidHandler(player, interactionHand, level, blockPos, blockHitResult.getDirection());
                    } catch (Exception ex) {
                        GrowthcraftCellar.LOGGER.error(String.format("Exception Thrown: %s unable to place fluid in CultureJarBlockEntity at %s.", player.getDisplayName().getString(), blockPos));
                        GrowthcraftCellar.LOGGER.error(ex.getMessage());
                        GrowthcraftCellar.LOGGER.error(ex.fillInStackTrace());
                    }
                }
                // Return based on whether interaction with the fluid handler item was successful or not.
                return fluidInteractionResult ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            } else {
                // if not holding a fluid, open the GUI
                try {
                    // Play sound
                    level.playSound(player, blockPos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    NetworkHooks.openScreen(((ServerPlayer) player), blockEntity, blockPos);
                } catch (Exception ex) {
                    GrowthcraftCellar.LOGGER.error(String.format("%s unable to open CultureJarBlockEntity GUI at %s.", player.getDisplayName().getString(), blockPos));
                    GrowthcraftCellar.LOGGER.error(ex.getMessage());
                    GrowthcraftCellar.LOGGER.error(ex.fillInStackTrace());
                }
                return InteractionResult.SUCCESS;
            }

        }
        // Always return SUCCESS for client side.
        return InteractionResult.SUCCESS;
    }
}
