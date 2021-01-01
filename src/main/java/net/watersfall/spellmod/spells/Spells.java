package net.watersfall.spellmod.spells;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.spellmod.WatersSpellMod;
import net.watersfall.spellmod.spells.Spell;

import java.util.Collection;
import java.util.HashMap;

public class Spells
{
	private static final BiMap<Identifier, Spell> spells = HashBiMap.create();

	public static Spell addSpell(Identifier id, Spell spell)
	{
		spells.put(id, spell);
		Registry.register(Registry.ITEM, id, spell.item);
		return spell;
	}

	public static Spell getSpell(Identifier id)
	{
		return spells.get(id);
	}

	public static Spell getSpell(String id)
	{
		return spells.get(new Identifier(id));
	}

	public static Collection<Spell> getAllSpells()
	{
		return spells.values();
	}

	public static Identifier getId(Spell spell)
	{
		return spells.inverse().get(spell);
	}
}
