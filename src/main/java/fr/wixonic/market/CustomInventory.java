package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public final class CustomInventory {
	public static final Map<String, String> index = new HashMap<>();
	private static final Map<UUID, CustomInventory> inventories = new HashMap<>();

	static {
		CustomInventory.index.put("market", "Market");
		CustomInventory.index.put("all", "List");
		CustomInventory.index.put("valuables", "Ores & Valuables");
		CustomInventory.index.put("mobdrops", "Mobdrops");
		CustomInventory.index.put("farming", "Farming & Food");
		CustomInventory.index.put("blocks", "Building Blocks");
		CustomInventory.index.put("special", "Special");
		CustomInventory.index.put("other", "Other");
		CustomInventory.index.put("@player", "@player");
	}

	private final Player player;
	private final Inventory inventory = Bukkit.createInventory(null, 54);
	private final Map<String, Integer> pageManager = new HashMap<>();
	private final ArrayList<String> history = new ArrayList<>();
	private String current = "market";
	private String title;

	public CustomInventory(Player player) {
		this.player = player;

		this.pageManager.put("current", 1);
		this.pageManager.put("max", 1);

		this.display();
	}

	public static CustomInventory getFor(Player player) {
		CustomInventory inventory = CustomInventory.inventories.get(player.getUniqueId());

		if (inventory != null) return inventory;
		else {
			inventory = new CustomInventory(player);
			CustomInventory.inventories.put(player.getUniqueId(), inventory);
			return inventory;
		}
	}

	private void display() {
		this.inventory.clear();
		
		Runnable categories = () -> {
			this.inventory.setItem(0, new CustomButton(Material.PAPER, 6, "All items", ChatColor.GRAY + this.count(Main.market.database.getInt("total.buying.requests"), Main.market.database.getInt("total.selling.requests"), "request")).itemStack);
			this.inventory.setItem(3, new CustomButton(Material.DIAMOND, 7, "Ores & Valuables", ChatColor.GRAY + this.count(Main.market.database.getInt("valuables.buying.requests"), Main.market.database.getInt("valuables.selling.requests"), "request")).itemStack);
			this.inventory.setItem(4, new CustomButton(Material.BLAZE_ROD, 8, "Mobdrops", ChatColor.GRAY + this.count(Main.market.database.getInt("mobdrops.buying.requests"), Main.market.database.getInt("mobdrops.selling.requests"), "request")).itemStack);
			this.inventory.setItem(5, new CustomButton(Material.WHEAT, 9, "Farming & Food", ChatColor.GRAY + this.count(Main.market.database.getInt("farming.buying.requests"), Main.market.database.getInt("farming.selling.requests"), "request")).itemStack);
			this.inventory.setItem(6, new CustomButton(Material.MOSSY_STONE_BRICK_STAIRS, 10, "Building Blocks", ChatColor.GRAY + this.count(Main.market.database.getInt("building.buying.requests"), Main.market.database.getInt("building.selling.requests"), "request")).itemStack);
			this.inventory.setItem(7, new CustomButton(Material.NETHER_STAR, 11, "Special", ChatColor.GRAY + this.count(Main.market.database.getInt("special.buying.requests"), Main.market.database.getInt("special.selling.requests"), "request")).itemStack);
			this.inventory.setItem(8, new CustomButton(Material.FLINT, 12, "Other", ChatColor.GRAY + this.count(Main.market.database.getInt("other.buying.requests"), Main.market.database.getInt("other.selling.requests"), "request")).itemStack);
		};

		Consumer<String> itemList = (String category) -> {
			List<Material> list = ItemManager.get(category);

			this.pageManager.put("max", (int) Math.floor(list.size() / 36) + 1);
			this.pageManager.put("current", Math.min(this.pageManager.get("current"), this.pageManager.get("max")));

			try {
				List<Material> sublist = list.subList((this.pageManager.get("current") - 1) * 36, Math.min(this.pageManager.get("current") * 36, list.size()));

				for (int x = 0; x < sublist.size(); ++x) {
					this.inventory.setItem(x + 9, new CustomButton(sublist.get(x), 13).itemStack);
				}
			} catch (RuntimeException e) {
				Main.getInstance().getLogger().warning("Failed to display item list for " + player.getName() + ": " + e);
			}
		};

		this.title = this.current;
		this.pageManager.put("max", 1);

		if (this.current.equals("market")) {
			this.history.clear();

			categories.run();

			this.pageManager.put("current", 1);
		} else if (this.current == "@player") {
			this.pageManager.put("current", 1);

			// Player page
		} else if (CustomInventory.index.containsKey(this.current)) {
			this.history.clear();
			this.history.add("market");

			categories.run();
			itemList.accept(this.current);
		} else {
			Material item = Material.getMaterial(this.current);
			
			this.inventory.setItem(4, new CustomButton(item, -1).itemStack);
			
			this.pageManager.put("current", 1);
			this.title = ItemManager.getNameFor(item.name()) != null ? ItemManager.getNameFor(item.name()) : "Unknown";
		}

		this.history.add(this.current);

		if (this.history.size() > 1)
			this.inventory.setItem(45, new CustomButton(CustomSkull.presets.get("leftArrow"), 0, "Back").itemStack);

		if (this.pageManager.get("current") > 2)
			this.inventory.setItem(47, new CustomButton(CustomSkull.presets.get("doubleBackward"), 1, "First").itemStack);
		if (this.pageManager.get("current") > 1)
			this.inventory.setItem(48, new CustomButton(CustomSkull.presets.get("backward"), 2, "Previous").itemStack);

		if (this.current != "@player")
			this.inventory.setItem(49, new CustomButton(new CustomSkull("You").setPlayer(player), 3, "You").itemStack);

		if (this.pageManager.get("current") < this.pageManager.get("max"))
			this.inventory.setItem(50, new CustomButton(CustomSkull.presets.get("forward"), 4, "Next").itemStack);
		if (this.pageManager.get("current") < this.pageManager.get("max") - 1)
			this.inventory.setItem(51, new CustomButton(CustomSkull.presets.get("doubleForward"), 5, "Last").itemStack);

		this.inventory.setItem(53, new CustomButton(Material.BOOK, -1, "About", Main.getInstance().getDescription().getName() + " v" + Main.getInstance().getDescription().getVersion()).itemStack);

		try {
			if (this.history.size() > 1) {
				String previousID = this.history.get(this.history.size() - 2);
				this.title = CustomInventory.index.getOrDefault(previousID, (ItemManager.getNameFor(previousID) != null ? ItemManager.getNameFor(previousID) : "Unknown")) + " → " + CustomInventory.index.getOrDefault(this.current, this.title);
			} else this.title = CustomInventory.index.getOrDefault(this.current, this.title);
		} catch (Exception e) {
			Bukkit.getLogger().warning(e.getMessage());
			this.title = "Error";
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
		return (this.title + (this.pageManager.getOrDefault("max", 1) > 1 ? " (" + this.pageManager.getOrDefault("current", 1) + "/" + this.pageManager.getOrDefault("max", 1) + ")" : "")).replaceAll("\\@player", player.getName());
	}

	public void navigateTo(String name) {
		this.current = name;
		this.display();
	}

	public void back() {
		this.history.remove(history.size() - 1);
		this.current = this.history.remove(history.size() - 1);
		this.display();
	}

	public void first() {
		this.pageManager.put("current", 1);
		this.current = this.history.remove(history.size() - 1);
		this.display();
	}

	public void previous() {
		this.pageManager.put("current", this.pageManager.get("current") - 1);
		this.current = this.history.remove(history.size() - 1);
		this.display();
	}

	public void next() {
		this.pageManager.put("current", this.pageManager.get("current") + 1);
		this.current = this.history.remove(history.size() - 1);
		this.display();
	}

	public void last() {
		this.pageManager.put("current", this.pageManager.get("max"));
		this.current = this.history.remove(history.size() - 1);
		this.display();
	}

	public String count(int buying, int selling, String type) {
		if (buying > 0 && selling > 0) {
			return (buying + selling) + " " + type + "s - " + count(buying, 0, "buying " + type) + ", " + count(0, selling, "selling " + type);
		} else if (buying > 0) {
			return buying > 1 ? buying + type + "s" : "One " + type;
		} else if (selling > 0) {
			return selling > 1 ? selling + type + "s" : "One " + type;
		} else {
			return "No " + type;
		}
	}
}