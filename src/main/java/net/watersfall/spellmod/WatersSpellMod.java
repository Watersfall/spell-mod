package net.watersfall.spellmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.spellmod.api.Spells;
import net.watersfall.spellmod.effect.SpecialStatusEffect;
import net.watersfall.spellmod.entity.AcidSplashEntity;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.cantrip.AcidSplashSpell;
import net.watersfall.spellmod.spells.cantrip.BladeWardSpell;
import net.watersfall.spellmod.spells.cantrip.BoomingBladeSpell;

import java.awt.*;

public class WatersSpellMod implements ModInitializer
{
	public static final String MOD_ID = "waters_spell_mod";

	public static final ItemGroup SPELL_MOD_GROUP;
	public static final SpellbookItem SPELLBOOK;
	public static final EntityType<AcidSplashEntity> ACID_SPLASH_TYPE;
	public static final StatusEffect BOOMING_BLADE_GIVE = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, Color.YELLOW.hashCode());
	public static final StatusEffect BOOMING_BLADE = new SpecialStatusEffect(StatusEffectType.HARMFUL, Color.YELLOW.hashCode());

	static
	{
		SPELL_MOD_GROUP = FabricItemGroupBuilder.build(getId("spells"), () -> new ItemStack(Items.BOOK));
		SPELLBOOK = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP));
		ACID_SPLASH_TYPE = Registry.register(Registry.ENTITY_TYPE,
				getId("acid_splash_entity"),
				FabricEntityTypeBuilder.<AcidSplashEntity>create(SpawnGroup.MISC, AcidSplashEntity::new)
						.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
						.trackRangeBlocks(4)
						.trackedUpdateRate(10)
						.build()
				);
	}

	public static Identifier getId(String id)
	{
		return new Identifier(MOD_ID, id);
	}

	@Override
	public void onInitialize()
	{
		Registry.register(Registry.ITEM, getId("spellbook"), SPELLBOOK);
		Spells.addSpell(getId("blade_ward_spell"), new BladeWardSpell(getId("blade_ward_spell").toString()));
		Spells.addSpell(getId("acid_splash_spell"), new AcidSplashSpell(getId("acid_splash_spell").toString()));
		Spells.addSpell(getId("booming_blade_spell"), new BoomingBladeSpell(getId("booming_blade_spell").toString()));
		Registry.register(Registry.STATUS_EFFECT, getId("effect_booming_blade_give"), BOOMING_BLADE_GIVE);
		Registry.register(Registry.STATUS_EFFECT, getId("effect_booming_blade"), BOOMING_BLADE);
		AttackEntityCallback.EVENT.register((player, world, hand, entity, result) -> {
			if(entity instanceof LivingEntity)
			{
				if(player.hasStatusEffect(BOOMING_BLADE_GIVE))
				{
					((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(BOOMING_BLADE, 6 * 20));
				}
			}
			return ActionResult.PASS;
		});
	}
}
