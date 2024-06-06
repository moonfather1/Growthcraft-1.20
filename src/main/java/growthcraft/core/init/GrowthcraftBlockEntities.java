package growthcraft.core.init;

import growthcraft.core.block.entity.RopeBlockEntity;
import growthcraft.core.shared.Reference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class GrowthcraftBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MODID
    );

    public static final RegistryObject<BlockEntityType<RopeBlockEntity>> ROPE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            Reference.UnlocalizedName.ROPE,
            () -> BlockEntityType.Builder.of(RopeBlockEntity::new, getBlocks()).build(null)
    );

    private GrowthcraftBlockEntities() {
        /* Disable automatic default public constructor */
    }

    // roped fence shouldn't be a block entity to begin with but probably nobody cares to rework them. //
    // anyway...

    /////////////////// ...we need to make arrangements to add these blocks from other mods (gc apples if not 3rd party) /////////
    /////////////////// without this, gc apples would need a block entity class that does nothing and a block class that extends roped fence and does nothing  /////

    private static final ArrayList<Supplier<Block>> blocksForFenceBE = new ArrayList<>();
    private static Block[] getBlocks() {
        ArrayList<Block> list = new ArrayList<>();
        list.add(GrowthcraftBlocks.ROPE_LINEN_ACACIA_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_BAMBOO_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_BIRCH_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_CHERRY_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_CRIMSON_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_DARK_OAK_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_JUNGLE_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_MANGROVE_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_OAK_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_SPRUCE_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_WARPED_FENCE.get());
        list.add(GrowthcraftBlocks.ROPE_LINEN_NETHER_BRICK_FENCE.get());
        for (var b : blocksForFenceBE) { list.add(b.get()); }
        return list.toArray(new Block[0]);
    }
    public static void registerRopedFence(Supplier<Block> anotherFence) {
        blocksForFenceBE.add(anotherFence);
    }
}
