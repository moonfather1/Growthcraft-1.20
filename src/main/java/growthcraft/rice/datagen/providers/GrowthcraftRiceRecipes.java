package growthcraft.rice.datagen.providers;

import static growthcraft.lib.utils.FormatUtils.HAS_ITEM;

import java.util.function.Consumer;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.ClocheRenderFunction.ClocheRenderReference;
import blusunrize.immersiveengineering.api.crafting.builders.ClocheRecipeBuilder;
import blusunrize.immersiveengineering.client.utils.ClocheRenderFunctions.RenderFunctionCrop;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.rice.init.GrowthcraftRiceBlocks;
import growthcraft.rice.init.GrowthcraftRiceItems;
import growthcraft.rice.shared.Reference;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import mekanism.common.registries.MekanismItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

public class GrowthcraftRiceRecipes extends RecipeProvider{

	public GrowthcraftRiceRecipes(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftRiceItems.CULTIVATOR.get())
		.pattern("iIi")
		.pattern("iSi")
		.pattern(" S ")
		.define('i', Tags.Items.NUGGETS_IRON)
		.define('I', Tags.Items.INGOTS_IRON)
		.define('S', Tags.Items.RODS_WOODEN)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GrowthcraftRiceItems.KNIFE.get())
		.pattern("  I")
		.pattern(" I ")
		.pattern("S  ")
		.define('I', Tags.Items.INGOTS_IRON)
		.define('S', Tags.Items.RODS_WOODEN)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GrowthcraftRiceItems.RICE_GRAINS.get(), 4)
		.requires(GrowthcraftRiceItems.RICE.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftRiceItems.RICE.get()).build()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, GrowthcraftRiceItems.SUSHI_ROLL.get(), 12)
		.pattern(" F ")
		.pattern("RRR")
		.pattern("KKK")
		.define('F', ItemTags.FISHES)
		.define('R', GrowthcraftRiceItems.RICE_COOKED.get())
		.define('K', Items.DRIED_KELP)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftRiceItems.RICE_COOKED.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GrowthcraftRiceItems.CHICKEN_RICE.get())
		.requires(GrowthcraftRiceItems.RICE_COOKED.get())
		.requires(Items.COOKED_CHICKEN)
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftRiceItems.RICE_COOKED.get()).build()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, GrowthcraftRiceItems.ONIGIRI.get(), 2)
		.pattern(" R ")
		.pattern("RSR")
		.pattern("RKR")
		.define('S', GrowthcraftTags.Items.SALT)
		.define('R', GrowthcraftRiceItems.RICE_COOKED.get())
		.define('K', Items.DRIED_KELP)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftRiceItems.RICE_COOKED.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Blocks.PACKED_MUD)
		.requires(GrowthcraftRiceItems.RICE_STALK.get())
		.requires(Blocks.MUD)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftRiceItems.RICE_STALK.get()).build()))
		.save(consumer);

		//Mekanism Bio Fule recipe, may not be needed in 1.21
		ItemStackToItemStackRecipeBuilder.crushing(
				IngredientCreatorAccess.item().from(GrowthcraftRiceItems.RICE_STALK.get()),
				MekanismItems.BIO_FUEL.getItemStack(2)
				).addCondition(new ModLoadedCondition("mekanism"))
		.build(consumer, new ResourceLocation(Reference.MODID, Mekanism.MODID + "/crushing/biofuel/" + GrowthcraftRiceItems.RICE_STALK.get()));
		
		//ImmersiveEngineering
		ClocheRecipeBuilder.builder(new ItemStack(GrowthcraftRiceItems.RICE.get(), 2))
		.addResult(new ItemStack(GrowthcraftRiceItems.RICE_STALK.get(), 3))
		.addInput(GrowthcraftRiceItems.RICE_GRAINS.get())
		.addSoil(Blocks.DIRT)
		.setTime(1000)
		.setRender(new ClocheRenderReference("crop", GrowthcraftRiceBlocks.RICE_CROP.get()))
		.build(consumer, new ResourceLocation(Reference.MODID, ImmersiveEngineering.MODID + "/cloche/rice"));
	}
	
    @Override
    public String getName() {
        return "Growthcraft Rice Recipes";
    }
}
