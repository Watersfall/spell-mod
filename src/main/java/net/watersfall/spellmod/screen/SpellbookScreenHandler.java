package net.watersfall.spellmod.screen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
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
		int cantrips = ((SpellbookItem)this.inventory.getStack().getItem()).spellClass.knownCantrips[SpellbookItem.getLevel(this.inventory.getStack())];
		SpellItem activeItem = SpellbookItem.getActiveSpell(this.inventory.getStack());
		int m;
		int l;
		for (m = 0; m < 3; ++m)
		{
			for (l = 0; l < 9; ++l)
			{
				if(this.inventory.size() > l + m * 9)
				{
					SpellSlot slot = new SpellSlot(inventory, l + m * 9, 8 + l * 18, 18 + m * 18, SpellClass.WIZARD, cantrips > l + m * 9 ? 0 : 1);
					if(activeItem != null && activeItem == slot.getStack().getItem())
					{
						slot.selected = true;
					}
					this.addSlot(slot);
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
					this.inventory.getStack().getTag().putString(TagKeys.ACTIVE_SPELL, Registry.ITEM.getId(this.inventory.getStack(slotId).getItem()).toString());
					this.inventory.getStack().getTag().putInt(TagKeys.SPELL_LEVEL, ((SpellItem)this.inventory.getStack(slotId).getItem()).spell.minLevel);
					if(!((SpellSlot)this.slots.get(slotId)).selected)
					{
						for(int i = 0; i < this.inventory.size(); i++)
						{
							((SpellSlot)slots.get(i)).selected = false;
						}
						((SpellSlot)this.slots.get(slotId)).selected = true;
					}
				}
			}
		}
		return super.onSlotClick(slotId, clickData, actionType, playerEntity);
	}

	public static class SpellSlot extends Slot
	{
		public static final Identifier BLOCK_ATLAS_TEXTURE = new Identifier("textures/atlas/blocks.png");
		public static final Identifier C_TEXTURE = WatersSpellMod.getId("item/c");
		public static final Identifier S_TEXTURE = WatersSpellMod.getId("item/s");

		public boolean selected = false;
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
			if(stack.getItem() instanceof SpellItem)
			{
				SpellItem item = (SpellItem) stack.getItem();
				if(!((SpellbookInventory)inventory).contains(item))
				{
					if(item.level <= 0 && this.level <= 0)
					{
						return true;
					}
					else return item.level > 0 && this.level > 0;
				}
			}
			return false;
		}

		@Override
		public boolean canTakeItems(PlayerEntity playerEntity)
		{
			return false;
		}

		@Override
		public Pair<Identifier, Identifier> getBackgroundSprite()
		{
			return level == 0 ? Pair.of(BLOCK_ATLAS_TEXTURE, C_TEXTURE) : Pair.of(BLOCK_ATLAS_TEXTURE, S_TEXTURE);
		}
	}
}
