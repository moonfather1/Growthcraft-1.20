package growthcraft.lib.utils;

public class FormatUtils {
    public static final String STRING_COLON_STRING = "%s:%s";
    public static final String STRING_DOT_STRING = "%s.%s";
    public static final String FLUID_TANK_SLOT = "fluid_tank_%s_%d";

    public static final String HAS_ITEM = "has_item";
    public static final String INVENTORY = "inventory";
    public static final String INPUT = "input";
    public static final String OUTPUT = "output";

    private FormatUtils() {
        /* Disable Automatic Creation of Public Constructor */
    }

    public static String fluidTankSlot(String predicate, int slot) {
        return String.format(FLUID_TANK_SLOT, predicate, slot);
    }

}
