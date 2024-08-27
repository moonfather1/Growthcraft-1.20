package growthcraft.apples.datagen.providers;

import java.util.function.Consumer;

import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.apples.init.GrowthcraftApplesItems;
import growthcraft.apples.shared.Reference;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import static growthcraft.lib.utils.FormatUtils.HAS_ITEM;

public class GrowthcraftApplesRecipes extends RecipeProvider{

	public GrowthcraftApplesRecipes(PackOutput  packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_PLANK_BUTTON.get())
		.requires(GrowthcraftApplesBlocks.APPLE_PLANK.get().asItem())
		.group("wooden_button")
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_PLANK.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_PLANK_DOOR.get(), 3)
		.pattern("##")
		.pattern("##")
		.pattern("##")
		.define('#', GrowthcraftApplesBlocks.APPLE_PLANK.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_PLANK.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_GATE.get(), 3)
		.pattern("121")
		.pattern("121")
		.define('1', Items.STICK)
		.define('2', GrowthcraftApplesBlocks.APPLE_PLANK.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_PLANK.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_PLANK_FENCE.get(), 3)
		.pattern("212")
		.pattern("212")
		.define('1', Items.STICK)
		.define('2', GrowthcraftApplesBlocks.APPLE_PLANK.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_PLANK.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_PLANK_PRESSURE_PLATE.get())
		.pattern("##")
		.define('#', GrowthcraftApplesBlocks.APPLE_PLANK.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_PLANK.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_PLANK_SLAB.get(), 6)
		.pattern("###")
		.define('#', GrowthcraftApplesBlocks.APPLE_PLANK.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_PLANK.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_PLANK_STAIRS.get(), 4)
		.pattern("#  ")
		.pattern("## ")
		.pattern("###")
		.define('#', GrowthcraftApplesBlocks.APPLE_PLANK.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_PLANK.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_PLANK_TRAPDOOR.get())
		.pattern("###")
		.pattern("###")
		.define('#', GrowthcraftApplesBlocks.APPLE_PLANK.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_PLANK.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_PLANK.get(), 4)
		.requires(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get().asItem())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GrowthcraftApplesItems.APPLE_SEEDS.get())
		.requires(Items.APPLE)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.APPLE))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesBlocks.APPLE_WOOD.get(), 3)
		.pattern("##")
		.pattern("##")
		.define('#', GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, GrowthcraftApplesBlocks.BEE_BOX_APPLE.get())
		.pattern("SPS")
		.pattern("PBP")
		.pattern("SPS")
		.define('B', GrowthcraftApplesBlocks.APPLE_PLANK_SLAB.get())
		.define('P', GrowthcraftApplesBlocks.APPLE_PLANK.get())
		.define('S', Tags.Items.RODS_WOODEN)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApplesBlocks.APPLE_PLANK.get()))
		.save(consumer);
	}
	
    @Override
    public String getName() {
        return "Growthcraft Apples Recipes";
    }
}
