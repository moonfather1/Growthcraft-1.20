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

    private static ForgeConfigSpec.IntValue lootChancePillagerOutpost;
    private static ForgeConfigSpec.IntValue lootChanceOceanRuin;
    private static ForgeConfigSpec.IntValue lootChanceShipwreck;
    private static ForgeConfigSpec.IntValue lootChanceVillage;
    private static ForgeConfigSpec.IntValue lootChanceBeachTreasure;
    private static ForgeConfigSpec.IntValue lootChanceDarkForestMansion;
    private static ForgeConfigSpec.IntValue lootChanceStronghold;

    static {
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

    public static void initLootConfig(ForgeConfigSpec.Builder server) {
        server.push("bottles_in_loot_chest");  // spaces would be fine here but i'll follow the existing style.
        lootChancePillagerOutpost = server
                .comment("Percentage chance you'll find a few bottles of mead in pillager tower chest. Number here seems high but that bum place has only one chest and this fits the theme.")
                .defineInRange("loot_chance_pillager_outpost", 90, 0, 100);
        lootChanceOceanRuin = server
                .comment("Percentage chance you'll find a few bottles of wine in underwater ruin chest. Default of 10% is not low because ruins will have half a dozen chests.")
                .defineInRange("loot_chance_underwater_ruins", 10, 0, 100);
        lootChanceShipwreck = server
                .comment("Percentage chance you'll find a few bottles of ale in shipwreck chest.")
                .defineInRange("loot_chance_shipwreck", 60, 0, 100);
        lootChanceVillage = server
                .comment("Percentage chance you'll find a few bottles of wine in villager home chest.")
                .defineInRange("loot_chance_village", 0, 0, 100);   // default is 0 - disabled for now.
        lootChanceBeachTreasure = server
                .comment("Percentage chance you'll find a few bottles of wine in buried beach chest.")
                .defineInRange("loot_chance_beach_treasure", 0, 0, 100);   // default is 0 - disabled for now.
        lootChanceDarkForestMansion = server
                .comment("Percentage chance you'll find a few bottles of lager in woodland mansion chest.")
                .defineInRange("loot_chance_dark_forest_mansion", 15, 0, 100);
        lootChanceStronghold = server
                .comment("Percentage chance you'll find a few bottles of wine in stronghold chest.")
                .defineInRange("loot_chance_stronghold", 0, 0, 100);   // default is 0 - disabled for now. some absorption won't be too bad.
        server.pop();
    }

    public static int getLootChancePillagerTower() {
        return lootChancePillagerOutpost.get();
    }

    public static int getLootChanceOceanRuin() {
        return lootChanceOceanRuin.get();
    }

    public static int getLootChanceShipwreck() {
        return lootChanceShipwreck.get();
    }

    public static int getLootChanceVillagerHome() {
        return lootChanceVillage.get();
    }

    public static int getLootChanceBeachTreasure() {
        return lootChanceBeachTreasure.get();
    }

    public static int getLootChanceMansion() {
        return lootChanceDarkForestMansion.get();
    }

    public static int getLootChanceStronghold() {
        return lootChanceStronghold.get();
    }

}
