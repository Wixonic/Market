package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomInventory {
	private static final Map<UUID, CustomInventory> inventories = new HashMap<>();
	public static final Map<String, String> index = new HashMap<>();

	public static CustomInventory getFor(Player player) {
		CustomInventory inventory = CustomInventory.inventories.get(player.getUniqueId());

		if (inventory != null) {
			return inventory;
		} else {
			inventory = new CustomInventory(player);
			CustomInventory.inventories.put(player.getUniqueId(), inventory);
			return inventory;
		}
	}

	static {
		index.put("market", "Market");
		index.put("all", "List");
		index.put("valuables", "Ores & Valuables");
		index.put("mobdrops", "Mobdrops");
		index.put("farming", "Farming & Food");
		index.put("blocks", "Building Blocks");
		index.put("special", "Special");
		index.put("other", "Other");
	}

	private final Player player;
	private final Inventory inventory = Bukkit.createInventory(null, 54);

	private String current = "market";
	private String title;

	private final Map<String, Integer> pageManager = new HashMap<>();
	private final ArrayList<String> history = new ArrayList<>();

	public CustomInventory(Player player) {
		this.player = player;

		this.pageManager.put("current", 1);
		this.pageManager.put("max", 5);

		this.display();
	}

	private void display() {
		for (int x = 0; x < inventory.getSize(); ++x)
			inventory.setItem(x, new CustomButton(Material.BLACK_STAINED_GLASS_PANE, -1, ChatColor.RESET.toString()).itemStack);

		Runnable categories = () -> {
			inventory.setItem(0, new CustomButton(Material.PAPER, 3, "All items", Main.market.database.count(Main.market.database.getInt("total-buying-requests"), Main.market.database.getInt("total-selling-requests"), "request")).itemStack);
			inventory.setItem(3, new CustomButton(Material.DIAMOND, 4, "Ores & Valuables", Main.market.database.count(Main.market.database.getInt("valuable-buying-requests"), Main.market.database.getInt("valuable-selling-requests"), "request")).itemStack);
			inventory.setItem(4, new CustomButton(Material.BLAZE_ROD, 5, "Mobdrops", Main.market.database.count(Main.market.database.getInt("mobdrops-buying-requests"), Main.market.database.getInt("mobdrops-selling-requests"), "request")).itemStack);
			inventory.setItem(5, new CustomButton(Material.WHEAT, 6, "Farming & Food", Main.market.database.count(Main.market.database.getInt("farming-buying-requests"), Main.market.database.getInt("farming-selling-requests"), "request")).itemStack);
			inventory.setItem(6, new CustomButton(Material.MOSSY_STONE_BRICK_STAIRS, 7, "Building Blocks", Main.market.database.count(Main.market.database.getInt("building-buying-requests"), Main.market.database.getInt("building-selling-requests"), "request")).itemStack);
			inventory.setItem(7, new CustomButton(Material.NETHER_STAR, 8, "Special", Main.market.database.count(Main.market.database.getInt("special-buying-requests"), Main.market.database.getInt("special-selling-requests"), "request")).itemStack);
			inventory.setItem(8, new CustomButton(Material.FLINT, 9, "Other", Main.market.database.count(Main.market.database.getInt("other-buying-requests"), Main.market.database.getInt("other-selling-requests"), "request")).itemStack);
		};

		switch (this.current) {
			case "market":
				this.history.clear();

				categories.run();
				break;

			case "farming":
				this.history.clear();
				this.history.add("Market");

				categories.run();

				// Display items
				break;

			default: // Item page
				Material item = Material.getMaterial(this.current);
				break;
		}

		inventory.setItem(53, new CustomButton(Material.BOOK, -1, "About", Bukkit.getServer().getPluginManager().getPlugin("WixMarket").getDescription().getName() + " v" + Bukkit.getServer().getPluginManager().getPlugin("WixMarket").getDescription().getVersion()).itemStack);

		this.history.add(this.current);

		for (String page : history) {
			if (page.equals("market")) this.title = "Market";
			else this.title += " → " + (CustomInventory.index.getOrDefault(page, "Unknown"));
		}
	}

	public Inventory get(String name) {
		this.navigateTo(name);
		return this.inventory;
	}

	public Inventory getCurrent() {
		return this.inventory;
	}

	public String getTitle() {
		return this.title + (this.pageManager.getOrDefault("max", 1) > 1 ? " (" + this.pageManager.getOrDefault("current", 1) + "/" + this.pageManager.getOrDefault("max", 1) + ")" : "");
	}

	public void navigateTo(String name) {
		this.current = name;
		this.display();
		this.history.add(name);
	}

	public void back() {
		this.history.remove(history.size() - 1);
		this.current = this.history.remove(history.size() - 1);
	}

	public void previous() {
		if (this.pageManager.get("current") > 1) this.pageManager.put("current", this.pageManager.get("current") - 1);
	}

	public void next() {
		if (this.pageManager.get("current") < this.pageManager.get("max"))
			this.pageManager.put("current", this.pageManager.get("current") + 1);
	}
}