package net.watersfall.spellmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.util.Packets;

import java.util.UUID;

public class MagicMissileEntity extends ProjectileSpellEntity
{
	public UUID target;
	public int targetEntityId;
	public int damage = 0;

	public MagicMissileEntity(EntityType<? extends ProjectileEntity> type, World world)
	{
		super(type, world);
		target = null;
		this.targetEntityId = 0;
	}

	public MagicMissileEntity(World world, LivingEntity owner, LivingEntity target)
	{
		super(WatersSpellMod.MAGIC_MISSILE_ENTITY, owner, world);
		this.target = target.getUuid();
		this.targetEntityId = target.getEntityId();
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		if(target != null)
		{
			tag.putUuid("target", this.target);
			tag.putInt("target_id", this.targetEntityId);
		}
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		if(tag.contains("target"))
		{
			this.target = tag.getUuid("target");
			this.targetEntityId = tag.getInt("target_id");
		}
	}

	public void setTarget(UUID uuid, int id)
	{
		this.target = uuid;
		this.targetEntityId = id;
	}

	public LivingEntity getTarget()
	{
		if(this.world instanceof ServerWorld)
		{
			return (LivingEntity)((ServerWorld)world).getEntity(this.target);
		}
		else
		{
			return (LivingEntity) world.getEntityById(this.targetEntityId);
		}
	}

	@Override
	public void tick()
	{
		Entity entity = this.getOwner();
		LivingEntity target = this.getTarget();
		if(this.world.isClient || (entity == null || !entity.removed) && this.world.isChunkLoaded(this.getBlockPos()))
		{
			if(target != null && !target.removed)
			{
				super.tick();
				HitResult hitResult = ProjectileUtil.getCollision(this, this::method_26958);
				if (hitResult.getType() == HitResult.Type.ENTITY)
				{
					this.onCollision(hitResult);
				}
				Vec3d pos = this.getPos();
				Vec3d targetPos = target.getPos();
				targetPos = targetPos.add(0, target.getEyeY() - targetPos.y, 0);
				Vec3d newPos = targetPos.subtract(pos).normalize();
				this.updateVelocity(0.25F, newPos);
 				this.updatePosition(this.getX() + this.getVelocity().getX(), this.getY() + this.getVelocity().getY(), this.getZ() + this.getVelocity().getZ());
			}
			else
			{
				this.remove();
			}
		}
		else
		{
			this.remove();
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		if(entityHitResult.getEntity().getUuid().equals(this.target))
		{
			if(!world.isClient)
			{
				float temp = this.getTarget().lastDamageTaken;
				this.getTarget().lastDamageTaken = 0;
				this.getTarget().damage(DamageSource.MAGIC, damage);
				this.getTarget().lastDamageTaken = temp;
				this.remove();
			}
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult)
	{

	}

	@Override
	protected void initDataTracker()
	{

	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return Packets.create(this, WatersSpellMod.MAGIC_MISSILE_PACKET_ID);
	}
}
