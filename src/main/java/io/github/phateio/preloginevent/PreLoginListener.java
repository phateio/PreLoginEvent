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
        plugin.getLogger().info(formatConnection(event));

        if (!plugin.isKickEnabled()) {
            return;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getUniqueId());
        if (!player.hasPlayedBefore()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, plugin.getKickMessage());
        }
    }

    private String formatConnection(AsyncPlayerPreLoginEvent event) {
        StringBuilder line = new StringBuilder()
                .append(event.getName())
                .append(" connected via ").append(event.getHostname())
                .append(" from ").append(event.getAddress().getHostAddress());
        GeoIpLookup geoIp = plugin.getGeoIp();
        if (geoIp != null) {
            line.append(" (").append(geoIp.describe(event.getAddress())).append(')');
        }
        return line.toString();
    }
}
