package fr.wixonic.market;

import java.io.FileOutputStream;
import java.io.FileInputStream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Market implements CommandExecutor {
	public final Database database;
	public boolean databaseIntegrityCompromised;

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
					CustomInventory playerInventory = CustomInventory.getFor(player);
					player.openInventory(playerInventory.getCurrent()).setTitle(playerInventory.getTitle());
				}
			}
			return true;
		}

		return false;
	}
}