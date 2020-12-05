package net.watersfall.spellmod.api;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;

public abstract class Spell
{
	public final String id;
	public final String translationKey;
	public final int castingTime;

	public Spell(String id, int castingTime)
	{
		this.id = id;
		this.castingTime = castingTime;
		translationKey = "spell." + id.split(":")[0] + "." + id.split(":")[1];
	}

	public SpellItem getItem()
	{
		return new SpellItem(new FabricItemSettings().group(WatersSpellMod.SPELL_MOD_GROUP).maxCount(1), this.castingTime);
	}

	public abstract TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user);
}
