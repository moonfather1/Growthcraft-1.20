package growthcraft.apiary.datagen.providers;

import java.util.Map;
import java.util.stream.Collectors;

import growthcraft.apiary.shared.Reference;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class GrowthcraftApiaryLootTables extends VanillaBlockLoot {
	
	@Override
	protected void generate() {
		getKnownBlocks().forEach(block -> dropSelf(block));
	}
	
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Reference.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
