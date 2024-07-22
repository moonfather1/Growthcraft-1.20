package growthcraft.cellar.datagen;

import growthcraft.cellar.datagen.providers.GrowthcraftCellarGlobalLootModifiersProvider;
import growthcraft.cellar.datagen.providers.GrowthcraftCellarLootTableProvider;
import growthcraft.cellar.datagen.providers.GrowthcraftCellarRecipes;
import growthcraft.cellar.shared.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftCellarDataGenerators {

	private GrowthcraftCellarDataGenerators() {
		/* Prevent generation of public constructor. */
	}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		
		generator.addProvider(event.includeServer(), new GrowthcraftCellarRecipes(packOutput));
		generator.addProvider(event.includeServer(), new GrowthcraftCellarLootTableProvider(packOutput));
//		generator.addProvider(event.includeServer(), new GrowthcraftCellarGlobalLootModifiersProvider(packOutput));
	}
}
