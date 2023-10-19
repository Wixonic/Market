package fr.wixonic.market;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

public class CustomButton {
	public final ItemStack itemStack;

	public CustomButton(Material material, int id, String displayName, String ...lore) {
		itemStack = new ItemStack(material, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setCustomModelData(id);
		itemMeta.setDisplayName(displayName);
		ArrayList<String> itemLore = new ArrayList<String>();
		Collections.addAll(itemLore, lore);
		itemMeta.setLore(itemLore);
		itemLore.clear();
		itemStack.setItemMeta(itemMeta);
	}
}
