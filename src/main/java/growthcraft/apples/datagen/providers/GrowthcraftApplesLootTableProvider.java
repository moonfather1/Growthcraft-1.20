package growthcraft.apples.datagen.providers;

import java.util.Collections;
import java.util.List;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class GrowthcraftApplesLootTableProvider extends LootTableProvider{

	public GrowthcraftApplesLootTableProvider(PackOutput output) {
		super(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(GrowthcraftApplesLootTables::new, LootContextParamSets.BLOCK)
        ));
	}

	@Override
	public String getName() {
		return "Growthcraft Apples Loot Tables";
	}
}
