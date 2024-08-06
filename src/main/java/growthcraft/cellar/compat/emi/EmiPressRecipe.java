package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.compat.EmiPluginForCellar;
import growthcraft.cellar.recipe.FruitPressRecipe;
import net.minecraft.world.item.crafting.Ingredient;

public class EmiPressRecipe extends BasicEmiRecipe
{
    public EmiPressRecipe(FruitPressRecipe recipe)
    {
        super(EmiPluginForCellar.PRESS_RECIPE_CATEGORY, recipe.getId(), 125, 20);
        this.inputs.add(EmiIngredient.of(Ingredient.of(recipe.getIngredientItemStack())));
        this.outputs.add(EmiStack.of(recipe.getResultingFluid().getFluid()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder)
    {
        // Add an arrow texture to indicate processing
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        // Adds an input slot on the left
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        // Adds an output slot on the right
        // Note that output slots need to call `recipeContext` to inform EMI about their recipe context
        // This includes being able to resolve recipe trees, favorite stacks with recipe context, and more
        widgetHolder.addSlot(outputs.get(0), 58, 0).recipeContext(this);
    }
}
