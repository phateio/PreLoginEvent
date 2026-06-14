package io.github.phateio.preloginevent;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.net.InetSocketAddress;

/**
 * Logs the virtual host of each MOTD (server-list ping) request, e.g.
 * "ping from 203.0.113.5 via example.com:25565". Unlike login, the ping
 * handshake carries the address the player typed, not the SRV-resolved target.
 *
 * <p>Relies on Paper's {@link PaperServerListPingEvent}; on plain Spigot the
 * ping virtual host is not exposed.
 */
final class MotdListener implements Listener {

    private final PreLoginEvent plugin;

    MotdListener(PreLoginEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(PaperServerListPingEvent event) {
        if (!plugin.isMotdLogEnabled()) {
            return;
        }
        InetSocketAddress virtualHost = event.getClient().getVirtualHost();
        String via = virtualHost == null
                ? "unknown"
                : virtualHost.getHostString() + ":" + virtualHost.getPort();
        StringBuilder line = new StringBuilder()
                .append("Ping from ").append(event.getAddress().getHostAddress())
                .append(" via ").append(via);
        GeoIpLookup geoIp = plugin.getGeoIp();
        if (geoIp != null) {
            line.append(" (").append(geoIp.describe(event.getAddress())).append(')');
        }
        plugin.getLogger().info(line.toString());
    }
}
