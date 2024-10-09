package growthcraft.milk.block.signs;

import growthcraft.milk.block.entity.ShopSignBlockEntity;
import growthcraft.milk.shared.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.Optional;

public class HangingSignBlock1 extends CeilingHangingSignBlock {
    public HangingSignBlock1(SignBlock originalBlock) {
        super(SharedSignCode.properties().mapColor(originalBlock.defaultMapColor()), originalBlock.type());
        this.originalBlock = originalBlock;
    }

    private final SignBlock originalBlock;

    ////// use ////////////////////////

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        Optional<InteractionResult> handlerResult = SharedSignCode.use(state, level, blockPos, player, hand, blockHitResult, this.originalBlock);
        return handlerResult.orElse(super.use(state, level, blockPos, player, hand, blockHitResult));
    }

    private static boolean shouldTryToChainAnotherHangingSign2(Player player, BlockHitResult blockHitResult, ItemStack itemStack) {
        return itemStack.getItem() instanceof HangingSignItem && blockHitResult.getDirection().equals(Direction.DOWN);
    }

    ////////////////////////////////////////////////

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) { return this.originalBlock.getCloneItemStack(state, target, level, pos, player); }

    @Override
    public String getDescriptionId() { return this.originalBlock.getDescriptionId(); }

    /////////////////////////////////////////////

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ShopSignBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return null;
    }

    //---------------------------------------------------------------------------------------------------------
    // this class is to appease some stupid code analysis.
    // yes, there will be less duplicated code, but it will be more difficult for me to transfer any fixes and improvements from upstream (my mod)...
    static class SharedSignCode {
        public static Properties properties() {
            return Properties.of().forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava();
        }

        public static Optional<InteractionResult> use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult, Block originalBlock) {
            if (level.getBlockEntity(blockPos) instanceof ShopSignBlockEntity signBlockEntity) {
                ItemStack itemstack = player.getItemInHand(hand);
                if (HangingSignBlock1.shouldTryToChainAnotherHangingSign2(player, blockHitResult, itemstack)) {
                    return Optional.empty();
                }
                if (hand.equals(InteractionHand.MAIN_HAND)) {
                    if (signBlockEntity.isWaxed()) {
                        return Optional.of(InteractionResult.FAIL);
                    }
                    if (signBlockEntity.getItem().isEmpty() && player.getItemInHand(hand).isEmpty()) {
                        // revert
                        BlockState newState = originalBlock.withPropertiesOf(state);
                        level.setBlockAndUpdate(blockPos, newState);
                        newState.getBlock().use(newState, level, blockPos, player, hand, blockHitResult);
                    }
                    else if (signBlockEntity.getItem().isEmpty() || ! (player.getItemInHand(hand).is(Items.HONEYCOMB) || player.getItemInHand(hand).is(Reference.ItemTag.GC_WAX))) {
                        // change item
                        signBlockEntity.setItem(player.getItemInHand(hand));
                    }
                    else {
                        // wax on
                        signBlockEntity.setWaxed(true);
                        level.levelEvent(player, 3003, blockPos, 0);
                        player.getItemInHand(hand).shrink(1);
                    }
                    return Optional.of(InteractionResult.sidedSuccess(level.isClientSide()));
                }
            }
            // shouldn't be here
            return Optional.empty();
        }
    }
}
