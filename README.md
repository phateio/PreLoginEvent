# PreLoginEvent

A Paper plugin that whitelists the server to returning players only.

When enabled, it checks during the asynchronous pre-login phase whether
the connecting player already has data on this server, and denies anyone
who has never joined before with a configurable kick message — keeping
the player base closed to newcomers. This enforcement is opt-in and off
by default (see `kick-enabled`).

On successful login it also logs the address each player used to reach
the server (the virtual host from the handshake), e.g.
`example.com:25565` or `203.0.113.5:25565`.

On Paper it additionally logs the virtual host of each MOTD / server-list
ping. Note that the ping handshake carries the address the player typed
directly (e.g. `example.com`), whereas login carries the SRV-resolved
target — so the two logs can differ for the same player.

## Configuration

`config.yml`:

| Key | Default | Description |
|---|---|---|
| `kick-enabled` | `false` | Whether to kick players who have never joined before. When `false`, no one is kicked. |
| `kick-message` | — | Message shown to kicked first-time players. Supports `&` colour codes; use `\n` for line breaks. |
| `motd-log-enabled` | `true` | Log the virtual host of each MOTD/server-list ping. Requires Paper; can be noisy on public servers. |

## Build

Runs on a Paper server (1.15+); the MOTD logging uses Paper's ping event.
Building requires JDK 17+ (the Gradle toolchain compiles to Java 8 bytecode).

```sh
./gradlew build
```

The plugin jar is written to `build/libs/`.
