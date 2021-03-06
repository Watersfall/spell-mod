package net.watersfall.spellmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.client.gui.SpellbookGui;
import net.watersfall.spellmod.client.rendering.CloudOfDaggersEntityRenderer;
import net.watersfall.spellmod.client.rendering.MagicMissileEntityRenderer;
import net.watersfall.spellmod.client.rendering.PedestalBlockEntityRenderer;
import net.watersfall.spellmod.entity.AcidSplashEntity;
import net.watersfall.spellmod.entity.ChillTouchEntity;
import net.watersfall.spellmod.entity.ChromaticOrbEntity;
import net.watersfall.spellmod.entity.MagicMissileEntity;
import net.watersfall.spellmod.util.Packets;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class WatersSpellModClient implements ClientModInitializer
{

	public void receiveEntityPacket()
	{
		ClientSidePacketRegistry.INSTANCE.register(WatersSpellMod.SPAWN_PACKET_ID, (ctx, byteBuf) -> {
			EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
			UUID uuid = byteBuf.readUuid();
			int entityId = byteBuf.readVarInt();
			Vec3d pos = Packets.PacketBufUtil.readVec3d(byteBuf);
			float pitch = Packets.PacketBufUtil.readAngle(byteBuf);
			float yaw = Packets.PacketBufUtil.readAngle(byteBuf);
			ctx.getTaskQueue().execute(() -> {
				if(MinecraftClient.getInstance().world == null)
					throw new IllegalStateException("Tried to spawn entity in a null world!");
				Entity e = et.create(MinecraftClient.getInstance().world);
				if(e == null)
					throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
				e.updateTrackedPosition(pos);
				e.setPos(pos.x, pos.y, pos.z);
				e.pitch = pitch;
				e.yaw = yaw;
				e.setEntityId(entityId);
				e.setUuid(uuid);
				MinecraftClient.getInstance().world.addEntity(entityId, e);
			});
		});
		ClientSidePacketRegistry.INSTANCE.register(WatersSpellMod.MAGIC_MISSILE_PACKET_ID, (ctx, byteBuf) -> {
			EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
			UUID uuid = byteBuf.readUuid();
			int entityId = byteBuf.readVarInt();
			Vec3d pos = Packets.PacketBufUtil.readVec3d(byteBuf);
			float pitch = Packets.PacketBufUtil.readAngle(byteBuf);
			float yaw = Packets.PacketBufUtil.readAngle(byteBuf);
			UUID target = byteBuf.readUuid();
			int targetId = byteBuf.readInt();

			ctx.getTaskQueue().execute(() -> {
				MagicMissileEntity e = (MagicMissileEntity) et.create(MinecraftClient.getInstance().world);
				e.updateTrackedPosition(pos);
				e.setPos(pos.x, pos.y, pos.z);
				e.pitch = pitch;
				e.yaw = yaw;
				e.setEntityId(entityId);
				e.setUuid(uuid);
				e.target = target;
				e.targetEntityId = targetId;
				MinecraftClient.getInstance().world.addEntity(entityId, e);
			});
		});
	}

	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(WatersSpellMod.ACID_SPLASH_TYPE, (d, c) -> new FlyingItemEntityRenderer<AcidSplashEntity>(d, c.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(WatersSpellMod.CHILL_TOUCH_ENTITY, (d, c) -> new FlyingItemEntityRenderer<ChillTouchEntity>(d, c.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(WatersSpellMod.CHROMATIC_ORB_ENTITY, (d, c) -> new FlyingItemEntityRenderer<ChromaticOrbEntity>(d, c.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(WatersSpellMod.CLOUD_OF_DAGGERS_ENTITY, (d, c) -> new CloudOfDaggersEntityRenderer(d, c.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(WatersSpellMod.MAGIC_MISSILE_ENTITY, (d, c) -> new MagicMissileEntityRenderer(d, c.getItemRenderer()));
		BlockRenderLayerMap.INSTANCE.putBlock(WatersSpellMod.BONFIRE_BLOCK, RenderLayer.getCutout());
		BlockEntityRendererRegistry.INSTANCE.register(WatersSpellMod.PEDESTAL_BLOCK_ENTITY, PedestalBlockEntityRenderer::new);
		ScreenRegistry.register(WatersSpellMod.SPELLBOOK_SCREEN_HANDLER, SpellbookGui::new);
		receiveEntityPacket();
	}
}
