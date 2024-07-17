package growthcraft.bamboo;

import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.bamboo.shared.Reference;
import growthcraft.core.init.GrowthcraftCreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Reference.MODID)
public class GrowthcraftBamboo {
    public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);

    public GrowthcraftBamboo() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::buildCreativeTabContents);

        // Blocks, Items, Fluids, Block Entities, Containers
        GrowthcraftBambooBlocks.init(modEventBus);
    }

    public void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == GrowthcraftCreativeModeTabs.CREATIVE_TAB.get()) {
            event.accept(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL);
        }
    }
}
