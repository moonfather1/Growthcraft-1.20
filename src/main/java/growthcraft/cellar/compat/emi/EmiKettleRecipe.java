package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.recipe.BrewKettleRecipe;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EmiKettleRecipe extends BasicEmiRecipe
{
    private final boolean hasByproduct, isSteaming;
    private final int chanceForByproduct;

    public EmiKettleRecipe(BrewKettleRecipe recipe) {
        super(EmiPlugin.KETTLE_RECIPE_CATEGORY, recipe.getId(), 125, 18);
        this.inputs.add(EmiIngredient.of(recipe.getIngredients().isEmpty() ? Ingredient.EMPTY : recipe.getIngredients().get(0)));
        this.inputs.add(EmiStack.of(recipe.getInputFluidStack().getFluid(), recipe.getInputFluidStack().getAmount()));
        this.outputs.add(EmiStack.of(recipe.getOutputFluidStack().getFluid().getBucket()));
        this.outputs.add(EmiStack.of(recipe.getOutputFluidStack().getFluid(),recipe.getOutputFluidStack().getAmount()));
        this.outputs.add(EmiStack.of(recipe.getByProduct()));
        this.hasByproduct = ! recipe.getByProduct().isEmpty() && recipe.getByProductChance() > 0;
        this.chanceForByproduct = recipe.getByProductChance();
        this.isSteaming = recipe.isLidRequired();
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        // Adds input slot 1
        widgetHolder.addSlot(inputs.get(1), 0, 0);
        // Add texture
        widgetHolder.addTexture(SMALL_PLUS, 20, 3);
        // Adds input slot 2
        widgetHolder.addSlot(inputs.get(0), 32, 0);
        // Add texture
        widgetHolder.addTexture(SMALL_ARROW, 52, 1);
        String key = this.isSteaming ? "message.growthcraft_cellar.kettle.jei_info_need_lid" : "message.growthcraft_cellar.kettle.jei_info_no_lid";
        widgetHolder.addTooltipText(List.of(Component.translatable(key)), 50, 0, 24, 17);
        // Adds an output slot on the right
        // Note that output slots need to call `recipeContext` to inform EMI about their recipe context
        // This includes being able to resolve recipe trees, favorite stacks with recipe context, and more
        widgetHolder.addSlot(outputs.get(1), 74, 0).recipeContext(this);
        if (this.hasByproduct) {
            // Add texture
            widgetHolder.addTexture(SMALL_PLUS, 94, 3);
            // Adds output slot 2
            widgetHolder.addSlot(outputs.get(2), 107, 0);
            widgetHolder.addTooltipText(List.of(Component.literal("%d%%".formatted(this.chanceForByproduct))), 92, 1, 14, 14);
        }
    }
    private static final ResourceLocation WIDGETS = new ResourceLocation("growthcraft:textures/gui/widgets.png");
    private static final EmiTexture SMALL_PLUS = new EmiTexture(WIDGETS, 83, 1, 10, 10);
    private static final EmiTexture SMALL_ARROW = new EmiTexture(WIDGETS, 44, 0, 20, 15);
}
