package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
import net.minecraft.world.item.crafting.Ingredient;

public class EmiBarrelRecipe extends BasicEmiRecipe
{
    public EmiBarrelRecipe(FermentationBarrelRecipe recipe)
    {
        super(EmiPlugin.BARREL_RECIPE_CATEGORY, recipe.getId(), 125, 18);
        this.inputs.add(EmiStack.of(recipe.getIngredientFluidStack().getFluid()));
        this.inputs.add(EmiIngredient.of(Ingredient.of(recipe.getIngredientItemStack())));
        this.outputs.add(EmiStack.of(recipe.getResultingFluid().getFluid().getBucket()));
        this.outputs.add(EmiStack.of(recipe.getResultingFluid().getFluid()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder)
    {
        // Add plus and arrow textures
        widgetHolder.addTexture(EmiTexture.PLUS, 27, 3);
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 75, 1);
        // Adds an input slot 1
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        // Adds an input slot 2
        widgetHolder.addSlot(inputs.get(1), 49, 0);
        // Adds an output slot on the right
        // Note that output slots need to call `recipeContext` to inform EMI about their recipe context
        // This includes being able to resolve recipe trees, favorite stacks with recipe context, and more
        widgetHolder.addSlot(outputs.get(1), 107, 0).recipeContext(this);
    }
}
