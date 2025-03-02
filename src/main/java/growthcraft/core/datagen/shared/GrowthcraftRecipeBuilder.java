package growthcraft.core.datagen.shared;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GrowthcraftRecipeBuilder {
    public static ExtendedShapelessRecipeBuilder crafting_shapeless(RecipeCategory category, ItemLike result) {
        return crafting_shapeless(category, result, 1);
    }
    public static ExtendedShapelessRecipeBuilder crafting_shapeless(RecipeCategory category, ItemLike result, int count) {
        return new ExtendedShapelessRecipeBuilder(category, result, count);
    }
    public static ExtendedShapedRecipeBuilder crafting_shaped(RecipeCategory category, ItemLike result) {
        return crafting_shaped(category, result, 1);
    }
    public static ExtendedShapedRecipeBuilder crafting_shaped(RecipeCategory category, ItemLike result, int count) {
        return new ExtendedShapedRecipeBuilder(category, result, count);
    }

    ///////////////////////////////////////////////////////////////////////

    public static class ExtendedShapelessRecipeBuilder extends ShapelessRecipeBuilder {

        public ExtendedShapelessRecipeBuilder(RecipeCategory category, ItemLike result, int count)
        {
            super(category, result, count);
        }

        public ExtendedShapelessRecipeBuilder addCondition(ICondition condition)
        {
            this.conditions.add(condition);
            return this;
        }
        private final List<ICondition> conditions = new ArrayList<>();



        @Override
        public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
            ResourceLocation location = BuiltInRegistries.ITEM.getKey(this.getResult().asItem());
            int countExisting = dupeCounter.getOrDefault(location, 0);
            dupeCounter.put(location, countExisting + 1);
            if (countExisting > 0) {
                location = new ResourceLocation(location.getNamespace(), location.getPath() + '_' + (countExisting + 1));
            }
            this.save(pFinishedRecipeConsumer, location);
        }
        private static final Map<ResourceLocation, Integer> dupeCounter = new HashMap<>();

        @Override
        public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation location)
        {
            super.save(recipe -> recipeConsumer.accept(this.appendConditions(recipe)), location);
        }



        private FinishedRecipe appendConditions(FinishedRecipe recipeWithoutConditions)
        {
            ConditionWrapperForCraftingRecipe ourRecipe = new ConditionWrapperForCraftingRecipe(recipeWithoutConditions, this.conditions.toArray(new ICondition[0]));
            return ourRecipe;
        }
    }

    public static class ExtendedShapedRecipeBuilder extends ShapedRecipeBuilder {

        public ExtendedShapedRecipeBuilder(RecipeCategory category, ItemLike result, int count)
        {
            super(category, result, count);
        }

        public ExtendedShapedRecipeBuilder addCondition(ICondition condition)
        {
            this.conditions.add(condition);
            return this;
        }
        private final List<ICondition> conditions = new ArrayList<>();

        @Override
        public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation location)
        {
            super.save(recipe -> recipeConsumer.accept(this.appendConditions(recipe)), location);
        }

        private FinishedRecipe appendConditions(FinishedRecipe recipeWithoutConditions)
        {
            ConditionWrapperForCraftingRecipe ourRecipe = new ConditionWrapperForCraftingRecipe(recipeWithoutConditions, this.conditions.toArray(new ICondition[0]));
            return ourRecipe;
        }
    }

    private static class ConditionWrapperForCraftingRecipe implements FinishedRecipe {
        private final ICondition[] conditions;
        private final FinishedRecipe original;

        protected ConditionWrapperForCraftingRecipe(FinishedRecipe original, ICondition... conditions) {
            this.conditions = conditions;
            this.original = original;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            this.original.serializeRecipeData(jsonObject);
            jsonObject.add("conditions", CraftingHelper.serialize(conditions));
        }
        /////////////////////
        @Override
        public ResourceLocation getId() { return this.original.getId(); }
        @Override
        public RecipeSerializer<?> getType() { return this.original.getType(); }
        @Nullable
        @Override
        public JsonObject serializeAdvancement() { return this.original.serializeAdvancement(); }
        @Nullable
        @Override
        public ResourceLocation getAdvancementId() { return this.original.getAdvancementId(); }
    }
}
