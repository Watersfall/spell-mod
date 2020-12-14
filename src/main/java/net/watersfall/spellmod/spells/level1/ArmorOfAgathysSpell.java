package net.watersfall.spellmod.spells.level1;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.Spell;

public class ArmorOfAgathysSpell extends Spell
{
	public ArmorOfAgathysSpell(String id)
	{
		super(id, 20, 1, 9);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		int level = SpellbookItem.getSpellLevel(stack);
		user.addStatusEffect(new StatusEffectInstance(WatersSpellMod.ARMOR_OF_AGATHYS_EFFECT, 60 * 20, level));
		return TypedActionResult.success(stack, world.isClient);
	}
}
