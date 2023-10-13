package fr.wixonic.market;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationManager {
	private final FileConfiguration config;

	public ConfigurationManager(FileConfiguration pluginConfig) {
		config = pluginConfig;
	}

	public void fillDefault() {
		config.addDefault("database-location", "market.db");

		config.options().copyDefaults(true);
	}

	public String getString(String key) {
		return config.getString(key);
	}
}