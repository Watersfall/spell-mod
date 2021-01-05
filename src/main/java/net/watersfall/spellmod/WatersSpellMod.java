package net.watersfall.spellmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
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
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.spellmod.block.PedestalBlock;
import net.watersfall.spellmod.block.entity.PedestalBlockEntity;
import net.watersfall.spellmod.effect.AidEffect;
import net.watersfall.spellmod.effect.ArmorOfAgathysEffect;
import net.watersfall.spellmod.effect.FriendshipEffect;
import net.watersfall.spellmod.entity.*;
import net.watersfall.spellmod.screen.SpellbookScreenHandler;
import net.watersfall.spellmod.spells.*;
import net.watersfall.spellmod.block.BonfireBlock;
import net.watersfall.spellmod.effect.SpecialStatusEffect;
import net.watersfall.spellmod.item.SpellbookItem;

import java.awt.*;

public class WatersSpellMod implements ModInitializer
{
	public static final String MOD_ID = "waters_spell_mod";

	public static final Identifier BUTTON_PACKET_ID = getId("button_packet");
	public static final Identifier SPAWN_PACKET_ID = getId("spawn_packet");
	public static final Identifier MAGIC_MISSILE_PACKET_ID = getId("magic_missile_packet");
	public static final Identifier TARGET_PACKET_ID = getId("target");
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
	public static final EntityType<ChromaticOrbEntity> CHROMATIC_ORB_ENTITY;
	public static final EntityType<CloudOfDaggersEntity> CLOUD_OF_DAGGERS_ENTITY;
	public static final EntityType<MagicMissileEntity> MAGIC_MISSILE_ENTITY;
	public static final StatusEffect BOOMING_BLADE_GIVE = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, Color.YELLOW.hashCode());
	public static final StatusEffect BOOMING_BLADE = new SpecialStatusEffect(StatusEffectType.HARMFUL, Color.YELLOW.hashCode());
	public static final StatusEffect CHILL_OF_THE_GRAVE = new SpecialStatusEffect(StatusEffectType.HARMFUL, Color.BLACK.hashCode());
	public static final StatusEffect FRIENDSHIP_EFFECT = new FriendshipEffect(StatusEffectType.HARMFUL, Color.RED.hashCode());
	public static final StatusEffect ARMOR_OF_AGATHYS_EFFECT = new ArmorOfAgathysEffect();
	public static final StatusEffect AID_STATUS_EFFECT = new AidEffect();
	public static final ScreenHandlerType<SpellbookScreenHandler> SPELLBOOK_SCREEN_HANDLER;
	public static final Tag<Item> SPELLBOOK_TAG;
	public static final Spell BLADE_WARD_SPELL;
	public static final Spell ACID_SPLASH_SPELL;
	public static final Spell BOOMING_BLADE_SPELL;
	public static final Spell CHILL_TOUCH_SPELL;
	public static final Spell CREATE_BONFIRE_SPELL;
	public static final Spell ANIMAL_FRIENDSHIP_SPELL;
	public static final Spell ARMOR_OF_AGATHYS_SPELL;
	public static final Spell CHROMATIC_ORB_SPELL;
	public static final Spell AID_SPELL;
	public static final Spell CLOUD_OF_DAGGERS_SPELL;
	public static final Spell MAGIC_MISSILE_SPELL;

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
		CHROMATIC_ORB_ENTITY = Registry.register(Registry.ENTITY_TYPE,
				getId("chromatic_orb_entity"),
				FabricEntityTypeBuilder.<ChromaticOrbEntity>create(SpawnGroup.MISC, ChromaticOrbEntity::new)
						.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
						.trackRangeBlocks(4)
						.trackedUpdateRate(10)
						.build()
		);
		CLOUD_OF_DAGGERS_ENTITY = Registry.register(Registry.ENTITY_TYPE,
				getId("cloud_of_daggers_entity"),
				FabricEntityTypeBuilder.<CloudOfDaggersEntity>create(SpawnGroup.MISC, CloudOfDaggersEntity::new)
						.dimensions(EntityDimensions.fixed(5F / 3F, 5F / 3F))
						.fireImmune()
						.trackRangeBlocks(4)
						.trackedUpdateRate(10)
						.build()
		);
		MAGIC_MISSILE_ENTITY = Registry.register(Registry.ENTITY_TYPE,
				getId("magic_missile_entity"),
				FabricEntityTypeBuilder.<MagicMissileEntity>create(SpawnGroup.MISC, MagicMissileEntity::new)
						.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
						.fireImmune()
						.trackRangeBlocks(4)
						.trackedUpdateRate(10)
						.build()
		);
		PEDESTAL_BLOCK = new PedestalBlock(AbstractBlock.Settings.of(Material.STONE));
		PEDESTAL_ITEM = new BlockItem(PEDESTAL_BLOCK, new FabricItemSettings().group(SPELL_MOD_GROUP));
		PEDESTAL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("pedestal_entity"), BlockEntityType.Builder.create(PedestalBlockEntity::new, PEDESTAL_BLOCK).build(null));
		SPELLBOOK_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(getId("spellbook_screen_handler"), SpellbookScreenHandler::new);
		ServerSidePacketRegistry.INSTANCE.register(BUTTON_PACKET_ID, (context, buf) -> {
			Hand hand = Hand.values()[buf.readByte()];
			ItemStack stack = buf.readItemStack();
			PlayerEntity player = context.getPlayer();
			context.getTaskQueue().execute(() -> {
				player.setStackInHand(hand, stack);
			});
		});
		ServerSidePacketRegistry.INSTANCE.register(TARGET_PACKET_ID, ((context, buf) -> {
			Hand hand = Hand.values()[buf.readByte()];
			ItemStack stack = buf.readItemStack();
			PlayerEntity player = context.getPlayer();
			context.getTaskQueue().execute(() -> {
				player.setStackInHand(hand, stack);
			});
		}));
		SPELLBOOK_TAG = TagRegistry.item(getId("spellbooks"));
		BLADE_WARD_SPELL = Spells.addSpell(getId("blade_ward_spell"),
				new Spell(new SpellProperties().setClasses(SpellClass.BARD, SpellClass.SORCERER, SpellClass.WARLOCK, SpellClass.WIZARD), SpellAction::BLADE_WARD));
		ACID_SPLASH_SPELL = Spells.addSpell(getId("acid_splash_spell"),
				new Spell(new SpellProperties().setClasses(SpellClass.SORCERER, SpellClass.WIZARD), SpellAction::ACID_SPLASH));
		BOOMING_BLADE_SPELL = Spells.addSpell(getId("booming_blade_spell"),
				new Spell(new SpellProperties().setClasses(SpellClass.SORCERER, SpellClass.WARLOCK, SpellClass.WIZARD), SpellAction::BOOMING_BLADE));
		CHILL_TOUCH_SPELL = Spells.addSpell(getId("chill_touch_spell"),
				new Spell(new SpellProperties().setRange(40D).setClasses(SpellClass.SORCERER, SpellClass.WARLOCK, SpellClass.WIZARD), SpellAction::CHILL_TOUCH));
		CREATE_BONFIRE_SPELL = Spells.addSpell(getId("create_bonfire_spell"),
				new Spell(new SpellProperties().setRange(20D).setClasses(SpellClass.DRUID, SpellClass.SORCERER, SpellClass.WARLOCK, SpellClass.WIZARD), SpellAction::CREATE_BONFIRE));
		ANIMAL_FRIENDSHIP_SPELL = Spells.addSpell(getId("animal_friendship_spell"),
				new Spell(new SpellProperties(1).setCanTargetSelect(true).setMaxTargets(SpellbookItem::getSpellLevel).setRange(10D).setIsValidTarget((entity) -> entity instanceof MobEntity).setClasses(SpellClass.BARD, SpellClass.DRUID, SpellClass.RANGER), SpellAction::ANIMAL_FRIENDSHIP));
		ARMOR_OF_AGATHYS_SPELL = Spells.addSpell(getId("armor_of_agathys_spell"),
				new Spell(new SpellProperties(1).setClasses(SpellClass.WARLOCK), SpellAction::ARMOR_OF_AGATHYS));
		CHROMATIC_ORB_SPELL = Spells.addSpell(getId("chromatic_orb_spell"),
				new Spell(new SpellProperties(1).setRange(30D).setHasMultipleModes(true).setModes(SpellModes.Elements.ACID, SpellModes.Elements.COLD, SpellModes.Elements.LIGHTNING, SpellModes.Elements.POISON, SpellModes.Elements.THUNDER).setClasses(SpellClass.SORCERER, SpellClass.WIZARD), SpellAction::CHROMATIC_ORB));
		AID_SPELL = Spells.addSpell(getId("aid_spell"),
				new Spell(new SpellProperties(2).setCanTargetSelect(true).setRange(10D).setMaxTargets((stack) -> 3).setIsValidTarget((entity) -> entity instanceof LivingEntity).setClasses(SpellClass.CLERIC, SpellClass.PALADIN), SpellAction::AID));
		CLOUD_OF_DAGGERS_SPELL = Spells.addSpell(getId("cloud_of_daggers_spell"),
				new Spell(new SpellProperties(2).setRange(20).setClasses(SpellClass.BARD, SpellClass.SORCERER, SpellClass.WARLOCK, SpellClass.WIZARD), SpellAction::CLOUD_OF_DAGGERS));
		MAGIC_MISSILE_SPELL = Spells.addSpell(getId("magic_missile_spell"),
				new Spell(new SpellProperties(1).setRange(40D).setClasses(SpellClass.SORCERER, SpellClass.WIZARD).setCanTargetSelect(true).setIsValidTarget((e) -> e instanceof LivingEntity).setMaxTargets((s) -> SpellbookItem.getSpellLevel(s) + 2), SpellAction::MAGIC_MISSILE));
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
		Registry.register(Registry.ITEM, getId("c"), new Item(new FabricItemSettings()));
		Registry.register(Registry.ITEM, getId("s"), new Item(new FabricItemSettings()));
		Registry.register(Registry.STATUS_EFFECT, getId("effect_booming_blade_give"), BOOMING_BLADE_GIVE);
		Registry.register(Registry.STATUS_EFFECT, getId("effect_booming_blade"), BOOMING_BLADE);
		Registry.register(Registry.STATUS_EFFECT, getId("effect_chill_of_the_grave"), CHILL_OF_THE_GRAVE);
		Registry.register(Registry.STATUS_EFFECT, getId("effect_friendship"), FRIENDSHIP_EFFECT);
		Registry.register(Registry.STATUS_EFFECT, getId("effect_armor_of_agathys"), ARMOR_OF_AGATHYS_EFFECT);
		Registry.register(Registry.STATUS_EFFECT, getId("effect_aid"), AID_STATUS_EFFECT);
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
