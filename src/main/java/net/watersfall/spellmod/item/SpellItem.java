package net.watersfall.spellmod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.watersfall.spellmod.spells.Spell;

public class SpellItem extends Item
{
	public final int castingTime;
	public final int level;
	public final Spell spell;

	public SpellItem(Settings settings, int castingTime, Spell spell)
	{
		super(settings);
		this.castingTime = castingTime;
		this.level = 0;
		this.spell = spell;
	}

	public SpellItem(Settings settings, int castingTime, int level, Spell spell)
	{
		super(settings);
		this.castingTime = castingTime;
		this.level = level;
		this.spell = spell;
	}
}
