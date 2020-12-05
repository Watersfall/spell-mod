package net.watersfall.spellmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow public abstract boolean damage(DamageSource source, float amount);

	@Shadow public abstract boolean canMoveVoluntarily();

	@Shadow public abstract boolean removeStatusEffect(StatusEffect type);

	@Shadow protected float lastDamageTaken;

	public LivingEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	private static void playSound(LivingEntity entity)
	{

	}

	@Inject(method = "travel", at = @At("HEAD"))
	public void boomingBladeApply(Vec3d movement, CallbackInfo info)
	{
		if(this.canMoveVoluntarily() || this.isLogicalSideForUpdatingMovement())
		{
			if(this.hasStatusEffect(WatersSpellMod.BOOMING_BLADE))
			{
				this.lastDamageTaken = 0;
				this.damage(DamageSource.MAGIC, (int)(Math.random() * 8) + 1);
				this.removeStatusEffect(WatersSpellMod.BOOMING_BLADE);
				this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
			}
		}
	}
}
