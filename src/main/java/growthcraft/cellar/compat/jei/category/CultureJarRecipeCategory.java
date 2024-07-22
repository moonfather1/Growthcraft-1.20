package growthcraft.cellar.compat.jei.category;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.compat.jei.JEIGrowthcraftCellarModPlugin;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.recipe.CultureJarRecipe;
import growthcraft.cellar.shared.Reference;
import growthcraft.lib.utils.TextureHelper;
import growthcraft.lib.utils.TickUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CultureJarRecipeCategory implements IRecipeCategory<CultureJarRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Reference.MODID, Reference.UnlocalizedName.CULTURE_JAR);

    private static final ResourceLocation TEXTURE = TextureHelper.getTextureGui(Reference.MODID, Reference.UnlocalizedName.CULTURE_JAR);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic overlayHeated;
    private final IDrawableStatic overlayTank;

    @Override
    public RecipeType<CultureJarRecipe> getRecipeType() {
        return JEIGrowthcraftCellarModPlugin.CULTURE_JAR_RECIPE_TYPE;
    }

    public CultureJarRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
                TEXTURE, 10, 10, 160, 70
        );
        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(GrowthcraftCellarBlocks.CULTURE_JAR.get())
        );

        this.overlayHeated = guiHelper.createDrawable(
                TEXTURE, 176, 28, 12, 13
        );

        this.overlayTank = guiHelper.createDrawable(
                TEXTURE, 176, 64, 12, 13
        );
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_cellar.category.culture_jar");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CultureJarRecipe recipe, IFocusGroup focuses) {
        // Input Item
        builder.addSlot(RecipeIngredientRole.INPUT, 84, 25)
                .addItemStack(recipe.getInputItemStack());

        builder.addSlot(RecipeIngredientRole.INPUT, 55, 8)
                .setFluidRenderer(4000, true, 16, 52)
                .addFluidStack(recipe.getInputFluidStack().getFluid(), recipe.getInputFluidStack().getAmount());

        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addFluidStack(recipe.getInputFluidStack().getFluid(), recipe.getInputFluidStack().getAmount());

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .addItemStack(recipe.getInputItemStack());
    }

    @Override
    public void draw(CultureJarRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        Font font = Minecraft.getInstance().font;

        try {
            if (recipe.isHeatSourceRequired()) overlayHeated.draw(guiGraphics, 86, 47);
        } catch (Exception ex) {
            GrowthcraftCellar.LOGGER.error("Failure to draw heat texture for Culture Jar recipe with JEI integration.");
        }

        guiGraphics.drawWordWrap(font, FormattedText.of("Culturing requires a item to grow."), 0, 7, 50, 0x404040);

        guiGraphics.drawString(font, "(" + TickUtils.toHoursMinutesSeconds(recipe.getRecipeProcessingTime()) + ")", 106, 50, 0x404040, false);
    }
}
