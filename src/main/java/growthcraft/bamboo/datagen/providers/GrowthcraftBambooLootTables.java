package growthcraft.bamboo.datagen.providers;

import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class GrowthcraftBambooLootTables extends VanillaBlockLoot {

	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return List.of(GrowthcraftBambooBlocks.BAMBOO_PLANK_BEE_BOX.get(), GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get(), GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get());
	}

	@Override
	protected void generate() {
        dropSelf(GrowthcraftBambooBlocks.BAMBOO_PLANK_BEE_BOX.get());
        dropSelf(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get());
        dropOther(GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get(), GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get());
	}
}
