package net.watersfall.spellmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.screen.SpellbookScreenHandler;
import net.watersfall.spellmod.spells.Spell;
import net.watersfall.spellmod.util.Packets;

import java.awt.*;

public class SpellbookGui extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/shulker_box.png");
	private static final int COLOR = new Color(0F, 0F, 1F, 0.25F).hashCode();
	private ButtonWidget decreaseLevelButton;
	private ButtonWidget increaseLevelButton;

	public SpellbookGui(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
	}

	@Override
	protected void init()
	{
		super.init();
		this.decreaseLevelButton = new ButtonWidget(this.x + this.backgroundWidth, this.y + 17, 18, 18, new LiteralText("<"), (button) -> {
			ItemStack stack = ((SpellbookScreenHandler)this.handler).inventory.getStack();
			Spell spell = SpellbookItem.getActiveSpell(stack).spell;
			int level = SpellbookItem.getSpellLevel(stack);
			if(level > spell.minLevel)
			{
				SpellbookItem.setSpellLevel(stack, level - 1);
				initButtons();
			}
		});
		this.increaseLevelButton = new ButtonWidget(this.x + this.backgroundWidth + 32, this.y + 17, 18, 18, new LiteralText(">"), (button) -> {
			ItemStack stack = ((SpellbookScreenHandler)this.handler).inventory.getStack();
			Spell spell = SpellbookItem.getActiveSpell(stack).spell;
			int level = SpellbookItem.getSpellLevel(stack);
			if(level < spell.maxLevel)
			{
				SpellbookItem.setSpellLevel(stack, level + 1);
				initButtons();
			}
		});
		initButtons();
	}

	private void initButtons()
	{
		ItemStack stack = ((SpellbookScreenHandler)this.handler).inventory.getStack();
		Spell spell = SpellbookItem.getActiveSpell(stack).spell;
		this.children.clear();
		this.buttons.clear();
		if(spell != null)
		{
			if(spell.maxLevel > spell.minLevel)
			{
				int level = SpellbookItem.getSpellLevel(stack);
				if(level > spell.minLevel)
				{
					this.addButton(decreaseLevelButton);
				}
				if(level < spell.maxLevel)
				{
					this.addButton(increaseLevelButton);
				}
				if(level == spell.minLevel)
				{
					this.buttons.remove(decreaseLevelButton);
					this.children.remove(decreaseLevelButton);
				}
				if(level == spell.maxLevel)
				{
					this.buttons.remove(increaseLevelButton);
					this.children.remove(increaseLevelButton);
				}
			}
		}
	}

	@Override
	protected void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType)
	{
		super.onMouseClick(slot, invSlot, clickData, actionType);
		initButtons();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		OrderedText text = new TranslatableText("text.waters_spell_mod.casting_level").asOrderedText();
		textRenderer.draw(matrices, text, (float)((this.x + this.backgroundWidth + 18 + 8) - textRenderer.getWidth(text) / 2), this.y + 6, 4210752);
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float)this.x, (float)this.y, 0.0F);
		for(Slot slot : this.handler.slots)
		{
			if(slot instanceof SpellbookScreenHandler.SpellSlot)
			{
				SpellbookScreenHandler.SpellSlot spellSlot = (SpellbookScreenHandler.SpellSlot) slot;
				if(spellSlot.selected)
				{
					RenderSystem.disableDepthTest();
					RenderSystem.colorMask(true, true, true, false);
					this.fillGradient(matrices, slot.x, slot.y, slot.x + 16, slot.y + 16, COLOR, COLOR);
					RenderSystem.colorMask(true, true, true, true);
					RenderSystem.enableDepthTest();
					break;
				}
			}
		}
		RenderSystem.popMatrix();
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	public void onClose()
	{
		super.onClose();
		MinecraftClient.getInstance().world.sendPacket(Packets.create(
				((SpellbookScreenHandler)this.handler).inventory.getStack(),
				Hand.MAIN_HAND,
				WatersSpellMod.PACKET_ID
		));
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		client.getTextureManager().bindTexture(TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
	}
}
