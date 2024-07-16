package growthcraft.apiary.datagen;

import growthcraft.apiary.datagen.providers.GrowthcraftApiaryGlobalLootModifiersProvider;
import growthcraft.apiary.datagen.providers.GrowthcraftApiaryLootTableProvider;
import growthcraft.apiary.datagen.providers.GrowthcraftApiaryRecipes;
import growthcraft.apiary.shared.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftApiaryDataGenerators {
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();

		generator.addProvider(event.includeServer(), new GrowthcraftApiaryRecipes(packOutput));
		generator.addProvider(event.includeServer(), new GrowthcraftApiaryLootTableProvider(packOutput));
//		generator.addProvider(event.includeServer(), new GrowthcraftApiaryGlobalLootModifiersProvider(packOutput));
	}
}
