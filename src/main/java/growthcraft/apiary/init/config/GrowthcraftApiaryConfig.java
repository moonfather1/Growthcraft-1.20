package growthcraft.apiary.init.config;

import growthcraft.core.init.config.GrowthcraftConfig;
import growthcraft.lib.utils.FormatUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

public class GrowthcraftApiaryConfig {

    public static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SERVER;

    public static final String SERVER_CONFIG = "growthcraft-apiary-server.toml";

    private static final String CATEGORY_BEE_BOX = "beeBox";

    private static ForgeConfigSpec.IntValue flowerReplicationRange;
    private static ForgeConfigSpec.IntValue beeBoxCycleUpdateTicks;
    private static ForgeConfigSpec.BooleanValue doFlowerReplication;
    private static ForgeConfigSpec.IntValue beeBreedingChance;
    private static ForgeConfigSpec.IntValue flowerReplicationChance;
    private static ForgeConfigSpec.IntValue flowerSaturationPercent;

    static {
        initServerConfig(SERVER_BUILDER);
        SERVER = SERVER_BUILDER.build();
    }

    private GrowthcraftApiaryConfig() {
        /* Prevent generation of public constructor */
    }

    public static void loadConfig() {
        loadConfig(SERVER, FMLPaths.CONFIGDIR.get().resolve(SERVER_CONFIG).toString());
    }

    public static void loadConfig(ForgeConfigSpec configSpec, String path) {
        GrowthcraftConfig.loadConfig(configSpec, path);
    }

    public static void initServerConfig(ForgeConfigSpec.Builder specBuilder) {
        flowerReplicationRange = specBuilder
                .comment("Set the range for the bee box to look for flowers.")
                .defineInRange(String.format(FormatUtils.STRING_DOT_STRING, CATEGORY_BEE_BOX, "flowerRange"), 4, 0, 18);
        beeBoxCycleUpdateTicks = specBuilder
                .comment("Set the process time for the bee box to update. Default is once a minute.")
                .defineInRange(String.format(FormatUtils.STRING_DOT_STRING, CATEGORY_BEE_BOX, "maxProcessingTime"), 1200, 200, 1728000);
        beeBreedingChance = specBuilder
                .comment("Set the percentage chance to increment bee population in the Bee Box.")
                .defineInRange(String.format(FormatUtils.STRING_DOT_STRING, CATEGORY_BEE_BOX, "chanceBeeIncrement"), 33, 1, 100);
        flowerReplicationChance = specBuilder
                .comment("Set the percentage chance to replicate a flower near by.")
                .defineInRange(String.format(FormatUtils.STRING_DOT_STRING, CATEGORY_BEE_BOX, "chanceReplicateFlower"), 10, 1, 100);
        doFlowerReplication = specBuilder
                .comment("Set to false to disable flower replication by the bee box")
                .define(String.format(FormatUtils.STRING_DOT_STRING, CATEGORY_BEE_BOX, "replicateFlowers"), true);
        flowerSaturationPercent = specBuilder
                .comment("Set the percentage of the area to be filled with replicated flowers.")
                .defineInRange(String.format(FormatUtils.STRING_DOT_STRING, CATEGORY_BEE_BOX, "flowerReplicationAreaPercent"), 100, 1, 100);
    }

    public static int getBeeBoxFlowerRange() {
        return flowerReplicationRange.get();
    }

    public static int getBeeBoxMaxProcessingTime() {
        return beeBoxCycleUpdateTicks.get();
    }

    public static boolean shouldReplicateFlowers() {
        return doFlowerReplication.get();
    }

    public static int getChanceToIncreaseBees() {
        return beeBreedingChance.get();
    }

    public static int getChanceToReplicateFlowers() {
        return flowerReplicationChance.get();
    }

    public static int getFlowerReplicationAreaPercent() {
        return flowerSaturationPercent.get();
    }

}