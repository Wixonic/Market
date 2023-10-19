package fr.wixonic.market;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class InventoryListener implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getView().getTitle().startsWith("Market")) {
			ItemStack itemStack = e.getCurrentItem();

			Player player = (Player) e.getWhoClicked();

			if (itemStack == null || itemStack.getType().isAir()) return;

			if (itemStack.getType() == Material.BARRIER) {
				player.openInventory(Main.market.marketInventory.inventory).setTitle("Market");
			} else if (itemStack.getItemMeta().hasCustomModelData()) {
				ItemMeta data = itemStack.getItemMeta();
				int id = data.getCustomModelData();

				Main.market.updateItemList(id, player);

				switch (id) {
					case 0:
						// Back
						break;

					case 1:
						// Previous
						break;

					case 2:
						// Next
						break;

					default:
						player.openInventory(Main.market.itemListInventory.inventory).setTitle("Market → " + data.getDisplayName());
						break;
				}
			}

			e.setCancelled(true);
		}
	}
}