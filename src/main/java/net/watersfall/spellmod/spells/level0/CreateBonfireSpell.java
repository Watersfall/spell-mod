package net.watersfall.spellmod.spells.level0;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.spells.Spell;

public class CreateBonfireSpell extends Spell
{
	public CreateBonfireSpell(String id)
	{
		super(id, 20, 0, 0);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			BlockHitResult result = raycast(world, user, RaycastContext.FluidHandling.ANY, 20);
			if(result.getBlockPos().isWithinDistance(user.getBlockPos(), 20D))
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
}
