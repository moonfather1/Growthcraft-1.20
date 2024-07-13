package growthcraft.apiary.init;

import growthcraft.apiary.shared.Reference;
import growthcraft.lib.item.GrowthcraftItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

public class GrowthcraftApiaryItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS, Reference.MODID
    );

    public static final RegistryObject<GrowthcraftItem> BEE = ITEMS.register(
            Reference.UnlocalizedName.BEE, GrowthcraftItem::new
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX, GrowthcraftItem::new
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_BLACK = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_BLACK, () -> new GrowthcraftItem(DyeColor.BLACK)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_BLUE = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_BLUE, () -> new GrowthcraftItem(DyeColor.BLUE)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_BROWN = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_BROWN, () -> new GrowthcraftItem(DyeColor.BROWN)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_CYAN = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_CYAN, () -> new GrowthcraftItem(DyeColor.CYAN)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_GRAY = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_GRAY, () -> new GrowthcraftItem(DyeColor.GRAY)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_GREEN = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_GREEN, () -> new GrowthcraftItem(DyeColor.GREEN)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_LIGHT_BLUE = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_LIGHT_BLUE, () -> new GrowthcraftItem(DyeColor.LIGHT_BLUE)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_LIGHT_GRAY = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_LIGHT_GRAY, () -> new GrowthcraftItem(DyeColor.LIGHT_GRAY)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_LIME = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_LIME, () -> new GrowthcraftItem(DyeColor.LIME)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_MAGENTA = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_MAGENTA, () -> new GrowthcraftItem(DyeColor.MAGENTA)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_ORANGE = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_ORANGE, () -> new GrowthcraftItem(DyeColor.ORANGE)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_PINK = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_PINK, () -> new GrowthcraftItem(DyeColor.PINK)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_PURPLE = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_PURPLE, () -> new GrowthcraftItem(DyeColor.PURPLE)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_RED = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_RED, () -> new GrowthcraftItem(DyeColor.RED)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_WHITE = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_WHITE, () -> new GrowthcraftItem(DyeColor.WHITE)
    );

    public static final RegistryObject<GrowthcraftItem> BEES_WAX_YELLOW = ITEMS.register(
            Reference.UnlocalizedName.BEES_WAX_YELLOW, () -> new GrowthcraftItem(DyeColor.YELLOW)
    );


    public static final RegistryObject<GrowthcraftItem> HONEY_COMB_EMPTY = ITEMS.register(
            Reference.UnlocalizedName.HONEY_COMB_EMPTY, GrowthcraftItem::new
    );

    public static final RegistryObject<GrowthcraftItem> HONEY_COMB_FULL = ITEMS.register(
            Reference.UnlocalizedName.HONEY_COMB_FULL, GrowthcraftItem::new
    );

    private GrowthcraftApiaryItems() {
        /* Prevent default public constructor */
    }

    public static boolean excludeItemRegistry(ResourceLocation registryName) {
        ArrayList<String> excludeItems = new ArrayList<>();
        //excludeItems.add(Reference.MODID + ":" + Reference.UnlocalizedName.APPLE_TREE_FRUIT);
        return excludeItems.contains(registryName.toString());
    }
}
