package net.watersfall.spellmod.client.rendering;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.entity.CloudOfDaggersEntity;

public class CloudOfDaggersEntityRenderer extends EntityRenderer<CloudOfDaggersEntity>
{
	private ItemRenderer itemRenderer;
	private static final ItemStack STACK = new ItemStack(Items.IRON_SWORD, 1);

	public CloudOfDaggersEntityRenderer(EntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	public CloudOfDaggersEntityRenderer(EntityRenderDispatcher dispatcher, ItemRenderer itemRenderer)
	{
		super(dispatcher);
		this.itemRenderer = itemRenderer;
	}

	@Override
	public Identifier getTexture(CloudOfDaggersEntity entity)
	{
		return WatersSpellMod.getId("textures/entity/cloud_of_daggers/cloud_of_daggers.png");
	}

	@Override
	public void render(CloudOfDaggersEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
		for(int x = -1; x <= 1; x++)
		{
			for(int y = 0; y <= 2; y++)
			{
				for(int z = -1; z <= 1; z++)
				{
					matrices.push();
					matrices.translate(x * 5F / 6F, y * 5F / 6F, z * 5F / 6F);
					itemRenderer.renderItem(STACK, ModelTransformation.Mode.GROUND, light, light, matrices, vertexConsumers);
					matrices.pop();
				}
			}
		}
	}
}
