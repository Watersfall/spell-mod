package net.watersfall.spellmod.client.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.watersfall.spellmod.block.entity.PedestalBlockEntity;

public class PedestalBlockEntityRenderer extends BlockEntityRenderer<PedestalBlockEntity>
{
	public PedestalBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(PedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		if(entity.getStack() != ItemStack.EMPTY)
		{
			matrices.push();
			matrices.translate(0.5F, 1 + (1 / 32F), 11 / 32F);
			matrices.scale(1.25F, 1.25F, 1.25F);
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
			int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
			MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
			matrices.pop();
		}
	}
}
