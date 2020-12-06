package net.watersfall.spellmod.spells.level0;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.spells.Spell;
import net.watersfall.spellmod.entity.ChillTouchEntity;

public class ChillTouchSpell extends Spell
{

	public ChillTouchSpell(String id)
	{
		super(id, 20);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			ChillTouchEntity entity = new ChillTouchEntity(user, world);
			entity.setOwner(user);
			entity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 0F);
			world.spawnEntity(entity);
		}
		return TypedActionResult.success(stack, world.isClient);
	}
}
