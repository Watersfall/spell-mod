package net.watersfall.spellmod.spells.level0;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.spells.Spell;

public class BoomingBladeSpell extends Spell
{
	public BoomingBladeSpell(String id)
	{
		super(id, 20, 0);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		user.addStatusEffect(new StatusEffectInstance(WatersSpellMod.BOOMING_BLADE_GIVE, 6 * 20));
		return TypedActionResult.success(stack, world.isClient);
	}
}
