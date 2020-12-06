package net.watersfall.spellmod.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.watersfall.spellmod.spells.Spell;
import net.watersfall.spellmod.spells.Spells;

import java.util.List;

public class SpellbookItem extends Item
{
	public SpellbookItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		float f = user.getAttackCooldownProgress(0F);
		if(f >= 1.0F)
		{
			user.setCurrentHand(hand);
			return TypedActionResult.consume(user.getStackInHand(hand));
		}
		return TypedActionResult.fail(user.getStackInHand(hand));
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
				if(tag != null && tag.contains("spell"))
				{
					tag.remove("spell");
				}
				stack.getTag().putString("spell", "waters_spell_mod:acid_splash_spell");
			}
			else
			{
				if(stack.getTag() != null)
				{
					Spell spell = Spells.getSpell(stack.getTag().getString("spell"));
					((PlayerEntity) user).getItemCooldownManager().set(stack.getItem(), spell.castingTime);
					spell.use(stack, world, (PlayerEntity) user);
				}
			}
		}
		return stack;
	}

	@Override
	public int getMaxUseTime(ItemStack stack)
	{
		if(stack.getTag() != null && stack.getTag().getString("spell") != null)
		{
			return Spells.getSpell(stack.getTag().getString("spell")).castingTime;
		}
		return 20;
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
			if(stack.getTag().getString("spell") != null)
			{
				tooltip.add(new TranslatableText(Spells.getSpell(stack.getTag().getString("spell")).translationKey).formatted(Formatting.GRAY, Formatting.ITALIC));
			}
		}
	}
}
