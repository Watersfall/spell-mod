package net.watersfall.spellmod.math;

public class Dice
{
	public static int roll(int amount, int sides)
	{
		int total = 0;
		while(amount > 0)
		{
			total += (int)(Math.random() * sides) + 1;
			amount--;
		}
		return total;
	}
}
