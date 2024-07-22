package growthcraft.cellar.block.entity;

import growthcraft.cellar.block.BrewKettleBlock;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.lib.networking.GrowthcraftCellarMessages;
import growthcraft.cellar.lib.networking.packet.BrewKettleFluidTankPacket;
import growthcraft.cellar.recipe.BrewKettleRecipe;
import growthcraft.cellar.screen.container.BrewKettleMenu;
import growthcraft.lib.block.entity.GrowthcraftFluidTank;
import growthcraft.lib.utils.BlockStateUtils;
import growthcraft.lib.utils.DirectionUtils;
import growthcraft.lib.utils.RandomGeneratorUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static growthcraft.cellar.block.CultureJarBlock.LIT;

public class BrewKettleBlockEntity extends BlockEntity implements BlockEntityTicker<BrewKettleBlockEntity>, MenuProvider {

    private int tickClock = 0;
    private int tickMax = -1;

    protected final ContainerData data;
    private Component customName;

    public static final int SLOT_BYPRODUCT = 2;
    public static final int SLOT_INPUT = 1;
    public static final int SLOT_LID = 0;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case SLOT_LID -> false; // was lid; will possibly turn into seasoning in the future.
                case SLOT_BYPRODUCT -> false;
                default -> true;
            };
        }
    };

    private LazyOptional<IItemHandler> inventoryItemHandler = LazyOptional.empty();

    private final GrowthcraftFluidTank FLUID_TANK_0 = new GrowthcraftFluidTank(4000) {
        @Override
        public void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                GrowthcraftCellarMessages.sendToClients(new BrewKettleFluidTankPacket(0, this.getFluid(), worldPosition));
            }
        }
    };

    private LazyOptional<IFluidHandler> fluidHandler0 = LazyOptional.empty();

    private final GrowthcraftFluidTank FLUID_TANK_1 = new GrowthcraftFluidTank(4000) {
        @Override
        public void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                GrowthcraftCellarMessages.sendToClients(new BrewKettleFluidTankPacket(1, this.fluid, worldPosition));
            }
        }
    };

    private LazyOptional<IFluidHandler> fluidHandler1 = LazyOptional.empty();

    public BrewKettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(GrowthcraftCellarBlockEntities.BREW_KETTLE_BLOCK_ENTITY.get(),
                blockPos, blockState);
    }

    public BrewKettleBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);

        this.FLUID_TANK_0.allowAnyFluid(true);
        this.FLUID_TANK_1.allowAnyFluid(true);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BrewKettleBlockEntity.this.tickClock;
                    case 1 -> BrewKettleBlockEntity.this.tickMax;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BrewKettleBlockEntity.this.tickClock = value;
                    case 1 -> BrewKettleBlockEntity.this.tickMax = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new BrewKettleMenu(containerId, inventory, this, this.data);
    }

    @Override
    public Component getDisplayName() {
        return this.customName != null
                ? this.customName
                : Component.translatable("container.growthcraft_cellar.brew_kettle");
    }

    private int lastInputTankAmount = 0, lastInputSlotAmount = 0; // to know if the contents have changed

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BrewKettleBlockEntity blockEntity) {
        if (level.isClientSide) { return; } // nesting got annoying.
        // first to see if anything has changed inside the kettle
        if (this.itemStackHandler.getStackInSlot(SLOT_INPUT).getCount() == this.lastInputSlotAmount && this.FLUID_TANK_0.getFluidAmount() == this.lastInputTankAmount) {
            // no change in input ingredients since the last tick
            if (this.itemStackHandler.getStackInSlot(SLOT_INPUT).isEmpty() || this.FLUID_TANK_0.isEmpty() || this.isOutputTankFull()) { return; }  // it would be more readable to keep this on top but i'd have to repeat the part from the bottom.
            // now, real work...
            if (this.tickClock <= this.tickMax) {
                // mid-proc tick
                this.tickClock++;
                if (level.getLevelData().getGameTime() % 20 == 11) {
                    level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
                }
            } // mid-proc tick
            else if (this.tickMax > 0) {
                // post-proc tick
                BrewKettleRecipe recipe = this.getFirstMatchingRecipe(); // won't check null, shouldn't happen
                this.itemStackHandler.getStackInSlot(SLOT_INPUT).shrink(
                        recipe.getInputItemStack().getCount()
                );
                this.getFluidTank(0).drain(
                        recipe.getInputFluidStack().getAmount(),
                        IFluidHandler.FluidAction.EXECUTE
                );
                this.getFluidTank(1).fill(
                        recipe.getOutputFluidStack().copy(),
                        IFluidHandler.FluidAction.EXECUTE
                );
                if (recipe.getByProductChance() != 0 && RandomGeneratorUtils.getRandomInt() <= recipe.getByProductChance()) { // Setting the by_product_chance to 0 in the recipe file should prevent it from being checked.
                    ItemStack byProductItemStack = recipe.getByProduct();
                    ItemStack existingByProductInSlot = this.itemStackHandler.getStackInSlot(SLOT_BYPRODUCT);
                    if (existingByProductInSlot.isEmpty() || existingByProductInSlot.getItem() == byProductItemStack.getItem()) {
                        int count = Math.min(byProductItemStack.getCount() + existingByProductInSlot.getCount(), byProductItemStack.getMaxStackSize());
                        byProductItemStack.setCount(count); // voiding excess
                        // Using insertStack does a check against isValiditem which is false by default for output only slots.
                        this.itemStackHandler.setStackInSlot(SLOT_BYPRODUCT, byProductItemStack);
                    }
                }
                this.resetTickClock();
                level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
            } // post-proc tick
            else if (this.tickMax == -1) {
                // awaiting recipe
                if (level.getLevelData().getGameTime() % 10 == 5) {
                    this.verifyHeated();
                    BrewKettleRecipe recipe = this.getFirstMatchingRecipe();
                    if (recipe != null && this.getFluidTank(1).canFluidStackFit(recipe.getOutputFluidStack())) {
                        this.tickMax = recipe.getProcessingTime();
                    }
                }
            } // awaiting recipe
        } // no change in input ingredients since the last tick
        else {
            // input changed
            this.resetTickClock();
            this.lastInputSlotAmount = this.itemStackHandler.getStackInSlot(SLOT_INPUT).getCount();
            this.lastInputTankAmount = this.FLUID_TANK_0.getFluidAmount();
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        } // input changed
    }

    private BrewKettleRecipe getFirstMatchingRecipe() {
        List<BrewKettleRecipe> recipes = level.getRecipeManager().getAllRecipesFor(BrewKettleRecipe.Type.INSTANCE);

        for (BrewKettleRecipe recipe : recipes) {
            if (recipe.matches(
                    this.itemStackHandler.getStackInSlot(SLOT_INPUT),
                    this.FLUID_TANK_0.getFluid(),
                    this.hasLid(),
                    this.isHeated()
            )) {
                if (!this.FLUID_TANK_1.getFluid().isEmpty()) {
                    if (this.FLUID_TANK_1.getFluid().getRawFluid() == recipe.getOutputFluidStack().getFluid()) {
                        return recipe;
                    }
                } else {
                    return recipe;
                }
            }
        }
        return null;
    }

    public boolean hasLid() {
        return this.getBlockState().getValue(BrewKettleBlock.HAS_LID);
    }

    public boolean isHeated() {
        return this.getBlockState().getValue(LIT);
    }

    public void verifyHeated() {
        boolean heated = BlockStateUtils.isHeatedFromBelow(this.level, this.getBlockPos());
        // Only change the blockstate if it is different.
        if (this.getBlockState().getValue(LIT) != heated) {
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(LIT, heated), Block.UPDATE_ALL);
        }
    }

    private void resetTickClock() {
        this.tickClock = 0;
        this.tickMax = -1;
    }

    public void setFluidStackInTank(int tankID, FluidStack fluidStack) {
        switch (tankID) {
            case 0 -> this.FLUID_TANK_0.setFluid(fluidStack);
            case 1 -> this.FLUID_TANK_1.setFluid(fluidStack);
            default -> {
                // Do nothing
            }
        }
    }

    public FluidStack getFluidStackInTank(int tankID) {
        return switch (tankID) {
            case 0 -> this.FLUID_TANK_0.getFluid();
            case 1 -> this.FLUID_TANK_1.getFluid();
            default -> null;
        };
    }

    public GrowthcraftFluidTank getFluidTank(int tankID) {
        return switch (tankID) {
            case 0 -> this.FLUID_TANK_0;
            case 1 -> this.FLUID_TANK_1;
            default -> null;
        };
    }

    public int getTickClock(String type) {
        switch (type) {
            case "current":
                return this.tickClock;
            case "max":
                return this.tickMax;
            default:
                return 0;
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.FLUID_TANK_0.readFromNBT(nbt.getCompound("fluid_tank_input_0"));
        this.FLUID_TANK_1.readFromNBT(nbt.getCompound("fluid_tank_output_0"));
        this.tickClock = nbt.getInt("CurrentProcessTicks");
        this.tickMax = nbt.getInt("MaxProcessTicks");

        if (nbt.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(nbt.getString("CustomName"));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.put("fluid_tank_input_0", FLUID_TANK_0.writeToNBT(new CompoundTag()));
        nbt.put("fluid_tank_output_0", FLUID_TANK_1.writeToNBT(new CompoundTag()));
        nbt.putInt("CurrentProcessTicks", this.tickClock);
        nbt.putInt("MaxProcessTicks", this.tickMax);

        if (this.customName != null) {
            nbt.putString("CustomName", Component.Serializer.toJson(this.customName));
        }

        super.saveAdditional(nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inventoryItemHandler = LazyOptional.of(() -> itemStackHandler);
        fluidHandler0 = LazyOptional.of(() -> FLUID_TANK_0);
        fluidHandler1 = LazyOptional.of(() -> FLUID_TANK_1);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryItemHandler.invalidate();
        fluidHandler0.invalidate();
        fluidHandler1.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (DirectionUtils.isSide(side)) {
                return this.fluidHandler1.cast();

            } else if (DirectionUtils.isTop(side)) {
                return this.fluidHandler0.cast();
            }
        } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.inventoryItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    public void dropItems() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.getLevel(), this.worldPosition, inventory);
    }

    public void drainFluidTank(int tankID, int amount) {
        this.getFluidTank(0).drain(amount, IFluidHandler.FluidAction.EXECUTE);
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState blockState, BrewKettleBlockEntity blockEntity)    {
        blockEntity.tick(level, pos, blockState, blockEntity);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, BrewKettleBlockEntity blockEntity) {
        RandomSource randomsource = level.random;
        if (randomsource.nextFloat() < 0.11F) {
            if (blockEntity.isProcessing() && !blockEntity.hasLid()) {
                SimpleParticleType simpleparticletype = ParticleTypes.CAMPFIRE_COSY_SMOKE;
                int count = randomsource.nextInt(2) + 2;
                for (int i = 0; i < count; ++i) {
                    level.addAlwaysVisibleParticle(
                            simpleparticletype,
                            true,
                            (double) blockPos.getX() + 0.5D + randomsource.nextDouble() / 3.0D * (double) (randomsource.nextBoolean() ? 1 : -1),
                            (double) blockPos.getY() + randomsource.nextDouble() + randomsource.nextDouble(),
                            (double) blockPos.getZ() + 0.5D + randomsource.nextDouble() / 3.0D * (double) (randomsource.nextBoolean() ? 1 : -1),
                            0.0D,
                            0.07D,
                            0.0D
                    );
                }
                level.playSound(null, blockPos, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public boolean isProcessing() {
        return this.tickClock > 0;
    }

    private boolean isOutputTankFull() {
        return (this.getFluidStackInTank(1).getAmount() == this.getFluidTank(1).getCapacity());
    }

    public int getPercentProgress() {
        float progress = (float) this.tickClock / this.tickMax;
        float percentage = progress * 100;
        return Math.round(percentage);
    }

    @Override
    public void setBlockState(BlockState newState) {
        if (newState.getValue(BrewKettleBlock.HAS_LID) != this.getBlockState().getValue(BrewKettleBlock.HAS_LID)
                || newState.getValue(BrewKettleBlock.LIT) != this.getBlockState().getValue(BrewKettleBlock.LIT)) {
            this.resetTickClock();
        }
        super.setBlockState(newState);
    }
}
