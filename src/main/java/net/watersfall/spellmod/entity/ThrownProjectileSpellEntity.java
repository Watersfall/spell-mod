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

	public ThrownProjectileSpellEntity(double x, double y, double z, World world)
	{
		super(WatersSpellMod.ACID_SPLASH_TYPE, x, y, z, world);
	}

	public ThrownProjectileSpellEntity(LivingEntity livingEntity, World world)
	{
		super(WatersSpellMod.ACID_SPLASH_TYPE, livingEntity, world);
	}
}
