package net.watersfall.spellmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.client.WatersSpellModClient;
import net.watersfall.spellmod.util.EntitySpawnPacket;

public class ChillTouchEntity extends ThrownProjectileSpellEntity
{
	public ChillTouchEntity(EntityType<? extends ThrownProjectileSpellEntity> type, World world)
	{
		super(type, world);
	}

	public ChillTouchEntity(LivingEntity user, World world)
	{
		super(user, world);
	}

	@Override
	protected Item getDefaultItem()
	{
		return WatersSpellMod.SPELLBOOK;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		super.onEntityHit(entityHitResult);
		if(entityHitResult.getEntity() instanceof LivingEntity)
		{
			int damage = (int)(Math.random() * 8) + 1;
			LivingEntity entity = (LivingEntity) entityHitResult.getEntity();
			entity.damage(DamageSource.WITHER, damage);
			entity.addStatusEffect(new StatusEffectInstance(WatersSpellMod.CHILL_OF_THE_GRAVE, 6 * 20));
			if(entity.isUndead())
			{
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 12 * 20));
			}
			this.destroy();
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
