package fr.wixonic.market;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Request {
	public int amount;
	public Player from;
	public int requestId;
	public Material item;
	public double price;
	public RequestType type;
	
	public Request(int amount, Player from, Material item, double price, RequestType type) {
		this.amount = amount;
		this.from = from;
		this.requestId = Main.market.database.getInt("requests.id");
		Main.market.database.set("request.id", this.requestId + 1);
		this.item = item;
		this.price = price;
		this.type = type;
		
		this.save();
	}

	public Request(int amount, Player from, int id, Material item, double price, RequestType type) {
		this.amount = amount;
		this.from = from;
		this.requestId = id;
		this.item = item;
		this.price = price;
		this.type = type;
	}

	public static Request fromMap(Map<String, String> map) {
		return new Request(
			Integer.parseInt(map.get("amount")),
			Main.getInstance().getServer().getPlayer(UUID.fromString(map.get("uuid"))),
			Integer.parseInt(map.get("id")),
			Material.getMaterial(map.get("item")),
			Double.parseDouble(map.get("price")),
			RequestType.valueOf(map.get("type"))
		);
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		map.put("amount", String.valueOf(this.amount));
		map.put("from", this.from.getUniqueId().toString());
		map.put("id", String.valueOf(this.requestId));
		map.put("item", this.item.name());
		map.put("price", String.valueOf(this.price));
		map.put("type", this.type.name());
		return map;
	}

	private void save() {
		Main.market.database.set("items." + this.item.name() + "." + this.type.name() + "." + this.requestId, this.toMap());
	}
	
	public void remove() {
		Main.market.database.remove("items." + this.item.name() + "." + this.type.name() + "." + this.requestId);
	}

	public void accept(Player player, int amount) {
		amount = Math.min(this.amount, amount);
		
		if (this.type == RequestType.BUY) {
			if (Main.vault.getBalance(player) >= amount * this.price) {
				Map<Integer, ItemStack> remainingMap = player.getInventory().addItem(new ItemStack(this.item, amount));
				int remainingItems = remainingMap.keySet().stream().toList().get(0);
				amount -= remainingItems;
				
				double price = amount * this.price;
				double taxedPrice = price * (1 - Main.configManager.getDouble("tax"));
				
				Main.vault.withdrawPlayer(player, price);
				Main.vault.depositPlayer(this.from, taxedPrice);
				
				this.amount -= amount;
				
				Main.getInstance().getLogger().info(player.getName() + " bought" + amount + " " + this.item.name() + " for $" + price + ". $" + taxedPrice);
				FormattedMessage.send(player, "You bough % % for %", amount, this.item, price);
			} else player.sendMessage(ChatColor.RED + "Failed to buy: You need $" + price);
		} else if (this.type == RequestType.SELL) {
			if (player.getInventory().contains(this.item)) {
				HashMap<Integer, ItemStack> inventory = (HashMap<Integer, ItemStack>) player.getInventory().all(this.item);
			} else player.sendMessage(ChatColor.RED + "Failed to sell: You don't have this item in your inventory");
		}
	}
}