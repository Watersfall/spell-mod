package net.watersfall.spellmod.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.item.SpellItem;
import net.watersfall.spellmod.item.SpellbookItem;
import net.watersfall.spellmod.spells.Spell;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity>
{
	@Shadow public abstract TextRenderer getFontRenderer();

	@Shadow @Final protected EntityRenderDispatcher dispatcher;

	@Inject(method = "render", at = @At("HEAD"))
	public void waters_render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info)
	{
		if(MinecraftClient.getInstance().player != null)
		{
			ItemStack stack = MinecraftClient.getInstance().player.getMainHandStack();
			if(stack.getItem().isIn(WatersSpellMod.SPELLBOOK_TAG))
			{
				SpellItem item = SpellbookItem.getActiveSpell(stack);
				if(item != null)
				{
					Spell spell = item.spell;
					if(spell.canTargetSelect())
					{
						if(SpellbookItem.isEntityTargeted(stack, entity))
						{
							boolean bl = !entity.isSneaky();
							float f = entity.getHeight() + 0.5F;
							int i = 0;
							matrices.push();
							matrices.translate(0.0D, (double)f, 0.0D);
							matrices.multiply(this.dispatcher.getRotation());
							matrices.scale(-0.025F, -0.025F, 0.025F);
							Matrix4f matrix4f = matrices.peek().getModel();
							float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
							int j = (int)(g * 255.0F) << 24;
							TextRenderer textRenderer = this.getFontRenderer();
							float h = (float)(-textRenderer.getWidth("test") / 2);
							textRenderer.draw("test", h, (float)i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
							if (bl) {
								textRenderer.draw("test", h, (float)i, -1, false, matrix4f, vertexConsumers, false, 0, light);
							}

							matrices.pop();
						}
					}
				}
			}
		}
	}
}
