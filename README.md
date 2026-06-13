# PreLoginEvent

A Spigot plugin that whitelists the server to returning players only.

During the asynchronous pre-login phase it checks whether the connecting
player already has data on this server. Players who have never joined
before are denied with a configurable kick message, so the existing
player base stays closed to newcomers.

## Configuration

`config.yml`:

| Key | Description |
|---|---|
| `kick-message` | Message shown to first-time players. Supports `&` colour codes; use `\n` for line breaks. |

## Build

Requires JDK 17+ (the Gradle toolchain compiles to Java 8 bytecode).

```sh
./gradlew build
```

The plugin jar is written to `build/libs/`.
