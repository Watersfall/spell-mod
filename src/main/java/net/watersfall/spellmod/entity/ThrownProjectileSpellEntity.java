package net.watersfall.spellmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;

public abstract class ThrownProjectileSpellEntity extends ThrownItemEntity
{
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
}
