package growthcraft.bamboo.datagen.providers;

import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GrowthcraftBambooTagsProvider extends BlockTagsProvider
{
    public GrowthcraftBambooTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(BlockTags.MINEABLE_WITH_AXE).add(GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get()).add(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get());
        tag(BlockTags.CLIMBABLE).add(GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get());
    }
}
