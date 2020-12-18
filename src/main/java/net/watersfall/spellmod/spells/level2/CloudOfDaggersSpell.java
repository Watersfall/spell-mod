package net.watersfall.spellmod.spells.level2;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.watersfall.spellmod.entity.CloudOfDaggersEntity;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.Spell;

public class CloudOfDaggersSpell extends Spell
{
	public CloudOfDaggersSpell(String id)
	{
		super(id, 20, 2, 9);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			BlockHitResult result = raycast(world, user, RaycastContext.FluidHandling.ANY, 20);
			if(result.getBlockPos().isWithinDistance(user.getBlockPos(), 20D))
			{
				int level = SpellbookItem.getSpellLevel(stack);
				CloudOfDaggersEntity entity = new CloudOfDaggersEntity(world);
				entity.setLevel(level);
				BlockPos pos = result.getBlockPos().offset(result.getSide());
				double x = pos.getX();
				double y = pos.getY();
				double z = pos.getZ();
				entity.setPos(x, y, z);
				entity.updatePosition(x, y, z);
				world.spawnEntity(entity);
			}
		}
		return TypedActionResult.success(stack, world.isClient);
	}
}
