package fr.wixonic.market;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemManager {
	public static Map<String, List<Material>> categories;
	public static List<Material> list;
	public static List<String> items;

	static {
		ItemManager.reload();
	}

	public static void reload() {
		ItemManager.categories = new HashMap<>();
		ItemManager.list = new ArrayList<>();
		ItemManager.items = Main.configManager.getKeys("items");

		for (String category : CustomInventory.index.keySet()) ItemManager.categories.put(category, new ArrayList<>());

		for (Material material : Material.values()) {
			if (material.isItem() && !material.isAir() && !material.isLegacy()) {
				String id = material.name();

				if (ItemManager.items.contains(id)) {
					String categoryID = ItemManager.getCategoryFor(id);
					List<Material> category = ItemManager.categories.get(categoryID);

					if (category == null) {
						categoryID = "other";
						category = ItemManager.categories.get(categoryID);
					}

					category.add(material);
					ItemManager.categories.put(categoryID, category);

					ItemManager.list.add(material);
				}
			}
		}
	}

	public static List<Material> get(String category) {
		if (category == "all") return ItemManager.list;
		else return ItemManager.categories.getOrDefault(category, new ArrayList<Material>());
	}

	public static String getCategoryFor(String id) {
		return Main.configManager.getString("items." + id + ".category");
	}

	public static String getCategoryFor(Material material) {
		return Main.configManager.getString("items." + material.name() + ".category");
	}

	public static String getNameFor(String id) {
		return Main.configManager.getString("items." + id + ".name");
	}

	public static String getNameFor(Material material) {
		return Main.configManager.getString("items." + material.name() + ".name");
	}

	public static double getSuggestedPriceFor(String id) {
		return Double.parseDouble(Main.configManager.getString("items." + id + ".price"));
	}

	public static double getSuggestedPriceFor(Material material) {
		return Double.parseDouble(Main.configManager.getString("items." + material.name() + ".price"));
	}
}
