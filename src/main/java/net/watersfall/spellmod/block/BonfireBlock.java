package net.watersfall.spellmod.block;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BonfireBlock extends AbstractFireBlock
{
	public BonfireBlock(Settings settings)
	{
		super(settings, 0F);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		return this.getDefaultState();
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
	{
		if(!entity.isFireImmune())
		{
			entity.damage(DamageSource.IN_FIRE, 1.0F);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		world.removeBlock(pos, false);
	}

	@Override
	protected boolean isFlammable(BlockState state)
	{
		return false;
	}
}
