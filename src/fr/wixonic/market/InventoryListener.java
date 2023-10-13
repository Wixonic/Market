package fr.wixonic.market;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class InventoryListener implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (Arrays.stream(Main.market.inventoryTitles).anyMatch(s -> s.equals(e.getView().getTitle()))) {
			ItemStack itemStack = e.getCurrentItem();

			if (itemStack == null || itemStack.getType().isAir()) return;

			if (itemStack.getType() == Material.BARRIER) {
				e.getWhoClicked().closeInventory();
			} else if (itemStack.getItemMeta().hasCustomModelData()) {
				switch (itemStack.getItemMeta().getCustomModelData()) {
					case 0:
						Main.market.updateItemList("All");
						e.getWhoClicked().openInventory(Main.market.itemListInventory);
						break;

					case 1:
						Main.market.updateItemList("Others");
						e.getWhoClicked().openInventory(Main.market.itemListInventory);
						break;

					case 2:
						Main.market.updateItemList("Others");
						e.getWhoClicked().openInventory(Main.market.itemListInventory);
						break;
				}
			}

			e.setCancelled(true);
		}
	}
}