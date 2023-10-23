package fr.wixonic.market;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public final class ConfigurationManager {
	private final FileConfiguration config;

	public ConfigurationManager(FileConfiguration pluginConfig) {
		this.config = pluginConfig;
	}

	public final void fillDefault() {
		this.config.addDefault("database-url", "market.db");
		this.config.addDefault("database-initialized", false);

		this.config.options().copyDefaults(true);
	}

	public final boolean getBoolean(String key) {
		return this.config.getBoolean(key);
	}

	public final int getInt(String key) {
		return this.config.getInt(key);
	}

	public final List<String> getKeys(String key) {
		return this.config.getConfigurationSection(key).getKeys();
	}

	public final List<String> getList(String key) {
		return this.config.getStringList(key);
	}

	public final String getString(String key) {
		return this.config.getString(key);
	}


	public final void set(String key, Object value) {
		this.config.set(key, value);
	}
}