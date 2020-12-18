package net.watersfall.spellmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.util.Packets;

public abstract class SpellEffectEntity extends Entity
{
	public SpellEffectEntity(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return Packets.create(this, WatersSpellMod.SPAWN_PACKET_ID);
	}
}
