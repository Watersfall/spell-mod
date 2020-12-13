package net.watersfall.spellmod.spells.level1;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.entity.AnimalFriendshipEntity;
import net.watersfall.spellmod.spells.Spell;

public class AnimalFriendshipSpell extends Spell
{
	public AnimalFriendshipSpell(String id)
	{
		super(id, 20, 1, 1);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			AnimalFriendshipEntity entity = new AnimalFriendshipEntity(user, world);
			entity.setOwner(user);
			entity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 0F);
			world.spawnEntity(entity);
		}
		return TypedActionResult.success(stack, world.isClient);
	}
}
