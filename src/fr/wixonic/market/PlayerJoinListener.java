package fr.wixonic.market;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HexFormat;

public class PlayerJoinListener implements Listener {
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		player.setResourcePack("https://assets.wixonic.fr/minecraft/2d_heads.zip", HexFormat.of().parseHex("86B5FDF2272B96D9D771DEAC2292FE6DE7E2B125"), "You need to download this resource pack to have a pretty interface on the market.", false);
	}
}