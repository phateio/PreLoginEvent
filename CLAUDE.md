# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

**Editing this file:** Consider the whole document before changing it — the right section, the right wording, the most essential form for every sentence. **Length limit: 200 lines** — trim or consolidate before adding.

## What this is

A Paper (Minecraft server) plugin that closes the server to newcomers: during async pre-login it can deny any player who has never joined before, and it logs the virtual host of every connection and (Paper-only) every MOTD ping, optionally enriched with GeoIP country/ASN.

## Build

Requires JDK 21. There is no test suite.

```sh
./gradlew build      # jar lands in build/libs/
```

`paper-api` and `geoip-api` are both `compileOnly` — Paper is provided by the server at runtime, and GeoIP is pulled in at runtime via `plugin.yml` `libraries:`. Keep both out of the shaded/runtime classpath; do not switch them to `implementation`.

## Architecture

Five classes under `io.github.phateio.preloginevent`:

- `PreLoginEvent` — `JavaPlugin` entry point. `onEnable()` loads config, opens the optional `GeoIpLookup` and `PingLog` resources, and registers the two listeners; `onDisable()` closes them. All config access goes through typed accessor methods here, so listeners never read `getConfig()` directly.
- `PreLoginListener` — handles `AsyncPlayerPreLoginEvent`. Always logs the connection line; then, only when `kick-enabled`, denies players where `!hasPlayedBefore()` with `Result.KICK_WHITELIST`.
- `MotdListener` — handles Paper's `PaperServerListPingEvent`, writing ping lines to `PingLog`.
- `GeoIpLookup` — wraps the **legacy** MaxMind `.dat` databases (country + ASN). Not part of the public GeoIP2 API. Created via `open()`, which returns `null` (after logging a warning) when the `.dat` files are absent — callers treat `null` as "GeoIP off". `LookupService` is not thread-safe, so `describe()`/`close()` are `synchronized`.
- `PingLog` — dedicated file writer, truncated each startup, that keeps noisy ping traffic out of the console. Falls back to the plugin logger if the file can't be opened; writes are `synchronized`.

### Things that bite

- **Login vs ping carry different hostnames.** Login (`getHostname()`) sees the SRV-resolved target; the ping handshake sees the literal address the player typed. The two logs legitimately differ for the same player — don't "fix" this.
- **Two graceful-degradation paths are intentional.** Missing GeoIP `.dat` → `geoIp == null`, logs omit the `(country, asn)` suffix. Unopenable ping file → `PingLog` falls back to console. Preserve both null-checks.
- **Everything runs off the main thread** (async pre-login + ping events). Any new shared state needs the same synchronization treatment as `GeoIpLookup`/`PingLog`.
- `MotdListener` references `PaperServerListPingEvent`, so the plugin assumes Paper. It is not Spigot-safe.

## Config & resources

`src/main/resources/config.yml` is the source of defaults; keep its keys, the constants in `PreLoginEvent`, and the README table in sync when changing config. `kick-enabled` defaults to **false** (logging-only out of the box). `plugin.yml` has `${name}`/`${version}` filtered in by the `processResources` task from `build.gradle.kts`.

## Release

Releasing is GitHub-driven, not local. Bump `version` in `build.gradle.kts` (commit `chore: bump version to X`), then publish a GitHub Release — `.github/workflows/release.yml` builds on JDK 21 and uploads the jar as a release asset. This repo is the canonical template for the other phateio Minecraft plugins' release flow.
