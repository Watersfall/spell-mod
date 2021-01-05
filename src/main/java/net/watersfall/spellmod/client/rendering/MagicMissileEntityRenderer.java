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
import net.minecraft.util.math.MathHelper;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.entity.MagicMissileEntity;

public class MagicMissileEntityRenderer extends EntityRenderer<MagicMissileEntity>
{
	private final ItemRenderer itemRenderer;
	private static final Identifier TEXTURE = WatersSpellMod.getId("textures/entity/magic_missile/magic_missile.png");
	private static final ItemStack STACK = new ItemStack(Items.GOLDEN_SWORD);

	public MagicMissileEntityRenderer(EntityRenderDispatcher dispatcher, ItemRenderer itemRenderer)
	{
		super(dispatcher);
		this.itemRenderer = itemRenderer;
	}

	@Override
	public Identifier getTexture(MagicMissileEntity entity)
	{
		return TEXTURE;
	}

	@Override
	public void render(MagicMissileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		matrices.push();
		double x = MathHelper.lerp(tickDelta, entity.prevX, entity.getX()) - entity.prevX;
		double y = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) - entity.prevY;
		double z = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ()) - entity.prevZ;
		matrices.translate(x, y, z);
		this.itemRenderer.renderItem(STACK, ModelTransformation.Mode.GROUND, light, light, matrices, vertexConsumers);
		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
