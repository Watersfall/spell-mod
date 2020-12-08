package net.watersfall.spellmod.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.constants.TagKeys;
import net.watersfall.spellmod.item.SpellbookItem;

import java.util.List;

public class PedestalBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable
{
	int ticks;
	private ItemStack stack = ItemStack.EMPTY;

	public PedestalBlockEntity()
	{
		super(WatersSpellMod.PEDESTAL_BLOCK_ENTITY);
	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
		markDirty();
	}

	public ItemStack getStack()
	{
		return this.stack;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag)
	{
		super.fromTag(state, tag);
		this.stack = ItemStack.fromTag(tag.getCompound("item"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		CompoundTag subTag = new CompoundTag();
		this.stack.toTag(subTag);
		tag.put("item", subTag);
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		this.stack = ItemStack.fromTag(compoundTag.getCompound("item"));
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		CompoundTag subTag = new CompoundTag();
		this.stack.toTag(subTag);
		compoundTag.put("item", subTag);
		return compoundTag;
	}

	@Override
	public void sync()
	{
		BlockEntityClientSerializable.super.sync();
	}

	@Override
	public void tick()
	{
		if(!this.world.isClient)
		{
			ticks++;
			if(ticks >= 100)
			{
				ticks = 0;
				if(!this.stack.isEmpty())
				{
					List<PlayerEntity> entities = this.world.getEntitiesByType(EntityType.PLAYER, new Box(this.pos).expand(5), (entity) -> entity.experienceLevel >= SpellbookItem.getLevel(this.stack) * 2);
					if(entities.size() > 0)
					{
						PlayerEntity victim = entities.get((int)(Math.random() * entities.size()));
						victim.experienceLevel -= SpellbookItem.getLevel(this.stack);
						this.stack.getOrCreateTag().putInt(TagKeys.LEVEL, this.stack.getTag().getInt(TagKeys.LEVEL) + 1);
						SpellbookItem.setSpellSlots(this.stack);
						this.sync();
						this.markDirty();
						((ServerPlayerEntity)victim).networkHandler.sendPacket(new ExperienceBarUpdateS2CPacket(victim.experienceProgress, victim.totalExperience, victim.experienceLevel));
					}
				}
			}
		}
	}
}
