package net.watersfall.spellmod.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.spellmod.constants.LangKeys;
import net.watersfall.spellmod.constants.TagKeys;
import net.watersfall.spellmod.inventory.SpellbookInventory;
import net.watersfall.spellmod.screen.SpellbookScreenHandler;
import net.watersfall.spellmod.spells.Spell;
import net.watersfall.spellmod.spells.SpellClass;
import net.watersfall.spellmod.spells.Spells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SpellbookItem extends Item
{
	public final SpellClass spellClass;

	public static void fixInventory(ItemStack stack)
	{
		SpellbookInventory inventory = new SpellbookInventory(stack);
		ArrayList<ItemStack> removed = new ArrayList<>();
		if(!inventory.isEmpty())
		{
			int cantrips = ((SpellbookItem)stack.getItem()).spellClass.knownCantrips[getBookLevel(stack) - 1];
			for(int i = 0; i < cantrips; i++)
			{
				ItemStack invStack = inventory.getStack(i);
				if(!invStack.isEmpty() && ((SpellItem)invStack.getItem()).spell.minLevel > 0)
				{
					removed.add(inventory.removeStack(i));
				}
			}
			int removedCounter = removed.size();
			for(int i = cantrips; i < inventory.size() && removedCounter > 0; i++)
			{
				ItemStack invStack = inventory.getStack(i);
				if(invStack.isEmpty())
				{
					inventory.setStack(i, removed.get(0));
					removed.remove(0);
					removedCounter--;
				}
			}
			inventory.markDirty();
		}
	}
	
	public static int getBookLevel(ItemStack stack)
	{
		if(stack.getTag() != null)
		{
			return stack.getTag().getInt(TagKeys.LEVEL);
		}
		return 1;
	}
	public static void setBookLevel(ItemStack stack, int level)
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
		tag.putIntArray(TagKeys.SPELL_SLOTS, spellClass.levels[getBookLevel(stack)]);
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

	public static int getSpellVariant(ItemStack stack)
	{
		if(stack.getTag() != null && stack.getTag().contains(TagKeys.SPELL_VARIANT))
		{
			return stack.getTag().getInt(TagKeys.SPELL_VARIANT);
		}
		return 0;
	}

	public static void setSpellVariant(ItemStack stack, int variant)
	{
		stack.getOrCreateTag().putInt(TagKeys.SPELL_VARIANT, variant);
	}

	public SpellbookItem(Settings settings, SpellClass spellClass)
	{
		super(settings);
		this.spellClass = spellClass;
	}

	@Environment(EnvType.CLIENT)
	public static void target(ItemStack stack, Entity entity)
	{
		if(isEntityTargeted(stack, entity))
		{
			long[] oldArray = stack.getOrCreateTag().getLongArray("targets");
			if(oldArray.length == 2)
			{
				stack.getOrCreateTag().remove("targets");
				return;
			}
			long[] uuid = new long[]{entity.getUuid().getMostSignificantBits(), entity.getUuid().getLeastSignificantBits()};
			long[] newArray = new long[oldArray.length - 2];
			int oldIndex = 0;
			for(int i = 0; i < newArray.length; i++)
			{
				if(oldArray[oldIndex] == uuid[0] && oldArray[oldIndex + 1] == uuid[1])
				{
					oldIndex += 2;
				}
				newArray[i] = oldArray[oldIndex];
				oldIndex++;
			}
			stack.getTag().putLongArray("targets", newArray);
		}
		else
		{
			long[] oldArray = stack.getOrCreateTag().getLongArray("targets");
			long[] uuid = new long[]{entity.getUuid().getMostSignificantBits(), entity.getUuid().getLeastSignificantBits()};
			Spell spell = getActiveSpell(stack).spell;
			int level = getSpellLevel(stack);
			if(oldArray.length / 2 >= spell.getMaxTargets(level))
			{
				long[] newArray = new long[spell.getMaxTargets(level) * 2];
				System.arraycopy(oldArray, 2, newArray, 0, newArray.length - 2);
				newArray[newArray.length - 2] = uuid[0];
				newArray[newArray.length - 1] = uuid[1];
				stack.getTag().putLongArray("targets", newArray);
			}
			else
			{
				long[] newArray = Arrays.copyOf(oldArray, oldArray.length + uuid.length);
				System.arraycopy(uuid, 0, newArray, oldArray.length, newArray.length - oldArray.length);
				stack.getTag().putLongArray("targets", newArray);
			}
		}
	}

	public static UUID[] getTargets(ItemStack stack)
	{
		UUID[] list;
		if(stack.getOrCreateTag().contains("targets"))
		{
			long[] array = stack.getOrCreateTag().getLongArray("targets");
			list = new UUID[array.length / 2];
			for(int i = 0; i < list.length; i++)
			{
				list[i] = new UUID(array[i * 2], array[i * 2 + 1]);
			}
		}
		else
		{
			list = new UUID[0];
		}
		return list;
	}

	public static boolean isEntityTargeted(ItemStack stack, Entity entity)
	{
		long[] array = stack.getOrCreateTag().getLongArray("targets");
		UUID uuid = entity.getUuid();
		for(int i = 0; i < array.length; i = i + 2)
		{
			if(uuid.getMostSignificantBits() == array[i])
			{
				if(uuid.getLeastSignificantBits() == array[i + 1])
				{
					return true;
				}
			}
		}
		return false;
	}

	public static void clearTargets(ItemStack stack)
	{
		stack.getOrCreateTag().remove("targets");
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner)
	{
		return false;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack stack = user.getStackInHand(hand);
		if(stack.getTag() == null)
		{
			stack = this.getDefaultStack().copy();
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
						int castingLevel = getSpellLevel(stack);
						if(getSpellSlots(stack, castingLevel - 1) > 0)
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
					if(spell.item.level <= 0 || getSpellSlots(stack, getSpellLevel(stack) - 1) > 0)
					{
						((PlayerEntity) user).getItemCooldownManager().set(stack.getItem(), spell.castingTime);
						spell.use(stack, world, (PlayerEntity) user);
						if(spell.item.level > 0)
						{
							subtractSpellSlot(stack, getSpellLevel(stack) - 1);
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
		setBookLevel(stack, 1);
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
			stack.setTag(getDefaultStack().getTag());
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
			tooltip.add(new TranslatableText(LangKeys.LEVEL).append(": " + getBookLevel(stack)));
			if(stack.getTag().contains(TagKeys.ACTIVE_SPELL))
			{
				Spell spell = Spells.getSpell(stack.getTag().getString(TagKeys.ACTIVE_SPELL));
				MutableText text = new TranslatableText(spell.translationKey).formatted(Formatting.GRAY, Formatting.ITALIC);
				if(spell.hasMultipleModes() && stack.getTag().contains(TagKeys.SPELL_VARIANT))
				{
					text.append(" (").append(new TranslatableText(LangKeys.getSpellVariant(spell, getSpellVariant(stack)))).append(")");
				}
				tooltip.add(text);
			}
			if(InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), MinecraftClient.getInstance().options.keySneak.boundKey.getCode()))
			{
				tooltip.add(new TranslatableText(LangKeys.SPELL_SLOTS).append(":"));
				for(int i = 1; i <= 9; i++)
				{
					if(this.spellClass.levels[getBookLevel(stack) - 1][i-1] > 0)
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

	@Override
	public boolean hasGlint(ItemStack stack)
	{
		return stack.getTag() != null;
	}

	@Override
	public Rarity getRarity(ItemStack stack)
	{
		int level = getBookLevel(stack);
		if(level <= 5)
			return Rarity.COMMON;
		else if(level <= 10)
			return Rarity.UNCOMMON;
		else if(level <= 15)
			return Rarity.RARE;
		else
			return Rarity.EPIC;
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
