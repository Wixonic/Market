package fr.wixonic.market;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.FileInputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class Market implements CommandExecutor {
	public final Database database;
	public boolean databaseIntegrityCompromised;

	public final ItemStack fillingItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);

	public final Inventory marketInventory;
	public final Inventory itemListInventory;
	public final Inventory itemInfoInventory;

	public Market() {
		database = new Database();

		try {
			database.load(new FileInputStream(Main.configManager.getString("database-location")));
			databaseIntegrityCompromised = false;
		} catch (Exception e) {
			if (Main.configManager.getBoolean("database-initialized")) {
				Bukkit.getLogger().severe("Failed to load database at \"" + Main.configManager.getString("database-location") + "\" - " + e);
				databaseIntegrityCompromised = true;
			} else {
				Main.configManager.set("database-initialized", true);
			}
		}

		ItemMeta fillingItemMeta = fillingItem.getItemMeta();
		fillingItemMeta.setDisplayName(ChatColor.RESET + "");
		fillingItem.setItemMeta(fillingItemMeta);

		marketInventory = Bukkit.createInventory(null, 54);

		ItemMeta itemMeta;
		ArrayList<String> itemLore = new ArrayList<String>();

		ItemStack listStack = new ItemStack(Material.PAPER, 1);
		itemMeta = listStack.getItemMeta();
		itemMeta.setCustomModelData(3);
		itemMeta.setDisplayName("All items");
		itemLore.add(count(database.getInt("total-buying-requests"), database.getInt("total-selling-requests"), "request"));
		itemMeta.setLore(itemLore);
		itemLore.clear();
		listStack.setItemMeta(itemMeta);

		ItemStack listValuableStack = new ItemStack(Material.WHEAT, 1);
		itemMeta = listValuableStack.getItemMeta();
		itemMeta.setCustomModelData(4);
		itemMeta.setDisplayName("Ores & Valuables");
		itemLore.add(count(database.getInt("valuable-buying-requests"), database.getInt("valuable-selling-requests"), "request"));
		itemMeta.setLore(itemLore);
		itemLore.clear();
		listValuableStack.setItemMeta(itemMeta);

		ItemStack listFarmingStack = new ItemStack(Material.WHEAT, 1);
		itemMeta = listFarmingStack.getItemMeta();
		itemMeta.setCustomModelData(5);
		itemMeta.setDisplayName("Farming & Food");
		itemLore.add(count(database.getInt("farming-buying-requests"), database.getInt("farming-selling-requests"), "request"));
		itemMeta.setLore(itemLore);
		itemLore.clear();
		listFarmingStack.setItemMeta(itemMeta);

		// Building Blocks

		// Special

		// Other

		for (int x = 0; x < 54; ++x) {
			marketInventory.setItem(x, fillingItem);
		}

		marketInventory.setItem(0, listStack);
		marketInventory.setItem(4, listValuableStack);
		marketInventory.setItem(5, listFarmingStack);
		// marketInventory.setItem(6, listBuildingBlocksStack);
		// marketInventory.setItem(7, listSpecialStack);
		// marketInventory.setItem(8, listOtherStack);

		itemListInventory = Bukkit.createInventory(null, 54);
		itemInfoInventory = Bukkit.createInventory(null, 54);
	}

	public String count(int buying, int selling, String type) {
		if (buying > 0 && selling > 0) {
			return (buying + selling) + " " + type + "s - " + count(buying, 0, "buying " + type) + ", "  + count(0, selling, "selling " + type);
		} else if (buying > 0) {
			return buying > 1 ? buying + type + "s" : "One " + type;
		} else if (selling > 0) {
			return selling > 1 ? selling + type + "s" : "One " + type;
		} else {
			return "No " + type;
		}
	}

	public void save() {
		if (!databaseIntegrityCompromised) {
			try {
				database.store(new FileOutputStream(Main.configManager.getString("database-location")), null);
			} catch (Exception e) {
				Bukkit.getLogger().severe("Failed to save the market. Use /market:save to retry.");
			}
		} else {
			Bukkit.broadcastMessage("§c--- Failed to save the market: Database compromised. Please check the console and restart the server.");
		}
	}

	public void updateNavigationLine(Player player, Inventory inventory, int currentPage, int pagesCount) {
		for (int x = 45; x < 54; ++x) {
			inventory.setItem(x, fillingItem);
		}

		ItemMeta itemMeta;
		ArrayList<String> itemLore = new ArrayList<String>();

		itemLore.add("Page " + (currentPage + 1) + "/" + pagesCount);

		if (currentPage > 0) {
			ItemStack leftArrow = CustomSkull.presets.get("leftArrow");
			itemMeta = leftArrow.getItemMeta();
			itemMeta.setLore(itemLore);
			inventory.setItem(48, leftArrow);
		}

		if (currentPage < pagesCount - 1) {
			ItemStack rightArrow = CustomSkull.presets.get("rightArrow");
			itemMeta = rightArrow.getItemMeta();
			itemMeta.setLore(itemLore);
			inventory.setItem(50, rightArrow);
		}

		itemLore.clear();

		inventory.setItem(49, new CustomSkull(player.getDisplayName()).setPlayer(player));
	}

	public void updateItemInfo(Material item) {
		itemInfoInventory.clear();

		for (int x = 0; x < 54; ++x) {
			itemInfoInventory.setItem(x, fillingItem);
		}
	}

	public void updateItemList(int category) {
		itemListInventory.clear();

		for (int x = 0; x < 54; ++x) {
			itemListInventory.setItem(x, fillingItem);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = ((Player) sender).getPlayer();

			if (cmd.getName().equals("market")) {
				if (databaseIntegrityCompromised) {
					Bukkit.broadcastMessage("§c--- Failed to save the market: Database compromised. Please check the console and restart the server.");
				} else {
					updateNavigationLine(player, marketInventory, 0, 1);
					player.openInventory(marketInventory).setTitle("Market");
				}
			}
			return true;
		}

		return false;
	}
}