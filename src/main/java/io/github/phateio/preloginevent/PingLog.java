package io.github.phateio.preloginevent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Writes MOTD ping lines to a dedicated file, truncated on each startup, so the
 * noisy ping traffic stays out of the main server console. Falls back to the
 * plugin logger if the file cannot be opened. Writes are synchronized, as pings
 * arrive concurrently on async threads.
 */
final class PingLog implements AutoCloseable {

    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final PrintWriter writer;
    private final Logger fallback;

    private PingLog(PrintWriter writer, Logger fallback) {
        this.writer = writer;
        this.fallback = fallback;
    }

    /**
     * Opens {@code file} for writing, creating parent directories and truncating
     * any existing content. Falls back to {@code pluginLogger} on failure.
     */
    static PingLog open(File file, Logger pluginLogger) {
        try {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8, false), true);
            return new PingLog(writer, pluginLogger);
        } catch (IOException e) {
            pluginLogger.warning("Ping log file unavailable (" + file + "), using console: " + e.getMessage());
            return new PingLog(null, pluginLogger);
        }
    }

    synchronized void info(String message) {
        if (writer != null) {
            writer.println("[" + LocalDateTime.now().format(TIMESTAMP) + "] " + message);
        } else {
            fallback.info(message);
        }
    }

    @Override
    public synchronized void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
