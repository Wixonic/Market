package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Database {
	public final static Map<String, List<String>> compatibilityTable = new HashMap<>();
	public final static String latestVersion = "v5";
	public static final Path location = Path.of(Main.getInstance().getDataFolder().getAbsolutePath(), "market.db");

	static {
		Database.compatibilityTable.put("v6", List.of("v6"));
		Database.compatibilityTable.put("v5", List.of("v5", "v4", "v3"));
		Database.compatibilityTable.put("v4", List.of("v5", "v4", "v3"));
		Database.compatibilityTable.put("v3", List.of("v5", "v4", "v3"));
		Database.compatibilityTable.put("v2", List.of("v2"));
		Database.compatibilityTable.put("v1", List.of("v1"));
	}

	public String version;
	public JSONObject data = new JSONObject();

	public final void initialize() throws IOException {
		Main.getInstance().getLogger().info("Initializing database at " + Database.location.toString());

		this.version = Database.latestVersion;
		this.set("core.version", this.version);
		Files.writeString(Database.location, this.data.toJSONString(), StandardCharsets.UTF_8);
	}

	public final void load() throws IOException, ParseException, RuntimeException {
		Main.getInstance().getLogger().info("Loading database from " + Database.location.toString());

		this.data = (JSONObject) new JSONParser().parse(Files.readString(Database.location));

		List<String> compatibilityTable = Database.compatibilityTable.get(this.getString("core.version"));

		if (compatibilityTable == null || !compatibilityTable.contains(Database.latestVersion)) {
			Main.getInstance().getLogger().severe("Invalid database version, please refer to the wiki (https://github.com/Wixonic/Market/wiki/Database-Compatibility)");
			Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
		}
	}

	public final void save() throws IOException {
		Files.writeString(Database.location, this.data.toJSONString(), StandardCharsets.UTF_8);
	}

	public final Object get(String path) {
		return this.data.get(path);
	}

	public final void set(String path, Object obj) {
		this.data.put(path, obj);

		try {
			this.save();
		} catch (IOException e) {
			Main.getInstance().getLogger().warning("Failed to save database to " + Database.location.toString() + " - " + e);
		}
	}

	public final void delete(String path) {
		this.data.remove(path);
	}

	public final boolean getBoolean(String path) {
		Object value = this.data.get(path);
		if (value == null) {
			this.set(path, false);
			return false;
		} else {
			return (boolean) this.data.get(path);
		}
	}

	public final int getInt(String path) {
		Object value = this.data.get(path);
		if (value == null) {
			this.set(path, 0);
			return 0;
		} else {
			return (int) this.data.get(path);
		}
	}

	public final String getString(String path) {
		Object value = this.data.get(path);
		if (value == null) {
			this.set(path, "");
			return "";
		} else {
			return (String) this.data.get(path);
		}
	}

	public final String count(int buying, int selling, String type) {
		if (buying > 0 && selling > 0) {
			return (buying + selling) + " " + type + "s - " + count(buying, 0, "buying " + type) + ", " + count(0, selling, "selling " + type);
		} else if (buying > 0) {
			return buying > 1 ? buying + type + "s" : "One " + type;
		} else if (selling > 0) {
			return selling > 1 ? selling + type + "s" : "One " + type;
		} else {
			return "No " + type;
		}
	}
}