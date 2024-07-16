package growthcraft.apiary.datagen.providers;

import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.apiary.shared.Reference;
import growthcraft.lib.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class GrowthcraftApiaryGlobalLootModifiersProvider extends GlobalLootModifierProvider{
	public GrowthcraftApiaryGlobalLootModifiersProvider(PackOutput output) {
		super(output, Reference.MODID);
	}
	
	@Override
	protected void start() {
		add("bee_from_vanilla_bee_nest", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.50f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BEE_NEST).build()}, 
				GrowthcraftApiaryItems.BEE.get().asItem()));
	}

}
