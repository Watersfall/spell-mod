package net.watersfall.spellmod.client.mixin;

import jdk.internal.org.objectweb.asm.Opcodes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.item.SpellItem;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.Spell;
import net.watersfall.spellmod.util.Packets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{
	@Shadow public ClientPlayerEntity player;

	@Shadow public HitResult crosshairTarget;

	@Shadow public abstract Entity getCameraEntity();

	@Shadow public ClientWorld world;

	@Shadow public abstract float getTickDelta();

	@Inject(method = "doAttack", at = @At(value = "JUMP", opcode = Opcodes.IFEQ, ordinal = 1), cancellable = true)
	public void waters_DoAttack(CallbackInfo info)
	{
		if(this.player.getMainHandStack().getItem().isIn(WatersSpellMod.SPELLBOOK_TAG))
		{
			SpellItem item = SpellbookItem.getActiveSpell(this.player.getMainHandStack());
			if(item != null)
			{
				Spell spell = item.spell;
				if(spell.canTargetSelect)
				{
					if(this.player.isSneaking())
					{
						SpellbookItem.clearTargets(player.getMainHandStack());
						player.swingHand(Hand.MAIN_HAND);
					}
					else
					{
						Entity entity = null;
						if(this.crosshairTarget.getType() == HitResult.Type.ENTITY)
						{
							entity = ((EntityHitResult)this.crosshairTarget).getEntity();
						}
						else if(this.crosshairTarget.getType() == HitResult.Type.MISS)
						{
							entity = getTargetedEntity(spell.range, this.getTickDelta());
						}
						if(entity != null && spell.isValidTarget.applyAsBoolean(entity))
						{
							SpellbookItem.target(player.getMainHandStack(), entity);
							MinecraftClient.getInstance().getNetworkHandler().sendPacket(Packets.create(player.getMainHandStack(), Hand.MAIN_HAND, WatersSpellMod.TARGET_PACKET_ID));
							player.swingHand(Hand.MAIN_HAND);
							info.cancel();
						}
					}
				}
			}
		}
	}

	public Entity getTargetedEntity(double range, float tickDelta)
	{
		Entity entity = this.getCameraEntity();
		if (entity != null) 
		{
			if (this.world != null) 
			{
				Vec3d vec3d = entity.getCameraPosVec(tickDelta);
				double e = range;
				e *= e;
				if (this.crosshairTarget.getType() == HitResult.Type.BLOCK)
				{
					e = this.crosshairTarget.getPos().squaredDistanceTo(vec3d);
				}
				Vec3d vec3d2 = entity.getRotationVec(1.0F);
				Vec3d vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
				Box box = entity.getBoundingBox().stretch(vec3d2.multiply(range)).expand(1.0D, 1.0D, 1.0D);
				EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, vec3d, vec3d3, box, (entity1) -> !entity1.isSpectator() && entity1.collides(), e);
				if (entityHitResult != null)
				{
					return entityHitResult.getEntity();
				}
			}
		}
		return null;
	}
}
