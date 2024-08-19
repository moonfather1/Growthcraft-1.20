package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.shared.Reference;
import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class EmiFarmingRecipe extends BasicEmiRecipe
{
    private static final ResourceLocation WIDGETS = new ResourceLocation("growthcraft:textures/gui/widgets.png");
    private static final EmiTexture SMALL_PLUS = new EmiTexture(WIDGETS, 83, 1, 10, 10);
    private static final EmiTexture SMALL_ARROW = new EmiTexture(WIDGETS, 44, 0, 20, 15);
    private static final EmiIngredient SLOT0 = EmiIngredient.of(Ingredient.of(Blocks.FARMLAND));
    private static final EmiIngredient SLOT2 = EmiIngredient.of(Ingredient.of(GrowthcraftItems.ROPE_LINEN.get()));
    private static int counter = 1;
    private final EmiIngredient SLOT1, SLOT3;

    public EmiFarmingRecipe(ItemLike input, ItemLike output)
    {
        super(VanillaEmiRecipeCategories.WORLD_INTERACTION, new ResourceLocation(Reference.MODID, "f"+counter), 125, 18);
        counter++;
        SLOT1 = EmiIngredient.of(Ingredient.of(input));
        SLOT3 = EmiIngredient.of(Ingredient.of(output));
        this.inputs.add(SLOT1);
        this.inputs.add(SLOT2);
        this.outputs.add(EmiStack.of(output));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder)
    {
        widgetHolder.addSlot(SLOT0, 0, 0);
        widgetHolder.addTexture(SMALL_PLUS, 20, 3);
        widgetHolder.addSlot(SLOT1, 32, 0);
        widgetHolder.addTexture(SMALL_PLUS, 52, 3);
        widgetHolder.addSlot(SLOT2, 64, 0);
        widgetHolder.addTexture(SMALL_ARROW, 83, 1);
        // Adds an output slot on the right
        // Note that output slots need to call `recipeContext` to inform EMI about their recipe context
        // This includes being able to resolve recipe trees, favorite stacks with recipe context, and more
        widgetHolder.addSlot(SLOT3, 106, 0).recipeContext(this);
    }
}
