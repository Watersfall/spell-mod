package net.watersfall.spellmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.spellmod.item.SpellbookItem;

public class WatersSpellMod implements ModInitializer
{
	public static final String MOD_ID = "waters_spell_mod";

	public static final ItemGroup SPELL_MOD_GROUP;
	public static final SpellbookItem SPELLBOOK;

	static
	{
		SPELL_MOD_GROUP = FabricItemGroupBuilder.build(getId("spells"), () -> new ItemStack(Items.BOOK));
		SPELLBOOK = new SpellbookItem(new FabricItemSettings().group(SPELL_MOD_GROUP));
	}

	private static Identifier getId(String id)
	{
		return new Identifier(MOD_ID, id);
	}

	@Override
	public void onInitialize()
	{
		Registry.register(Registry.ITEM, getId("spellbook"), SPELLBOOK);
	}
}
