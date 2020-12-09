package net.watersfall.spellmod.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.MobEntity;

public class FriendshipEffect extends SpecialStatusEffect
{
	public FriendshipEffect(StatusEffectType type, int color)
	{
		super(type, color);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier)
	{
		if(entity instanceof MobEntity)
		{
			((MobEntity)entity).setTarget(null);
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier)
	{
		return true;
	}
}
