package net.watersfall.spellmod.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.watersfall.spellmod.spells.Spell;

import java.util.List;

public class SpellItem extends Item
{
	public final int castingTime;
	public final int level;
	public final Spell spell;

	public SpellItem(Settings settings, int castingTime, Spell spell)
	{
		this(settings, castingTime, 0, spell);
	}

	public SpellItem(Settings settings, int castingTime, int level, Spell spell)
	{
		super(settings);
		this.castingTime = castingTime;
		this.level = level;
		this.spell = spell;
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("text.waters_spell_mod.spell_classes"));
		for(int i = 0; i < this.spell.classes.length; i++)
		{
			tooltip.add(new LiteralText(" - ").append(new TranslatableText(this.spell.classes[i].getTranslationKey())).formatted(Formatting.GRAY));
		}
	}
}
