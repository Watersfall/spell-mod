package net.watersfall.spellmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.spellmod.block.PedestalBlock;
import net.watersfall.spellmod.block.entity.PedestalBlockEntity;
import net.watersfall.spellmod.screen.SpellbookScreenHandler;
import net.watersfall.spellmod.spells.SpellClass;
import net.watersfall.spellmod.spells.Spells;
import net.watersfall.spellmod.block.BonfireBlock;
import net.watersfall.spellmod.effect.SpecialStatusEffect;
import net.watersfall.spellmod.entity.AcidSplashEntity;
import net.watersfall.spellmod.entity.ChillTouchEntity;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.level0.*;

import java.awt.*;

public class WatersSpellMod implements ModInitializer
{
	public static final String MOD_ID = "waters_spell_mod";

	public static final ItemGroup SPELL_MOD_GROUP;
	public static final SpellbookItem SPELLBOOK_BARD;
	public static final SpellbookItem SPELLBOOK_CLERIC;
	public static final SpellbookItem SPELLBOOK_DRUID;
	public static final SpellbookItem SPELLBOOK_PALADIN;
	public static final SpellbookItem SPELLBOOK_RANGER;
	public static final SpellbookItem SPELLBOOK_SORCERER;
	public static final SpellbookItem SPELLBOOK_WARLOCK;
	public static final SpellbookItem SPELLBOOK_WIZARD;
	public static final BonfireBlock BONFIRE_BLOCK;
	public static final PedestalBlock PEDESTAL_BLOCK;
	public static final BlockItem PEDESTAL_ITEM;
	public static final BlockEntityType<PedestalBlockEntity> PEDESTAL_BLOCK_ENTITY;
	public static final BlockItem BONFIRE_ITEM;
	public static final EntityType<AcidSplashEntity> ACID_SPLASH_TYPE;
	public static final EntityType<ChillTouchEntity> CHILL_TOUCH_ENTITY;
	public static final StatusEffect BOOMING_BLADE_GIVE = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, Color.YELLOW.hashCode());
	public static final StatusEffect BOOMING_BLADE = new SpecialStatusEffect(StatusEffectType.HARMFUL, Color.YELLOW.hashCode());
	public static final StatusEffect CHILL_OF_THE_GRAVE = new SpecialStatusEffect(StatusEffectType.HARMFUL, Color.BLACK.hashCode());
	public static final ScreenHandlerType<SpellbookScreenHandler> SPELLBOOK_SCREEN_HANDLER;

	static
	{
		SPELL_MOD_GROUP = FabricItemGroupBuilder.build(getId("spells"), () -> new ItemStack(Items.BOOK));
		SPELLBOOK_BARD = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP).maxCount(1), SpellClass.BARD);
		SPELLBOOK_CLERIC = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP).maxCount(1), SpellClass.CLERIC);
		SPELLBOOK_DRUID = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP).maxCount(1), SpellClass.DRUID);
		SPELLBOOK_PALADIN = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP).maxCount(1), SpellClass.PALADIN);
		SPELLBOOK_RANGER = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP).maxCount(1), SpellClass.RANGER);
		SPELLBOOK_SORCERER = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP).maxCount(1), SpellClass.SORCERER);
		SPELLBOOK_WARLOCK = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP).maxCount(1), SpellClass.WARLOCK);
		SPELLBOOK_WIZARD = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP).maxCount(1), SpellClass.WIZARD);
		BONFIRE_BLOCK = new BonfireBlock(FabricBlockSettings.of(Material.FIRE, MaterialColor.LAVA).noCollision().strength(-1.0F, 3600000.0F).luminance(15));
		BONFIRE_ITEM = new BlockItem(BONFIRE_BLOCK, new FabricItemSettings());
		ACID_SPLASH_TYPE = Registry.register(Registry.ENTITY_TYPE,
				getId("acid_splash_entity"),
				FabricEntityTypeBuilder.<AcidSplashEntity>create(SpawnGroup.MISC, AcidSplashEntity::new)
						.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
						.trackRangeBlocks(4)
						.trackedUpdateRate(10)
						.build()
				);
		CHILL_TOUCH_ENTITY = Registry.register(Registry.ENTITY_TYPE,
				getId("chill_touch_entity"),
				FabricEntityTypeBuilder.<ChillTouchEntity>create(SpawnGroup.MISC, ChillTouchEntity::new)
						.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
						.trackRangeBlocks(4)
						.trackedUpdateRate(10)
						.build()
				);
		PEDESTAL_BLOCK = new PedestalBlock(AbstractBlock.Settings.of(Material.STONE));
		PEDESTAL_ITEM = new BlockItem(PEDESTAL_BLOCK, new FabricItemSettings().group(SPELL_MOD_GROUP));
		PEDESTAL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("pedestal_entity"), BlockEntityType.Builder.create(PedestalBlockEntity::new, PEDESTAL_BLOCK).build(null));
		SPELLBOOK_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(getId("spellbook_screen_handler"), SpellbookScreenHandler::new);

	}

	public static Identifier getId(String id)
	{
		return new Identifier(MOD_ID, id);
	}

	@Override
	public void onInitialize()
	{
		Registry.register(Registry.ITEM, getId("spellbook_bard"), SPELLBOOK_BARD);
		Registry.register(Registry.ITEM, getId("spellbook_cleric"), SPELLBOOK_CLERIC);
		Registry.register(Registry.ITEM, getId("spellbook_druid"), SPELLBOOK_DRUID);
		Registry.register(Registry.ITEM, getId("spellbook_paladin"), SPELLBOOK_PALADIN);
		Registry.register(Registry.ITEM, getId("spellbook_ranger"), SPELLBOOK_RANGER);
		Registry.register(Registry.ITEM, getId("spellbook_sorcerer"), SPELLBOOK_SORCERER);
		Registry.register(Registry.ITEM, getId("spellbook_warlock"), SPELLBOOK_WARLOCK);
		Registry.register(Registry.ITEM, getId("spellbook_wizard"), SPELLBOOK_WIZARD);
		Registry.register(Registry.BLOCK, getId("bonfire"), BONFIRE_BLOCK);
		Registry.register(Registry.BLOCK, getId("pedestal"), PEDESTAL_BLOCK);
		Registry.register(Registry.ITEM, getId("bonfire"), BONFIRE_ITEM);
		Registry.register(Registry.ITEM, getId("pedestal"), PEDESTAL_ITEM);
		Spells.addSpell(getId("blade_ward_spell"), new BladeWardSpell(getId("blade_ward_spell").toString()));
		Spells.addSpell(getId("acid_splash_spell"), new AcidSplashSpell(getId("acid_splash_spell").toString()));
		Spells.addSpell(getId("booming_blade_spell"), new BoomingBladeSpell(getId("booming_blade_spell").toString()));
		Spells.addSpell(getId("chill_touch_spell"), new ChillTouchSpell(getId("chill_touch_spell").toString()));
		Spells.addSpell(getId("create_bonfire_spell"), new CreateBonfireSpell(getId("create_bonfire_spell").toString()));
		Registry.register(Registry.STATUS_EFFECT, getId("effect_booming_blade_give"), BOOMING_BLADE_GIVE);
		Registry.register(Registry.STATUS_EFFECT, getId("effect_booming_blade"), BOOMING_BLADE);
		Registry.register(Registry.STATUS_EFFECT, getId("effect_chill_of_the_grave"), CHILL_OF_THE_GRAVE);
		AttackEntityCallback.EVENT.register((player, world, hand, entity, result) -> {
			if(entity instanceof LivingEntity)
			{
				if(player.hasStatusEffect(BOOMING_BLADE_GIVE))
				{
					((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(BOOMING_BLADE, 6 * 20));
					player.removeStatusEffect(BOOMING_BLADE_GIVE);
				}
			}
			return ActionResult.PASS;
		});
	}
}
