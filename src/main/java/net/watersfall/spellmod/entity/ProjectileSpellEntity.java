package net.watersfall.spellmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.util.Packets;

public abstract class ProjectileSpellEntity extends ProjectileEntity
{
	protected ProjectileSpellEntity(EntityType<? extends ProjectileEntity> type, World world)
	{
		super(type, world);
	}

	protected ProjectileSpellEntity(EntityType<? extends ProjectileEntity> entityType, LivingEntity entity, World world)
	{
		super(entityType, world);
		this.setOwner(entity);
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return Packets.create(this, WatersSpellMod.SPAWN_PACKET_ID);
	}
}
