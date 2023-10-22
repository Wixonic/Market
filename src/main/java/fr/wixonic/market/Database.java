package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Database {
	public final static Map<String, List<String>> compatibilityTable = new HashMap<>();
	public final static String latestVersion = "v5";

	static {
		Database.compatibilityTable.put("v6", List.of("v6"));
		Database.compatibilityTable.put("v5", List.of("v5", "v4", "v3"));
		Database.compatibilityTable.put("v4", List.of("v5", "v4", "v3"));
		Database.compatibilityTable.put("v3", List.of("v5", "v4", "v3"));
		Database.compatibilityTable.put("v2", List.of("v2"));
		Database.compatibilityTable.put("v1", List.of("v1"));
	}

	public String version;
	public JSONObject data;

	public final void initialize(String url) throws IOException { // TODO: Something is null and idk why
		this.data = new JSONObject();
		JSONObject core = new JSONObject();
		core.put("version", Database.latestVersion);
		this.data.put("core", core);
		Files.writeString(Path.of(url), this.data.toJSONString());
	}

	public final void load(String url) throws IOException, ParseException, RuntimeException {
		this.data = (JSONObject) new JSONParser().parse(Files.readString(Path.of(url)));

		JSONObject core = (JSONObject) this.data.get("core");
		List<String> compatibilityTable = Database.compatibilityTable.get(core.get("version"));

		if (compatibilityTable.indexOf(Database.latestVersion) < 0) {
			throw new RuntimeException("Invalid database version, please refer to the wiki (https://github.com/Wixonic/WixMarket/wiki/Database-Compatibility)");
		}
	}

	public final Object get(String path) {

		return null;
	}

	public final void set(String path, Object obj) {

	}

	public final void delete(String path) {

	}

	public final boolean getBoolean(String path) {
		try {
			return (boolean) this.get(path);
		} catch (Exception e) {
			try {
				this.set(path, false);
			} catch (Exception e2) {
				Bukkit.getLogger().severe("The database might be compromised. Please check previous logs.");
			}

			return false;
		}
	}

	public final int getInt(String path) {
		try {
			return (int) this.get(path);
		} catch (Exception ignored) {
			try {
				this.set(path, 0);
			} catch (Exception e2) {
				Bukkit.getLogger().severe("The database might be compromised. Please check previous logs.");
			}

			return 0;
		}
	}

	public final String getString(String path) {
		try {
			return (String) this.get(path);
		} catch (Exception ignored) {
			try {
				this.set(path, "");
			} catch (Exception e2) {
				Bukkit.getLogger().severe("The database might be compromised. Please check previous logs.");
			}

			return "";
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