package growthcraft.milk.recipe;

import com.google.gson.JsonObject;
import growthcraft.lib.utils.CraftingUtils;
import growthcraft.lib.utils.RecipeUtils;
import growthcraft.milk.GrowthcraftMilk;
import growthcraft.milk.shared.Reference;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MixingVatFluidRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation recipeId;
    private final RecipeUtils.Category category;
    private final ItemStack activationTool;
    private final NonNullList<Ingredient> ingredients;

    private final FluidStack inputFluidStack;
    private final int processingTime;

    private final FluidStack reagentFluidStack;
    private final FluidStack outputFluidStack;
    private final FluidStack wasteFluidStack;
    private final boolean requiresHeat;


    public MixingVatFluidRecipe(ResourceLocation recipeId, RecipeUtils.Category category,
                                FluidStack inputFluidStack, FluidStack reagentFluidStack,
                                NonNullList<Ingredient> inputIngredients, int processingTime,
                                FluidStack outputFluidStack, FluidStack wasteFluidStack,
                                ItemStack activationTool,
                                boolean requiresHeat) {
        this.recipeId = recipeId;
        this.category = category;
        this.inputFluidStack = inputFluidStack;
        this.reagentFluidStack = reagentFluidStack;
        this.ingredients = inputIngredients;
        this.processingTime = processingTime;
        this.outputFluidStack = outputFluidStack;
        this.wasteFluidStack = wasteFluidStack;
        this.activationTool = activationTool;
        this.requiresHeat = requiresHeat;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, @NotNull Level level) {
        return false;
    }

    public boolean matches(FluidStack testBaseFluidStack, FluidStack testReagentFluidStack,
                           List<ItemStack> testIngredients, boolean hasHeatSource) {

        boolean fluidMatches = CraftingUtils.doesFluidMatch(testBaseFluidStack, this.getInputFluidStack())
                && CraftingUtils.doesFluidMatch(reagentFluidStack, this.getReagentFluidStack());

        boolean ingredientMatches = false;

        if (this.getIngredients().size() == testIngredients.size()) {
            int itemCount = this.getIngredientList().size();
            int matchCount = 0;
            for (int i = 0; i < this.getIngredientList().size(); i++) {
                if (this.getIngredientList().get(i).getItem() == testIngredients.get(i).getItem() &&
                        this.getIngredientList().get(i).getCount() == testIngredients.get(i).getCount()) {
                    matchCount++;
                }
            }
            ingredientMatches = itemCount == matchCount;
        }

        return fluidMatches && ingredientMatches && hasHeatSource == isHeatRequired();
    }

    public FluidStack getInputFluidStack() {
        return this.inputFluidStack.copy();
    }

    public FluidStack getReagentFluidStack() {
        return this.reagentFluidStack.copy();
    }

    public FluidStack getOutputFluidStack() {
        return this.outputFluidStack.copy();
    }

    public FluidStack getWasteFluidStack() {
        return this.wasteFluidStack.copy();
    }

    public boolean isHeatRequired() {
        return requiresHeat;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return this.outputFluidStack.getFluid().getBucket().getDefaultInstance();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.outputFluidStack.getFluid().getBucket().getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    public RecipeUtils.Category getCategory() {
        return this.category;
    }

    public ItemStack getActivationTool() {
        return this.activationTool;
    }

    public boolean activationToolValid(ItemStack tool) {
        return this.activationTool.getItem() == tool.getItem();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public List<ItemStack> getIngredientList() {
        return Arrays.stream(ingredients.get(0).getItems()).toList();
    }

    public List<Item> getIngredientItems() {
        List<Item> ingredientItems = new ArrayList<>();
        this.getIngredientList().forEach(
                itemStack -> ingredientItems.add(itemStack.getItem())
        );
        return ingredientItems;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public boolean is(RecipeUtils.Category category) {
        return this.category == category;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<MixingVatFluidRecipe> {
        private Type() { /* Prevent default constructor */ }

        public static final MixingVatFluidRecipe.Type INSTANCE = new MixingVatFluidRecipe.Type();
        public static final String ID = Reference.UnlocalizedName.MIXING_VAT_FLUID_RECIPE;
    }

    public static class Serializer implements RecipeSerializer<MixingVatFluidRecipe> {

        public static final MixingVatFluidRecipe.Serializer INSTANCE = new MixingVatFluidRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(
                Reference.MODID,
                Reference.UnlocalizedName.MIXING_VAT_FLUID_RECIPE);

        private static final int maxIngredients = 3;

        @Override
        public MixingVatFluidRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            int processingTime = GsonHelper.getAsInt(json, "processing_time", 1200);
            boolean requiresHeat = GsonHelper.getAsBoolean(json, "requires_heat");

            FluidStack inputFluid = CraftingUtils.getFluidStack(
                    GsonHelper.getAsJsonObject(json, "input_fluid"));

            ItemStack activationTool = CraftingHelper.getItemStack(
                    GsonHelper.getAsJsonObject(json, "activation_tool"), false);

            NonNullList<Ingredient> inputIngredient = CraftingUtils.readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));

            FluidStack reagentFluid = CraftingUtils.getFluidStack(
                    GsonHelper.getAsJsonObject(json, "reagent_fluid"));
            FluidStack resultFluid = CraftingUtils.getFluidStack(
                    GsonHelper.getAsJsonObject(json, "result_fluid"));
            FluidStack wasteFluid = CraftingUtils.getFluidStack(
                    GsonHelper.getAsJsonObject(json, "result_fluid_waste"));

            return new MixingVatFluidRecipe(recipeId, RecipeUtils.Category.FLUID,
                    inputFluid, reagentFluid, inputIngredient, processingTime, resultFluid, wasteFluid, activationTool, requiresHeat);

        }

        @Override
        public @Nullable MixingVatFluidRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            try {
                int processingTime = buffer.readVarInt();
                boolean requiresHeat = buffer.readBoolean();

                FluidStack inputFluidStack = buffer.readFluidStack();
                ItemStack activationTool = buffer.readItem();

                int ingredientSize = buffer.readVarInt();
                NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientSize, Ingredient.EMPTY);

                for (int i = 0; i < ingredientSize; i++) {
                    ingredients.set(i, Ingredient.fromNetwork(buffer));
                }

                FluidStack reagentFluidStack = buffer.readFluidStack();
                FluidStack outputFluidStack = buffer.readFluidStack();
                FluidStack wasteFluidStack = buffer.readFluidStack();

                return new MixingVatFluidRecipe(recipeId, RecipeUtils.Category.ITEM, inputFluidStack, reagentFluidStack,
                        ingredients, processingTime, outputFluidStack, wasteFluidStack, activationTool, requiresHeat);

            } catch (Exception ex) {
                String message = String.format("Unable to read recipe (%s) from network buffer.", recipeId);
                GrowthcraftMilk.LOGGER.error(message);
                throw ex;
            }
        }

        public void toNetwork(FriendlyByteBuf buffer, MixingVatFluidRecipe recipe) {
            buffer.writeVarInt(recipe.getProcessingTime());
            buffer.writeBoolean(recipe.isHeatRequired());

            buffer.writeFluidStack(recipe.getInputFluidStack());
            buffer.writeItemStack(recipe.getActivationTool(), false);

            buffer.writeVarInt(recipe.getIngredientList().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeFluidStack(recipe.getReagentFluidStack());
            buffer.writeFluidStack(recipe.getOutputFluidStack());
            buffer.writeFluidStack(recipe.getWasteFluidStack());
        }

    }
}
