package growthcraft.milk.datagen;

import growthcraft.milk.datagen.providers.GrowthcraftMilkBlockStateProvider;
import growthcraft.milk.datagen.providers.GrowthcraftMilkLootTableProvider;
import growthcraft.milk.datagen.providers.GrowthcraftMilkRecipes;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.shared.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftMilkDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(
                event.includeServer(),
                new GrowthcraftMilkLootTableProvider(output)
        );

        generator.addProvider(event.includeClient(), new GrowthcraftMilkBlockStateProvider(output, helper));
        generator.addProvider(event.includeClient(), new GrowthcraftMilkRecipes(output));

    }

    //@SubscribeEvent
    public static void modelBake(ModelEvent.ModifyBakingResult event) {
        GrowthcraftMilkItems.addItemModelProperties();
    }


}
