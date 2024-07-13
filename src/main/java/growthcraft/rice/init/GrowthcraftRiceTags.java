package growthcraft.rice.init;

import growthcraft.core.shared.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class GrowthcraftRiceTags {
    public static void init() {
        Blocks.init();
        Items.init();
        Fluids.init();
    }

    private GrowthcraftRiceTags() {
        /* Private constructor to hide the implicit public one. */
    }

    public static class Blocks {

        private Blocks() {
            /* Private constructor to hide the implicit public one. */
        }

        private static void init(){
            // Do nothing, simply instantiate static variables
        }

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Reference.MODID, name));
        }
    }

    public static class Items {
        private Items() {
            /* Private constructor to hide the implicit public one. */
        }

        private static void init(){
            // Do nothing, simply instantiate static variables
        }
        
        public static final TagKey<Item> TAG_CROPS_RICE = forgeTag(Reference.UnlocalizedName.TAG_CROPS_RICE);
        public static final TagKey<Item> TAG_GRAIN_RICE = forgeTag(Reference.UnlocalizedName.TAG_GRAIN_RICE);

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Reference.MODID, name));
        }
        
        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Fluids {
        private Fluids() {
            /* Private constructor to hide the implicit public one. */
        }

        private static void init() {
            // Do nothing, simply instantiate static variables
        }

        private static TagKey<Fluid> tag(String name) {
            return FluidTags.create(new ResourceLocation(Reference.MODID, name));
        }
    }
}
