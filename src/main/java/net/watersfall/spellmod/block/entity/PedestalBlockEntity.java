package net.watersfall.spellmod.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.watersfall.spellmod.WatersSpellMod;

public class PedestalBlockEntity extends BlockEntity implements BlockEntityClientSerializable
{
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
}
