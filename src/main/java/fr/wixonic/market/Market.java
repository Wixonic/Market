package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Market implements CommandExecutor {
	public final Database database;
	public boolean databaseIntegrityCompromised = true;

	public Market() {
		database = new Database();

		if (Main.configManager.getBoolean("database-initialized")) {
			try {
				database.load(Main.configManager.getString("database-location"));
				databaseIntegrityCompromised = false;
			} catch (Exception e) {
				Bukkit.getLogger().severe("Failed to connect to database at \"" + Main.configManager.getString("database-location") + "\" - " + e);
				Bukkit.getLogger().severe("Please check if the database-url field matches with the path of the database.");
			}
		} else {
			try {
				database.initialize(Main.configManager.getString("database-location"));
				Main.configManager.set("database-initialized", true);
				databaseIntegrityCompromised = false;
			} catch (Exception e) {
				Bukkit.getLogger().severe("Failed to create the database at \"" + Main.configManager.getString("database-location") + "\" - " + e);
			}
		}
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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