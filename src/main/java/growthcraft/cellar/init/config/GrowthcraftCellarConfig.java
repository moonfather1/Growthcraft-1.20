package growthcraft.cellar.init.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class GrowthcraftCellarConfig {

    public static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SERVER;
    public static final String SERVER_CONFIG = "growthcraft-cellar-server.toml";

    private static final String CATEGORY_BREW_KETTLE = "brew_kettle";

    private static ForgeConfigSpec.IntValue loot_chance_pillager_outpost;
    private static ForgeConfigSpec.IntValue loot_chance_ocean_ruin;
    private static ForgeConfigSpec.IntValue loot_chance_shipwreck;
    private static ForgeConfigSpec.IntValue loot_chance_village;
    private static ForgeConfigSpec.IntValue loot_chance_beach_treasure;
    private static ForgeConfigSpec.IntValue loot_chance_dark_forest_mansion;
    private static ForgeConfigSpec.IntValue loot_chance_stronghold;

    static {
        initBrewKettleConfig(SERVER_BUILDER);
        SERVER = SERVER_BUILDER.build();
    }

    private GrowthcraftCellarConfig() {
        /* Disable default public constructor */
    }

    public static void loadConfig() {
        loadConfig(SERVER, FMLPaths.CONFIGDIR.get().resolve(SERVER_CONFIG).toString());
    }

    public static void loadConfig(ForgeConfigSpec configSpec, String path) {
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(
                new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();

        fileConfig.load();
        configSpec.setConfig(fileConfig);
    }

    public static void initBrewKettleConfig(ForgeConfigSpec.Builder server) {
        // brew_kettle_lit_light_level = server
        //        .comment("Set the light level for the brew kettle when it is lit. Setting to 0 uses neighbor light level.")
        //        .defineInRange(String.format("%s.%s", CATEGORY_BREW_KETTLE, "LitLightLevel"), 15, 0, 15);
    }

}
