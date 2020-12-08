package net.watersfall.spellmod.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.registry.Registry;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.constants.TagKeys;
import net.watersfall.spellmod.inventory.SpellbookInventory;
import net.watersfall.spellmod.item.SpellItem;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.SpellClass;

public class SpellbookScreenHandler extends ScreenHandler
{
	public final SpellbookInventory inventory;

	public SpellbookScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf)
	{
		this(syncId, playerInventory, new SpellbookInventory(buf.readItemStack()));
	}

	public SpellbookScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory)
	{
		super(WatersSpellMod.SPELLBOOK_SCREEN_HANDLER, syncId);
		this.inventory = (SpellbookInventory) inventory;

		int m;
		int l;
		for (m = 0; m < 3; ++m)
		{
			for (l = 0; l < 9; ++l)
			{
				if(this.inventory.size() > l + m * 9)
				{
					this.addSlot(new SpellSlot(inventory, l + m * 9, 8 + l * 18, 18 + m * 18, SpellClass.WIZARD, 0));
				}
			}
		}

		//Player Inventory
		for (m = 0; m < 3; ++m)
		{
			for (l = 0; l < 9; ++l)
			{
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}
		for (m = 0; m < 9; ++m)
		{
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	public ItemStack onSlotClick(int slotId, int clickData, SlotActionType actionType, PlayerEntity playerEntity)
	{
		if(slotId >= 0)
		{
			ItemStack stack = getSlot(slotId).getStack();
			if(stack.getItem() instanceof SpellbookItem)
			{
				return stack;
			}
			else if(slotId < this.inventory.size())
			{
				if(this.inventory.getStack(slotId) != ItemStack.EMPTY)
				{
					((SpellbookInventory)this.inventory).getStack().getTag().putString(TagKeys.ACTIVE_SPELL, Registry.ITEM.getId(this.inventory.getStack(slotId).getItem()).toString());
				}
			}
		}
		return super.onSlotClick(slotId, clickData, actionType, playerEntity);
	}

	static class SpellSlot extends Slot
	{
		public final SpellClass spellClass;
		public final int level;

		public SpellSlot(Inventory inventory, int index, int x, int y, SpellClass spellClass, int level)
		{
			super(inventory, index, x, y);
			this.spellClass = spellClass;
			this.level = level;
		}

		@Override
		public boolean canInsert(ItemStack stack)
		{
			return stack.getItem() instanceof SpellItem;
		}

		@Override
		public boolean canTakeItems(PlayerEntity playerEntity)
		{
			return false;
		}
	}
}