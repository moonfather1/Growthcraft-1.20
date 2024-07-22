package growthcraft.cellar.datagen.providers;

import java.util.function.BiConsumer;

import growthcraft.cellar.init.GrowthcraftCellarFluids;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.init.config.GrowthcraftCellarConfig;
import growthcraft.cellar.shared.Reference;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class GrowthcraftCellarChestLootTables implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceLocation, Builder> output) {
		createBottleLootTable(output, Reference.LootTable.PILLAGER_OUTPOST_CHEST_LOOT, GrowthcraftCellarItems.POTION_WINE.get(), "fluid_type.growthcraft_apiary.honey_mead_fluid", GrowthcraftCellarConfig.getLootChancePillagerTower(), UniformGenerator.between(6F, 14F), new MobEffectInstance(MobEffects.HEALTH_BOOST, 3600, 1, true, false));
		createBottleLootTable(output, Reference.LootTable.UNDERWATER_RUIN_LOOT, GrowthcraftCellarItems.POTION_WINE.get(), "fluid_type.growthcraft_cellar.white_grape_wine_fluid", GrowthcraftCellarConfig.getLootChanceOceanRuin(), UniformGenerator.between(1F, 4F), new MobEffectInstance(MobEffects.SATURATION, 1200, 1, true, false), new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 1200, 0, true, false));
		createBottleLootTable(output, Reference.LootTable.SHIPWRECK_CHEST_LOOT, GrowthcraftCellarItems.POTION_WINE.get(), "fluid_type.growthcraft_cellar.old_port_ale_fluid", GrowthcraftCellarConfig.getLootChanceShipwreck(), UniformGenerator.between(4F, 8F), new MobEffectInstance(MobEffects.LUCK, 6000, 1, true, false));
		createBottleLootTable(output, Reference.LootTable.VILLAGE_CHEST_LOOT, GrowthcraftCellarItems.POTION_WINE.get(), "fluid_type.growthcraft_cellar.white_grape_wine_fluid", GrowthcraftCellarConfig.getLootChanceVillagerHome(), UniformGenerator.between(1F, 3F), new MobEffectInstance(MobEffects.SATURATION, 1200, 1, true, false));
		createBottleLootTable(output, Reference.LootTable.BURIED_TREASURE_CHEST_LOOT, GrowthcraftCellarItems.POTION_WINE.get(), "fluid_type.growthcraft_cellar.purple_grape_wine_fluid", GrowthcraftCellarConfig.getLootChanceBeachTreasure(), UniformGenerator.between(2F, 4F), new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1, true, false));
		createBottleLootTable(output, Reference.LootTable.WOODLAND_MANSION_CHEST_LOOT, GrowthcraftCellarItems.POTION_WINE.get(), "fluid_type.growthcraft_cellar.copper_lager_fluid", GrowthcraftCellarConfig.getLootChanceMansion(), UniformGenerator.between(4F, 8F), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3600, 2, true, false));
		createBottleLootTable(output, Reference.LootTable.STRONGHOLD_CHEST_LOOT, GrowthcraftCellarItems.POTION_WINE.get(), "fluid_type.growthcraft_cellar.purple_grape_wine_fluid", GrowthcraftCellarConfig.getLootChanceStronghold(), UniformGenerator.between(2F, 10F), new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1, true, false));
	}

	private static void createBottleLootTable(BiConsumer<ResourceLocation, Builder> output, String lootTableName, Item item, String bottleName, float chance, UniformGenerator count, MobEffectInstance... potionEffects) {
		output.accept(lootTableName(lootTableName), 
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(item)
								.when(LootItemRandomChanceCondition.randomChance(chance))
								.apply(SetItemCountFunction.setCount(count))
								.apply(SetNameFunction.setName(Component.translatable(bottleName)))
								.apply(SetNbtFunction.setTag(createNbtTag(potionEffects))))
						)
				);
	}
	
	private static CompoundTag createNbtTag(MobEffectInstance... potionEffects) {
		ItemStack bottles = GrowthcraftCellarItems.POTION_WINE.get().getDefaultInstance();
		PotionUtils.setCustomEffects(bottles, ObjectArrayList.of(potionEffects));
		return bottles.getTag();
	}
	
	private static ResourceLocation lootTableName(String lootTableName) {
		return new ResourceLocation(Reference.MODID, "chests/" + lootTableName);
	}
}
