package growthcraft.core.world;

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
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SALT_ORE_KEY
            = registerKey(Reference.UnlocalizedName.SALT_ORE);

    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_SALT_ORE_KEY
            = registerKey(Reference.UnlocalizedName.SALT_ORE + "_nether");

    public static final ResourceKey<ConfiguredFeature<?, ?>> END_SALT_ORE_KEY
            = registerKey(Reference.UnlocalizedName.SALT_ORE + "_end");

    public static final ResourceKey<ConfiguredFeature<?, ?>> DEEPSLATE_SALT_ORE_KEY
            = registerKey(Reference.UnlocalizedName.SALT_ORE + "_deepslate");

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

        register(context, OVERWORLD_SALT_ORE_KEY, Feature.ORE, new OreConfiguration(stoneReplaceable,
                GrowthcraftBlocks.SALT_ORE.get().defaultBlockState(), SALT_ORE_GEN_VEIN_SIZE));
        register(context, DEEPSLATE_SALT_ORE_KEY, Feature.ORE, new OreConfiguration(deepslateReplaceable,
                GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get().defaultBlockState(), SALT_ORE_GEN_VEIN_SIZE));
        register(context, END_SALT_ORE_KEY, Feature.ORE, new OreConfiguration(endstoneReplaceable,
                GrowthcraftBlocks.SALT_ORE_END.get().defaultBlockState(), 9));
        register(context, NETHER_SALT_ORE_KEY, Feature.ORE, new OreConfiguration(netherrackReplaceable,
                GrowthcraftBlocks.SALT_ORE_NETHER.get().defaultBlockState(), 9));
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
