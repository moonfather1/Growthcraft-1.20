package growthcraft.apples.datagen.providers;

import java.util.Map;
import java.util.stream.Collectors;

import growthcraft.apples.block.AppleTreeLeaves;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.apples.shared.Reference;
import growthcraft.core.Growthcraft;
import growthcraft.core.block.RopeBlock;
import growthcraft.core.init.GrowthcraftItems;
import growthcraft.lib.block.GrowthcraftDoorBlock;
import growthcraft.lib.block.GrowthcraftSlabBlock;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class GrowthcraftApplesLootTables extends VanillaBlockLoot{
	
	@Override
	protected void generate() {
		getKnownBlocks().forEach(entry -> {
			if (entry instanceof GrowthcraftDoorBlock) {
				add(entry, createDoorTable(entry));
			}
			else if (entry instanceof GrowthcraftSlabBlock) {
				add(entry, createSlabItemTable(entry));
			}
			else if (entry instanceof AppleTreeLeaves) {
				add(entry, createLeavesDrops(entry, GrowthcraftApplesBlocks.APPLE_TREE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
			}
			else if (entry instanceof RopeBlock) {
				dropOther(entry, GrowthcraftItems.ROPE_LINEN.get());
			}
			else {
				dropSelf(entry);
			}
		});		
	}
	
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Reference.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
