package net.watersfall.spellmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.spellmod.screen.SpellbookScreenHandler;

import java.awt.*;

public class SpellbookGui extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/shulker_box.png");
	private static final int COLOR = new Color(0F, 0F, 1F, 0.25F).hashCode();

	public SpellbookGui(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
	}

	@Override
	protected void init()
	{
		super.init();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
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
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		client.getTextureManager().bindTexture(TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
	}
}
