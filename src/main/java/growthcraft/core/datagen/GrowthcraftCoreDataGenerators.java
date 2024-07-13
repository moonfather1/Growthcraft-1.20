package growthcraft.core.datagen;

import java.util.concurrent.CompletableFuture;

import growthcraft.core.datagen.providers.GrowthcraftCoreBlockTags;
import growthcraft.core.datagen.providers.GrowthcraftCoreItemTags;
import growthcraft.core.datagen.providers.GrowthcraftCoreLootTableProvider;
import growthcraft.core.datagen.providers.GrowthcraftCoreRecipes;
import growthcraft.core.shared.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftCoreDataGenerators {
	
	private GrowthcraftCoreDataGenerators(){
	}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		GrowthcraftCoreBlockTags blockTags = new GrowthcraftCoreBlockTags(packOutput, lookupProvider, existingFileHelper);
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(),  new GrowthcraftCoreItemTags(packOutput, lookupProvider, blockTags, existingFileHelper));
		generator.addProvider(event.includeServer(), new GrowthcraftCoreRecipes(packOutput));
		generator.addProvider(event.includeServer(), new GrowthcraftCoreLootTableProvider(packOutput));

//		generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
//                List.of(new LootTableProvider.SubProviderEntry(GrowthcraftCoreLootTables::new, LootContextParamSets.BLOCK))));
	}

	
}
