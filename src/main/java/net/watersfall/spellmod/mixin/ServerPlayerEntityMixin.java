package net.watersfall.spellmod.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.accessor.ArmorOfAgathysAccessor;
import net.watersfall.spellmod.constants.TagKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ArmorOfAgathysAccessor
{
	private float armorOfAgathysAmount = 0;

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile)
	{
		super(world, pos, yaw, profile);
	}

	@Inject(method = "readCustomDataFromTag", at = @At("HEAD"))
	public void readData(CompoundTag tag, CallbackInfo info)
	{
		if(tag.contains(TagKeys.ARMOR_OF_AGATHYS_AMOUNT))
		{
			this.armorOfAgathysAmount = tag.getFloat(TagKeys.ARMOR_OF_AGATHYS_AMOUNT);
		}
	}

	@Inject(method = "writeCustomDataToTag", at = @At("HEAD"))
	public void writeData(CompoundTag tag, CallbackInfo info)
	{
		if(this.armorOfAgathysAmount > 0)
		{
			tag.putFloat(TagKeys.ARMOR_OF_AGATHYS_AMOUNT, armorOfAgathysAmount);
		}
	}

	@Override
	public float getArmorOfAgathysAmount()
	{
		return this.armorOfAgathysAmount;
	}

	@Override
	public void setArmorOfAgathysAmount(float amount)
	{
		if(amount <= 0F)
		{
			amount = 0F;
			this.removeStatusEffect(WatersSpellMod.ARMOR_OF_AGATHYS_EFFECT);
		}
		this.armorOfAgathysAmount = amount;
	}
}
