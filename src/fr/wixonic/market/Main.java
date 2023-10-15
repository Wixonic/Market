package fr.wixonic.market;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static ConfigurationManager configManager;
	public static InventoryListener inventoryListener;
	public static PlayerJoinListener playerJoinListener;
	public static Market market;

	@Override
	public void onEnable() {
		configManager = new ConfigurationManager(getConfig());
		configManager.fillDefault();
		saveConfig();

		CustomSkull.presets.put("leftArrow", new CustomSkull().setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzNlOTE5MTlkYjBhY2VmZGMyNzJkNjdmZDg3YjRiZTg4ZGM0NGE5NTg5NTg4MjQ0NzRlMjFlMDZkNTNlNiJ9fX0="));
		CustomSkull.presets.put("rightArrow", new CustomSkull().setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0="));

		inventoryListener = new InventoryListener();
		playerJoinListener = new PlayerJoinListener();

		market = new Market();
		market.save();

		getServer().getPluginManager().registerEvents(inventoryListener, this);
		getServer().getPluginManager().registerEvents(playerJoinListener, this);
		getCommand("market").setExecutor(market);
	}
}