package growthcraft.bamboo.datagen;

import growthcraft.bamboo.datagen.providers.GrowthcraftBambooLootTableProvider;
import growthcraft.bamboo.datagen.providers.GrowthcraftBambooRecipes;
import growthcraft.bamboo.datagen.providers.GrowthcraftBambooTagsProvider;
import growthcraft.bamboo.shared.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftBambooDataGenerators{
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		
		generator.addProvider(event.includeServer(), new GrowthcraftBambooRecipes(packOutput));
		generator.addProvider(event.includeServer(), new GrowthcraftBambooLootTableProvider(packOutput));
		generator.addProvider(event.includeServer(), new GrowthcraftBambooTagsProvider(packOutput, event.getLookupProvider(), Reference.MODID, null));
	}
}
