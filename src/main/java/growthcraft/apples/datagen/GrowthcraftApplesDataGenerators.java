package growthcraft.apples.datagen;

import growthcraft.apiary.datagen.GrowthcraftApiaryDataGenerators;
import growthcraft.apples.datagen.providers.GrowthcraftApplesLootTableProvider;
import growthcraft.apples.datagen.providers.GrowthcraftApplesRecipes;
import growthcraft.apples.shared.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftApplesDataGenerators {

	private GrowthcraftApplesDataGenerators() {
		/* Prevent generation of public constructor */
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();

		generator.addProvider(event.includeServer(), new GrowthcraftApplesRecipes(packOutput));
		generator.addProvider(event.includeServer(), new GrowthcraftApplesLootTableProvider(packOutput));
	}
}
