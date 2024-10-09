package growthcraft.milk.block.signs;

import growthcraft.milk.block.entity.ShopSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.Optional;

public class HangingSignBlock2 extends WallHangingSignBlock {
    public HangingSignBlock2(SignBlock originalBlock) {
        super(HangingSignBlock1.SharedSignCode.properties().mapColor(originalBlock.defaultMapColor()), originalBlock.type());
        this.originalBlock = originalBlock;
    }

    private final SignBlock originalBlock;

    ////// use ////////////////////////

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        Optional<InteractionResult> handlerResult = HangingSignBlock1.SharedSignCode.use(state, level, blockPos, player, hand, blockHitResult, this.originalBlock);
        return handlerResult.orElse(super.use(state, level, blockPos, player, hand, blockHitResult));
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
}
