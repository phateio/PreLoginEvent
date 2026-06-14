package io.github.phateio.preloginevent;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Whitelists the server to returning players: anyone without existing
 * player data on this server is denied at login.
 */
public final class PreLoginEvent extends JavaPlugin {

    private static final String KICK_ENABLED_KEY = "kick-enabled";
    private static final String KICK_MESSAGE_KEY = "kick-message";
    private static final String MOTD_LOG_ENABLED_KEY = "motd-log-enabled";
    private static final String DEFAULT_KICK_MESSAGE =
            "This server is open to returning players only.";

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PreLoginListener(this), this);
        pluginManager.registerEvents(new ConnectionLogListener(this), this);
        pluginManager.registerEvents(new MotdListener(this), this);
    }

    /**
     * Whether first-time players should be kicked. Defaults to {@code false}.
     */
    boolean isKickEnabled() {
        return getConfig().getBoolean(KICK_ENABLED_KEY, false);
    }

    /**
     * Whether MOTD ping virtual hosts should be logged. Defaults to {@code true}.
     */
    boolean isMotdLogEnabled() {
        return getConfig().getBoolean(MOTD_LOG_ENABLED_KEY, true);
    }

    /**
     * The message shown to first-time players, with {@code &} colour codes translated.
     */
    String getKickMessage() {
        String message = getConfig().getString(KICK_MESSAGE_KEY, DEFAULT_KICK_MESSAGE);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
