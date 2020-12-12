package net.watersfall.spellmod.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.accessor.ArmorOfAgathysAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
	@Shadow public abstract float getAbsorptionAmount();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(method = "setAbsorptionAmount", at = @At("HEAD"))
	public void updateAbsorption(float amount, CallbackInfo info)
	{
		if(this instanceof ArmorOfAgathysAccessor)
		{
			if(this.getAbsorptionAmount() > amount)
			{
				ArmorOfAgathysAccessor accessor = (ArmorOfAgathysAccessor) this;
				accessor.setArmorOfAgathysAmount(accessor.getArmorOfAgathysAmount() - this.getAbsorptionAmount() + amount);
			}
		}
	}

	@Inject(method = "damage", at = @At(value = "RETURN", ordinal = 3, shift = At.Shift.BY, by = 3))
	public void hurtAttacker(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info)
	{
		if(source.getAttacker() != null)
		{
			if(source.getAttacker() instanceof LivingEntity)
			{
				if(this.hasStatusEffect(WatersSpellMod.ARMOR_OF_AGATHYS_EFFECT))
				{
					LivingEntity livingEntity = (LivingEntity)source.getAttacker();
					livingEntity.damage(DamageSource.MAGIC, 5);
				}
			}
		}
	}
}
