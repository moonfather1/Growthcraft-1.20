package growthcraft.core.datagen.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftItems;
import growthcraft.core.shared.Reference;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

public class GrowthcraftCoreLootTables extends VanillaBlockLoot{
	
	/**
	 * This method is used to generate loot tables for different types of blocks.
	 * It iterates through different blocks and calls specific methods based on the block type.
	 */
	@Override
	protected void generate() {

		getBlocksThatDropOres().forEach( (block, item) -> {
			add(block, functionBlock -> createOreLikeDrops(block, item));
		});

		getBlocksThatDropSelf().forEach(block -> {
			dropSelf(block);
		});

		getBlocksThatDropRope().forEach(block -> {
			dropOther(block, GrowthcraftItems.ROPE_LINEN.get());
		});
	}
	
	/**
	 * Retrieves an iterable of all the known blocks in the system.
	 *
	 * @return An iterable of Block objects representing the known blocks
	 */
	@Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Reference.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

	/**
	 * Returns an iterable of blocks that drop themselves when harvested.
	 *
	 * @return The iterable of blocks that drop themselves
	 */
	private Iterable<Block> getBlocksThatDropSelf() {
		List<Block> blocksThatDropSelf = new ArrayList<>();

		blocksThatDropSelf.add(GrowthcraftBlocks.ROPE_LINEN.get());
		blocksThatDropSelf.add(GrowthcraftBlocks.SALT_BLOCK.get());

		return blocksThatDropSelf;
	}

	/**
	 * Returns a map of blocks that drop ores and their corresponding dropped item.
	 *
	 * @return The map of blocks that drop ores and their corresponding dropped item
	 */
	private Map<Block, Item> getBlocksThatDropOres() {
		Map<Block, Item> blocksThatDropOres = new HashMap<>();

		blocksThatDropOres.put(GrowthcraftBlocks.SALT_ORE.get(), GrowthcraftItems.SALT.get());
		blocksThatDropOres.put(GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get(), GrowthcraftItems.SALT.get());
		blocksThatDropOres.put(GrowthcraftBlocks.SALT_ORE_END.get(), GrowthcraftItems.SALT.get());
		blocksThatDropOres.put(GrowthcraftBlocks.SALT_ORE_NETHER.get(), GrowthcraftItems.SALT.get());

		return blocksThatDropOres;
	}

	/**
	 * Returns an iterable of blocks that drop rope when harvested.
	 *
	 * @return The iterable of blocks that drop rope
	 */
	private Iterable<Block> getBlocksThatDropRope() {
		return ForgeRegistries.BLOCKS.getEntries().stream()
									 .filter(e -> e.getKey().location().getNamespace().equals(Reference.MODID))
									 .filter(e -> e.getKey().location().getPath().contains("fence"))
									 .map(Map.Entry::getValue)
									 .collect(Collectors.toList());
	}

	protected LootTable.Builder createOreLikeDrops(Block pBlock, Item item) {
		return createSilkTouchDispatchTable(pBlock,
				this.applyExplosionDecay(pBlock,
						LootItem.lootTableItem(item)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
								.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
}
