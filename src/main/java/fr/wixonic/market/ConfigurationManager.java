package fr.wixonic.market;

import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigurationManager {
	private final FileConfiguration config;

	public ConfigurationManager(FileConfiguration pluginConfig) {
		config = pluginConfig;
	}

	public final void fillDefault() {
		config.addDefault("database-url", "market.db");
		config.addDefault("database-initialized", false);

		config.options().copyDefaults(true);
	}

	public final String getString(String key) {
		return config.getString(key);
	}

	public final boolean getBoolean(String key) {
		return config.getBoolean(key);
	}

	public final void set(String key, Object value) {
		config.set(key, value);
	}
}