package net.watersfall.spellmod.spells;

public enum SpellClass
{
	BARD(new int[]{2,2,2,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4},
			new int[]{4,5,6,7,8,9,10,11,12,14,15,15,16,18,19,20,22,22,22},
			new int[][]{
			{2,0,0,0,0,0,0,0,0},
			{3,0,0,0,0,0,0,0,0},
			{4,2,0,0,0,0,0,0,0},
			{4,3,0,0,0,0,0,0,0},
			{4,3,2,0,0,0,0,0,0},
			{4,3,3,0,0,0,0,0,0},
			{4,3,3,1,0,0,0,0,0},
			{4,3,3,2,0,0,0,0,0},
			{4,3,3,3,1,0,0,0,0},
			{4,3,3,3,2,0,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,1},
			{4,3,3,3,3,1,1,1,1},
			{4,3,3,3,3,2,1,1,1},
			{4,3,3,3,3,2,2,1,1},
	}),
	CLERIC(new int[]{3,3,3,4,4,4,4,4,4,5,5,5,5,5,5,5,5,5,5,5},
			new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20},
			new int[][]{
			{2,0,0,0,0,0,0,0,0},
			{3,0,0,0,0,0,0,0,0},
			{4,2,0,0,0,0,0,0,0},
			{4,3,0,0,0,0,0,0,0},
			{4,3,2,0,0,0,0,0,0},
			{4,3,3,0,0,0,0,0,0},
			{4,3,3,1,0,0,0,0,0},
			{4,3,3,2,0,0,0,0,0},
			{4,3,3,3,1,0,0,0,0},
			{4,3,3,3,2,0,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,1},
			{4,3,3,3,3,1,1,1,1},
			{4,3,3,3,3,2,1,1,1},
			{4,3,3,3,3,2,2,1,1},
	}),
	DRUID(new int[]{2,2,2,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4},
			new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20},
			new int[][]{
			{2,0,0,0,0,0,0,0,0},
			{3,0,0,0,0,0,0,0,0},
			{4,2,0,0,0,0,0,0,0},
			{4,3,0,0,0,0,0,0,0},
			{4,3,2,0,0,0,0,0,0},
			{4,3,3,0,0,0,0,0,0},
			{4,3,3,1,0,0,0,0,0},
			{4,3,3,2,0,0,0,0,0},
			{4,3,3,3,1,0,0,0,0},
			{4,3,3,3,2,0,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,1},
			{4,3,3,3,3,1,1,1,1},
			{4,3,3,3,3,2,1,1,1},
			{4,3,3,3,3,2,2,1,1},
	}),
	PALADIN(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			new int[]{1,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10},
			new int[][]{
			{0,0,0,0,0,0,0,0,0},
			{2,0,0,0,0,0,0,0,0},
			{3,0,0,0,0,0,0,0,0},
			{3,0,0,0,0,0,0,0,0},
			{4,2,0,0,0,0,0,0,0},
			{4,2,0,0,0,0,0,0,0},
			{4,3,0,0,0,0,0,0,0},
			{4,3,0,0,0,0,0,0,0},
			{4,3,2,0,0,0,0,0,0},
			{4,3,2,0,0,0,0,0,0},
			{4,3,3,0,0,0,0,0,0},
			{4,3,3,0,0,0,0,0,0},
			{4,3,3,1,0,0,0,0,0},
			{4,3,3,1,0,0,0,0,0},
			{4,3,3,2,0,0,0,0,0},
			{4,3,3,2,0,0,0,0,0},
			{4,3,3,3,1,0,0,0,0},
			{4,3,3,3,1,0,0,0,0},
			{4,3,3,3,2,0,0,0,0},
			{4,3,3,3,2,0,0,0,0},
	}),
	RANGER(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			new int[]{0,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11},
			new int[][]{
			{0,0,0,0,0,0,0,0,0},
			{2,0,0,0,0,0,0,0,0},
			{3,0,0,0,0,0,0,0,0},
			{3,0,0,0,0,0,0,0,0},
			{4,2,0,0,0,0,0,0,0},
			{4,2,0,0,0,0,0,0,0},
			{4,3,0,0,0,0,0,0,0},
			{4,3,0,0,0,0,0,0,0},
			{4,3,2,0,0,0,0,0,0},
			{4,3,2,0,0,0,0,0,0},
			{4,3,3,0,0,0,0,0,0},
			{4,3,3,0,0,0,0,0,0},
			{4,3,3,1,0,0,0,0,0},
			{4,3,3,1,0,0,0,0,0},
			{4,3,3,2,0,0,0,0,0},
			{4,3,3,2,0,0,0,0,0},
			{4,3,3,3,1,0,0,0,0},
			{4,3,3,3,1,0,0,0,0},
			{4,3,3,3,2,0,0,0,0},
			{4,3,3,3,2,0,0,0,0},
	}),
	SORCERER(new int[]{4,4,4,5,5,5,5,5,5,6,6,6,6,6,6,6,6,6,6,6},
			new int[]{2,3,4,5,6,7,8,9,10,11,12,12,13,13,14,14,15,15,15,15},
			new int[][]{
			{2,0,0,0,0,0,0,0,0},
			{3,0,0,0,0,0,0,0,0},
			{4,2,0,0,0,0,0,0,0},
			{4,3,0,0,0,0,0,0,0},
			{4,3,2,0,0,0,0,0,0},
			{4,3,3,0,0,0,0,0,0},
			{4,3,3,1,0,0,0,0,0},
			{4,3,3,2,0,0,0,0,0},
			{4,3,3,3,1,0,0,0,0},
			{4,3,3,3,2,0,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,1},
			{4,3,3,3,3,1,1,1,1},
			{4,3,3,3,3,2,1,1,1},
			{4,3,3,3,3,2,2,1,1},
	}),
	WARLOCK(new int[]{2,2,2,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4},
			new int[]{2,3,4,5,6,7,8,9,10,10,11,11,12,12,13,13,14,14,15,15},
			new int[][]{
			{1,0,0,0,0,0,0,0,0},
			{2,0,0,0,0,0,0,0,0},
			{0,2,0,0,0,0,0,0,0},
			{0,2,0,0,0,0,0,0,0},
			{0,0,2,0,0,0,0,0,0},
			{0,0,2,0,0,0,0,0,0},
			{0,0,0,2,0,0,0,0,0},
			{0,0,0,2,0,0,0,0,0},
			{0,0,0,0,2,0,0,0,0},
			{0,0,0,0,2,0,0,0,0},
			{0,0,0,0,3,0,0,0,0},
			{0,0,0,0,3,0,0,0,0},
			{0,0,0,0,3,0,0,0,0},
			{0,0,0,0,3,0,0,0,0},
			{0,0,0,0,3,0,0,0,0},
			{0,0,0,0,3,0,0,0,0},
			{0,0,0,0,4,0,0,0,0},
			{0,0,0,0,4,0,0,0,0},
			{0,0,0,0,4,0,0,0,0},
			{0,0,0,0,4,0,0,0,0},
	}),
	WIZARD(new int[]{3,3,3,4,4,4,4,4,4,5,5,5,5,5,5,5,5,5,5,5},
			new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20},
			new int[][]{
			{2,0,0,0,0,0,0,0,0},
			{3,0,0,0,0,0,0,0,0},
			{4,2,0,0,0,0,0,0,0},
			{4,3,0,0,0,0,0,0,0},
			{4,3,2,0,0,0,0,0,0},
			{4,3,3,0,0,0,0,0,0},
			{4,3,3,1,0,0,0,0,0},
			{4,3,3,2,0,0,0,0,0},
			{4,3,3,3,1,0,0,0,0},
			{4,3,3,3,2,0,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,0,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,0,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,0},
			{4,3,3,3,2,1,1,1,1},
			{4,3,3,3,3,1,1,1,1},
			{4,3,3,3,3,2,1,1,1},
			{4,3,3,3,3,2,2,1,1},
	});

	public final int[] knownCantrips;
	public final int[] knownSpells;
	public final int[][] levels;

	SpellClass(int[] knownCantrips, int[] knownSpells, int[][] levels)
	{
		this.knownCantrips = knownCantrips;
		this.knownSpells = knownSpells;
		this.levels = levels;
	}

	public String getTranslationKey()
	{
		return "class.waters_spell_mod." + this.name().toLowerCase();
	}

	public int getMaxLevel(int level)
	{
		int max = 0;
		for(int i = 0; i < levels[level].length; i++)
		{
			if(levels[level][i] > 0)
			{
				max = i;
			}
		}
		return max;
	}
}