package growthcraft.bamboo.init;

import growthcraft.apiary.block.BeeBoxBlock;
import growthcraft.apiary.init.GrowthcraftApiaryBlockEntities;
import growthcraft.bamboo.block.BambooPostBlock;
import growthcraft.bamboo.shared.Reference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class GrowthcraftBambooBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            ForgeRegistries.BLOCKS, Reference.MODID
    );
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS, Reference.MODID
    );

    //--------------------------------//

    public static final RegistryObject<Block> BAMBOO_POST_VERTICAL = registerBlock(
            Reference.UnlocalizedName.BAMBOO_POST_VERTICAL, () -> BambooPostBlock.create(true),
            false
    );
    public static final RegistryObject<Block> BAMBOO_POST_HORIZONTAL = registerBlock(
            Reference.UnlocalizedName.BAMBOO_POST_HORIZONTAL, () -> BambooPostBlock.create(false),
            true
    );

    public static final RegistryObject<Block> BAMBOO_PLANK_BEE_BOX = registerBeeBox(Reference.UnlocalizedName.BAMBOO_PLANK_BEE_BOX, BeeBoxBlock::new);

    //--------------------------------//

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block, boolean excludeBlockItemRegistry) {
        RegistryObject<Block> registryObject = BLOCKS.register(name, block);
        if (! excludeBlockItemRegistry) {
            ITEMS.register(name, () -> new BlockItem(registryObject.get(), new Item.Properties()));
        }
        return registryObject;
    }

    private static RegistryObject<Block> registerBeeBox(String name, Supplier<Block> block) {
        RegistryObject<Block> blockRegistryObject = registerBlock(name, block, false);
        GrowthcraftApiaryBlockEntities.registerBeeBox(blockRegistryObject);
        return blockRegistryObject;
    }

    public static void init(IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}