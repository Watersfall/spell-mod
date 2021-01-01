package net.watersfall.spellmod.spells;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.watersfall.spellmod.util.function.ToBooleanFunction;

import java.util.function.ToIntFunction;

public class SpellProperties
{
	private boolean hasMultipleModes;
	private boolean canTargetSelect;
	private double range;
	private int minLevel;
	private int maxLevel;
	private int castingTime;
	private ToIntFunction<ItemStack> maxTargets;
	private ToBooleanFunction<Entity> isValidTarget;
	private int[] modes;

	public SpellProperties()
	{
		this.hasMultipleModes = false;
		this.canTargetSelect = false;
		this.range = 0D;
		this.minLevel = 0;
		this.maxLevel = 0;
		this.castingTime = 20;
		this.maxTargets = (stack) -> 0;
		this.modes = new int[]{0};
		this.isValidTarget = (entity) -> false;
	}

	public SpellProperties(int level)
	{
		this.hasMultipleModes = false;
		this.canTargetSelect = false;
		this.range = 0D;
		this.minLevel = level;
		this.maxLevel = 9;
		this.castingTime = 20;
		this.maxTargets = (stack) -> 0;
		this.modes = new int[]{0};
		this.isValidTarget = (entity) -> false;
	}

	public SpellProperties setHasMultipleModes(boolean hasMultipleModes)
	{
		this.hasMultipleModes = hasMultipleModes;
		return this;
	}

	public SpellProperties setCanTargetSelect(boolean canTargetSelect)
	{
		this.canTargetSelect = canTargetSelect;
		return this;
	}

	public SpellProperties setRange(double range)
	{
		this.range = range;
		return this;
	}

	public SpellProperties setMinLevel(int minLevel)
	{
		this.minLevel = minLevel;
		return this;
	}

	public SpellProperties setMaxLevel(int maxLevel)
	{
		this.maxLevel = maxLevel;
		return this;
	}

	public SpellProperties setCastingTime(int castingTime)
	{
		this.castingTime = castingTime;
		return this;
	}

	public SpellProperties setMaxTargets(ToIntFunction<ItemStack> maxTargets)
	{
		this.maxTargets = maxTargets;
		return this;
	}

	public SpellProperties setModes(int... modes)
	{
		this.modes = modes;
		return this;
	}

	public ToBooleanFunction<Entity> getIsValidTarget()
	{
		return isValidTarget;
	}

	public SpellProperties setIsValidTarget(ToBooleanFunction<Entity> isValidTarget)
	{
		this.isValidTarget = isValidTarget;
		return this;
	}

	public boolean hasMultipleModes()
	{
		return hasMultipleModes;
	}

	public boolean canTargetSelect()
	{
		return canTargetSelect;
	}

	public double getRange()
	{
		return range;
	}

	public int getMinLevel()
	{
		return minLevel;
	}

	public int getMaxLevel()
	{
		return maxLevel;
	}

	public int getCastingTime()
	{
		return castingTime;
	}

	public ToIntFunction<ItemStack> getMaxTargets()
	{
		return maxTargets;
	}

	public int[] getModes()
	{
		return modes;
	}
}
