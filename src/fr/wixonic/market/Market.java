package fr.wixonic.market;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Market implements CommandExecutor {
	public final String[] inventoryTitles = {"Market", "Item list", "Item info"};

	public final Inventory marketInventory;
	public final Inventory itemListInventory;

	public Inventory itemInfoInventory;

	public Market() {
		marketInventory = Bukkit.createInventory(null, 54, inventoryTitles[0]);

		ItemMeta itemMeta;

		ItemStack quitStack = new ItemStack(Material.BARRIER, 1);
		itemMeta = quitStack.getItemMeta();
		itemMeta.setCustomModelData(0);
		itemMeta.setDisplayName("Close");
		quitStack.setItemMeta(itemMeta);

		ItemStack listStack = new ItemStack(Material.MAP, 1);
		itemMeta = listStack.getItemMeta();
		itemMeta.setCustomModelData(1);
		itemMeta.setDisplayName("All items");
		listStack.setItemMeta(itemMeta);

		ItemStack listOthersStack = new ItemStack(Material.MAP, 1);
		itemMeta = listOthersStack.getItemMeta();
		itemMeta.setCustomModelData(2);
		itemMeta.setDisplayName("Other");
		listOthersStack.setItemMeta(itemMeta);

		marketInventory.setItem(0, listStack);
		marketInventory.setItem(2, listOthersStack);
		marketInventory.setItem(8, quitStack);

		itemListInventory = Bukkit.createInventory(null, 54, inventoryTitles[1]);
	}

	public void updateItemInfo(Material item) {
		itemInfoInventory = Bukkit.createInventory(null, 54, inventoryTitles[2]);
	}

	public void updateItemList(String category) {
		itemListInventory.clear();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (cmd.getName().equals("market")) ((Player) sender).openInventory(marketInventory);
			return true;
		}

		return false;
	}
}