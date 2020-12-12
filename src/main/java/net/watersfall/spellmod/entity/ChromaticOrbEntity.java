package net.watersfall.spellmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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
import net.watersfall.spellmod.math.Dice;
import net.watersfall.spellmod.spells.Spells;
import net.watersfall.spellmod.util.EntitySpawnPacket;

public class ChromaticOrbEntity extends ThrownProjectileSpellEntity
{

	public ChromaticOrbEntity(EntityType<? extends ThrownItemEntity> entityType, World world)
	{
		super(entityType, world);
	}


	public ChromaticOrbEntity(LivingEntity livingEntity, World world)
	{
		super(WatersSpellMod.CHROMATIC_ORB_ENTITY, livingEntity, world);
	}

	@Override
	protected Item getDefaultItem()
	{
		return Spells.getSpell("waters_spell_mod:chromatic_orb_spell").item;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		if(!entityHitResult.getEntity().getEntityWorld().isClient)
		{
			super.onEntityHit(entityHitResult);
			if(entityHitResult.getEntity() instanceof LivingEntity)
			{
				LivingEntity entity = (LivingEntity) entityHitResult.getEntity();
				entity.damage(DamageSource.MAGIC, Dice.roll(3, 8));
			}
			this.remove();
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult)
	{
		this.destroy();
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return EntitySpawnPacket.create(this, WatersSpellModClient.PACKET_ID);
	}
}
