package growthcraft.apiary.init;

import growthcraft.apiary.block.entity.BeeBoxBlockEntity;
import growthcraft.apiary.shared.Reference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class GrowthcraftApiaryBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MODID
    );

    public static final RegistryObject<BlockEntityType<BeeBoxBlockEntity>> BEE_BOX_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            Reference.UnlocalizedName.BEE_BOX,
            () -> BlockEntityType.Builder.of(BeeBoxBlockEntity::new,
                    getBlocks()
                    ).build(null)
    );

    private GrowthcraftApiaryBlockEntities() {
        /* Disable automatic default public constructor */
    }

    /////////////////// we need to make arrangements to add bee boxes from other mods (gc apples if not 3rd party) /////////
    /////////////////// without this, gc apples would need a block entity class that does nothing and a block class that extends BeeBoxBlock and does nothing  /////

    private static final ArrayList<Supplier<Block>> blocksForBeeBoxBE = new ArrayList<>();
    private static Block[] getBlocks() {
        ArrayList<Block> list = new ArrayList<>();
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_ACACIA.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_BIRCH.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_CHERRY.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_CRIMSON.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_DARK_OAK.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_JUNGLE.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_MANGROVE.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_OAK.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_SPRUCE.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_WARPED.get());
        list.add(GrowthcraftApiaryBlocks.BEE_BOX_BAMBOO.get());
        for (var b : blocksForBeeBoxBE) { list.add(b.get()); }
        return list.toArray(new Block[0]);
    }
    public static void registerBeeBox(Supplier<Block> anotherBeeBox) {
        blocksForBeeBoxBE.add(anotherBeeBox);
    }
}
