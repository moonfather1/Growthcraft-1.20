package growthcraft.rice.datagen.providers;

import growthcraft.rice.init.GrowthcraftRiceItems;
import growthcraft.rice.shared.Reference;
import growthcraft.lib.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class GrowthcraftRiceGlobalLootModifiersProvider extends GlobalLootModifierProvider{
	public GrowthcraftRiceGlobalLootModifiersProvider(PackOutput output) {
		super(output, Reference.MODID);
	}
	
	@Override
	protected void start() {
		add("thistle_seeds_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.01f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftRiceItems.RICE_STALK.get().asItem()));
	}

}
