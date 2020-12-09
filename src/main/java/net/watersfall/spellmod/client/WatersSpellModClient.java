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
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.client.gui.SpellbookGui;
import net.watersfall.spellmod.client.rendering.PedestalBlockEntityRenderer;
import net.watersfall.spellmod.entity.AcidSplashEntity;
import net.watersfall.spellmod.entity.AnimalFriendshipEntity;
import net.watersfall.spellmod.entity.ChillTouchEntity;
import net.watersfall.spellmod.util.EntitySpawnPacket;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class WatersSpellModClient implements ClientModInitializer
{
	public static final Identifier PACKET_ID = WatersSpellMod.getId("spawn_packet");

	public void receiveEntityPacket()
	{
		ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, (ctx, byteBuf) -> {
			EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
			UUID uuid = byteBuf.readUuid();
			int entityId = byteBuf.readVarInt();
			Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
			float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
			float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
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
	}

	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(WatersSpellMod.ACID_SPLASH_TYPE, (d, c) -> new FlyingItemEntityRenderer<AcidSplashEntity>(d, c.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(WatersSpellMod.CHILL_TOUCH_ENTITY, (d, c) -> new FlyingItemEntityRenderer<ChillTouchEntity>(d, c.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(WatersSpellMod.ANIMAL_FRIENDSHIP_ENTITY, (d, c) -> new FlyingItemEntityRenderer<AnimalFriendshipEntity>(d, c.getItemRenderer()));
		BlockRenderLayerMap.INSTANCE.putBlock(WatersSpellMod.BONFIRE_BLOCK, RenderLayer.getCutout());
		BlockEntityRendererRegistry.INSTANCE.register(WatersSpellMod.PEDESTAL_BLOCK_ENTITY, PedestalBlockEntityRenderer::new);
		ScreenRegistry.register(WatersSpellMod.SPELLBOOK_SCREEN_HANDLER, SpellbookGui::new);
		receiveEntityPacket();
	}
}
