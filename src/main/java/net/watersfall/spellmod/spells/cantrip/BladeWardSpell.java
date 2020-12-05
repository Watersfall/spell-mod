package net.watersfall.spellmod.spells.cantrip;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.api.Spell;

public class BladeWardSpell extends Spell
{
	public BladeWardSpell(String id)
	{
		super(id, 20);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6 * 20));
		return TypedActionResult.success(stack, world.isClient);
	}
}
