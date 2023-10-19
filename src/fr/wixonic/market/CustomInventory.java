package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CustomInventory {
	public final Inventory inventory;

	public CustomInventory(int size, Player player) {
		inventory = Bukkit.createInventory(player, size > 54 || size % 9 != 0 || size < 9 ? 54 : size);
		reset(player);
	}

	public void setItem(ItemStack itemStack, int position) {
		inventory.setItem(position - 1, itemStack);
	}

	public void reset(Player player) {

	}
}
