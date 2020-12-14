package net.watersfall.spellmod.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectType;
import net.watersfall.spellmod.accessor.ArmorOfAgathysAccessor;

import java.awt.*;

public class ArmorOfAgathysEffect extends SpecialStatusEffect
{
	public ArmorOfAgathysEffect()
	{
		super(StatusEffectType.BENEFICIAL, Color.GREEN.hashCode());
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		super.onRemoved(entity, attributes, amplifier);
		entity.setAbsorptionAmount(entity.getAbsorptionAmount() - 5 * amplifier);
		if(entity instanceof ArmorOfAgathysAccessor)
		{
			ArmorOfAgathysAccessor accessor = (ArmorOfAgathysAccessor) entity;
			accessor.setArmorOfAgathysAmount(0);
		}
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		super.onApplied(entity, attributes, amplifier);
		entity.setAbsorptionAmount(entity.getAbsorptionAmount() + 5 * amplifier);
		if(entity instanceof ArmorOfAgathysAccessor)
		{
			ArmorOfAgathysAccessor accessor = (ArmorOfAgathysAccessor) entity;
			accessor.setArmorOfAgathysAmount(5 * amplifier);
		}
	}
}
