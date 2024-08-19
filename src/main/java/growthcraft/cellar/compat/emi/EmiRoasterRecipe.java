package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.recipe.RoasterRecipe;
import growthcraft.cellar.shared.Reference;
import growthcraft.lib.utils.TickUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class EmiRoasterRecipe extends BasicEmiRecipe
{
    private final String time;
    private final int level;
    private static EmiTexture texture = null;

    public EmiRoasterRecipe(RoasterRecipe recipe) {
        super(EmiPlugin.ROASTER_RECIPE_CATEGORY, recipe.getId(), 125, 30);
        this.inputs.add(EmiIngredient.of(Ingredient.of(recipe.getInputItemStack())));
        this.outputs.add(EmiStack.of(recipe.getResultItem()));
        this.time = TickUtils.toHoursMinutesSeconds(recipe.getRecipeProcessingTime() * 30 * 20);
        this.level = recipe.getRecipeProcessingTime();
        if (texture == null) {
            texture = new EmiTexture(new ResourceLocation(Reference.MODID, "textures/gui/brew_kettle_screen.png"), 54, 184, 11, 11);
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        // Add an arrow texture to indicate processing
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 23, 1);
        // Adds an input slot on the left
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        // Adds an output slot on the right
        // Note that output slots need to call `recipeContext` to inform EMI about their recipe context
        // This includes being able to resolve recipe trees, favorite stacks with recipe context, and more
        widgetHolder.addSlot(outputs.get(0), 52, 0).recipeContext(this);
        // additional info
        widgetHolder.addText(Component.translatable("label.growthcraft_cellar.roaster_level", this.level), 4, 20, 0xFF444433, false);
        widgetHolder.addText(Component.literal(this.time), 97, 5, 0xFF444433, false);
        widgetHolder.addTexture(texture, 84, 3);
    }
}
