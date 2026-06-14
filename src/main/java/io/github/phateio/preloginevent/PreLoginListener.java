package io.github.phateio.preloginevent;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * Logs each connection's virtual host and, when enabled, denies players
 * who have never joined this server before.
 */
final class PreLoginListener implements Listener {

    private final PreLoginEvent plugin;

    PreLoginListener(PreLoginEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        plugin.getLogger().info(event.getName() + " connected via " + event.getHostname());

        if (!plugin.isKickEnabled()) {
            return;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getUniqueId());
        if (!player.hasPlayedBefore()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, plugin.getKickMessage());
        }
    }
}
