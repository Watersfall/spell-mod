package net.watersfall.spellmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.watersfall.spellmod.block.entity.PedestalBlockEntity;
import net.watersfall.spellmod.item.SpellbookItem;

public class PedestalBlock extends Block implements BlockEntityProvider
{
	public PedestalBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return new PedestalBlockEntity();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ItemStack stack = player.getStackInHand(hand);
		Item item = stack.getItem();
		if(item instanceof SpellbookItem)
		{
			if(!world.isClient)
			{
				PedestalBlockEntity entity = (PedestalBlockEntity) world.getBlockEntity(pos);
				entity.setStack(player.getStackInHand(hand).copy());
				player.setStackInHand(hand, ItemStack.EMPTY);
				entity.sync();
			}
			return ActionResult.success(world.isClient);
		}
		else if(stack == ItemStack.EMPTY)
		{
			if(!world.isClient)
			{
				PedestalBlockEntity entity = (PedestalBlockEntity) world.getBlockEntity(pos);
				if(entity.getStack() != ItemStack.EMPTY)
				{
					player.setStackInHand(hand, entity.getStack().copy());
					entity.setStack(ItemStack.EMPTY);
					entity.sync();
				}
			}
			return ActionResult.success(world.isClient);
		}
		return ActionResult.PASS;
	}
}
