package net.watersfall.spellmod.spells.level2;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

public class AidSpell extends Spell
{

	public AidSpell(String id)
	{
		super(id, 20, 2, 9);
	}

	@Override
	public boolean canTargetSelect()
	{
		return true;
	}

	@Override
	public boolean isValidTarget(Entity entity)
	{
		return entity instanceof LivingEntity;
	}

	@Override
	public int getMaxTargets(int level)
	{
		return 3;
	}

	@Override
	public double getRange()
	{
		return 10D;
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			int level = SpellbookItem.getSpellLevel(stack) - 2;
			UUID[] uuids = SpellbookItem.getTargets(stack);
			for(int i = 0; i < uuids.length; i++)
			{
				MobEntity entity = (MobEntity)((ServerWorld)world).getEntity(uuids[i]);
				if(entity != null)
				{
					if(entity.hasStatusEffect(WatersSpellMod.AID_STATUS_EFFECT))
					{
						entity.removeStatusEffect(WatersSpellMod.AID_STATUS_EFFECT);
					}
					entity.addStatusEffect(new StatusEffectInstance(WatersSpellMod.AID_STATUS_EFFECT, 20 * 60 * 60 * 8, level));
				}
			}
			if(uuids.length < 3)
			{
				if(user.hasStatusEffect(WatersSpellMod.AID_STATUS_EFFECT))
				{
					user.removeStatusEffect(WatersSpellMod.AID_STATUS_EFFECT);
				}
				user.addStatusEffect(new StatusEffectInstance(WatersSpellMod.AID_STATUS_EFFECT, 20 * 60 * 60 * 8, level));
			}
			SpellbookItem.clearTargets(stack);
		}
		return TypedActionResult.success(stack, world.isClient);
	}
}
