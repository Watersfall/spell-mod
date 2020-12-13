package net.watersfall.spellmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.client.WatersSpellModClient;
import net.watersfall.spellmod.util.Packets;

public abstract class ThrownProjectileSpellEntity extends ThrownItemEntity
{
	protected int level;

	public ThrownProjectileSpellEntity(EntityType<? extends ThrownItemEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public ThrownProjectileSpellEntity(EntityType<? extends ThrownItemEntity> entityType, double x, double y, double z, World world)
	{
		super(entityType, x, y, z, world);
	}

	public ThrownProjectileSpellEntity(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world)
	{
		super(entityType, livingEntity, world);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		this.remove();
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult)
	{
		this.remove();
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return Packets.create(this, WatersSpellModClient.PACKET_ID);
	}

	@Override
	public void tick()
	{
		super.tick();
		//Eventually max range stuff
	}

	public void setLevel(int level)
	{
		this.level = level;
	}
}
