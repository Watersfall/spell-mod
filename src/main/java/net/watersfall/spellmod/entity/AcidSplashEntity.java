package net.watersfall.spellmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.spells.Spells;

public class AcidSplashEntity extends ThrownProjectileSpellEntity
{
	public AcidSplashEntity(EntityType<? extends ThrownProjectileSpellEntity> type, World world)
	{
		super(type, world);
	}

	public AcidSplashEntity(LivingEntity user, World world)
	{
		super(WatersSpellMod.ACID_SPLASH_TYPE, user, world);
	}

	@Override
	protected Item getDefaultItem()
	{
		return Spells.getSpell("waters_spell_mod:acid_splash_spell").item;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		if(entityHitResult.getEntity() instanceof LivingEntity)
		{
			int damage = (int)(Math.random() * 6) + 1;
			LivingEntity entity = (LivingEntity) entityHitResult.getEntity();
			entity.damage(DamageSource.MAGIC, damage);
			this.destroy();
		}
		super.onEntityHit(entityHitResult);
	}
}
