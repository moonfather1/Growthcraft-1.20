package growthcraft.apiary.datagen.providers;

import java.util.function.Consumer;

import growthcraft.apiary.init.GrowthcraftApiaryBlocks;
import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.apiary.init.GrowthcraftApiaryTags;
import growthcraft.apiary.shared.Reference;
import growthcraft.lib.item.GrowthcraftItem;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import static growthcraft.lib.utils.FormatUtils.HAS_ITEM;

public class GrowthcraftApiaryRecipes extends RecipeProvider{

	public GrowthcraftApiaryRecipes(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		// Bee Box Recipes
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_ACACIA.get(),
				Items.ACACIA_SLAB, Items.ACACIA_PLANKS, Tags.Items.RODS_WOODEN);
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_BIRCH.get(),
				Items.BIRCH_SLAB, Items.BIRCH_PLANKS, Tags.Items.RODS_WOODEN);
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_CHERRY.get(),
				Items.CHERRY_SLAB, Items.CHERRY_PLANKS, Tags.Items.RODS_WOODEN);
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_CRIMSON.get(),
				Items.CRIMSON_SLAB, Items.CRIMSON_PLANKS, Tags.Items.RODS_WOODEN);
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_DARK_OAK.get(),
				Items.DARK_OAK_SLAB, Items.DARK_OAK_PLANKS, Tags.Items.RODS_WOODEN);
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_JUNGLE.get(),
				Items.JUNGLE_SLAB, Items.JUNGLE_PLANKS, Tags.Items.RODS_WOODEN);
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_MANGROVE.get(),
				Items.MANGROVE_SLAB, Items.MANGROVE_PLANKS, Tags.Items.RODS_WOODEN);
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_OAK.get(),
				Items.OAK_SLAB, Items.OAK_PLANKS, Tags.Items.RODS_WOODEN);
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_SPRUCE.get(),
				Items.SPRUCE_SLAB, Items.SPRUCE_PLANKS, Tags.Items.RODS_WOODEN);
		this.beeBoxRecipeBuilder(consumer, GrowthcraftApiaryBlocks.BEE_BOX_WARPED.get(),
				Items.WARPED_SLAB, Items.WARPED_PLANKS, Tags.Items.RODS_WOODEN);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, GrowthcraftApiaryBlocks.BEE_BOX_BAMBOO.get())
						   .pattern("SPS")
						   .pattern("PBP")
						   .pattern("SPS")
						   .define('B', Ingredient.of(Items.BAMBOO_SLAB, Items.BAMBOO_MOSAIC_SLAB))
						   .define('P', Ingredient.of(Items.BAMBOO_PLANKS, Items.BAMBOO_MOSAIC))
						   .define('S', Tags.Items.RODS_WOODEN)
						   .group(growthcraft.bamboo.shared.Reference.MODID)
						   .unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAMBOO_PLANKS))
						   .save(consumer);

		// Bees Wax
		GrowthcraftApiaryItems.ITEMS.getEntries().forEach(item -> {
			if(item.getId().getPath().contains("bees_wax_")) {
				ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item.get(), 8)
				.pattern("AAA")
				.pattern("ABA")
				.pattern("AAA")
				.define('A', GrowthcraftApiaryItems.BEES_WAX.get())
				.define('B', ((GrowthcraftItem) item.get()).getDyeColor().getTag())
				.group(Reference.MODID)
				.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftApiaryItems.BEES_WAX.get()))
				.save(consumer);
			}
		});

		// Candle
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.CANDLE)
		.pattern("S")
		.pattern("H")
		.define('S', Items.STRING)
		.define('H', GrowthcraftApiaryTags.Items.HONEY_COMB)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.HONEYCOMB))
		.save(consumer);

		// Dyed Candles
		for (DyeColor dye : DyeColor.values()) {
			ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:"+dye+"_candle")))
			.requires(Items.CANDLE)
			.requires(dye.getTag())
			.group("dyed_candle")
			.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.CANDLE))
			.save(consumer);
		}
	}

	/**
	 * Builds a recipe for creating a bee box using the provided parameters.
	 *
	 * @param consumer  The consumer to accept the finished recipe.
	 * @param beeBoxBlock  The block representing the bee box.
	 * @param slab  The item representing the slab.
	 * @param planks  The item representing the planks.
	 * @param tagRod  The tag key for the rod item.
	 */
	private void beeBoxRecipeBuilder(Consumer<FinishedRecipe> consumer, Block beeBoxBlock, Item slab, Item planks, TagKey<Item> tagRod) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, beeBoxBlock)
				.pattern("SPS")
				.pattern("PBP")
				.pattern("SPS")
				.define('B', slab)
				.define('P', planks)
				.define('S', tagRod)
				.group(Reference.MODID)
				.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(planks))
				.save(consumer);
	}

    @Override
    public String getName() {
        return "Growthcraft Apiary Recipes";
    }
}
