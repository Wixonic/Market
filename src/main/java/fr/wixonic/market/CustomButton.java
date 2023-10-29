package fr.wixonic.market;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

public final class CustomButton {
	public final ItemStack itemStack;

	public CustomButton(Material material, int id) {
		this.itemStack = new ItemStack(material, 1);
		this.setUp(id, null);
	}

	public CustomButton(Material material, int id, String displayName) {
		this.itemStack = new ItemStack(material, 1);
		this.setUp(id, displayName);
	}

	public CustomButton(Material material, int id, String displayName, String... lore) {
		this.itemStack = new ItemStack(material, 1);
		this.setUp(id, displayName);
		this.setLore(lore);
	}

	public CustomButton(ItemStack itemStack, int id) {
		this.itemStack = itemStack;
		this.setUp(id, null);
	}

	public CustomButton(ItemStack itemStack, int id, String displayName) {
		this.itemStack = itemStack;
		this.setUp(id, displayName);
	}

	public CustomButton(ItemStack itemStack, int id, String displayName, String... lore) {
		this.itemStack = itemStack;
		this.setUp(id, displayName);
		this.setLore(lore);
	}

	public void setLore(String... lore) {
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		ArrayList<String> itemLore = new ArrayList<String>();
		Collections.addAll(itemLore, lore);
		itemMeta.setLore(itemLore);
		this.itemStack.setItemMeta(itemMeta);
	}

	private void setUp(int id, String displayName) {
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.setCustomModelData(id);
		if (displayName != null) itemMeta.setDisplayName(displayName);
		this.itemStack.setItemMeta(itemMeta);
	}
}