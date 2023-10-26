package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Market implements CommandExecutor {
	public final Database database;

	public Market() {
		database = new Database();

		if (Main.configManager.getBoolean("database-initialized")) {
			try {
				database.load();
			} catch (Exception e) {
				Main.getInstance().getLogger().severe("Failed to connect to database at \"" + Database.location + "\" - " + e);
				Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
			}
		} else {
			try {
				database.initialize();
				Main.configManager.set("database-initialized", true);
				Main.getInstance().saveConfig();
			} catch (Exception e) {
				Main.getInstance().getLogger().severe("Failed to create the database at \"" + Database.location + "\" - " + e);
				Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
			}
		}
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = ((Player) sender).getPlayer();

			if (cmd.getName().equals("market")) {
				CustomInventory playerInventory = CustomInventory.getFor(player);
				player.openInventory(playerInventory.getCurrent()).setTitle(playerInventory.getTitle());
			}
			return true;
		}

		return false;
	}
}