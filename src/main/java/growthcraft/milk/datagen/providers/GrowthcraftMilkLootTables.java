package growthcraft.milk.datagen.providers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import growthcraft.lib.utils.FormatUtils;
import growthcraft.milk.block.BaseCheeseWheel;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.shared.Reference;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

public class GrowthcraftMilkLootTables extends VanillaBlockLoot{
	
	@Override
	protected void generate() {
		createStandardTable(
			GrowthcraftMilkBlocks.CHEESE_PRESS.get(),
			GrowthcraftMilkBlockEntities.CHEESE_PRESS_BLOCK_ENTITY.get(),
			FormatUtils.INVENTORY);
		createStandardTable(
			GrowthcraftMilkBlocks.CHURN.get(),
			GrowthcraftMilkBlockEntities.CHURN_BLOCK_ENTITY.get(),
			FormatUtils.INVENTORY,
			FormatUtils.fluidTankSlot(FormatUtils.INPUT, 0));
		createStandardTable(
			GrowthcraftMilkBlocks.MIXING_VAT.get(),
			GrowthcraftMilkBlockEntities.MIXING_VAT_BLOCK_ENTITY.get(),
			FormatUtils.INVENTORY,
			FormatUtils.fluidTankSlot(FormatUtils.INPUT, 0));
		createStandardTable(
			GrowthcraftMilkBlocks.PANCHEON.get(),
			GrowthcraftMilkBlockEntities.PANCHEON_BLOCK_ENTITY.get(),
			FormatUtils.INVENTORY,
			FormatUtils.fluidTankSlot(FormatUtils.INPUT, 0),
			FormatUtils.fluidTankSlot(FormatUtils.OUTPUT, 0),
			FormatUtils.fluidTankSlot(FormatUtils.OUTPUT, 1));
	}
	
    @Override
    protected Iterable<Block> getKnownBlocks() {
    	List<Block> knownBlocks = new LinkedList<Block>();
    	knownBlocks.add(GrowthcraftMilkBlocks.CHEESE_PRESS.get());
    	knownBlocks.add(GrowthcraftMilkBlocks.CHURN.get());
    	knownBlocks.add(GrowthcraftMilkBlocks.MIXING_VAT.get());
    	knownBlocks.add(GrowthcraftMilkBlocks.PANCHEON.get());
//    	excludedBlocks().forEach(e -> knownBlocks.remove(e));
    			
    	return knownBlocks;

    }

	/**
	 * @deprecated ("9.2.0", Used to be used for excluding from the item registration so that JEI and creative tab would not have these blocks include.)
	 */
	@Deprecated(since = "9.2.0", forRemoval = false)
    private List<Block> excludedBlocks() {
    	List<Block> excludedList = new LinkedList<Block>();
    	excludedList.add(GrowthcraftMilkBlocks.THISTLE_CROP.get());
    	excludedList.addAll(BaseCheeseWheel.Cheese.allCheeses().toList());
    	excludedList.add(GrowthcraftMilkBlocks.APPENZELLER_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.ASIAGO_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.CHEDDAR_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.EMMENTALER_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.GOUDA_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.MONTEREY_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.PARMESAN_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.RICOTTA_CHEESE_CURDS.get());
    	excludedList.add(GrowthcraftMilkBlocks.PROVOLONE_CHEESE_CURDS.get());
    	
    	return excludedList;
    }
    
    private void createStandardTable(Block block, BlockEntityType<?> type, String... tags) {
        LootPoolSingletonContainer.Builder<?> lti = LootItem.lootTableItem(block);
        lti.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
        for (String tag : tags) {
            lti.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy(tag, "BlockEntityTag." + tag, CopyNbtFunction.MergeStrategy.REPLACE));
        }
        lti.apply(SetContainerContents.setContents(type).withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))));

        LootPool.Builder builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(lti);
        add(block, LootTable.lootTable().withPool(builder));
    }
}
