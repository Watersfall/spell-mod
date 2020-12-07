package net.watersfall.spellmod.constants;

public class LangKeys
{
	public static final String LEVEL = "text.waters_spell_mod.level";
	public static final String SPELL_SLOTS = "text.waters_spell_mod.spell_slots";

	private static final String LEVELED_SPELL_SLOT = "text.waters_spell_mod.level_%d_spell_slots";

	public static String getLeveledSpellSlot(int level)
	{
		return String.format(LEVELED_SPELL_SLOT, level);
	}
}
