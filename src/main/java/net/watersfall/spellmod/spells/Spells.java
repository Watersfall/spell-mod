package net.watersfall.spellmod.spells;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.spellmod.spells.Spell;

import java.util.Collection;
import java.util.HashMap;

public class Spells
{
	private static final HashMap<String, Spell> spells = new HashMap<>();

	public static Spell addSpell(Identifier id, Spell spell)
	{
		spells.put(id.toString(), spell);
		Registry.register(Registry.ITEM, id, spell.getItem());
		return spell;
	}

	public static Spell addSpell(String id, Spell spell)
	{
		spells.put(id, spell);
		Registry.register(Registry.ITEM, new Identifier(id), spell.getItem());
		return spell;
	}

	public static Spell getSpell(Identifier id)
	{
		return spells.get(id.toString());
	}

	public static Spell getSpell(String id)
	{
		return spells.get(id);
	}

	public static Collection<Spell> getAllSpells()
	{
		return spells.values();
	}
}
