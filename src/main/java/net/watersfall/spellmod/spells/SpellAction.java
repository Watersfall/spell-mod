package net.watersfall.spellmod.spells;

import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.entity.AcidSplashEntity;
import net.watersfall.spellmod.entity.ChillTouchEntity;
import net.watersfall.spellmod.entity.ChromaticOrbEntity;
import net.watersfall.spellmod.entity.CloudOfDaggersEntity;
import net.watersfall.spellmod.item.SpellbookItem;

import java.util.UUID;

@FunctionalInterface
public interface SpellAction
{
	TypedActionResult<ItemStack> use(Spell spell, ItemStack stack, World world, PlayerEntity user);

	public static TypedActionResult<ItemStack> BLADE_WARD(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6 * 20));
		return TypedActionResult.success(stack, world.isClient);
	}

	public static TypedActionResult<ItemStack> ACID_SPLASH(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			AcidSplashEntity entity = new AcidSplashEntity(user, world);
			entity.setOwner(user);
			entity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 0F);
			world.spawnEntity(entity);
		}
		return TypedActionResult.success(stack, world.isClient);
	}

	public static TypedActionResult<ItemStack> BOOMING_BLADE(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		user.addStatusEffect(new StatusEffectInstance(WatersSpellMod.BOOMING_BLADE_GIVE, 6 * 20));
		return TypedActionResult.success(stack, world.isClient);
	}

	public static TypedActionResult<ItemStack> CHILL_TOUCH(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			ChillTouchEntity entity = new ChillTouchEntity(user, world);
			entity.setOwner(user);
			entity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 0F);
			world.spawnEntity(entity);
		}
		return TypedActionResult.success(stack, world.isClient);
	}

	public static TypedActionResult<ItemStack> CREATE_BONFIRE(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			BlockHitResult result = Spell.raycast(world, user, RaycastContext.FluidHandling.ANY, spell.range);
			if(result.getBlockPos().isWithinDistance(user.getBlockPos(), spell.range))
			{
				BlockState state = world.getBlockState(result.getBlockPos().up());
				if(state.isAir())
				{
					world.setBlockState(result.getBlockPos().up(), WatersSpellMod.BONFIRE_BLOCK.getDefaultState());
					world.getBlockTickScheduler().schedule(result.getBlockPos().up(), WatersSpellMod.BONFIRE_BLOCK, 60 * 20);
				}
			}
		}
		return TypedActionResult.success(stack, world.isClient);
	}

	public static TypedActionResult<ItemStack> ANIMAL_FRIENDSHIP(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			UUID[] uuids = SpellbookItem.getTargets(stack);
			for(int i = 0; i < uuids.length; i++)
			{
				MobEntity entity = (MobEntity)((ServerWorld)world).getEntity(uuids[i]);
				if(entity != null)
				{
					entity.setTarget(null);
					entity.addStatusEffect(new StatusEffectInstance(WatersSpellMod.FRIENDSHIP_EFFECT, 20 * 60 * 60 * 24));
				}
			}
			SpellbookItem.clearTargets(stack);
		}
		return TypedActionResult.success(stack, world.isClient);
	}

	public static TypedActionResult<ItemStack> ARMOR_OF_AGATHYS(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		int level = SpellbookItem.getSpellLevel(stack);
		user.addStatusEffect(new StatusEffectInstance(WatersSpellMod.ARMOR_OF_AGATHYS_EFFECT, 60 * 20, level));
		return TypedActionResult.success(stack, world.isClient);
	}

	public static TypedActionResult<ItemStack> CHROMATIC_ORB(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			ChromaticOrbEntity entity = new ChromaticOrbEntity(user, world);
			entity.setLevel(SpellbookItem.getSpellLevel(stack));
			entity.setVariant(SpellbookItem.getSpellVariant(stack));
			entity.setOwner(user);
			entity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 0F);
			world.spawnEntity(entity);
		}
		return TypedActionResult.success(stack, world.isClient);
	}

	public static TypedActionResult<ItemStack> AID(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			int level = SpellbookItem.getSpellLevel(stack) - 2;
			UUID[] uuids = SpellbookItem.getTargets(stack);
			for(int i = 0; i < uuids.length; i++)
			{
				MobEntity entity = (MobEntity)((ServerWorld)world).getEntity(uuids[i]);
				if(entity != null)
				{
					if(entity.hasStatusEffect(WatersSpellMod.AID_STATUS_EFFECT))
					{
						entity.removeStatusEffect(WatersSpellMod.AID_STATUS_EFFECT);
					}
					entity.addStatusEffect(new StatusEffectInstance(WatersSpellMod.AID_STATUS_EFFECT, 20 * 60 * 60 * 8, level));
				}
			}
			if(uuids.length < 3)
			{
				if(user.hasStatusEffect(WatersSpellMod.AID_STATUS_EFFECT))
				{
					user.removeStatusEffect(WatersSpellMod.AID_STATUS_EFFECT);
				}
				user.addStatusEffect(new StatusEffectInstance(WatersSpellMod.AID_STATUS_EFFECT, 20 * 60 * 60 * 8, level));
			}
			SpellbookItem.clearTargets(stack);
		}
		return TypedActionResult.success(stack, world.isClient);
	}

	public static TypedActionResult<ItemStack> CLOUD_OF_DAGGERS(Spell spell, ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			BlockHitResult result = Spell.raycast(world, user, RaycastContext.FluidHandling.ANY, spell.range);
			if(result.getBlockPos().isWithinDistance(user.getBlockPos(), spell.range))
			{
				int level = SpellbookItem.getSpellLevel(stack);
				CloudOfDaggersEntity entity = new CloudOfDaggersEntity(world);
				entity.setLevel(level);
				BlockPos pos = result.getBlockPos().offset(result.getSide());
				double offsetX = 5D / 12D;
				double offsetZ = 5D / 12D;
				double offsetY = result.getSide() == Direction.DOWN ? -1D : 0D;
				double x = pos.getX() + offsetX;
				double y = pos.getY() + offsetY;
				double z = pos.getZ() + offsetZ;
				entity.setPos(x, y, z);
				entity.updatePosition(x, y, z);
				world.spawnEntity(entity);
			}
		}
		return TypedActionResult.success(stack, world.isClient);
	}
}
