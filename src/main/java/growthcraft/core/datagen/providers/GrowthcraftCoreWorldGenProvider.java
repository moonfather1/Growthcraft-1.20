package growthcraft.core.datagen.providers;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import growthcraft.core.shared.Reference;
import growthcraft.core.world.GrowthcraftBiomeModifiers;
import growthcraft.core.world.GrowthcraftConfiguredFeatures;
import growthcraft.core.world.GrowthcraftPlacedFeatures;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class GrowthcraftCoreWorldGenProvider extends DatapackBuiltinEntriesProvider{
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, GrowthcraftConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, GrowthcraftPlacedFeatures::bootstrap)
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, GrowthcraftBiomeModifiers::bootstrap);
	
	public GrowthcraftCoreWorldGenProvider(PackOutput output, CompletableFuture<Provider> registries) {
		super(output, registries, BUILDER, Set.of(Reference.MODID));
	}
	
    @Override
    public String getName() {
        return "Growthcraft WorldGen";
    }

}
