package growthcraft.bamboo.datagen.providers;

import java.util.Collections;
import java.util.List;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class GrowthcraftBambooLootTableProvider extends LootTableProvider {

	public GrowthcraftBambooLootTableProvider(PackOutput output) {
		super(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(GrowthcraftBambooLootTables::new, LootContextParamSets.BLOCK)
        ));
	}

	@Override
	public String getName() {
		return "Growthcraft Bamboo Loot Tables";
	}
}
