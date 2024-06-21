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
    private static final String CATEGORY_GRAPE_VINES = "grape_vines";

    private static ForgeConfigSpec.IntValue brew_kettle_lit_light_level;
    private static ForgeConfigSpec.IntValue default_brewing_ticks;

    private static ForgeConfigSpec.IntValue grape_vine_min_fruit;
    private static ForgeConfigSpec.IntValue grape_vine_max_fruit;

    private static ForgeConfigSpec.IntValue hops_min_fruit;
    private static ForgeConfigSpec.IntValue hops_max_fruit;

    private static ForgeConfigSpec.IntValue loot_chance_pillager_outpost;
    private static ForgeConfigSpec.IntValue loot_chance_ocean_ruin;
    private static ForgeConfigSpec.IntValue loot_chance_shipwreck;
    private static ForgeConfigSpec.IntValue loot_chance_village;
    private static ForgeConfigSpec.IntValue loot_chance_beach_treasure;
    private static ForgeConfigSpec.IntValue loot_chance_dark_forest_mansion;
    private static ForgeConfigSpec.IntValue loot_chance_stronghold;

    static {
        initBrewKettleConfig(SERVER_BUILDER);
        initGrapeVineConfig(SERVER_BUILDER);
        initHopsCropConfig(SERVER_BUILDER);
        initLootConfig(SERVER_BUILDER);

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
        brew_kettle_lit_light_level = server
                .comment("Set the light level for the brew kettle when it is lit. Setting to 0 uses neighbor light level.")
                .defineInRange("brewKettle.LitLightLevel", 15, 0, 15);
        default_brewing_ticks = server
                .comment("Set the Brew Kettle processing time in ticks.")
                .defineInRange("brewKettle.DefaultProcessingTime", 600, 20, 24000);
    }

    public static void initGrapeVineConfig(ForgeConfigSpec.Builder server) {
        grape_vine_min_fruit = server
                .comment("Set to the minimum amount of fruit dropped by grape vines.")
                .defineInRange("grape_vine.min_fruit_yield", 1, 1, 100);
        grape_vine_max_fruit = server
                .comment("Set to the maximum amount of fruit dropped by grape vines.")
                .defineInRange("grape_vine.max_fruit_yield", 4, 1, 100);
    }

    public static void initHopsCropConfig(ForgeConfigSpec.Builder server) {
        hops_min_fruit = server
                .comment("Set to the minimum amount of hops dropped by Hope crops.")
                .defineInRange("hops_crop.min_fruit_yield", 1, 1, 100);
        hops_max_fruit = server
                .comment("Set to the maximum amount of hops dropped by Hope crops.")
                .defineInRange("hops_crop.max_fruit_yield", 3, 1, 100);
    }

    public static void initLootConfig(ForgeConfigSpec.Builder server) {
        server.push("bottles_in_loot_chest");  // spaces would be fine here but i'll follow the existing style.
        loot_chance_pillager_outpost = server
                .comment("Percentage chance you'll find a few bottles of mead in pillager tower chest. Number here seems high but that bum place has only one chest and this fits the theme.")
                .defineInRange("loot_chance_pillager_outpost", 90, 0, 100);   // spaces would be fine here but i'll follow the existing style.
        loot_chance_ocean_ruin = server
                .comment("Percentage chance you'll find a few bottles of wine in underwater ruin chest. Default of 10% is not low because ruins will have half a dozen chests.")
                .defineInRange("loot_chance_underwater_ruins", 10, 0, 100);
        loot_chance_shipwreck = server
                .comment("Percentage chance you'll find a few bottles of ale in shipwreck chest.")
                .defineInRange("loot_chance_shipwreck", 60, 0, 100);
        loot_chance_village = server
                .comment("Percentage chance you'll find a few bottles of wine in villager home chest.")
                .defineInRange("loot_chance_village", 0, 0, 100);   // default is 0 - disabled for now.
        loot_chance_beach_treasure = server
                .comment("Percentage chance you'll find a few bottles of wine in buried beach chest.")
                .defineInRange("loot_chance_beach_treasure", 0, 0, 100);   // default is 0 - disabled for now.
        loot_chance_dark_forest_mansion = server
                .comment("Percentage chance you'll find a few bottles of lager in woodland mansion chest.")
                .defineInRange("loot_chance_dark_forest_mansion", 15, 0, 100);
        loot_chance_stronghold = server
                .comment("Percentage chance you'll find a few bottles of wine in stronghold chest.")
                .defineInRange("loot_chance_stronghold", 0, 0, 100);   // default is 0 - disabled for now. some absorption won't be too bad.
        server.pop();
    }

    public static int getBrewKettleLitLightLevel() {
        return brew_kettle_lit_light_level.get();
    }

    public static int getDefaultProcessingTime() {
        return default_brewing_ticks.get();
    }

    public static int getGrapeVineMinFruitYield() {
        return grape_vine_min_fruit.get();
    }

    public static int getGrapeVineMaxFruitYield() {
        return grape_vine_max_fruit.get();
    }

    public static int getHopsCropMinFruitYield() {
        return hops_min_fruit.get();
    }

    public static int getHopsCropMaxFruitYield() {
        return hops_max_fruit.get();
    }

    // bug, may2024: all the above 6 values are unused

    public static int getLootChancePillagerTower() { return loot_chance_pillager_outpost.get(); }
    public static int getLootChanceOceanRuin()     { return loot_chance_ocean_ruin.get(); }
    public static int getLootChanceShipwreck()     { return loot_chance_shipwreck.get(); }
    public static int getLootChanceVillagerHome()  { return loot_chance_village.get(); }
    public static int getLootChanceBeachTreasure() { return loot_chance_beach_treasure.get(); }
    public static int getLootChanceMansion()       { return loot_chance_dark_forest_mansion.get(); }
    public static int getLootChanceStronghold()    { return loot_chance_stronghold.get(); }
}
