package net.watersfall.spellmod.spells.level1;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.Spell;

import java.util.UUID;

public class AnimalFriendshipSpell extends Spell
{
	public AnimalFriendshipSpell(String id)
	{
		super(id, 20, 1, 1);
	}

	@Override
	public boolean canTargetSelect()
	{
		return true;
	}

	@Override
	public int getMaxTargets(int level)
	{
		return level;
	}

	@Override
	public boolean isValidTarget(Entity entity)
	{
		return entity instanceof MobEntity;
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			UUID[] uuids = SpellbookItem.getTargets(stack);
			for(int i = 0; i < uuids.length; i++)
			{
				MobEntity entity = (MobEntity)((ServerWorld)world).getEntity(uuids[i]);
				if(entity != null)
				{
					entity.setTarget(null);
					entity.addStatusEffect(new StatusEffectInstance(WatersSpellMod.FRIENDSHIP_EFFECT, 20 * 60 * 60 * 24));
				}
			}
			SpellbookItem.clearTargets(stack);
		}
		return TypedActionResult.success(stack, world.isClient);
	}
}
