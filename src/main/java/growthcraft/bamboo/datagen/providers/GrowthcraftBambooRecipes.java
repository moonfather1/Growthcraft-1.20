package growthcraft.bamboo.datagen.providers;

import java.util.function.Consumer;

import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.bamboo.shared.Reference;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

public class GrowthcraftBambooRecipes extends RecipeProvider{

	public GrowthcraftBambooRecipes(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, GrowthcraftBambooBlocks.BAMBOO_PLANK_BEE_BOX.get())
			.pattern("SPS")
			.pattern("PBP")
			.pattern("SPS")
			.define('B', Ingredient.of(Items.BAMBOO_SLAB, Items.BAMBOO_MOSAIC_SLAB))
			.define('P', Ingredient.of(Items.BAMBOO_PLANKS, Items.BAMBOO_MOSAIC))
			.define('S', Tags.Items.RODS_WOODEN)
			.group(Reference.MODID)
			.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAMBOO_PLANKS))
			.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get(), 2)
			.pattern("B")
			.pattern("B")
			.pattern("B")
			.define('B', Items.BAMBOO_BLOCK)
			.group(Reference.MODID)
			.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAMBOO_BLOCK))
			.save(consumer);
	}
	
    @Override
    public String getName() {
        return "Growthcraft Bamboo Recipes";
    }
}
