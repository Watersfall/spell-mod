package net.watersfall.spellmod.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.watersfall.spellmod.constants.LangKeys;
import net.watersfall.spellmod.constants.TagKeys;
import net.watersfall.spellmod.inventory.SpellbookInventory;
import net.watersfall.spellmod.screen.SpellbookScreenHandler;
import net.watersfall.spellmod.spells.Spell;
import net.watersfall.spellmod.spells.SpellClass;
import net.watersfall.spellmod.spells.Spells;

import java.util.List;

public class SpellbookItem extends Item
{
	public final SpellClass spellClass;
	
	public static int getLevel(ItemStack stack)
	{
		if(stack.getTag() != null)
		{
			return stack.getTag().getInt(TagKeys.LEVEL);
		}
		return 1;
	}
	public static void setLevel(ItemStack stack, int level)
	{
		stack.getOrCreateTag().putInt(TagKeys.LEVEL, level);
	}

	public static int getSpellLevel(ItemStack stack)
	{
		if(stack.getOrCreateTag() != null && stack.getTag().contains(TagKeys.SPELL_LEVEL))
		{
			return stack.getTag().getInt(TagKeys.SPELL_LEVEL);
		}
		return 0;
	}

	public static void setSpellLevel(ItemStack stack, int level)
	{
		stack.getOrCreateTag().putInt(TagKeys.SPELL_LEVEL, level);
	}

	public static void setSpellSlots(ItemStack stack)
	{
		CompoundTag tag = stack.getOrCreateTag();
		SpellClass spellClass = ((SpellbookItem)stack.getItem()).spellClass;
		tag.putIntArray(TagKeys.SPELL_SLOTS, spellClass.levels[getLevel(stack)]);
	}

	public static int getSpellSlots(ItemStack stack, int level)
	{
		if(stack.getTag() != null)
		{
			return stack.getTag().getIntArray(TagKeys.SPELL_SLOTS)[level];
		}
		return 0;
	}

	public static void subtractSpellSlot(ItemStack stack, int level)
	{
		CompoundTag tag = stack.getTag();
		int[] array = tag.getIntArray(TagKeys.SPELL_SLOTS);
		array[level] = array[level] - 1;
		tag.putIntArray(TagKeys.SPELL_SLOTS, array);
	}

	public static SpellItem getActiveSpell(ItemStack stack)
	{
		if(stack.getTag() != null && stack.getTag().contains(TagKeys.ACTIVE_SPELL))
		{
			return Spells.getSpell(stack.getTag().getString(TagKeys.ACTIVE_SPELL)).item;
		}
		return null;
	}

	public SpellbookItem(Settings settings, SpellClass spellClass)
	{
		super(settings);
		this.spellClass = spellClass;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack stack = user.getStackInHand(hand);
		if(stack.getTag() == null)
		{
			stack = this.getDefaultStack().copy();
			stack.getTag().putString(TagKeys.SPELL_LIST, "waters_spell_mod:acid_splash_spell;waters_spell_mod:chill_touch_spell");
			user.setStackInHand(hand, stack);
			return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
		}
		else
		{
			if(user.isSneaking())
			{
				user.openHandledScreen(createScreenHandlerFactory(stack));
				return TypedActionResult.success(stack);
			}
			else if(stack.getOrCreateTag().contains(TagKeys.ACTIVE_SPELL))
			{
				float f = user.getAttackCooldownProgress(0F);
				if(f >= 1.0F)
				{
					Spell spell = Spells.getSpell(stack.getTag().getString(TagKeys.ACTIVE_SPELL));
					if(spell.item.level > 0)
					{
						if(getSpellSlots(stack, spell.item.level - 1) > 0)
						{
							user.setCurrentHand(hand);
							return TypedActionResult.consume(stack);
						}
					}
					else
					{
						user.setCurrentHand(hand);
						return TypedActionResult.consume(stack);
					}
				}
			}
		}
		return TypedActionResult.pass(user.getStackInHand(hand));
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
	{
		if(user instanceof PlayerEntity)
		{
			if(stack.getTag() != null && stack.getTag().contains(TagKeys.ACTIVE_SPELL))
			{
				Spell spell = Spells.getSpell(stack.getTag().getString(TagKeys.ACTIVE_SPELL));
				if(spell != null)
				{
					if(spell.item.level <= 0 || getSpellSlots(stack, spell.item.level - 1) > 0)
					{
						((PlayerEntity) user).getItemCooldownManager().set(stack.getItem(), spell.castingTime);
						spell.use(stack, world, (PlayerEntity) user);
						if(spell.item.level > 0)
						{
							subtractSpellSlot(stack, spell.item.level - 1);
						}
					}
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getDefaultStack()
	{
		ItemStack stack = super.getDefaultStack();
		setLevel(stack, 1);
		for(int i = 1; i <= 9; i++)
		{
			setSpellSlots(stack);
		}
		return stack;
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player)
	{
		super.onCraft(stack, world, player);
		for(int i = 1; i <= 9; i++)
		{
			setSpellSlots(stack);
		}
	}

	@Override
	public int getMaxUseTime(ItemStack stack)
	{
		if(stack.getTag() != null && stack.getTag().contains(TagKeys.ACTIVE_SPELL))
		{
			return Spells.getSpell(stack.getTag().getString(TagKeys.ACTIVE_SPELL)).castingTime;
		}
		return 0;
	}

	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.BOW;
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
	{
		if(stack.getTag() != null)
		{
			tooltip.add(new TranslatableText(LangKeys.LEVEL).append(": " + getLevel(stack)));
			if(stack.getTag().contains(TagKeys.ACTIVE_SPELL))
			{
				tooltip.add(new TranslatableText(Spells.getSpell(stack.getTag().getString(TagKeys.ACTIVE_SPELL)).translationKey).formatted(Formatting.GRAY, Formatting.ITALIC));
			}
			if(InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), MinecraftClient.getInstance().options.keySneak.boundKey.getCode()))
			{
				tooltip.add(new TranslatableText(LangKeys.SPELL_SLOTS).append(":"));
				for(int i = 1; i <= 9; i++)
				{
					if(this.spellClass.levels[getLevel(stack) - 1][i-1] > 0)
					{
						tooltip.add(new LiteralText(" - ")
								.append(new TranslatableText(LangKeys.getLeveledSpellSlot(i)))
								.append(": " + getSpellSlots(stack, i - 1))
								.formatted(Formatting.GRAY, Formatting.ITALIC));
					}
				}
			}
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		if(entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entity;
			if(player.isSleepingLongEnough())
			{
				setSpellSlots(stack);
			}
		}
	}

	private ExtendedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack)
	{
		return new ExtendedScreenHandlerFactory()
		{
			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player)
			{
				return new SpellbookScreenHandler(syncId, inventory, new SpellbookInventory(stack));
			}

			@Override
			public Text getDisplayName()
			{
				return stack.getName();
			}

			@Override
			public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf)
			{
				buf.writeItemStack(stack);
			}
		};
	}
}
