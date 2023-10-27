package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
	public static ConfigurationManager configManager;
	public static InventoryListener inventoryListener;
	public static PlayerJoinListener playerJoinListener;
	public static Market market;
	private static Main instance;

	public static Main getInstance() {
		return Main.instance;
	}

	@Override
	public void onEnable() {
		Main.instance = this;

		Main.configManager = new ConfigurationManager(this.getConfig());
		Main.configManager.fillDefault();
		this.saveConfig();

		CustomSkull.presets.put("leftArrow", new CustomSkull("Back").setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzNlOTE5MTlkYjBhY2VmZGMyNzJkNjdmZDg3YjRiZTg4ZGM0NGE5NTg5NTg4MjQ0NzRlMjFlMDZkNTNlNiJ9fX0="));
		CustomSkull.presets.put("doubleBackward", new CustomSkull("First").setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMzMDFhMTdjOTU1ODA3ZDg5ZjljNzJhMTkyMDdkMTM5M2I4YzU4YzRlNmU0MjBmNzE0ZjY5NmE4N2ZkZCJ9fX0="));
		CustomSkull.presets.put("backward", new CustomSkull("Previous").setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDgzNDhhYTc3ZjlmYjJiOTFlZWY2NjJiNWM4MWI1Y2EzMzVkZGVlMWI5MDVmM2E4YjkyMDk1ZDBhMWYxNDEifX19"));
		CustomSkull.presets.put("forward", new CustomSkull("Next").setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTdiMDNiNzFkM2Y4NjIyMGVmMTIyZjk4MzFhNzI2ZWIyYjI4MzMxOWM3YjYyZTdkY2QyZDY0ZDk2ODIifX19"));
		CustomSkull.presets.put("doubleForward", new CustomSkull("Last").setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU0ZmFiYjE2NjRiOGI0ZDhkYjI4ODk0NzZjNmZlZGRiYjQ1MDVlYmE0Mjg3OGM2NTNhNWQ3OTNmNzE5YjE2In19fQ=="));
		CustomSkull.presets.put("checkmark", new CustomSkull("Validate").setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdiNjJkMjc1ZDg3YzA5Y2UxMGFjYmNjZjM0YzRiYTBiNWYxMzVkNjQzZGM1MzdkYTFmMWRmMzU1YTIyNWU4MiJ9fX0="));
		// CustomSkull.presets.put("name", new CustomSkull("displayName").setTexture("texture"));

		Main.inventoryListener = new InventoryListener();
		Main.playerJoinListener = new PlayerJoinListener();

		Main.market = new Market();

		this.getServer().getPluginManager().registerEvents(Main.inventoryListener, this);
		this.getServer().getPluginManager().registerEvents(Main.playerJoinListener, this);
		this.getCommand("market").setExecutor(Main.market);
	}
}