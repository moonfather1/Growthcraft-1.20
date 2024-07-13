package growthcraft.cellar.block.entity;

import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.lib.networking.GrowthcraftCellarMessages;
import growthcraft.cellar.lib.networking.packet.FermentationBarrelFluidTankPacket;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
import growthcraft.cellar.screen.container.FermentationBarrelMenu;
import growthcraft.lib.block.entity.GrowthcraftFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

public class FermentationBarrelBlockEntity extends BlockEntity implements BlockEntityTicker<FermentationBarrelBlockEntity>, MenuProvider {

    private static final int BRANCH_PROCESSING = 1;
    private static final int BRANCH_RESET_HAPPENED = 2;
    private static final int BRANCH_IDLE = 0;

    protected final ContainerData data;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final GrowthcraftFluidTank FLUID_TANK_INPUT_0 = new GrowthcraftFluidTank(4000) {
        @Override
        public void onContentsChanged() {
            setChanged();
            if (!level.isClientSide) {
                GrowthcraftCellarMessages.sendToClients(new FermentationBarrelFluidTankPacket(0, this.fluid, worldPosition));
            }
        }
    };

    private int tickerState = BRANCH_IDLE;
    private int tickClock = 0;
    private int tickMax = -1;

    private boolean yeastWarning = false;
    private boolean yeastError = false;

    private Component customName;

    private LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyInputFluidHandler0 = LazyOptional.empty();

    private ItemStack lastItemStack = ItemStack.EMPTY;
    private FluidStack lastFluidStack = FluidStack.EMPTY;
    
    public FermentationBarrelBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(GrowthcraftCellarBlockEntities.FERMENTATION_BARREL_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public FermentationBarrelBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);

