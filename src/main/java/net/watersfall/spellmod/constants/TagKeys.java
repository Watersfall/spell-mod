package net.watersfall.spellmod.constants;

public class TagKeys
{
	public static final String ACTIVE_SPELL = "active_spell";
	public static final String LEVEL = "level";

	private static final String SPELL_SLOT = "level_%d_spell_slots";

	public static String getSpellSlotTag(int level)
	{
		return String.format(SPELL_SLOT, level);
	}
}
