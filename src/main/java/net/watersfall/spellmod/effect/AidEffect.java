package net.watersfall.spellmod.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectType;

import java.awt.*;

public class AidEffect extends SpecialStatusEffect
{
	public AidEffect()
	{
		super(StatusEffectType.BENEFICIAL, Color.RED.hashCode());
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		this.addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 5.0D, EntityAttributeModifier.Operation.ADDITION);
		super.onApplied(entity, attributes, amplifier);
		entity.heal(5 + 5 * amplifier);
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		super.onRemoved(entity, attributes, amplifier);
	}
}
