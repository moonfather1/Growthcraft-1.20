package growthcraft.cellar.compat.jei.category;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.compat.jei.JEIGrowthcraftCellarModPlugin;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.recipe.BrewKettleRecipe;
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
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class BrewKettleRecipeCategory implements IRecipeCategory<BrewKettleRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Reference.MODID, Reference.UnlocalizedName.BREW_KETTLE);

    private static final ResourceLocation TEXTURE = TextureHelper.getTextureGui(Reference.MODID, Reference.UnlocalizedName.BREW_KETTLE);

    private static final Component INFO_NO_LID = Component.translatable("message.growthcraft_cellar.kettle.jei_info_no_lid").withStyle(Style.EMPTY.withColor(0xff787070));
    private static final Component INFO_NEED_LID = Component.translatable("message.growthcraft_cellar.kettle.jei_info_need_lid").withStyle(Style.EMPTY.withColor(0xff787070));

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic overlayHeated;
    private final IDrawableStatic iconInfo, timeInfo;

    public BrewKettleRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
                TEXTURE, 10, 10, 160, 70
        );

        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(GrowthcraftCellarBlocks.BREW_KETTLE.get())
        );

        // info icon
        this.iconInfo = guiHelper.createDrawable(
                TEXTURE, 66, 184, 11, 11
        );

        // hourglass icon
        this.timeInfo = guiHelper.createDrawable(
                TEXTURE, 54, 184, 11, 11
        );

        // Heated Overlay
        this.overlayHeated = guiHelper.createDrawable(
                TEXTURE, 176, 28, 12, 13
        );
    }

    @Override
    public RecipeType<BrewKettleRecipe> getRecipeType() {
        return JEIGrowthcraftCellarModPlugin.BREW_KETTLE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.growthcraft_cellar.brew_kettle");
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
    public void setRecipe(IRecipeLayoutBuilder builder, BrewKettleRecipe recipe, IFocusGroup focuses) {
        // Input Item with Tag Support
        for (Ingredient ingredient : recipe.getIngredients()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 70, 25)
                    .addIngredients(ingredient);
        }

        // TODO: Input Fluid
        builder.addSlot(RecipeIngredientRole.INPUT, 36, 7)
                .setFluidRenderer(4000, true, 16, 52)
                .addFluidStack(recipe.getInputFluidStack().getFluid(), recipe.getInputFluidStack().getAmount());

        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addFluidStack(recipe.getInputFluidStack().getFluid(), recipe.getInputFluidStack().getAmount());

        // By-Product Item
        builder.addSlot(RecipeIngredientRole.OUTPUT, 131, 7)
                .addItemStack(recipe.getByProduct());

        // TODO: Output Fluid
        builder.addSlot(RecipeIngredientRole.INPUT, 104, 7)
                .setFluidRenderer(4000, true, 16, 52)
                .addFluidStack(recipe.getOutputFluidStack().getFluid(), recipe.getOutputFluidStack().getAmount());

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .addFluidStack(recipe.getOutputFluidStack().getFluid(), recipe.getOutputFluidStack().getAmount());
    }

    @Override
    public void draw(BrewKettleRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        try {
            if (recipe.isHeatRequired()) overlayHeated.draw(guiGraphics, 59, 44);
        } catch (Exception ex) {
            GrowthcraftCellar.LOGGER.error("Failure to draw heat texture for Brew Kettle recipe with JEI integration.");
        }

        Font font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, String.format("(%d%%)", recipe.getByProductChance()), 131, 26,0x404040, false );

        timeInfo.draw(guiGraphics, 2, 27);
        guiGraphics.drawString(font, TickUtils.toHoursMinutesSeconds(recipe.getProcessingTime()), 5, 40,0x404040, false );
        iconInfo.draw(guiGraphics, 1, 50);
        Component steamingOrBoiling = recipe.isLidRequired() ? INFO_NEED_LID : INFO_NO_LID;
        guiGraphics.drawString(font, steamingOrBoiling, 5, 62,0x606068, false );
    }

}
