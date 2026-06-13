package io.github.phateio.preloginevent;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Whitelists the server to returning players: anyone without existing
 * player data on this server is denied at login.
 */
public final class PreLoginEvent extends JavaPlugin {

    private static final String KICK_MESSAGE_KEY = "kick-message";
    private static final String DEFAULT_KICK_MESSAGE =
            "This server is open to returning players only.";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PreLoginListener(this), this);
    }

    /**
     * The message shown to first-time players, with {@code &} colour codes translated.
     */
    String getKickMessage() {
        String message = getConfig().getString(KICK_MESSAGE_KEY, DEFAULT_KICK_MESSAGE);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
