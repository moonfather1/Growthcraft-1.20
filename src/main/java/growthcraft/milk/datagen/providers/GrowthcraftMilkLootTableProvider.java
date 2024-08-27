package growthcraft.milk.datagen.providers;

import growthcraft.milk.datagen.providers.loot.CheeseWheelLootTable;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;

public class GrowthcraftMilkLootTableProvider extends LootTableProvider {
    public GrowthcraftMilkLootTableProvider(PackOutput output) {
        super(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(CheeseWheelLootTable::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(GrowthcraftMilkLootTables::new, LootContextParamSets.BLOCK)
        ));
    }

    @Override
    public String getName() {
        return "Growthcraft Milk Loot Tables";
    }
}
