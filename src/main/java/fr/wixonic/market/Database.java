package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public final class Database extends ConfigurationManager {
	public final static Map<String, List<String>> compatibilityTable = new HashMap<>();
	public final static String latestVersion = "v7";
	public final static Path location = Path.of(Main.getInstance().getDataFolder().getAbsolutePath(), "market.db");

	static {
		Database.compatibilityTable.put("v7", List.of("v7"));
		Database.compatibilityTable.put("v6", List.of("v6"));
		Database.compatibilityTable.put("v5", List.of("v5", "v4", "v3"));
		Database.compatibilityTable.put("v4", List.of("v5", "v4", "v3"));
		Database.compatibilityTable.put("v3", List.of("v5", "v4", "v3"));
		Database.compatibilityTable.put("v2", List.of("v2"));
		Database.compatibilityTable.put("v1", List.of("v1"));
	}

	public String version;

	public Database() {
		super(new YamlConfiguration());
	}

	public void initialize() throws IOException {
		Main.getInstance().getLogger().info("Initializing database at " + Database.location.toString());

		this.version = Database.latestVersion;
		this.set("version", this.version);
		this.save();
		Main.configManager.set("database-initialized", true);
	}

	public void load() throws IOException, ParseException, RuntimeException {
		Main.getInstance().getLogger().info("Loading database from " + Database.location.toString());
		YamlConfiguration.loadConfiguration(Database.location.toFile());
		
		List<String> compatibilityTable = Database.compatibilityTable.get(this.getString("version"));
		if (compatibilityTable == null || !compatibilityTable.contains(Database.latestVersion)) {
			Main.getInstance().getLogger().severe("Invalid database version, please refer to the wiki (https://github.com/Wixonic/Market/wiki/Database-Compatibility)");
			Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
		}
	}

	public void save() {
		try {
			this.config.save(Database.location.toFile());
		} catch (IOException e) {
			Main.getInstance().getLogger().warning("Failed to save database to " + Database.location.toString() + " - " + e);
		}
	}
}