package fr.wixonic.market;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

public class CustomButton {
	public final ItemStack itemStack;
	private final int id;
	private final String displayName;
	private final String[] lore;

	public CustomButton(Material material, int id, String displayName, String... lore) {
		this.itemStack = new ItemStack(material, 1);
		this.id = id;
		this.displayName = displayName;
		this.lore = lore;

		this.setUp();
	}

	public CustomButton(CustomSkull skull, int id, String displayName, String... lore) {
		this.itemStack = skull.head;
		this.id = id;
		this.displayName = displayName;
		this.lore = lore;

		this.setUp();
	}

	public CustomButton(ItemStack itemStack, int id, String displayName, String... lore) {
		this.itemStack = itemStack;
		this.id = id;
		this.displayName = displayName;
		this.lore = lore;
		
		this.setUp();
	}

	private void setUp() {
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.setCustomModelData(this.id);
		itemMeta.setDisplayName(this.displayName);
		ArrayList<String> itemLore = new ArrayList<String>();
		Collections.addAll(itemLore, this.lore);
		itemMeta.setLore(itemLore);
		itemLore.clear();
		this.itemStack.setItemMeta(itemMeta);
	}
}