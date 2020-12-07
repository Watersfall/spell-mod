package net.watersfall.spellmod.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
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
import net.watersfall.spellmod.spells.Spell;
import net.watersfall.spellmod.spells.SpellClass;
import net.watersfall.spellmod.spells.Spells;
import org.lwjgl.glfw.GLFW;

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
			user.setStackInHand(hand, this.getDefaultStack().copy());
			return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
		}
		else
		{
			if(getLevel(stack) < 20)
			{
				setLevel(stack, getLevel(stack) + 1);
				setSpellSlots(stack);
			}
			if(stack.getOrCreateTag().contains(TagKeys.ACTIVE_SPELL))
			{
				float f = user.getAttackCooldownProgress(0F);
				if(f >= 1.0F)
				{
					user.setCurrentHand(hand);
					return TypedActionResult.consume(stack);
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
			if(user.isSneaking())
			{
				//Open GUI
				//But for now this will work for testing
				if(stack.getTag() == null)
				{
					stack.setTag(new CompoundTag());
				}
				CompoundTag tag = stack.getTag();
				if(tag != null && tag.contains(TagKeys.ACTIVE_SPELL))
				{
					tag.remove(TagKeys.ACTIVE_SPELL);
				}
				stack.getTag().putString(TagKeys.ACTIVE_SPELL, "waters_spell_mod:acid_splash_spell");
				for(int i = 1; i <= 9; i++)
				{
					tag.putInt(TagKeys.getSpellSlotTag(i), this.spellClass.levels[getLevel(stack) - 1][i - 1]);
				}
			}
			else
			{
				if(stack.getTag() != null && stack.getTag().contains(TagKeys.ACTIVE_SPELL))
				{
					if(stack.getTag().getInt("level_1_spell_slots") > 0)
					{
						Spell spell = Spells.getSpell(stack.getTag().getString("spell"));
						((PlayerEntity) user).getItemCooldownManager().set(stack.getItem(), spell.castingTime);
						spell.use(stack, world, (PlayerEntity) user);
						stack.getTag().putInt("level_1_spell_slots", stack.getTag().getInt("level_1_spell_slots") - 1);
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
			stack.getOrCreateTag().putInt(TagKeys.getSpellSlotTag(i), this.spellClass.levels[getLevel(stack) - 1][i - 1]);
		}
		return stack;
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player)
	{
		super.onCraft(stack, world, player);
		for(int i = 1; i <= 9; i++)
		{
			stack.getOrCreateTag().putInt(TagKeys.getSpellSlotTag(i), this.spellClass.levels[getLevel(stack) - 1][i - 1]);
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
								.append(": " + stack.getTag().getInt(TagKeys.getSpellSlotTag(i)))
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

	private void setSpellSlots(ItemStack stack)
	{
		CompoundTag tag = stack.getOrCreateTag();
		for(int i = 1; i <= 9; i++)
		{
			tag.putInt(TagKeys.getSpellSlotTag(i), this.spellClass.levels[getLevel(stack) - 1][i - 1]);
		}
	}
}
