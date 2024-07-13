package growthcraft.cellar.datagen.providers;

import java.util.function.Consumer;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.shared.Reference;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import static growthcraft.lib.utils.FormatUtils.HAS_ITEM;

public class GrowthcraftCellarRecipes extends RecipeProvider{

	public GrowthcraftCellarRecipes(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GrowthcraftCellarBlocks.BREW_KETTLE.get())
		.requires(Blocks.CAULDRON)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CAULDRON))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftCellarItems.BREW_KETTLE_LID.get())
		.pattern(" i ")
		.pattern("III")
		.define('I', Tags.Items.INGOTS_IRON)
		.define('i', Tags.Items.NUGGETS_IRON)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftCellarBlocks.CULTURE_JAR.get())
		.pattern("BAB")
		.pattern("B B")
		.pattern("BBB")
		.define('A', ItemTags.PLANKS)
		.define('B', Tags.Items.GLASS_PANES)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get())
		.pattern("AAA")
		.pattern("BBB")
		.pattern("AAA")
		.define('A', Tags.Items.INGOTS_IRON)
		.define('B', Blocks.OAK_PLANKS)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.OAK_PLANKS))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftCellarBlocks.FRUIT_PRESS.get())
		.pattern("ABA")
		.pattern("CCC")
		.pattern("DDD")
		.define('A', Tags.Items.FENCES)
		.define('B', Blocks.PISTON)
		.define('C', Tags.Items.INGOTS_IRON)
		.define('D', ItemTags.PLANKS)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.PISTON))
		.save(consumer);
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.WHEAT), RecipeCategory.FOOD, GrowthcraftCellarItems.GRAIN.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
		.save(consumer);;
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftCellarBlocks.ROASTER.get())
		.pattern(" I ")
		.pattern(" B ")
		.pattern("I I")
		.define('I', Tags.Items.INGOTS_IRON)
		.define('B', GrowthcraftCellarBlocks.BREW_KETTLE.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftCellarBlocks.BREW_KETTLE.get()))
		.save(consumer);
	}
	
    @Override
    public String getName() {
        return "Growthcraft Cellar Recipes";
    }
}
