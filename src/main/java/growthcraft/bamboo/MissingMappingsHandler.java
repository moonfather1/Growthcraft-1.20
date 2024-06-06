package growthcraft.bamboo;

import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.bamboo.shared.Reference;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class MissingMappingsHandler
{
    @SubscribeEvent
    public static void mappingEvent(MissingMappingsEvent event) {
        List<MissingMappingsEvent.Mapping<Block>> blockList = event.getMappings(Registries.BLOCK, Reference.MODID);
        for (MissingMappingsEvent.Mapping<Block> mapping : blockList) {
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK)) mapping.remap(Blocks.BAMBOO_PLANKS); // could have made a list but not worth the bother
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_STAIRS)) mapping.remap(Blocks.BAMBOO_STAIRS);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_BUTTON)) mapping.remap(Blocks.BAMBOO_BUTTON);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_DOOR)) mapping.remap(Blocks.BAMBOO_DOOR);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_FENCE)) mapping.remap(Blocks.BAMBOO_FENCE);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_FENCE_GATE)) mapping.remap(Blocks.BAMBOO_FENCE_GATE);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_PRESSURE_PLATE)) mapping.remap(Blocks.BAMBOO_PRESSURE_PLATE);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_TRAPDOOR)) mapping.remap(Blocks.BAMBOO_TRAPDOOR);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_SLAB)) mapping.remap(Blocks.BAMBOO_SLAB);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_WOOD)) mapping.remap(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get());
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_WOOD_LOG)) mapping.remap(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get());
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_WOOD_LOG_STRIPPED)) mapping.remap(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get());
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_WOOD_STRIPPED)) mapping.remap(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get());
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_FENCE_ROPE_LINEN)) mapping.remap(GrowthcraftBlocks.ROPE_LINEN_BAMBOO_FENCE.get());
        }
        List<MissingMappingsEvent.Mapping<Item>> itemList = event.getMappings(Registries.ITEM, Reference.MODID);
        for (MissingMappingsEvent.Mapping<Item> mapping : itemList) {
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK)) mapping.remap(Items.BAMBOO_PLANKS);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_STAIRS)) mapping.remap(Items.BAMBOO_STAIRS);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_BUTTON)) mapping.remap(Items.BAMBOO_BUTTON);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_DOOR)) mapping.remap(Items.BAMBOO_DOOR);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_FENCE)) mapping.remap(Items.BAMBOO_FENCE);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_FENCE_GATE)) mapping.remap(Items.BAMBOO_FENCE_GATE);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_PRESSURE_PLATE)) mapping.remap(Items.BAMBOO_PRESSURE_PLATE);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_TRAPDOOR)) mapping.remap(Items.BAMBOO_TRAPDOOR);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_PLANK_SLAB)) mapping.remap(Items.BAMBOO_SLAB);
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_WOOD)) mapping.remap(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get().asItem());
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_WOOD_LOG)) mapping.remap(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get().asItem());
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_WOOD_LOG_STRIPPED)) mapping.remap(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get().asItem());
            if (mapping.getKey().getPath().equals(Reference.UnlocalizedName.BAMBOO_WOOD_STRIPPED)) mapping.remap(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get().asItem());
        }
    }
}
