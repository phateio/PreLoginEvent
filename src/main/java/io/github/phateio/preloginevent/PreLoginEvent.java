package io.github.phateio.preloginevent;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
    private static final String GEOIP_ENABLED_KEY = "geoip-enabled";
    private static final String GEOIP_COUNTRY_DB_KEY = "geoip-country-db";
    private static final String GEOIP_ASN_DB_KEY = "geoip-asn-db";
    private static final String DEFAULT_COUNTRY_DB = "/usr/share/GeoIP/GeoIP.dat";
    private static final String DEFAULT_ASN_DB = "/usr/share/GeoIP/GeoIPASNum.dat";
    private static final String PING_LOG_FILE_KEY = "ping-log-file";
    private static final String DEFAULT_PING_LOG = "logs/ping.log";

    private GeoIpLookup geoIp;
    private PingLog pingLog;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (getConfig().getBoolean(GEOIP_ENABLED_KEY, true)) {
            geoIp = GeoIpLookup.open(
                    new File(getConfig().getString(GEOIP_COUNTRY_DB_KEY, DEFAULT_COUNTRY_DB)),
                    new File(getConfig().getString(GEOIP_ASN_DB_KEY, DEFAULT_ASN_DB)),
                    getLogger());
        }

        if (isMotdLogEnabled()) {
            pingLog = PingLog.open(
                    new File(getConfig().getString(PING_LOG_FILE_KEY, DEFAULT_PING_LOG)),
                    getLogger());
        }

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PreLoginListener(this), this);
        pluginManager.registerEvents(new MotdListener(this), this);
    }

    @Override
    public void onDisable() {
        if (geoIp != null) {
            geoIp.close();
            geoIp = null;
        }
        if (pingLog != null) {
            pingLog.close();
            pingLog = null;
        }
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
     * The GeoIP lookup, or {@code null} when disabled or unavailable.
     */
    GeoIpLookup getGeoIp() {
        return geoIp;
    }

    /**
     * The ping log writer, or {@code null} when MOTD logging is disabled.
     */
    PingLog getPingLog() {
        return pingLog;
    }

    /**
     * The message shown to first-time players, with {@code &} colour codes translated.
     */
    String getKickMessage() {
        String message = getConfig().getString(KICK_MESSAGE_KEY, DEFAULT_KICK_MESSAGE);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
