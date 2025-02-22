package growthcraft.milk.datagen;

import growthcraft.bamboo.datagen.providers.GrowthcraftBambooTagsProvider;
import growthcraft.milk.datagen.providers.*;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.shared.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftMilkDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event. getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(
                event.includeServer(),
                new GrowthcraftMilkLootTableProvider(output)
        );

        generator.addProvider(event.includeClient(), new GrowthcraftMilkBlockStateProvider(output, helper));
        generator.addProvider(event.includeServer(), new GrowthcraftMilkRecipes(output));
        BlockTagsProvider blockProvider =
                generator.addProvider(event.includeServer(), new GrowthcraftMilkBlockTagsProvider(output, event.getLookupProvider(), growthcraft.milk.shared.Reference.MODID, helper));
        generator.addProvider(event.includeServer(), new GrowthcraftMilkItemTagsProvider(output, event.getLookupProvider(), blockProvider.contentsGetter(), growthcraft.milk.shared.Reference.MODID, helper));
    }

    //@SubscribeEvent
    public static void modelBake(ModelEvent.ModifyBakingResult event) {
        GrowthcraftMilkItems.addItemModelProperties();
    }
}
