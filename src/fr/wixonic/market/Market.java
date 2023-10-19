package fr.wixonic.market;

import java.io.FileOutputStream;
import java.io.FileInputStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Market implements CommandExecutor {
	public final Database database;
	public boolean databaseIntegrityCompromised;

	public final CustomInventory marketInventory;
	public final CustomInventory itemListInventory;
	public final CustomInventory itemInfoInventory;

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

		marketInventory = new CustomInventory(54);

		CustomButton listStack = new CustomButton(Material.PAPER, 3, "All items", count(database.getInt("total-buying-requests"), database.getInt("total-selling-requests"), "request"));
		marketInventory.setItem(listStack.itemStack, 1);

		CustomButton listValuableStack = new CustomButton(Material.WHEAT, 4, "Ores & Valuables", count(database.getInt("valuable-buying-requests"), database.getInt("valuable-selling-requests"), "request"));
		marketInventory.setItem(listValuableStack.itemStack, 4);

		CustomButton listMobdropsStack = new CustomButton(Material.BLAZE_ROD, 5, "Mobdrops", count(database.getInt("mobdrops-buying-requests"), database.getInt("mobdrops-selling-requests"), "request"));
		marketInventory.setItem(listMobdropsStack.itemStack, 5);

		CustomButton listFarmingStack = new CustomButton(Material.WHEAT, 6, "Farming & Food", count(database.getInt("farming-buying-requests"), database.getInt("farming-selling-requests"), "request"));
		marketInventory.setItem(listFarmingStack.itemStack, 6);

		CustomButton listBuildingBlocksStack = new CustomButton(Material.MOSSY_STONE_BRICK_STAIRS, 7, "Building Blocks", count(database.getInt("building-buying-requests"), database.getInt("building-selling-requests"), "request"));
		marketInventory.setItem(listBuildingBlocksStack.itemStack, 7);

		CustomButton listSpecialStack = new CustomButton(Material.NETHER_STAR, 8, "Special", count(database.getInt("special-buying-requests"), database.getInt("special-selling-requests"), "request"));
		marketInventory.setItem(listSpecialStack.itemStack, 8);

		CustomButton listOtherStack = new CustomButton(Material.FLINT, 9, "Other", count(database.getInt("other-buying-requests"), database.getInt("other-selling-requests"), "request"));
		marketInventory.setItem(listOtherStack.itemStack, 9);

		itemListInventory = new CustomInventory(54);
		itemInfoInventory = new CustomInventory(54);
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

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = ((Player) sender).getPlayer();

			if (cmd.getName().equals("market")) {
				if (databaseIntegrityCompromised) {
					Bukkit.broadcastMessage("§c--- Failed to save the market: Database compromised. Please check the console and restart the server.");
				} else {
					marketInventory.update(player, 0, 0);
					player.openInventory(marketInventory.inventory).setTitle("Market");
				}
			}
			return true;
		}

		return false;
	}
}