package fr.wixonic.market;

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

public class Market implements CommandExecutor {
	public final Database database;

	public final ItemStack fillingItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);

	public final Inventory marketInventory;
	public final Inventory itemListInventory;
	public final Inventory itemInfoInventory;

	public Market() {
		database = new Database();

		try {
			database.load(new FileInputStream(Main.configManager.getString("database-location")));
		} catch (Exception ignored) {}

		fillingItem.getItemMeta().setDisplayName(ChatColor.RESET + "");

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

		ItemStack listFarmingStack = new ItemStack(Material.WHEAT, 1);
		itemMeta = listFarmingStack.getItemMeta();
		itemMeta.setCustomModelData(4);
		itemMeta.setDisplayName("Farming");
		itemLore.add(count(database.getInt("farming-buying-requests"), database.getInt("farming-selling-requests"), "request"));
		itemMeta.setLore(itemLore);
		itemLore.clear();
		listFarmingStack.setItemMeta(itemMeta);

		for (int x = 0; x < 54; ++x) {
			marketInventory.setItem(x, fillingItem);
		}

		marketInventory.setItem(0, listStack);
		marketInventory.setItem(8, listFarmingStack);

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
		try {
			database.store(new FileOutputStream(Main.configManager.getString("database-location")), null);
		} catch (Exception e) {
			Bukkit.getLogger().severe("Failed to save the market. Use /save to retry.");
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

		inventory.setItem(49, new ItemStack(Material.PLAYER_HEAD));
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
				updateNavigationLine(player, marketInventory, 1, 3);
				player.openInventory(marketInventory).setTitle("Market");
			}
			return true;
		}

		return false;
	}
}