# PreLoginEvent

A Paper plugin that whitelists the server to returning players only.

When enabled, it checks during the asynchronous pre-login phase whether
the connecting player already has data on this server, and denies anyone
who has never joined before with a configurable kick message — keeping
the player base closed to newcomers. This enforcement is opt-in and off
by default (see `kick-enabled`).

It also logs the virtual host of every connection attempt (the address
from the login handshake) during async pre-login — including attempts the
whitelist rejects. When the GeoIP databases are present, each line is
enriched with the source IP's country and ASN:

```
Steve connected via example.com:25565 from 203.0.113.5 (TW, AS3462 HINET)
```

On Paper it additionally logs the virtual host of each MOTD / server-list
ping to a separate file (`logs/ping.log`, truncated on each startup) so the
noisy ping traffic stays out of the console. Note that the ping handshake
carries the address the player typed directly (e.g. `example.com`), whereas
login carries the SRV-resolved target — so the two logs can differ for the
same player.

## Configuration

`config.yml`:

| Key | Default | Description |
|---|---|---|
| `kick-enabled` | `false` | Whether to kick players who have never joined before. When `false`, no one is kicked. |
| `kick-message` | — | Message shown to kicked first-time players. Supports `&` colour codes; use `\n` for line breaks. |
| `motd-log-enabled` | `true` | Log the virtual host of each MOTD/server-list ping. Requires Paper. |
| `ping-log-file` | `logs/ping.log` | File the ping log is written to (kept out of the console; truncated each startup). |
| `geoip-enabled` | `true` | Append the source IP's country + ASN to connection and ping logs. Auto-disables if the `.dat` files are missing. |
| `geoip-country-db` | `/usr/share/GeoIP/GeoIP.dat` | Path to the legacy MaxMind GeoIP country database. |
| `geoip-asn-db` | `/usr/share/GeoIP/GeoIPASNum.dat` | Path to the legacy MaxMind GeoIP ASN database. |

## Build

Runs on a Paper server (1.21+). Building requires JDK 21.

```sh
./gradlew build
```

The plugin jar is written to `build/libs/`.
