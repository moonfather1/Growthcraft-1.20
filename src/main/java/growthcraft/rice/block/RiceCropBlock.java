package growthcraft.rice.block;

import growthcraft.rice.init.GrowthcraftRiceBlocks;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class RiceCropBlock extends CropBlock {

    public RiceCropBlock() {
        super(getInitProperties());
    }

    private static BlockBehaviour.Properties getInitProperties() {
        Object AbstractBlock;
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(Blocks.WHEAT);
        properties.noCollission();
        properties.randomTicks();
        properties.instabreak();
        properties.sound(SoundType.CROP);
        return properties;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return GrowthcraftRiceItems.RICE_GRAINS.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (state.getValue(AGE) == this.getMaxAge()) {
        	popResource(level, pos, new ItemStack(GrowthcraftRiceItems.RICE.get()));
        	popResource(level, pos, new ItemStack(GrowthcraftRiceItems.RICE_STALK.get()));
            level.setBlock(pos, this.getStateForAge(0), 2);
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get());
    }

}