        this.FLUID_TANK_INPUT_0.allowAnyFluid(true);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FermentationBarrelBlockEntity.this.tickClock;
                    case 1 -> FermentationBarrelBlockEntity.this.tickMax;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FermentationBarrelBlockEntity.this.tickClock = value;
                    case 1 -> FermentationBarrelBlockEntity.this.tickMax = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return this.customName != null
                ? this.customName
                : Component.translatable("container.growthcraft_cellar.fermentation_barrel");
    }

    public void tick() {
        if (this.getLevel() != null) {
            this.tick(this.getLevel(), this.getBlockPos(), this.getBlockState(), this);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, FermentationBarrelBlockEntity blockEntity) {
        if (level.isClientSide || this.getFluidTank(0).isEmpty()) {
            return;
        }

        if (this.changed()) {
            // change in fluid tank or yeast slot... reset everything.
            this.resetTickClock();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
            this.tickerState = BRANCH_RESET_HAPPENED;
        } else if (this.tickerState == BRANCH_PROCESSING && this.tickClock <= this.tickMax) {
            // processing, somewhere in the middle
            this.tickClock++; // move the counter
            if (tickClock % 10 == 7) {  // sent client update twice per second. it's a tiny progress bar for a rather long process.
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
            }
        } else if (this.tickerState == BRANCH_PROCESSING && this.tickClock > this.tickMax) {
            // hitting the specified processing time (specified in the recipe)
            FermentationBarrelRecipe recipe = this.getFirstMatchingRecipe();
            int multiplier = recipe.getOutputMultiplier(this.getFluidStackInTank(0));
            this.itemStackHandler.getStackInSlot(0).shrink(multiplier);

            FluidStack resultingFluidStack = recipe.getResultingFluid().copy();
            int resultingAmount = resultingFluidStack.getAmount() * multiplier;
            resultingFluidStack.setAmount(resultingAmount);
            this.setFluidStackInTank(0, resultingFluidStack);

            this.resetTickClock();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
            this.tickerState = BRANCH_IDLE;
        } else if (this.tickerState == BRANCH_RESET_HAPPENED   // barrel contents changed? second part of the condition (once per second) is in case of recipe data reload.
                || (this.tickerState == BRANCH_IDLE && level.getGameTime() % 20 == 9)) {   // we may improve this second part in another pr as multiple machines should share it.
            FermentationBarrelRecipe recipe = this.getFirstMatchingRecipe();
            if (this.tickerState == BRANCH_RESET_HAPPENED || recipe != null) {
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL); // this is to update tooltips on the gui
            }
            this.tickerState = BRANCH_IDLE; // in case we find nothing after content change
            if (recipe != null) {
                this.tickMax = recipe.getProcessingTime() * recipe.getOutputMultiplier(this.getFluidStackInTank(0));
                this.tickerState = BRANCH_PROCESSING;
            }
        }
    }

    private boolean changed() {
        if (this.lastFluidStack.isFluidEqual(this.FLUID_TANK_INPUT_0.getFluid())
                && this.lastFluidStack.getAmount() == this.FLUID_TANK_INPUT_0.getFluid().getAmount()
                && this.lastItemStack.equals(this.itemStackHandler.getStackInSlot(0), false)) {
            return false;
            // yes, item handler and fluid tank have "changed" method which we could override in subclass, but they often have false-positive triggers. not a problem in many cases but here we can't afford to reset processing due to a false positive.
        } else {
            this.lastFluidStack = this.FLUID_TANK_INPUT_0.getFluid().copy();
            this.lastItemStack = this.itemStackHandler.getStackInSlot(0).copy();
            return true;
        }
    }

    private FermentationBarrelRecipe getFirstMatchingRecipe() {
        this.yeastWarning = this.yeastError = false;
        List<FermentationBarrelRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(FermentationBarrelRecipe.Type.INSTANCE);
        for (FermentationBarrelRecipe recipe : recipes) {
            if (recipe.matchesInput(this.getFluidStackInTank(0))) {
                if (recipe.matches(this.itemStackHandler.getStackInSlot(0), this.getFluidStackInTank(0))) {
                    return recipe;
                } else if (this.itemStackHandler.getStackInSlot(0).isEmpty()) {
                    return null;
                } else if (recipe.matches(this.itemStackHandler.getStackInSlot(0).copyWithCount(64), this.getFluidStackInTank(0))) {
                    this.yeastWarning = true;
                    return null;
                } else {
                    this.yeastError = true;
                    return null;
                }
            }
        }
        return null;
    }

    @Nullable
    public ItemStack getResultingPotionItemStack() {
        FermentationBarrelRecipe recipe = null;
        List<FermentationBarrelRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(FermentationBarrelRecipe.Type.INSTANCE);
        for (FermentationBarrelRecipe recipe0 : recipes) {
            if (recipe0.matchesOutput(this.getFluidStackInTank(0))) {
                recipe = recipe0;
                break;
            }
        }
        return recipe != null ? recipe.getBottleItemStack().copy() : null;
    }

    private void resetTickClock() {
        this.tickClock = 0;
        this.tickMax = -1;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new FermentationBarrelMenu(containerId, inventory, this, this.data);
    }

    public boolean isInputTankFull() {
        return this.getFluidTank(0).getCapacity() == this.getFluidTank(0).getFluidAmount();
    }

    public void setFluidStackInTank(int tankID, FluidStack fluidStack) {
        this.FLUID_TANK_INPUT_0.setFluid(fluidStack);
    }

    public FluidStack getFluidStackInTank(int tankID) {
        return this.FLUID_TANK_INPUT_0.getFluid();
    }

    @Nonnull
    public GrowthcraftFluidTank getFluidTank(int tankID) {
        return this.FLUID_TANK_INPUT_0;
    }

    public boolean isFluidEmpty() {
        return getFluidStackInTank(0).isEmpty();
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
        this.FLUID_TANK_INPUT_0.readFromNBT(nbt.getCompound("fluid_tank_input_0"));
        this.tickClock = nbt.getInt("CurrentProcessTicks");
        this.tickMax = nbt.getInt("MaxProcessTicks");
        this.yeastWarning = nbt.getBoolean("ShowYeastWarning");
        this.yeastError = nbt.getBoolean("ShowYeastError");

        if (nbt.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(nbt.getString("CustomName"));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.put("fluid_tank_input_0", FLUID_TANK_INPUT_0.writeToNBT(new CompoundTag()));
        nbt.putInt("CurrentProcessTicks", this.tickClock);
        nbt.putInt("MaxProcessTicks", this.tickMax);
        nbt.putBoolean("ShowYeastWarning", this.yeastWarning);
        nbt.putBoolean("ShowYeastError", this.yeastError);

        if (this.customName != null) {
            nbt.putString("CustomName", Component.Serializer.toJson(this.customName));
        }

        super.saveAdditional(nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(Objects.requireNonNull(pkt.getTag()));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        itemHandlerLazyOptional = LazyOptional.of(() -> itemStackHandler);
        lazyInputFluidHandler0 = LazyOptional.of(() -> FLUID_TANK_INPUT_0);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerLazyOptional.invalidate();
        lazyInputFluidHandler0.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return this.lazyInputFluidHandler0.cast();
        } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandlerLazyOptional.cast();
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

    public int getPercentProgress() {
        float progress = (float) this.tickClock / this.tickMax;
        float percentage = progress * 100;
        return Math.round(percentage);
    }

    public boolean hasYeastWarning() {
        return this.yeastWarning;
    }

    public boolean hasYeastError() {
        return this.yeastError;
    }
}
