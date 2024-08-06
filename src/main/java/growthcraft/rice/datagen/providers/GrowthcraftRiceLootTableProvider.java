package growthcraft.rice.datagen.providers;

import java.util.Collections;
import java.util.List;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class GrowthcraftRiceLootTableProvider extends LootTableProvider{

	public GrowthcraftRiceLootTableProvider(PackOutput output) {
		super(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(GrowthcraftRiceLootTables::new, LootContextParamSets.BLOCK)
        ));
	}

	@Override
	public String getName() {
		return "Growthcraft Rice Loot Tables";
	}
}
