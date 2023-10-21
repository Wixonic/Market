package fr.wixonic.market;

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

			CustomInventory playerInventory = CustomInventory.getFor(player);

			if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().hasCustomModelData()) {
				ItemMeta data = itemStack.getItemMeta();
				int id = data.getCustomModelData();

				switch (id) {
					case 0:
						playerInventory.back();
						break;

					case 1:
						// playerInventory.first();
						break;

					case 2:
						playerInventory.previous();
						break;
						
					case 3:
						playerInventory.navigateTo("player");
						break;

					case 4:
						playerInventory.next();
						break;

					case 5:
						// playerInventory.last();
						break;

					case 6:
						playerInventory.navigateTo("all");
						break;

					case 7:
						playerInventory.navigateTo("valuables");
						break;

					case 8:
						playerInventory.navigateTo("mobdrops");
						break;

					case 9:
						playerInventory.navigateTo("farming");
						break;
				}

				player.openInventory(playerInventory.getCurrent()).setTitle(playerInventory.getTitle());
			}

			e.setCancelled(true);
		}
	}
}