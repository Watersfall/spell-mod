package net.watersfall.spellmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.client.WatersSpellModClient;
import net.watersfall.spellmod.spells.Spells;
import net.watersfall.spellmod.util.EntitySpawnPacket;

public class AnimalFriendshipEntity extends ThrownProjectileSpellEntity
{
	public AnimalFriendshipEntity(EntityType<? extends ThrownItemEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public AnimalFriendshipEntity(LivingEntity livingEntity, World world)
	{
		super(WatersSpellMod.ANIMAL_FRIENDSHIP_ENTITY, livingEntity, world);
	}

	@Override
	protected Item getDefaultItem()
	{
		return Spells.getSpell("waters_spell_mod:animal_friendship_spell").item;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		if(entityHitResult.getEntity() instanceof MobEntity)
		{
			MobEntity entity = (MobEntity) entityHitResult.getEntity();
			entity.setTarget(null);
			entity.addStatusEffect(new StatusEffectInstance(WatersSpellMod.FRIENDSHIP_EFFECT, 20 * 60 * 60 * 24));
		}
		super.onEntityHit(entityHitResult);
	}
}
