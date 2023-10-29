package fr.wixonic.market;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryListener implements Listener {
	private boolean isCustom(Inventory inventory) {
		try {
			return inventory.getItem(53).getItemMeta().getCustomModelData() == -1;
		} catch (Exception ignored) {
			return false;
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInteract(InventoryInteractEvent e) {
		if (this.isCustom(e.getInventory())) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent e) {
		if (this.isCustom(e.getInventory())) {
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
						playerInventory.first();
						break;

					case 2:
						playerInventory.previous();
						break;

					case 3:
						playerInventory.navigateTo("@player");
						break;

					case 4:
						playerInventory.next();
						break;

					case 5:
						playerInventory.last();
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

					case 10:
						playerInventory.navigateTo("blocks");
						break;

					case 11:
						playerInventory.navigateTo("special");
						break;

					case 12:
						playerInventory.navigateTo("other");
						break;

					case 13:
						playerInventory.navigateTo(e.getCurrentItem().getType().name());
						break;
				}

				player.openInventory(playerInventory.getCurrent()).setTitle(playerInventory.getTitle());
			}
		}
	}
}