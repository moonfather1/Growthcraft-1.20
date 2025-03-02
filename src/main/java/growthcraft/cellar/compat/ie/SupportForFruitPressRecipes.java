package growthcraft.cellar.compat.ie;

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import growthcraft.cellar.recipe.FruitPressRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.util.Lazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SupportForFruitPressRecipes {
    public static void insertFruitPressRecipes(MinecraftServer currentServer) {
        List<FruitPressRecipe> ourList = currentServer.getRecipeManager().getAllRecipesFor(FruitPressRecipe.Type.INSTANCE);
        if (ourList.isEmpty()) { return; }
        Collection<Recipe<?>> all = currentServer.getRecipeManager().getRecipes();
        ResourceLocation idToCheckDuplicates = new ResourceLocation(ourList.get(0).getId().getNamespace(), "ie_"+ourList.get(0).getId().getPath());
        for (Recipe<?> anyRecipe : all) {
            if (anyRecipe.getId().equals(idToCheckDuplicates)) {
                return;
            }
        }
        List<Recipe<?>> mutable = new ArrayList<>(all);
        for (FruitPressRecipe recipe : ourList) {
            SqueezerRecipe newRecipe = new SqueezerRecipe(new ResourceLocation(recipe.getId().getNamespace(), "ie_"+recipe.getId().getPath()), recipe.getResultingFluid(), Lazy.of(() -> ItemStack.EMPTY), IngredientWithSize.of(recipe.getIngredientItemStack()), 4000);
            newRecipe.modifyTimeAndEnergy(()->20d, ()->1d); // 80 sec instead of 4 sec.
            mutable.add(newRecipe);
        }
        currentServer.getRecipeManager().replaceRecipes(mutable);      // to other GC devs: please do not do what i did here.
    }               // we could have even done it manually - via json files. not like anyone is ever going to add a fruit press recipe.
}
