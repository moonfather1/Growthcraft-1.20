package growthcraft.cellar.init;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.config.GrowthcraftCellarConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

// puts some wine and ale into loot chests in world to tell players that there are drinks to be brewed.
public class LootModifierForBottlesInChests extends LootModifier
{
    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
    {
        // step 0: this assertion will crash the game in 1.20.6 and later
        // this will force someone to update comparisons below (PILLAGER_OUTPOST will become a ResourceKey).
        // without this, in 1.20.6,  lootTableId.equals(some table)  would just quietly always return false.
        // that took some of us many hours to figure out
        assert BuiltInLootTables.PILLAGER_OUTPOST.getClass().getSimpleName().equals("ResourceLocation");

        // step 1: roll the dice
        int chance = 0;
        ResourceLocation lootTableId = context.getQueriedLootTableId();
        if (lootTableId.equals(BuiltInLootTables.PILLAGER_OUTPOST))
        {
            chance = GrowthcraftCellarConfig.getLootChancePillagerTower();
        }
        else if (lootTableId.equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL) || lootTableId.equals(BuiltInLootTables.UNDERWATER_RUIN_BIG))
        {
            chance = GrowthcraftCellarConfig.getLootChanceOceanRuin();
        }
        else if (lootTableId.equals(BuiltInLootTables.SHIPWRECK_SUPPLY) || lootTableId.equals(BuiltInLootTables.SHIPWRECK_MAP))
        {
            chance = GrowthcraftCellarConfig.getLootChanceShipwreck();
        }
        else if (lootTableId.getPath().startsWith("chests/village/"))
        {
            chance = GrowthcraftCellarConfig.getLootChanceVillagerHome();
        }
        else if (lootTableId.equals(BuiltInLootTables.BURIED_TREASURE))
        {
            chance = GrowthcraftCellarConfig.getLootChanceBeachTreasure();
        }
        else if (lootTableId.equals(BuiltInLootTables.WOODLAND_MANSION))
        {
            chance = GrowthcraftCellarConfig.getLootChanceMansion();
        }
        else if (lootTableId.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR) || lootTableId.equals(BuiltInLootTables.STRONGHOLD_CROSSING))
        {
            chance = GrowthcraftCellarConfig.getLootChanceStronghold();
        }
        if (context.getRandom().nextInt(100) >= chance)
        {
            return generatedLoot; // random chance failed, pack it in. chance is 0 for the majority of cases anyway.
        }

        // step 2: decide on loot item. if there are multiple bottles in a chest (no reason why not), we want them of the same type so that they don't take more than 1 inventory slot.
        // yes we're repeating branches from above. above, there was a 99% chance the chance would be 0, and we didn't want to do any more logic other than rolling.
        CompoundTag tag = new CompoundTag();
        int min = 1;
        int max = 2;
        MobEffectInstance effect1 = null;
        MobEffectInstance effect2 = null;
        String nameKey = "asd";
        if (lootTableId.equals(BuiltInLootTables.PILLAGER_OUTPOST))
        {
            min = 6; max = 14;
            effect1 = new MobEffectInstance(MobEffects.HEALTH_BOOST, 3600, 1, true, false);
            nameKey = "fluid_type.growthcraft_apiary.honey_mead_fluid";
        }
        else if (lootTableId.equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL) || lootTableId.equals(BuiltInLootTables.UNDERWATER_RUIN_BIG))
        {
            min = 1; max = 4;
            effect1 = new MobEffectInstance(MobEffects.SATURATION, 1200, 1, true, false);
            effect2 = new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 1200, 0, true, false);
            nameKey = "fluid_type.growthcraft_cellar.white_grape_wine_fluid";
        }
        else if (lootTableId.equals(BuiltInLootTables.SHIPWRECK_SUPPLY) || lootTableId.equals(BuiltInLootTables.SHIPWRECK_MAP))
        {
            min = 4; max = 8;
            effect1 = new MobEffectInstance(MobEffects.LUCK, 6000, 1, true, false);
            nameKey = "fluid_type.growthcraft_cellar.old_port_ale_fluid";
        }
        else if (lootTableId.getPath().startsWith("chests/village/"))
        {
            min = 1; max = 3;
            effect1 = new MobEffectInstance(MobEffects.SATURATION, 1200, 1, true, false);
            nameKey = "fluid_type.growthcraft_cellar.white_grape_wine_fluid";
        }
        else if (lootTableId.equals(BuiltInLootTables.BURIED_TREASURE))
        {
            min = 2; max = 4;
            effect1 = new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1, true, false); // recipe's 1min is way too short
            nameKey = "fluid_type.growthcraft_cellar.purple_grape_wine_fluid";
        }
        else if (lootTableId.equals(BuiltInLootTables.WOODLAND_MANSION))
        {
            min = 4; max = 8;
            effect1 = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3600, 2, true, false);
            nameKey = "fluid_type.growthcraft_cellar.copper_lager_fluid";
        }
        else if (lootTableId.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR) || lootTableId.equals(BuiltInLootTables.STRONGHOLD_CROSSING))
        {
            min = 2; max = 10;
            effect1 = new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1, true, false); // recipe's 1min is way too short
            nameKey = "fluid_type.growthcraft_cellar.purple_grape_wine_fluid";
        }

        // step 3: make a stack of bottles
        ItemStack bottles = GrowthcraftCellarItems.POTION_WINE.get().getDefaultInstance();
        bottles.setCount(context.getRandom().nextIntBetweenInclusive(min, max));
        if (effect1 != null) {
            if (effect2 == null) {
                PotionUtils.setCustomEffects(bottles, ObjectArrayList.of(effect1));
            }
            else {
                PotionUtils.setCustomEffects(bottles, ObjectArrayList.of(effect1, effect2));
            }
        }
        bottles.setHoverName(Component.translatable(nameKey));

        // step 3b: add a stack of bottles
        if (generatedLoot.size() < 27)
        {
            generatedLoot.add(bottles);
        }
        else
        {
            // step 3c: if full, pick an empty slot and add a stack of bottles.
            for (int i = generatedLoot.size() - 1; i >= 0; i--)
            {
                if (generatedLoot.get(i).isEmpty()) // we could replace some useless items (wheat, etc) but that might be too fancy for growthcraft.
                {
                    generatedLoot.set(i, bottles);
                    break;
                }
            }
        }

        return generatedLoot;
    }

    //////  nothing useful below this line ///////////////////////////////////////

    protected LootModifierForBottlesInChests(LootItemCondition[] conditionsIn)
    {
        super(conditionsIn);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec()
    {
        return null;
    }

    public static final Supplier<Codec<LootModifierForBottlesInChests>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    // no fields. if there were, we'd call .and() here
                    .apply(inst, LootModifierForBottlesInChests::new)));
}
