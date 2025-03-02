package growthcraft.cellar.compat.ie;

import growthcraft.core.init.config.OptionalFeatureCondition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber
public class DataReloadListener
{
    private static void insertOurRecipes(MinecraftServer currentServer) {
        if (ModList.get().isLoaded("immersiveengineering") && OptionalFeatureCondition.testModuleOrModuleFeature("cellar")) {
            SupportForFruitPressRecipes.insertFruitPressRecipes(currentServer);
        }
    }

    //-----------------------------------

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event)
    {
        insertOurRecipes(event.getServer());
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        for (PreparableReloadListener listener: event.getListeners()) {
            if (listener instanceof ReloadListener) {
                return;
            }
        }
        event.addListener(lissy);
    }

    private static final PreparableReloadListener lissy = new ReloadListener();
    private static class ReloadListener implements PreparableReloadListener {
        @Override
        public CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
            if (ServerLifecycleHooks.getCurrentServer() != null) { // will be null once during load
                insertOurRecipes(ServerLifecycleHooks.getCurrentServer());
            }
            return pPreparationBarrier.wait(null);
        }
    }
}
