package fr.wixonic.market;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		InventoryListener inventoryListener = new InventoryListener();
		Market market = new Market();

		inventoryListener.market = market;

		getServer().getPluginManager().registerEvents(inventoryListener, this);
		getCommand("market").setExecutor(market);
	}
}