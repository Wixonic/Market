package fr.wixonic.market;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Request {
	private static int id = Main.market.database.getInt("requests.id");

	public int amount;
	public Player from;
	public int requestId;
	public Material item;
	public double price;
	public RequestType type;
	
	public Request(int amount, Player from, Material item, double price, RequestType type) {
		this.amount = amount;
		this.from = from;
		this.requestId = Request.id++;
		this.item = item;
		this.price = price;
		this.type = type;
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
		map.put("id", String.valueOf(this.id));
		map.put("item", this.item.name());
		map.put("price", String.valueOf(this.price));
		map.put("type", this.type.name());
		return map;
	}
}