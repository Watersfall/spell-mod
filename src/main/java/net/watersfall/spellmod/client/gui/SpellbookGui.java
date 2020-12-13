package net.watersfall.spellmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonListWidget;
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
import net.watersfall.spellmod.constants.LangKeys;
import net.watersfall.spellmod.constants.TagKeys;
import net.watersfall.spellmod.item.SpellItem;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.screen.SpellbookScreenHandler;
import net.watersfall.spellmod.spells.Spell;
import net.watersfall.spellmod.util.Packets;

import java.awt.*;

public class SpellbookGui extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = WatersSpellMod.getId("textures/gui/container/spellbook.png");
	private static final int COLOR = new Color(0F, 0F, 1F, 0.25F).hashCode();
	private ButtonWidget decreaseLevelButton;
	private ButtonWidget increaseLevelButton;
	private int spellLevel = -1;
	private int variant = -1;

	public SpellbookGui(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
	}

	@Override
	protected void init()
	{
		this.backgroundWidth = 244;
		super.init();
		this.decreaseLevelButton = new ButtonWidget(this.x + 172 + ((244 - 176) / 2) - 18 - 9, this.y + 17, 18, 18, new LiteralText("<"), (button) -> {
			ItemStack stack = ((SpellbookScreenHandler)this.handler).inventory.getStack();
			Spell spell = SpellbookItem.getActiveSpell(stack).spell;
			int level = SpellbookItem.getSpellLevel(stack);
			if(level > spell.minLevel)
			{
				SpellbookItem.setSpellLevel(stack, level - 1);
				initButtons();
			}
		});
		this.increaseLevelButton = new ButtonWidget(this.x + 172 + ((244 - 176) / 2) + 18 - 9, this.y + 17, 18, 18, new LiteralText(">"), (button) -> {
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
		this.spellLevel = -1;
		ItemStack stack = ((SpellbookScreenHandler)this.handler).inventory.getStack();
		SpellItem spellItem = SpellbookItem.getActiveSpell(stack);
		if(spellItem != null)
		{
			Spell spell = spellItem.spell;
			this.spellLevel = SpellbookItem.getSpellLevel(stack);
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
				if(spell.hasMultipleModes())
				{
					variant = SpellbookItem.getSpellVariant(stack);
					for(int i : spell.getModes())
					{
						this.addButton(new SpellEffectButton(
								this.x + 172 + ((244 - 176) / 2) - 18 - 9,
								this.y + 17 + ((i + 2) * 18),
								18 + 18 + 18,
								18,
								stack,
								i,
								spell,
								(button -> {
									if(SpellbookItem.getSpellVariant(stack) != i)
									{
										for(AbstractButtonWidget widget : this.buttons)
										{
											if(widget instanceof SpellEffectButton)
											{
												((SpellEffectButton)widget).selected = false;
											}
										}
										SpellbookItem.setSpellVariant(stack, i);
									}
								}),
								variant == i
						));
					}
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
		textRenderer.draw(matrices, text, (float)(this.x + 172 + ((244 - 176) / 2) - textRenderer.getWidth(text) / 2), this.y + 6, 4210752);
		if(this.spellLevel != -1)
		{
			RenderSystem.pushMatrix();
			OrderedText text2 = new LiteralText("" + this.spellLevel).asOrderedText();
			textRenderer.draw(matrices, text2, (float)(this.x + 172 + ((244 - 176) / 2) - 2), this.y + 22, 4210752);
			RenderSystem.popMatrix();
		}
		if(this.variant != -1)
		{
			RenderSystem.pushMatrix();
			OrderedText text2 = new TranslatableText("text.waters_spell_mod.variant").asOrderedText();
			textRenderer.draw(matrices, text2, (float)(this.x + 172 + ((244 - 176) / 2) - (textRenderer.getWidth(text2) / 2)), this.y + 40, 4210752);
			RenderSystem.popMatrix();
		}
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
				WatersSpellMod.BUTTON_PACKET_ID
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

	public static class SpellEffectButton extends ButtonWidget
	{
		public ItemStack stack;
		public Spell spell;
		public final int variant;
		public boolean selected = false;

		public SpellEffectButton(int x, int y, int width, int height, ItemStack stack, int variant, Spell spell, ButtonWidget.PressAction onPress, boolean selected)
		{
			super(x, y, width, height, new TranslatableText(getLangKey(spell, variant)), onPress);
			this.variant = variant;
			this.stack = stack;
			this.spell = spell;
			this.selected = selected;
		}

		public static String getLangKey(Spell spell, int variant)
		{
			return LangKeys.getSpellVariant(spell, variant);
		}

		@Override
		public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
		{
			super.render(matrices, mouseX, mouseY, delta);
			if(selected)
			{
				RenderSystem.disableDepthTest();
				RenderSystem.colorMask(true, true, true, false);
				this.fillGradient(matrices, this.x, this.y, this.x + this.width, this.y + this.height, COLOR, COLOR);
				RenderSystem.colorMask(true, true, true, true);
				RenderSystem.enableDepthTest();
			}
		}

		@Override
		public void onClick(double mouseX, double mouseY)
		{
			super.onClick(mouseX, mouseY);
			this.selected = true;
		}

		@Override
		public boolean isHovered()
		{
			if(selected)
			{
				return false;
			}
			return super.isHovered();
		}
	}
}
