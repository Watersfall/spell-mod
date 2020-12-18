package net.watersfall.spellmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.math.Dice;

import java.util.List;

public class CloudOfDaggersEntity extends SpellEffectEntity
{
	private int maxLife = 20 * 60;
	private int life;
	private int level;

	public CloudOfDaggersEntity(World world)
	{
		super(WatersSpellMod.CLOUD_OF_DAGGERS_ENTITY, world);
	}

	public CloudOfDaggersEntity(EntityType<? extends CloudOfDaggersEntity> type, World world)
	{
		super(type, world);
	}

	@Override
	protected void initDataTracker()
	{

	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag)
	{
		if(tag.contains("level"))
		{
			this.level = tag.getInt("level");
		}
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag)
	{
		tag.putInt("level", this.level);
	}

	public int getMaxLife()
	{
		return this.maxLife;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	@Override
	public void tick()
	{
		super.tick();
		if(!world.isClient)
		{
			life++;
			if(life % 20 == 0)
			{
				List<Entity> list = this.world.getOtherEntities(this, this.getBoundingBox());
				for(int i = 0; i < list.size(); i++)
				{
					if(list.get(i) instanceof LivingEntity)
					{
						int damage = Dice.roll(4 + ((level - 2) * 2), 4);
						list.get(i).damage(DamageSource.GENERIC, damage);
					}
				}
				if(this.life >= this.maxLife)
				{
					this.remove();
				}
			}
		}
	}
}
