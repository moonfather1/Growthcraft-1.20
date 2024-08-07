package growthcraft.cellar.block;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.BrewKettleBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.core.utils.BlockPropertiesUtils;
import growthcraft.lib.utils.BlockStateUtils;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BrewKettleBlock extends BaseEntityBlock {

    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final BooleanProperty HAS_LID = BooleanProperty.create("has_lid");
    private final VoxelShape[] SHAPES_LEGS = new VoxelShape[] {
            Block.box(0, 0, 2, 2, 3, 4),
            Block.box(0, 0, 0, 4, 3, 2),
            Block.box(12, 0, 0, 16, 3, 2),
            Block.box(12, 0, 14, 16, 3, 16),
            Block.box(0, 0, 14, 4, 3, 16),
            Block.box(14, 0, 2, 16, 3, 4),
            Block.box(14, 0, 12, 16, 3, 14),
            Block.box(0, 0, 12, 2, 3, 14)
    };
    private final VoxelShape[] SHAPES_SIDES = new VoxelShape[] {
            Block.box(1, 3, 1, 15, 4, 15),
            Block.box(1, 3, 0, 15, 15, 1),
            Block.box(15, 3, 0, 16, 15, 16),
            Block.box(1, 3, 15, 15, 15, 16),
            Block.box(0, 3, 0, 1, 15, 16)
    };
    private final VoxelShape BLOCK_SHAPE_WITHOUT_LID = Shapes.or(Shapes.or(Shapes.empty(), SHAPES_SIDES), SHAPES_LEGS);
    private final VoxelShape BLOCK_SHAPE_WITH_LID = Shapes.or(Block.box(0, 3, 0, 16, 15, 16), SHAPES_LEGS);

    private static final Component MESSAGE_NO_LID = Component.translatable("message.growthcraft_cellar.kettle.outside_lid_off").withStyle(Style.EMPTY.withColor(0xffccccd5));
    private static final Component MESSAGE_HAS_LID = Component.translatable("message.growthcraft_cellar.kettle.outside_lid_on").withStyle(Style.EMPTY.withColor(0xffccccd5));
    private static final Component MESSAGE_ON_PLACED = Component.translatable("message.growthcraft_cellar.kettle.on_place").withStyle(Style.EMPTY.withColor(0xffccccd5));

    public BrewKettleBlock() {
        this(getInitProperties());
    }

    public BrewKettleBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, false)
                .setValue(HAS_LID, false)
        );
    }

    private static Properties getInitProperties() {
        Properties properties = Properties.copy(Blocks.FURNACE);
        properties.noOcclusion();
        properties.sound(SoundType.METAL);
        properties.isRedstoneConductor(BlockPropertiesUtils::never);
        return properties;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return blockState.getValue(HAS_LID) ? BLOCK_SHAPE_WITH_LID : BLOCK_SHAPE_WITHOUT_LID;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(LIT, HAS_LID);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(LIT, BlockStateUtils.isHeatedFromBelow(context.getLevel(), context.getClickedPos()))
                .setValue(HAS_LID, false);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return GrowthcraftCellarBlockEntities.BREW_KETTLE_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return createTickerHelper(blockEntityType, GrowthcraftCellarBlockEntities.BREW_KETTLE_BLOCK_ENTITY.get(), BrewKettleBlockEntity::clientTick);
        }
        else  {
            return createTickerHelper(blockEntityType, GrowthcraftCellarBlockEntities.BREW_KETTLE_BLOCK_ENTITY.get(), BrewKettleBlockEntity::serverTick);
        }
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            if (player.isCrouching()) {
                boolean newState = !blockState.getValue(HAS_LID);
                level.setBlockAndUpdate(blockPos, blockState.setValue(HAS_LID, newState));
                player.displayClientMessage(newState ? MESSAGE_HAS_LID : MESSAGE_NO_LID, true);
                return InteractionResult.SUCCESS;
            }
            BrewKettleBlockEntity blockEntity = (BrewKettleBlockEntity) level.getBlockEntity(blockPos);
            if (blockEntity == null) return InteractionResult.FAIL;

            // Try to do fluid handling first.
            if (player.getItemInHand(interactionHand)
                    .getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                    .isPresent()
            ) {
                boolean fluidInteractionResult = false;

                // If the player is holding a generic bucket. Always try and pull from the output fluid first.
                if (player.getItemInHand(interactionHand).getItem() == Items.BUCKET) {
                    if (!blockEntity.getFluidTank(1).isEmpty()) {
                        fluidInteractionResult = FluidUtil.interactWithFluidHandler(player, interactionHand, level, blockPos, Direction.NORTH);
                    } else if (!blockEntity.getFluidTank(0).isEmpty()) {
                        fluidInteractionResult = FluidUtil.interactWithFluidHandler(player, interactionHand, level, blockPos, Direction.UP);
                    }
                } else if (player.getItemInHand(interactionHand).getItem() == Items.MILK_BUCKET) {
                    // If the player is holding a vanilla milk bucket, then we need to process it
                    // into a Growthcraft Milk Fluid.
                    int capacity = blockEntity.getFluidTank(0).getCapacity();
                    int amount = blockEntity.getFluidTank(0).getFluidAmount();
                    int remainingFill = capacity - amount;

                    if (blockEntity.getFluidTank(0).isEmpty()
                            || (remainingFill >= 1000
                            && blockEntity.getFluidStackInTank(0).getFluid().getFluidType() == GrowthcraftMilkFluids.MILK.source.get().getFluidType())
                    ) {
                        FluidStack fluidStack = new FluidStack(GrowthcraftMilkFluids.MILK.source.get().getSource(), 1000);
                        blockEntity.getFluidTank(0).fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        player.setItemInHand(interactionHand, new ItemStack(Items.BUCKET));
                    }
                } else {
                    // Otherwise, try and fill the input tank.
                    fluidInteractionResult = FluidUtil.interactWithFluidHandler(player, interactionHand, level, blockPos, Direction.UP);
                }

                level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS);
                // Return based on whether interaction with the fluid handler item was successful or not.
                return fluidInteractionResult ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            } else {
                try {
                    level.playSound(null, blockPos, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS);
                    NetworkHooks.openScreen(((ServerPlayer) player), blockEntity, blockPos);
                    return InteractionResult.SUCCESS;
                } catch (Exception ex) {
                    GrowthcraftCellar.LOGGER.error(
                            String.format("%s unable to open BrewKettleBlockEntity GUI at %s.",
                                    player.getDisplayName().getString(),
                                    blockPos)
                    );
                    GrowthcraftCellar.LOGGER.error(ex.getMessage());
                    GrowthcraftCellar.LOGGER.error(ex.fillInStackTrace());

                    return InteractionResult.FAIL;
                }
            }
        }

        // Always return SUCCESS for client side.
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        return true;
    }

    @Override
    public void neighborChanged(BlockState p_60509_, Level level, BlockPos blockPos, Block p_60512_, BlockPos p_60513_, boolean p_60514_)  {
        super.neighborChanged(p_60509_, level, blockPos, p_60512_, p_60513_, p_60514_);
        if (level.getBlockEntity(blockPos) instanceof BrewKettleBlockEntity kettle) {
            kettle.verifyHeated();
        }
    }

    /////////////// message when block is placed //////////////

    @Mod.EventBusSubscriber
    private static class BlockEventHandler {
        @SubscribeEvent
        public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
            if (event.getEntity() instanceof Player player && event.getPlacedBlock().is(GrowthcraftCellarBlocks.BREW_KETTLE.get())) {
                player.displayClientMessage(MESSAGE_ON_PLACED, true);
            }
        }
    }
}
