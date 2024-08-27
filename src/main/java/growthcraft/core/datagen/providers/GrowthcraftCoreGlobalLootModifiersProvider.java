package growthcraft.core.datagen.providers;

import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.shared.Reference;
import growthcraft.lib.loot.AddItemModifier;
import growthcraft.lib.loot.AddLootTableModifier;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class GrowthcraftCoreGlobalLootModifiersProvider extends GlobalLootModifierProvider{
	public GrowthcraftCoreGlobalLootModifiersProvider(PackOutput output) {
		super(output, Reference.MODID);
	}
	
	@Override
	protected void start() {
		//GC Apiary
		add("bee_from_vanilla_bee_nest", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.50f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BEE_NEST).build()}, 
				GrowthcraftApiaryItems.BEE.get().asItem()));
		
		//GC Cellar
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.PILLAGER_OUTPOST_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.PILLAGER_OUTPOST_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.PILLAGER_OUTPOST).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.UNDERWATER_RUIN_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.UNDERWATER_RUIN_LOOT), LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_SMALL).or(LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG)).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.SHIPWRECK_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.SHIPWRECK_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_SUPPLY).or(LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_MAP)).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.VILLAGE_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.VILLAGE_CHEST_LOOT),  LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_BUTCHER).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.BURIED_TREASURE_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.BURIED_TREASURE_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.WOODLAND_MANSION_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.WOODLAND_MANSION_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.WOODLAND_MANSION).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.STRONGHOLD_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.STRONGHOLD_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR).or(LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CROSSING)).build()));
		
		add("grape_seeds_purple_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.2f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.GRAPE_PURPLE_SEED.get().asItem()));
		
		add("grape_seeds_red_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.2f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.GRAPE_RED_SEEDS.get().asItem()));
		
		add("grape_seeds_white_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.2f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.GRAPE_WHITE_SEEDS.get().asItem()));
		
		add("hops_seeds_white_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.2f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.HOPS_SEED.get().asItem()));
		
		add("yeast_bayanus_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.1f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.YEAST_BAYANUS.get().asItem()));
		
		add("yeast_brewers_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.1f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.YEAST_BREWERS.get().asItem()));
		
		add("yeast_ethereal_from_chorus_flower", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.4f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CHORUS_FLOWER).build()}, 
				GrowthcraftCellarItems.YEAST_ETHEREAL.get().asItem()));
		
		add("yeast_lager_from_snow", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.2f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SNOW).build()}, 
				GrowthcraftCellarItems.YEAST_LAGER.get().asItem()));
		
//		GC Milk
		add("thistle_seeds_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.2f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftMilkItems.THISTLE_SEED.get().asItem()));
		
//		GC Rice
		add("rice_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.2f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftRiceItems.RICE_GRAINS.get().asItem()));
		
	}
	
	private static LootModifier loot(ResourceLocation id, LootItemCondition... cond) {
		return new AddLootTableModifier(cond, id);
	}
	
	private static ResourceLocation name(String lootTableName) {
		return new ResourceLocation(growthcraft.cellar.shared.Reference.MODID, "chests/" + lootTableName);
	}
}
