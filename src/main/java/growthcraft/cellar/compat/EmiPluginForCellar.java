package growthcraft.cellar.compat;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import growthcraft.cellar.compat.emi.EmiPressRecipe;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.recipe.FruitPressRecipe;
import growthcraft.cellar.shared.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

@EmiEntrypoint
public class EmiPluginForCellar implements EmiPlugin
{
    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.removeEmiStacks(EmiStack.of(GrowthcraftCellarItems.POTION_ALE.get()));
        /////////////////
        EmiStack workstation = EmiStack.of(GrowthcraftCellarBlocks.FRUIT_PRESS.get());
        emiRegistry.addCategory(PRESS_RECIPE_CATEGORY);
        emiRegistry.addWorkstation(PRESS_RECIPE_CATEGORY, PRESS_RECIPE_WORKSTATION);
        for (FruitPressRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(FruitPressRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiPressRecipe(recipe));
        }
    }
    private static final EmiStack PRESS_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.FRUIT_PRESS.get());
    private static final ResourceLocation MY_SPRITE_SHEET = new ResourceLocation(Reference.MODID, "textures/gui/fruit_press_screen.png");
    public static final EmiRecipeCategory PRESS_RECIPE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "press"), PRESS_RECIPE_WORKSTATION, new EmiTexture(MY_SPRITE_SHEET, 0, 0, 16, 16));
}
