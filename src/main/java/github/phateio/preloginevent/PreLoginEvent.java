package github.phateio.preloginevent;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PreLoginEvent extends JavaPlugin {

	@Override
	public void onEnable() {
		PreLoginEvent plugin = this;

		getServer().getPluginManager().registerEvents(new Listener() {

			@EventHandler
			public void onPreLogin(AsyncPlayerPreLoginEvent event) {
				final String playerName = event.getName();
				final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
				final String reason = "This is a test server for who played example.com only";

				if (!offlinePlayer.hasPlayedBefore()) {
					event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, reason);
				}
				
			}
		}, this);
	}

	@Override
	public void onDisable() {
	}
}
