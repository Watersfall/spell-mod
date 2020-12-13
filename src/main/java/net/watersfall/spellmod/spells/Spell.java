package net.watersfall.spellmod.spells;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.item.SpellItem;

public abstract class Spell
{
	public final SpellItem item;
	public final String id;
	public final String translationKey;
	public final int minLevel, maxLevel;
	public final int castingTime;

	//Same as Item#raycast, but with a range
	public static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling, double range) 
	{
		float f = player.pitch;
		float g = player.yaw;
		Vec3d vec3d = player.getCameraPosVec(1.0F);
		float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
		float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
		float j = -MathHelper.cos(-f * 0.017453292F);
		float k = MathHelper.sin(-f * 0.017453292F);
		float l = i * j;
		float n = h * j;
		Vec3d vec3d2 = vec3d.add((double)l * range, (double)k * range, (double)n * range);
		return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
	}

	public Spell(String id, int castingTime, int level, int maxLevel)
	{
		this.id = id;
		this.castingTime = castingTime;
		translationKey = "item." + id.split(":")[0] + "." + id.split(":")[1];
		item = new SpellItem(new FabricItemSettings().group(WatersSpellMod.SPELL_MOD_GROUP).maxCount(1), this.castingTime, level, this);
		this.minLevel = level;
		this.maxLevel = maxLevel;
	}

	public SpellItem getItem()
	{
		return item;
	}

	public boolean hasMultipleModes()
	{
		return false;
	}

	public abstract TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity user);
}
