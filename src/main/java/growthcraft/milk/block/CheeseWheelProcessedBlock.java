package growthcraft.milk.block;

import growthcraft.core.init.GrowthcraftTags;
import growthcraft.milk.block.entity.CheeseWheelBlockEntity;
import growthcraft.rice.init.GrowthcraftRiceTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CheeseWheelProcessedBlock extends BaseCheeseWheel {

    public CheeseWheelProcessedBlock(Cheese variant) {
        super(variant);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide()) return InteractionResult.CONSUME;

        CheeseWheelBlockEntity entity = (CheeseWheelBlockEntity) level.getBlockEntity(blockPos);
        assert entity != null;
        System.out.println(player.getItemInHand(interactionHand).getTags().toList());
        // handle taking slices
        if (!player.isCrouching() && player.getItemInHand(interactionHand).is(GrowthcraftTags.Items.TAG_KNIFE)) {
            entity.takeSlice(1);
            player.getInventory().add(getVariant().getSlices(1));
            if (entity.getSliceCount() == 0) level.destroyBlock(blockPos, false);
            return InteractionResult.SUCCESS;
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}
