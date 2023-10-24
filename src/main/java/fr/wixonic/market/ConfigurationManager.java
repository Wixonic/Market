package fr.wixonic.market;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public final class ConfigurationManager {
	private final FileConfiguration config;

	public ConfigurationManager(FileConfiguration pluginConfig) {
		this.config = pluginConfig;
	}

	public final void fillDefault() {
		if (this.config.getKeys(false).isEmpty()) {
			try {
				this.config.loadFromString(Arrays.toString(Objects.requireNonNull(Main.getInstance().getResource("config.yml")).readAllBytes()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public final boolean getBoolean(String key) {
		return this.config.getBoolean(key);
	}

	public final int getInt(String key) {
		return this.config.getInt(key);
	}

	public final List<String> getKeys(String key) {
		return this.config.getConfigurationSection(key).getKeys(false).stream().toList();
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