package growthcraft.rice.datagen.providers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import growthcraft.rice.shared.Reference;
import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.rice.init.GrowthcraftRiceBlocks;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.registries.ForgeRegistries;

public class GrowthcraftRiceLootTables extends VanillaBlockLoot{
	
	@Override
	protected void generate() {
		add(GrowthcraftRiceBlocks.RICE_CROP.get(), createCropDrops(GrowthcraftRiceBlocks.RICE_CROP.get(), GrowthcraftRiceItems.RICE_STALK.get(), GrowthcraftRiceItems.RICE_GRAINS.get(), LootItemBlockStatePropertyCondition.hasBlockStateProperties(GrowthcraftRiceBlocks.RICE_CROP.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7))));	
		dropOther(GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get(), Blocks.DIRT);
	}
	
    @Override
    protected Iterable<Block> getKnownBlocks() {
    	return List.of(GrowthcraftRiceBlocks.RICE_CROP.get(), GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get());
    }
}
