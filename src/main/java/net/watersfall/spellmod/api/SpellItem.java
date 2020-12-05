package net.watersfall.spellmod.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpellItem extends Item
{
	public final int castingTime;

	public SpellItem(Settings settings, int castingTime)
	{
		super(settings);
		this.castingTime = castingTime;
	}
}
