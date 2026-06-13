package io.github.phateio.preloginevent;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * Denies login to players who have never joined this server before.
 */
final class PreLoginListener implements Listener {

    private final PreLoginEvent plugin;

    PreLoginListener(PreLoginEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getUniqueId());
        if (!player.hasPlayedBefore()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, plugin.getKickMessage());
        }
    }
}
