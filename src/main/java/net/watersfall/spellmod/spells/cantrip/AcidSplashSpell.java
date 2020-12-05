package net.watersfall.spellmod.spells.cantrip;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.api.Spell;
import net.watersfall.spellmod.entity.AcidSplashEntity;

public class AcidSplashSpell extends Spell
{
	public AcidSplashSpell(String id)
	{
		super(id, 20);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			AcidSplashEntity entity = new AcidSplashEntity(user, world);
			entity.setItem(stack);
			entity.setOwner(user);
			entity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 0F);
			world.spawnEntity(entity);
		}
		return TypedActionResult.success(stack, world.isClient);
	}
}
