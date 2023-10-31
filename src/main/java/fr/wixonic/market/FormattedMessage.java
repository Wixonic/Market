package fr.wixonic.market;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public final class FormattedMessage {
	public static void send(final Player player, final String message, final Object... components) {
		player.spigot().sendMessage(new TextComponent(new FormattedMessage(message, components).toString()));
	}
	
	private final String message;
	
	public FormattedMessage(final String message, final Object... components) {
		String formattedMessage = "";
		
		formattedMessage = message;
		
		this.message = formattedMessage;
	}

	@Override
	public String toString() {
		return this.message;
	}
}