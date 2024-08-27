package growthcraft.cellar.datagen.providers;

import java.util.Collections;
import java.util.List;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class GrowthcraftCellarLootTableProvider extends LootTableProvider{

	public GrowthcraftCellarLootTableProvider(PackOutput output) {
		super(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(GrowthcraftCellarBlockLootTables::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(GrowthcraftCellarChestLootTables::new, LootContextParamSets.CHEST)
        ));
	}

	@Override
	public String getName() {
		return "Growthcraft Cellar Loot Tables";
	}
}
