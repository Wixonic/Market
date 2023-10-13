package fr.wixonic.market;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class InventoryListener implements Listener {
	public Market market;

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (Arrays.stream(market.inventoryTitles).anyMatch(s -> s.equals(e.getView().getTitle()))) {
			ItemStack itemStack = e.getCurrentItem();

			if (itemStack == null || itemStack.getType().isAir()) return;

			if (itemStack.getType() == Material.BARRIER) {
				e.getWhoClicked().closeInventory();
			} else if (itemStack.getItemMeta().hasCustomModelData()) {
				switch (itemStack.getItemMeta().getCustomModelData()) {
					case 0:
						e.getWhoClicked().openInventory(market.itemListInventory);
						break;

					case 1:
						e.getWhoClicked().openInventory(market.itemInfoInventory);
						market.updateItemList("All");
						break;

					case 1:
						e.getWhoClicked().openInventory(market.itemInfoInventory);
						market.updateItemList("Others");
						break;
				}
			}

			e.setCancelled(true);
		}
	}
}