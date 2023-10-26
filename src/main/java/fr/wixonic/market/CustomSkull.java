package fr.wixonic.market;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CustomSkull {
	public static Map<String, ItemStack> presets = new HashMap<String, ItemStack>();

	public final ItemStack head;
	final SkullMeta meta;
	final GameProfile profile;

	public CustomSkull(String displayName) {
		head = new ItemStack(Material.PLAYER_HEAD, 1);
		meta = (SkullMeta) head.getItemMeta();
		profile = new GameProfile(UUID.fromString("52e485b7-5156-498e-8341-dd44f4a38908"), "Wixonic");
		meta.setDisplayName(displayName);
		head.setItemMeta(meta);
	}

	public ItemStack setTexture(String base64) {
		profile.getProperties().put("textures", new Property("textures", base64));

		Field profileField = null;

		try {
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		} catch (Exception ignored) {
		}

		head.setItemMeta(meta);

		return head;
	}

	public ItemStack setPlayer(Player player) {
		meta.setOwningPlayer(player);
		head.setItemMeta(meta);
		return head;
	}
}