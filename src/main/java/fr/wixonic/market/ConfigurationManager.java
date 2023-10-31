package fr.wixonic.market;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ConfigurationManager {
	final FileConfiguration config;
	
	public ConfigurationManager(FileConfiguration pluginConfig) {
		this.config = pluginConfig;
	}

	public boolean getBoolean(String key) {
		return this.config.getBoolean(key);
	}

	public double getDouble(String key) {
		return this.config.getDouble(key);
	}

	public int getInt(String key) {
		return this.config.getInt(key);
	}

	public List<String> getKeys(String key) {
		return this.config.getConfigurationSection(key).getKeys(false).stream().toList();
	}

	public List<String> getList(String key) {
		return this.config.getStringList(key);
	}

	public String getString(String key) {
		return this.config.getString(key);
	}

	public void set(String key, Object value) {
		this.config.set(key, value);
	}
	
	public void remove(String key) {
		this.config.set(key, null);
	}
}