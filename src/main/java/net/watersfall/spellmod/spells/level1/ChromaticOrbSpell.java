package net.watersfall.spellmod.spells.level1;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.entity.ChromaticOrbEntity;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.Spell;

public class ChromaticOrbSpell extends Spell
{
	public static final byte ACID = 0;
	public static final byte COLD = 1;
	public static final byte LIGHTNING = 2;
	public static final byte POISON = 3;
	public static final byte THUNDER = 4;

	public ChromaticOrbSpell(String id)
	{
		super(id, 20, 1, 9);
	}

	@Override
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user)
	{
		if(!world.isClient)
		{
			ChromaticOrbEntity entity = new ChromaticOrbEntity(user, world);
			entity.setLevel(SpellbookItem.getSpellLevel(stack));
			entity.setOwner(user);
			entity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 0F);
			world.spawnEntity(entity);
		}
		return TypedActionResult.success(stack, world.isClient);
	}
}
