package growthcraft.rice.datagen;

import growthcraft.rice.datagen.providers.GrowthcraftRiceGlobalLootModifiersProvider;
import growthcraft.rice.datagen.providers.GrowthcraftRiceRecipes;
import growthcraft.rice.shared.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftRiceDataGenerators {

	private GrowthcraftRiceDataGenerators() {
		/* Prevent generation of public constructor. */
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		
		generator.addProvider(event.includeServer(), new GrowthcraftRiceRecipes(packOutput));
//		generator.addProvider(event.includeServer(), new GrowthcraftRiceGlobalLootModifiersProvider(packOutput));
	}
}
