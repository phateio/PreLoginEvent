package io.github.phateio.preloginevent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.logging.Logger;

/**
 * Logs the address each player used to reach the server: the virtual host
 * from the login handshake, e.g. "example.com:25565" or "203.0.113.5:25565".
 */
final class ConnectionLogListener implements Listener {

    private final Logger logger;

    ConnectionLogListener(PreLoginEvent plugin) {
        this.logger = plugin.getLogger();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        logger.info(event.getPlayer().getName() + " connected via " + event.getHostname());
    }
}
