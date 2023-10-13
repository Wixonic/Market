package fr.wixonic.market;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static ConfigurationManager configManager;
	public static InventoryListener inventoryListener;
	public static Market market;

	@Override
	public void onEnable() {
		configManager = new ConfigurationManager(getConfig());
		configManager.fillDefault();
		saveConfig();

		inventoryListener = new InventoryListener();
		market = new Market();
		market.save();

		getServer().getPluginManager().registerEvents(inventoryListener, this);
		getCommand("market").setExecutor(market);
	}
}