package net.watersfall.spellmod.spells.level1;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.spells.Spell;

public class ArmorOfAgathysSpell extends Spell
{
	public ArmorOfAgathysSpell(String id)
	{
		super(id, 20, 1);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		user.addStatusEffect(new StatusEffectInstance(WatersSpellMod.ARMOR_OF_AGATHYS_EFFECT, 60 * 20));
		return TypedActionResult.success(stack, world.isClient);
	}
}