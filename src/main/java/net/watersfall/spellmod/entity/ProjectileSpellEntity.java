package net.watersfall.spellmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;

public abstract class ProjectileSpellEntity extends ProjectileEntity
{
	protected ProjectileSpellEntity(EntityType<? extends ProjectileEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public ProjectileSpellEntity(World world, LivingEntity owner, double x, double y, double z, float pitch, float yaw)
	{
		super(WatersSpellMod.ACID_SPLASH_TYPE, world);
		this.setOwner(owner);
		this.setPos(x, y, z);
		this.setVelocity(Vec3d.fromPolar(pitch, yaw));
	}
}
