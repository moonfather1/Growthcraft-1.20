package growthcraft.core.world;

import growthcraft.core.Growthcraft;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.config.GrowthcraftConfig;
import growthcraft.core.shared.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class GrowthcraftConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SALT_ORE_KEY
            = registerKey(Reference.UnlocalizedName.SALT_ORE);

    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_SALT_ORE_KEY
            = registerKey("nether_" + Reference.UnlocalizedName.SALT_ORE);

    public static final ResourceKey<ConfiguredFeature<?, ?>> END_SALT_ORE_KEY
            = registerKey("end_" + Reference.UnlocalizedName.SALT_ORE);

    public static final ResourceKey<ConfiguredFeature<?, ?>> DEEPSLATE_SALT_ORE_KEY
            = registerKey("deepslate_" + Reference.UnlocalizedName.SALT_ORE);

    private static final int SALT_ORE_GEN_VEIN_SIZE
            = GrowthcraftConfig.getSaltOreGenVeinSize(); // Iron is 9, Diamond is 0.7

    private GrowthcraftConfiguredFeatures() {
        /* Prevent generation of default public constructor. */
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplaceable = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endstoneReplaceable = new BlockMatchTest(Blocks.END_STONE);
        
        if(GrowthcraftConfig.isSaltOreGenEnabled()) {
            register(context, SALT_ORE_KEY, Feature.ORE, new OreConfiguration(stoneReplaceable,
                    GrowthcraftBlocks.SALT_ORE.get().defaultBlockState(), SALT_ORE_GEN_VEIN_SIZE));

            if (GrowthcraftConfig.isSaltOreGenEnabledForDimension("deepslate")) {
                register(context, DEEPSLATE_SALT_ORE_KEY, Feature.ORE, new OreConfiguration(deepslateReplaceable,
                        GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get().defaultBlockState(), SALT_ORE_GEN_VEIN_SIZE));
            }

            if (GrowthcraftConfig.isSaltOreGenEnabledForDimension("theend")) {
                register(context, END_SALT_ORE_KEY, Feature.ORE, new OreConfiguration(endstoneReplaceable,
                        GrowthcraftBlocks.SALT_ORE_END.get().defaultBlockState(), SALT_ORE_GEN_VEIN_SIZE));
            }

            if (GrowthcraftConfig.isSaltOreGenEnabledForDimension("nether")) {
                register(context, NETHER_SALT_ORE_KEY, Feature.ORE, new OreConfiguration(netherrackReplaceable,
                        GrowthcraftBlocks.SALT_ORE_NETHER.get().defaultBlockState(), SALT_ORE_GEN_VEIN_SIZE));
            }
        } else {
            Growthcraft.LOGGER.info("Growthcraft Core config has salt generation disabled.");
        }
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Reference.MODID, name));
    }

    private static <FeatureConfig extends FeatureConfiguration, FeatureType extends Feature<FeatureConfig>> void register(
            BootstapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            FeatureType feature,
            FeatureConfig configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
