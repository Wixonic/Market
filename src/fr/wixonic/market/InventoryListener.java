package fr.wixonic.market;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getView().getTitle().startsWith("Market")) {
			ItemStack itemStack = e.getCurrentItem();

			if (itemStack == null || itemStack.getType().isAir()) return;

			if (itemStack.getType() == Material.BARRIER) {
				e.getWhoClicked().openInventory(Main.market.marketInventory).setTitle("Market");
			} else if (itemStack.getItemMeta().hasCustomModelData()) {
				int data = itemStack.getItemMeta().getCustomModelData();
				Main.market.updateItemList(data);

				switch (data) {
					case 3:
						e.getWhoClicked().openInventory(Main.market.itemListInventory).setTitle("Market → All items");
						break;

					case 4:
						e.getWhoClicked().openInventory(Main.market.itemListInventory).setTitle("Market → Ores & Valuables");
						break;

					case 5:
						e.getWhoClicked().openInventory(Main.market.itemListInventory).setTitle("Market → Farming");
						break;
				}
			}

			e.setCancelled(true);
		}
	}
}