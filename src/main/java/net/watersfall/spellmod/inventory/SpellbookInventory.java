package net.watersfall.spellmod.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;
import net.watersfall.spellmod.constants.TagKeys;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.SpellClass;

public class SpellbookInventory implements Inventory
{
	private final ItemStack stack;
	private final DefaultedList<ItemStack> contents;

	public SpellbookInventory(ItemStack stack)
	{
		this.stack = stack;
		SpellClass spellClass = ((SpellbookItem)stack.getItem()).spellClass;
		int count = spellClass.knownCantrips[stack.getTag().getInt(TagKeys.LEVEL) - 1] + spellClass.knownSpells[stack.getTag().getInt(TagKeys.LEVEL) - 1];
		contents = DefaultedList.ofSize(count, ItemStack.EMPTY);
		CompoundTag tag = stack.getSubTag(TagKeys.SPELL_LIST);
		if (tag != null) {
			Inventories.fromTag(tag, contents);
		}
	}

	@Override
	public int size()
	{
		return contents.size();
	}

	@Override
	public boolean isEmpty()
	{
		return contents.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStack(int slot)
	{
		return contents.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount)
	{
		if(amount >= contents.get(slot).getCount())
		{
			return contents.set(slot, ItemStack.EMPTY);
		}
		else
		{
			contents.get(slot).setCount(contents.get(slot).getCount() - amount);
			return new ItemStack(contents.get(slot).getItem(), amount);
		}
	}

	@Override
	public ItemStack removeStack(int slot)
	{
		return contents.set(slot, ItemStack.EMPTY);
	}

	@Override
	public void setStack(int slot, ItemStack stack)
	{
		this.contents.set(slot, stack);
	}

	@Override
	public void markDirty()
	{
		CompoundTag tag = stack.getOrCreateSubTag(TagKeys.SPELL_LIST);
		Inventories.toTag(tag, contents);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	public void clear()
	{

	}

	public boolean contains(Item item)
	{
		return this.contents.stream().anyMatch((stack) -> stack.getItem() == item);
	}

	public ItemStack getStack()
	{
		return this.stack;
	}
}
