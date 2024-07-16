package growthcraft.milk.datagen.providers;

import growthcraft.milk.shared.Reference;
import growthcraft.lib.loot.AddItemModifier;
import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class GrowthcraftMilkGlobalLootModifiersProvider extends GlobalLootModifierProvider{
	public GrowthcraftMilkGlobalLootModifiersProvider(PackOutput output) {
		super(output, Reference.MODID);
	}
	
	@Override
	protected void start() {
		add("thistle_seeds_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.01f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftMilkItems.THISTLE_SEED.get().asItem()));
	}

}
