package growthcraft.core.datagen.providers;

import java.util.function.Consumer;

import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftItems;
import growthcraft.core.shared.Reference;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class GrowthcraftCoreRecipes extends RecipeProvider{

	public GrowthcraftCoreRecipes(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_BLACK.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.BLACK_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_BLUE.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.BLUE_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_BROWN.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.BROWN_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_CYAN.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.CYAN_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_GRAY.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.GRAY_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_GREEN.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.GREEN_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_LIGHT_BLUE.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.LIGHT_BLUE_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_LIGHT_GRAY.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.LIGHT_GRAY_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_LIME.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.LIME_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_MAGENTA.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.MAGENTA_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_ORANGE.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.ORANGE_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_PINK.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.PINK_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_PURPLE.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.PURPLE_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_RED.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.RED_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_WHITE.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.WHITE_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.CROWBAR_YELLOW.get())
		.pattern("  A")
		.pattern("CBC")
		.pattern("A  ")
		.define('A', Tags.Items.NUGGETS_IRON)
		.define('B', Tags.Items.INGOTS_IRON)
		.define('C', Items.YELLOW_CARPET)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftItems.ROPE_LINEN.get(), 8)
		.pattern("AAA")
		.pattern("ABA")
		.pattern("AAA")
		.define('A', Items.STRING)
		.define('B', Items.LEAD)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEAD))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftItems.ROPE_LINEN.get(), 8)
		.pattern("AAA")
		.pattern("ABA")
		.pattern("AAA")
		.define('A', Items.STRING)
		.define('B', GrowthcraftItems.ROPE_LINEN.get())
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEAD))
		.save(consumer, new ResourceLocation(Reference.MODID,"rope_linen_lengthen"));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftBlocks.SALT_BLOCK.get())
		.pattern("###")
		.pattern("###")
		.pattern("###")
		.define('#', GrowthcraftItems.SALT.get())
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftItems.SALT.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GrowthcraftItems.SALT.get(),9)
		.requires(GrowthcraftBlocks.SALT_BLOCK.get())
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftItems.SALT.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftItems.WRENCH.get())
		.pattern(" BB")
		.pattern(" AB")
		.pattern("A  ")
		.define('A', Tags.Items.INGOTS_IRON)
		.define('B', Tags.Items.NUGGETS_IRON)
		.group(Reference.MODID)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftItems.SALT.get()))
		.save(consumer);
	}
	
    @Override
    public String getName() {
        return "Growthcraft Core Recipes";
    }
}
