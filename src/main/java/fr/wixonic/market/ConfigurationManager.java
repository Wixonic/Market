package fr.wixonic.market;

import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigurationManager {
	private final FileConfiguration config;

	public ConfigurationManager(FileConfiguration pluginConfig) {
		config = pluginConfig;
	}

	public void fillDefault() {
		config.addDefault("database-location", "market.db");
		config.addDefault("database-initialized", "false");

		config.options().copyDefaults(true);
	}

	public String getString(String key) {
		return config.getString(key);
	}

	public boolean getBoolean(String key) {
		return config.getBoolean(key);
	}

	public void set(String key, Object value) {
		config.set(key, value);
	}
}