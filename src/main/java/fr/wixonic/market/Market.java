package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public final class Market implements CommandExecutor, TabCompleter {
	public final Database database;

	public Market() {
		this.database = new Database();

		if (Main.configManager.getBoolean("database-initialized")) {
			try {
				this.database.load();
			} catch (Exception e) {
				Main.getInstance().getLogger().severe("Failed to connect to database at \"" + Database.location + "\" - " + e);
				Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
			}
		} else {
			try {
				this.database.initialize();
				Main.configManager.set("database-initialized", true);
				Main.getInstance().saveDefaultConfig();
			} catch (Exception e) {
				Main.getInstance().getLogger().severe("Failed to create the database at \"" + Database.location + "\" - " + e);
				Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
			}
		}
	}
	
	public void buy(Player player, Material item, Double price) {
		if (player.hasPermission("market.buy") && ItemManager.list.contains(item)) {
			Main.market.database.getRequests("items." + item.name() + ".buy.requests");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = ((Player) sender).getPlayer();
			
			if (cmd.getName().equals("market")) {
				if ((args.length == 0 || args[0].equals("view")) && player.hasPermission("market")) {
					CustomInventory playerInventory = CustomInventory.getFor(player);
					player.openInventory(playerInventory.getCurrent()).setTitle(playerInventory.getTitle());
				} else if (args[0].equals("config") && player.hasPermission("market.manage")) {
					switch (args[1]) {
						case "reset":
							File configFile = new File(Main.getInstance().getDataFolder(), "config.yml");
							try {
								Files.copy(Path.of(configFile.getAbsolutePath()), Path.of(Main.getInstance().getDataFolder().getAbsolutePath(), "config_backup.yml"), StandardCopyOption.REPLACE_EXISTING);
								configFile.delete();
								Main.getInstance().saveDefaultConfig();
								Main.getInstance().reloadConfig();
								ItemManager.reload();
								player.sendMessage(ChatColor.YELLOW + "The configuration file has been reset. A backup was saved at " + Path.of(Main.getInstance().getDataFolder().getAbsolutePath(), "config_backup.yml"));
							} catch (IOException ignored) {
								player.sendMessage(ChatColor.RED + "Failed to create backup, please retry");
							}
							break;

						default:
							player.sendMessage(ChatColor.RED + "This command doesn't exist");
							break;
					}
				} else {
					Main.getInstance().getLogger().info(player.getName() + " tried to use \"" + cmd.getName() + " " + String.join(" ", args) + "\" without permission");
					player.sendMessage(ChatColor.RED + "You don't have permission to use this commmand.");
				}
			}

			return true;
		} else {
			Main.getInstance().getLogger().warning("Please use this command as a Player");
			return false;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = ((Player) sender).getPlayer();

			List<String> list = new ArrayList<>();

			switch (args.length) {
				case 0:
					if (player.hasPermission("market")) list.add("view");
					if (player.hasPermission("market.manage")) list.add("config");
					break;

				case 1:
					if ("view".startsWith(args[0]) && player.hasPermission("market")) list.add("view");
					if ("config".startsWith(args[0]) && player.hasPermission("market.manage")) list.add("config");
					break;

				case 2:
					switch (args[0]) {
						case "config":
							if ("reset".startsWith(args[1]) && player.hasPermission("market.manage")) list.add("reset");
							break;
					}
			}

			return list;
		} else {
			return new ArrayList<>();
		}
	}
}