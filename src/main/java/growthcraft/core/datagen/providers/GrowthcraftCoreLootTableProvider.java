package growthcraft.core.datagen.providers;

import java.util.Collections;
import java.util.List;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class GrowthcraftCoreLootTableProvider extends LootTableProvider{

	public GrowthcraftCoreLootTableProvider(PackOutput output) {
		super(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(GrowthcraftCoreLootTables::new, LootContextParamSets.BLOCK)
        ));
	}

	@Override
	public String getName() {
		return "Growthcraft Core Loot Tables";
	}
}
