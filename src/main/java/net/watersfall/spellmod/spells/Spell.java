package net.watersfall.spellmod.spells;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.item.SpellItem;
import net.watersfall.spellmod.util.function.ToBooleanFunction;

import java.util.function.ToIntFunction;

public class Spell
{
	public final SpellItem item;
	public final boolean hasMultipleModes;
	public final boolean canTargetSelect;
	public final double range;
	public final int minLevel;
	public final int maxLevel;
	public final int castingTime;
	public final ToIntFunction<ItemStack> maxTargets;
	public final ToBooleanFunction<Entity> isValidTarget;
	public final int[] modes;
	public final SpellClass[] classes;
	public final SpellAction action;

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

	public Spell(SpellProperties properties, SpellAction action)
	{
		this.hasMultipleModes = properties.hasMultipleModes();
		this.canTargetSelect = properties.canTargetSelect();
		this.range = properties.getRange();
		this.minLevel = properties.getMinLevel();
		this.maxLevel = properties.getMaxLevel();
		this.castingTime = properties.getCastingTime();
		this.maxTargets = properties.getMaxTargets();
		this.modes = properties.getModes();
		this.isValidTarget = properties.getIsValidTarget();
		this.classes = properties.getClasses();
		this.action = action;
		this.item = new SpellItem(new FabricItemSettings().group(WatersSpellMod.SPELL_MOD_GROUP).maxCount(1), this.castingTime, minLevel, this);
	}

	public String getTranslationKey()
	{
		return this.item.getTranslationKey();
	}

	public boolean isInClass(SpellClass spellClass)
	{
		for(int i = 0; i < classes.length; i++)
		{
			if(classes[i] == spellClass)
			{
				return true;
			}
		}
		return false;
	}
}
